package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
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
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkSyrupExtractorBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.screen.clockwork.ClockworkSyrupExtractorMenu;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClockworkSyrupExtractorEntity extends ClockworkBlockEntity implements MenuProvider {

    public final ItemStackHandler inputItemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final ItemStackHandler outputItemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final ContainerData data;

    private int progress = 0;
    private int maxProgress = 1800;

    private int nextCheck = 0;

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_syrup_extractor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player pPlayer) {
        return new ClockworkSyrupExtractorMenu(i, inventory, this, this.data);
    }

    public ClockworkSyrupExtractorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_SYRUP_EXTRACTOR_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> ClockworkSyrupExtractorEntity.this.progress;
                    case 1 -> ClockworkSyrupExtractorEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> ClockworkSyrupExtractorEntity.this.progress = i1;
                    case 1 -> ClockworkSyrupExtractorEntity.this.maxProgress = i1;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
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
        SimpleContainer inventory = new SimpleContainer(18);

        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }

        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(i + 9, outputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("clockwork_syrup_extractor.inputInventory", inputItemHandler.serializeNBT(registries));
        pTag.put("clockwork_syrup_extractor.outputInventory", outputItemHandler.serializeNBT(registries));
        pTag.putInt("clockwork_syrup_extractor.progress", this.progress);
        pTag.putInt("clockwork_syrup_extractor.max_progress", this.maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.inputItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_syrup_extractor.inputInventory"));
        this.outputItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_syrup_extractor.outputInventory"));
        this.progress = pTag.getInt("clockwork_syrup_extractor.progress");
        this.maxProgress = pTag.getInt("clockwork_syrup_extractor.max_progress");
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
        return (this.outputItemHandler.getStackInSlot(slot).isEmpty() || this.outputItemHandler.getStackInSlot(slot).getItem() == item.getItem())
                && (this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount() <= item.getMaxStackSize());
    }


    public void insertIntoSlot(int slot, ItemStack item){
        ItemStack inserted;
        if (this.outputItemHandler.getStackInSlot(slot).isEmpty()){
            inserted = item;
        } else {
            inserted = new ItemStack(item.getItem(),
                    this.outputItemHandler.getStackInSlot(slot).getCount() + item.getCount());
        }
        inserted.applyComponents(item.getComponents());
        this.outputItemHandler.setStackInSlot(slot, inserted);
    }

    public Integer getAvailableSlot(ItemStack item){
        for (int i = 0; i < 9; i++){
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

    public ItemStack getInputStack(){
        for (int i = 0; i < 9; i++){
            if (this.inputItemHandler.getStackInSlot(i).getItem() instanceof BottleItem){
                return this.inputItemHandler.getStackInSlot(i);
            }
        }
        return null;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (pLevel.getBlockState(pPos.relative(pState.getValue(ClockworkSyrupExtractorBlock.FACING).getOpposite(), 1))
                .is(ModTags.Blocks.GENTLIT_LOGS)){

            this.progress += getClockworkProgressAmount();

            this.nextCheck++;
            if (this.nextCheck > 20){
                this.nextCheck = 0;
            }

            if (this.progress >= this.maxProgress){

                if (this.nextCheck % 10 == 0){
                    ItemStack input = getInputStack();
                    if (input != null && isOutputClear(new ItemStack(ModItems.GENTLIT_SYRUP.get()))){
                        input.shrink(1);
                        outputItems(new ItemStack(ModItems.GENTLIT_SYRUP.get()));
                        this.progress = 0;
                    } else {
                        this.progress = this.maxProgress;
                    }
                } else {
                    this.progress = this.maxProgress;
                }
            }
        }
    }
}
