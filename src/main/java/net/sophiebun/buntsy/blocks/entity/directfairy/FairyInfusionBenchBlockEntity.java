package net.sophiebun.buntsy.blocks.entity.directfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.recipe.FairyInfusionRecipe;
import net.sophiebun.buntsy.screen.FairyInfusionBenchMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FairyInfusionBenchBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private final ItemStackHandler outputItemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT_START = 0;
    private static final int INPUT_SLOT_COUNT = 5;
    private static final int OUTPUT_SLOT_START = 0;
    private static final int OUTPUT_SLOT_COUNT = 5;

    private final List<Integer> randomRotations;

    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

    public List<Integer> getRandomRotations() {
        return randomRotations;
    }

    public FairyInfusionBenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get(), pPos, pBlockState);
        setConsumption(1.25f);

        Random random = new Random();

        randomRotations = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            randomRotations.add(random.nextInt(0, 270));
        }
    }

    @Override
    public boolean isTitular() {
        return true;
    }

    @Override
    public int getFairyWeight() {
        return 2;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side.equals(Direction.DOWN)){
                return outputLazyItemHandler.cast();
            }
            else {
                return inputLazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputLazyItemHandler = LazyOptional.of((() -> this.inputItemHandler));
        outputLazyItemHandler = LazyOptional.of((() -> this.outputItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT);
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(i, outputItemHandler.getStackInSlot(i));
        }
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(i + OUTPUT_SLOT_COUNT, outputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.fairy_infusion_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FairyInfusionBenchMenu(i, inventory, this);
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inputInventory", inputItemHandler.serializeNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.inputItemHandler.deserializeNBT(pTag.getCompound("inputInventory"));
        this.outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
    }

    public List<ItemStack> getRenderInputItems(){

        List<Integer> inputs = getFilledInputSlotList();

        List<ItemStack> renderItems = new ArrayList<>();

        for (Integer i : inputs){
            if (renderItems.size() < 4){
                renderItems.add(this.inputItemHandler.getStackInSlot(i));
            }
        }

        return renderItems;
    }

    public ItemStack getFirstOutputItemStack(){
        for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT; i++){
            if (!outputItemHandler.getStackInSlot(i).isEmpty()){
                return outputItemHandler.getStackInSlot(i);
            }
        }
        return outputItemHandler.getStackInSlot(OUTPUT_SLOT_START);
    }

    public void infuse() {
        FairyInfusionRecipe recipe = getCurrentInfusion().get();
        int slot = getFirstFilledInputSlot(recipe.getInputs().get(0).getItems()[0].getItem());
        ItemStack result = recipe.getResultItem(null);
        int outputSlot = getClearOutput(result);

        this.inputItemHandler.extractItem(slot, 1, false);

        this.outputItemHandler.setStackInSlot(outputSlot,
                new ItemStack(result.getItem(),
                        this.outputItemHandler.getStackInSlot(outputSlot).getCount() + result.getCount()));
    }

    public boolean hasInfusion() {
        Optional<FairyInfusionRecipe> recipe = getCurrentInfusion();
        if (recipe.isEmpty()){
            return false;
        }

        return isOutputClear();
    }

    private Optional<FairyInfusionRecipe> getCurrentInfusion() {
        SimpleContainer inventory = new SimpleContainer(this.inputItemHandler.getSlots());
        for(int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, this.inputItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(FairyInfusionRecipe.Type.INSTANCE, inventory, level);
    }

    private Integer getFirstFilledInputSlot(Item item) {
        List<Integer> slotList = new ArrayList<Integer>();
        for (int i = INPUT_SLOT_START; i < INPUT_SLOT_START + INPUT_SLOT_COUNT; i++){
            if (this.inputItemHandler.getStackInSlot(i).is(item)){
                slotList.add(i);
            }
        }
        return slotList.isEmpty() ? null : slotList.get(0);
    }
    private List<Integer> getFilledInputSlotList() {
        List<Integer> slotList = new ArrayList<Integer>();
        for (int i = INPUT_SLOT_START; i < INPUT_SLOT_START + INPUT_SLOT_COUNT; i++){
            if (!this.inputItemHandler.getStackInSlot(i).isEmpty()){
                slotList.add(i);
            }
        }
        return slotList;
    }

    private boolean isOutputClear() {
        return getClearOutput(getCurrentInfusion().get().getResultItem(null)) != null;
    }

    private Integer getClearOutput(ItemStack result) {
        for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT; i++){
            if ((outputItemHandler.getStackInSlot(i).isEmpty() || this.outputItemHandler.getStackInSlot(i).getItem() == result.getItem())
                    && (this.outputItemHandler.getStackInSlot(i).getCount() + result.getCount() <= result.getMaxStackSize())){
                return i;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

}