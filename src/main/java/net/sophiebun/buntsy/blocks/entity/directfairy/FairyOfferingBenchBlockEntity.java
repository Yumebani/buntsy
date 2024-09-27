package net.sophiebun.buntsy.blocks.entity.directfairy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;
import net.sophiebun.buntsy.recipe.FairyOfferingRecipe;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.screen.FairyOfferingBenchMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FairyOfferingBenchBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private final ItemStackHandler outputItemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int FAIRY_FOOD_SLOT_START = 0;
    private static final int FAIRY_FOOD_SLOT_COUNT = 4;
    private static final int FAIRY_FOOD_OUTPUT_SLOT_START = 0;
    private static final int FAIRY_FOOD_OUTPUT_SLOT_COUNT = 4;

    public final ContainerData data;
    private final List<Integer> randomRotations;

    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> outputItemHandler);

    public List<Integer> getRandomRotations() {
        return randomRotations;
    }

    public FairyOfferingBenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.OFFERING_BENCH_BLOCK_ENTITY.get(), pPos, pBlockState);

        Random random = new Random();

        randomRotations = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            randomRotations.add(random.nextInt(0, 270));
        }

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> FairyOfferingBenchBlockEntity.this.isEnchanted() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> FairyOfferingBenchBlockEntity.this.setEnchanted(i1 == 1);
                }
                ;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side.equals(Direction.DOWN)){
                return outputLazyItemHandler.cast();
            }
            else{
                return inputLazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputLazyItemHandler = LazyOptional.of((() -> inputItemHandler));
        outputLazyItemHandler = LazyOptional.of((() -> outputItemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyItemHandler.invalidate();
        outputLazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(FAIRY_FOOD_SLOT_COUNT + FAIRY_FOOD_OUTPUT_SLOT_COUNT);
        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(i + FAIRY_FOOD_SLOT_COUNT, outputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.fairy_offering_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FairyOfferingBenchMenu(i, inventory, this, this.data);
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

    public List<ItemStack> getRenderItems(){

        List<Integer> inputs = getFilledInputSlotList();
        List<Integer> outputs = getFilledOutputSlotList();

        List<ItemStack> renderItems = new ArrayList<>();

        for (Integer i : inputs){
            if (renderItems.size() < 4){
                renderItems.add(this.inputItemHandler.getStackInSlot(i));
            }
        }

        for (Integer i : outputs){
            if (renderItems.size() < 4){
                renderItems.add(this.outputItemHandler.getStackInSlot(i));
            }
        }

        return renderItems;
    }

    public boolean hasFood() {
        return getFirstValidInputSlot() != null;
    }

    public Optional<FairyOfferingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(FAIRY_FOOD_SLOT_COUNT + FAIRY_FOOD_OUTPUT_SLOT_COUNT);
        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        for (int i = 0; i < FAIRY_FOOD_SLOT_COUNT; i++) {
            inventory.setItem(i + FAIRY_FOOD_OUTPUT_SLOT_COUNT, outputItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(FairyOfferingRecipe.Type.INSTANCE, inventory, level);
    }

    public void consumeFood() {
        outputFoodItem(getCurrentRecipe().get().getResultItem(null));
        int slot = getFirstValidInputSlot();
        this.inputItemHandler.extractItem(slot, 1, false);
    }

    private void outputFoodItem(ItemStack outputItem){
        if (!outputItem.is(Blocks.AIR.asItem())){
            int slot = getClearOutput(outputItem);
            this.outputItemHandler.setStackInSlot(slot,
                    new ItemStack(outputItem.getItem(),this.outputItemHandler.getStackInSlot(slot).getCount() + 1));
        }
    }

    private Integer getFirstValidInputSlot(){
        List<Integer> values = getFilledInputSlotList().stream().filter((value) -> isOutputClear()).toList();
        return values.isEmpty() ? null : values.get(0);
    }

    private List<Integer> getFilledInputSlotList() {

        List<Integer> slots = new ArrayList<>();
        for (int i = FAIRY_FOOD_SLOT_START; i < FAIRY_FOOD_SLOT_START + FAIRY_FOOD_SLOT_COUNT; i++){
            if (!this.inputItemHandler.getStackInSlot(i).isEmpty() && this.inputItemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    private List<Integer> getFilledOutputSlotList() {

        List<Integer> slots = new ArrayList<>();
        for (int i = FAIRY_FOOD_OUTPUT_SLOT_START; i < FAIRY_FOOD_OUTPUT_SLOT_START + FAIRY_FOOD_OUTPUT_SLOT_COUNT; i++){
            if (!this.outputItemHandler.getStackInSlot(i).isEmpty() && this.outputItemHandler.getStackInSlot(i).is(ModTags.Items.FAIRY_FOOD)){
                slots.add(i);
            }
        }
        return slots;
    }

    private boolean isOutputClear() {
        ItemStack output = getCurrentRecipe().get().getResultItem(null);
        return output.is(Blocks.AIR.asItem()) || getClearOutput(output) != null;
    }

    private Integer getClearOutput(ItemStack result) {
        for (int i = FAIRY_FOOD_OUTPUT_SLOT_START; i < FAIRY_FOOD_OUTPUT_SLOT_START + FAIRY_FOOD_OUTPUT_SLOT_COUNT; i++){
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