package net.sophiebun.buntsy.blocks.entity.directfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.recipe.FairyInfusionInput;
import net.sophiebun.buntsy.recipe.FairyInfusionRecipe;
import net.sophiebun.buntsy.recipe.InfusionAltarInput;
import net.sophiebun.buntsy.screen.FairyInfusionBenchMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FairyInfusionBenchBlockEntity extends FairyInteractBlockEntity implements MenuProvider {

    public final ItemStackHandler inputItemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    public final ItemStackHandler outputItemHandler = new ItemStackHandler(5) {
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

    public ItemStackHandler getItemHandler(Direction side) {
        if (side.equals(Direction.DOWN)){
            return outputItemHandler;
        }
        else {
            return inputItemHandler;
        }
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
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("inputInventory", inputItemHandler.serializeNBT(registries));
        pTag.put("outputInventory", outputItemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.inputItemHandler.deserializeNBT(registries, pTag.getCompound("inputInventory"));
        this.outputItemHandler.deserializeNBT(registries, pTag.getCompound("outputInventory"));
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
        FairyInfusionRecipe recipe = getCurrentInfusion().get().value();
        int slot = getFirstFilledInputSlot(recipe.getInputs().get(0).getItems()[0].getItem());
        ItemStack result = recipe.getResultItem(null);
        int outputSlot = getClearOutput(result);

        this.inputItemHandler.extractItem(slot, 1, false);

        this.outputItemHandler.setStackInSlot(outputSlot,
                new ItemStack(result.getItem(),
                        this.outputItemHandler.getStackInSlot(outputSlot).getCount() + result.getCount()));
    }

    public boolean hasInfusion() {
        Optional<RecipeHolder<FairyInfusionRecipe>> recipe = getCurrentInfusion();
        if (recipe.isEmpty()){
            return false;
        }

        return isOutputClear();
    }

    private Optional<RecipeHolder<FairyInfusionRecipe>> getCurrentInfusion() {
        List<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < inputItemHandler.getSlots(); i++) {
            list.add(this.inputItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(FairyInfusionRecipe.Type.INSTANCE, new FairyInfusionInput(list), level);
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
        return getClearOutput(getCurrentInfusion().get().value().getResultItem(null)) != null;
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

}