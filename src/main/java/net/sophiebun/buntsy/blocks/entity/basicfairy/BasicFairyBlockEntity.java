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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BasicFairyBlockEntity extends FairyInteractBlockEntity {

    protected final ItemStackHandler inputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public LazyOptional<IItemHandler> getInputLazyItemHandler() {
        return inputLazyItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputLazyItemHandler() {
        return outputLazyItemHandler;
    }

    protected final ItemStackHandler outputItemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    protected static final int INPUT_SLOT = 0;
    protected static final int OUTPUT_SLOT = 0;
    protected static final int SECONDARY_OUTPUT_SLOT = 1;
    protected static final int FAIRY_WEIGHT = 1;

    protected final ContainerData data;
    protected int progress = 0;
    protected int maxProgress = 200;
    protected int nextRollChance = 0;


    private LazyOptional<IItemHandler> inputLazyItemHandler = LazyOptional.of(() -> this.inputItemHandler);
    private LazyOptional<IItemHandler> outputLazyItemHandler = LazyOptional.of(() -> this.outputItemHandler);

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
        SimpleContainer inventory = new SimpleContainer(3);
        inventory.setItem(0, inputItemHandler.getStackInSlot(0));
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(i + 1, outputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inputInventory", inputItemHandler.serializeNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        pTag.putInt("basic_fairy_block.progress", this.progress);
        pTag.putInt("basic_fairy_block.max_progress", this.maxProgress);
        pTag.putInt("basic_fairy_block.next_roll_chance", this.nextRollChance);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.inputItemHandler.deserializeNBT(pTag.getCompound("inputInventory"));
        this.outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
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
                craftItem();
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
        return hasRecipe() && isEnchanted();
    }

    public abstract void craftItem();

    public abstract boolean hasRecipe();

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
        return this.inputItemHandler.getStackInSlot(0);
    }

    public void insertIntoSlot(int slot, ItemStack item){
        this.outputItemHandler.setStackInSlot(slot,
                new ItemStack(item.getItem(),
                        this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount()));
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
        return (this.outputItemHandler.getStackInSlot(slot).isEmpty() || this.outputItemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }

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
