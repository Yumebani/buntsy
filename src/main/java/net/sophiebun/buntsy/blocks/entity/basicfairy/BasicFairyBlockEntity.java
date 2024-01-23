package net.sophiebun.buntsy.blocks.entity.basicfairy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.recipe.TempRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BasicFairyBlockEntity extends FairyInteractBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int SECONDARY_OUTPUT_SLOT = 2;
    private static final int FAIRY_WEIGHT = 1;

    private static final List<TempRecipe> recipeList = new ArrayList<>();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int nextRollChance = 0;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public BasicFairyBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> BasicFairyBlockEntity.this.progress;
                    case 1 -> BasicFairyBlockEntity.this.maxProgress;
                    case 2 -> BasicFairyBlockEntity.this.nextRollChance;
                    case 3 -> BasicFairyBlockEntity.this.isEnchanted() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> BasicFairyBlockEntity.this.progress = i1;
                    case 1 -> BasicFairyBlockEntity.this.maxProgress = i1;
                    case 2 -> BasicFairyBlockEntity.this.nextRollChance = i1;
                    case 3 -> BasicFairyBlockEntity.this.setEnchanted(i1 == 1);
                };
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of((() -> itemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("basic_fairy_block.progress", this.progress);
        pTag.putInt("basic_fairy_block.max_progress", this.maxProgress);
        pTag.putInt("basic_fairy_block.next_roll_chance", this.nextRollChance);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("basic_fairy_block.progress");
        this.maxProgress = pTag.getInt("basic_fairy_block.max_progress");
        this.nextRollChance = pTag.getInt("basic_fairy_block.next_roll_chance");
    }

    @Override
    public int getFairyWeight() {
        return FAIRY_WEIGHT;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        setAdditional(pLevel, pPos, pState);

        if (canRun()){

            setRunning(pLevel, pPos, pState, true);

            if (!Mth.equal(getConsumption(), 1f)){
                setConsumption(1f);
            }

            increaseProgress();

            if (hasProgressFinished()){
                resetProgress();
                rollNewChance();
                tempCraftItem();
                tick(pLevel, pPos, pState);
            }
        }
        else{

            setRunning(pLevel, pPos, pState, false);

            if (Mth.equal(getConsumption(), 1f)){
                setConsumption(0);
            }

            resetProgress();
        }
    }

    public void setAdditional(Level pLevel, BlockPos pPos, BlockState pState) {
    }

    public void setRunning(Level pLevel, BlockPos pPos, BlockState pState, boolean b) {
        if (getBlockState().getValue(ThreadReelerBlock.RUNNING) != b){
            pState = pState.setValue(ThreadReelerBlock.RUNNING, b);
            pLevel.setBlock(pPos, pState, 3);
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    }

    public boolean canRun(){
        return tempHasRecipe() && isEnchanted();
    }

    public void rollNewChance(){
        this.nextRollChance = this.level.random.nextInt(0, 100);
    }

    public boolean isOutputClear(ItemStack primary, ItemStack secondary){
        Integer pSlot = getPrimaryAvailableSlot(primary);
        return ((secondary == null && pSlot != null) || (secondary != null && pSlot == OUTPUT_SLOT));
    }

    public void outputItems(ItemStack primary, ItemStack secondary){
        int pSlot = getPrimaryAvailableSlot(primary);
        insertIntoSlot(pSlot, primary);
        if (secondary != null){
            insertIntoSlot(SECONDARY_OUTPUT_SLOT, secondary);
        }
    }

    public ItemStack getInputStack(){
        return this.itemHandler.getStackInSlot(INPUT_SLOT);
    }

    public void insertIntoSlot(int slot, ItemStack item){
        this.itemHandler.setStackInSlot(slot,
                new ItemStack(item.getItem(),
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + item.getCount()));
    }

    public Integer getPrimaryAvailableSlot(ItemStack item){
        for (int i = OUTPUT_SLOT; i <= SECONDARY_OUTPUT_SLOT; i++){
            if (isSlotClear(i, item)) {
                return i;
            }
        }
        return null;
    }

    public boolean isSlotClear(int slot, ItemStack item){
        return (this.itemHandler.getStackInSlot(slot).isEmpty() || this.itemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.itemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }

    public void tempCraftItem() {
        TempRecipe recipe = tempGetCurrentRecipe();
        List<ItemStack> result = recipe.getResults(this.nextRollChance);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        outputItems(result.get(0), result.size() == 1 ? null : result.get(2));
    }

    public boolean tempHasRecipe() {
        TempRecipe recipe = tempGetCurrentRecipe();
        if (recipe == null){
            return false;
        }

        List<ItemStack> result = recipe.getResults(this.nextRollChance);
        return isOutputClear(result.get(0), result.size() == 1 ? null : result.get(2));
    }

    public TempRecipe tempGetCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        for (TempRecipe recipe : getRecipeList()){
            if (recipe.isPresent(inventory, new int[]{INPUT_SLOT})){
                return recipe;
            }
        }

        return null;
    }

    public List<TempRecipe> getRecipeList() {
        return recipeList;
    }
    /*

    private void craftItem() {
        Optional<GrindingWheelRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(result.getItem(),
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<GrindingWheelRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            System.out.println("no recipe detected");
            return false;
        }
        ItemStack result = recipe.get().getResultItem(null);

        return isOutputClear(result, OUTPUT_SLOT);
    }

    private boolean isOutputClear(ItemStack result, int slot) {
        return (this.itemHandler.getStackInSlot(slot).isEmpty() || this.itemHandler.getStackInSlot(slot).getItem() == result.getItem())
                && (this.itemHandler.getStackInSlot(slot).getCount() + result.getCount() <= result.getMaxStackSize());
    }


    private Optional<GrindingWheelRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GrindingWheelRecipe.Type.INSTANCE, inventory, level);
    }
     */

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseProgress() {
        this.progress++;
    }

    private void resetProgress() {
        this.progress = 0;
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
