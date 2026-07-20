package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ClockworkPassiveCollectorEntity extends ClockworkBlockEntity {

    public final ItemStackHandler inventoryItemHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    protected final ContainerData data;

    private int progress = 0;
    protected abstract int getProgressCheck(Level level, BlockPos blockPos, RandomSource randomSource);

    public ClockworkPassiveCollectorEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> ClockworkPassiveCollectorEntity.this.progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> ClockworkPassiveCollectorEntity.this.progress = i1;
                };
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public ItemStackHandler getItemHandler(Direction side) {
        return inventoryItemHandler;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(15);

        for (int i = 0; i < inventoryItemHandler.getSlots(); i++) {
            inventory.setItem(i, inventoryItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("clockwork_passive_collector.inputInventory", inventoryItemHandler.serializeNBT(registries));
        pTag.putInt("clockwork_passive_collector.progress", this.progress);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.inventoryItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_passive_collector.inputInventory"));
        this.progress = pTag.getInt("clockwork_passive_collector.progress");
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

    public boolean isSlotClear(int slot, ItemStack item){
        return (this.inventoryItemHandler.getStackInSlot(slot).isEmpty() || this.inventoryItemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.inventoryItemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }


    public void insertIntoSlot(int slot, ItemStack item){
        ItemStack inserted;
        if (this.inventoryItemHandler.getStackInSlot(slot).isEmpty()){
            inserted = item;
        } else {
            inserted = new ItemStack(item.getItem(),
                    this.inventoryItemHandler.getStackInSlot(slot).getCount() + item.getCount());
        }
        inserted.applyComponents(item.getComponents());
        this.inventoryItemHandler.setStackInSlot(slot, inserted);
    }

    public Integer getAvailableSlot(ItemStack item){
        for (int i = 0; i < 15; i++){
            if (isSlotClear(i, item)) {
                return i;
            }
        }
        return null;
    }


    public boolean isOutputClear(ItemStack stack){
        return getAvailableSlot(stack) != null;
    }

    public void outputItems(ItemStack stack){
        int pSlot = getAvailableSlot(stack);
        insertIntoSlot(pSlot, stack);
    }

    protected abstract ItemStack generateOutput(RandomSource random);

    protected abstract boolean canWork(Level level, BlockPos pPos, BlockState pState);

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (!canWork(pLevel, pPos, pState)) return;

        this.progress += getClockworkProgressAmount();

        if (this.progress >= getProgressCheck(pLevel, pPos, pLevel.random)){

            ItemStack output = generateOutput(pLevel.random);
            if (isOutputClear(output)){
                outputItems(output);
            }

            this.progress = 0;
        }
    }
}
