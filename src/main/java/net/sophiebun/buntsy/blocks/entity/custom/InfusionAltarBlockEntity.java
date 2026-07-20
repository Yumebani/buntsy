package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class InfusionAltarBlockEntity extends BlockEntity {

    protected final List<BlockPos> linkedRelays = new ArrayList<>();
    protected final List<BlockPos> infusionPedestals = new ArrayList<>();
    protected int maxProgress = 999;
    protected int progress = 0;

    protected final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final Integer randomRotation;

    public Integer getRandomRotation() {
        return randomRotation;
    }

    public void addRelay(BlockPos pos){
        linkedRelays.add(pos);
    }

    public void removeRelay(Level pLevel, BlockPos pos){
        if (pLevel.getBlockEntity(pos) != null && pLevel.getBlockEntity(pos) instanceof  FairyPowerRelayBlockEntity){
            ((FairyPowerRelayBlockEntity) pLevel.getBlockEntity(pos)).removeLinked(pLevel);
        }
    }

    public void removeAllRelays(Level pLevel){
        for (BlockPos relayPos : infusionPedestals.stream().toList()){
            removeRelay(pLevel, relayPos);
            this.infusionPedestals.remove(relayPos);
        }
    }

    public void clearRelay(BlockPos pos){
        linkedRelays.remove(pos);
    }

    public InfusionAltarBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);

        Random random = new Random();
        randomRotation = random.nextInt(0, 270);
    }


    public ItemStackHandler getItemHandler(Direction side) {
        if (itemHandler.getStackInSlot(0).isEmpty()){
            return itemHandler;
        } else return null;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        inventory.setItem(0, itemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("inventory", itemHandler.serializeNBT(registries));
        pTag.putInt("infusion_altar.progress", this.progress);
        pTag.putInt("infusion_altar.max_progress", this.maxProgress);

        pTag.putInt("infusion_altar.linked_relays_count", this.linkedRelays.size());

        for (int i = 0; i < this.linkedRelays.size(); i++){
            BlockPos pos = this.linkedRelays.get(0);
            pTag.put("infusion_altar.linked_relay_" + i, NbtUtils.writeBlockPos(pos));
        }

        pTag.putInt("infusion_altar.infusion_pedestals_count", this.infusionPedestals.size());

        for (int i = 0; i < this.infusionPedestals.size(); i++){
            BlockPos pos = this.infusionPedestals.get(i);
            pTag.put("infusion_altar.infusion_pedestal_" + i, NbtUtils.writeBlockPos(pos));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.itemHandler.deserializeNBT(registries, pTag.getCompound("inventory"));
        this.progress = pTag.getInt("infusion_altar.progress");
        this.maxProgress = pTag.getInt("infusion_altar.max_progress");

        int loopCount = pTag.getInt("infusion_altar.linked_relays_count");

        for (int i = 0; i < loopCount; i++){
            this.linkedRelays.add(NbtUtils.readBlockPos(pTag, "infusion_altar.linked_relay_" + i).get());
        }

        loopCount = pTag.getInt("infusion_altar.infusion_pedestals_count");

        for (int i = 0; i < loopCount; i++){
            this.infusionPedestals.add(NbtUtils.readBlockPos(pTag, "infusion_altar.infusion_pedestal_" + i).get());
        }
    }

    public ItemStack getItem(){
        return itemHandler.getStackInSlot(0);
    }

    public ItemStack extractItem(){
        return itemHandler.extractItem(0, 1, false);
    }

    public void depositItem(ItemStack item) {
        if (itemHandler.getStackInSlot(0).isEmpty()){
            itemHandler.setStackInSlot(0, item);
        }
    }

    public abstract void tick(Level pLevel, BlockPos pPos, BlockState pState);

    public void animateParticles(Level pLevel, BlockPos anchor){

        ((ServerLevel) pLevel).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemHandler.getStackInSlot(0)),
                anchor.getX()+ 0.5f, anchor.getY() + 1f, anchor.getZ()+ 0.5f, 2,
                0, 0, 0, 0.1f);

        for (BlockPos pos : infusionPedestals){

            ((ServerLevel) pLevel).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, getItemFromPedestal(pLevel, pos)),
                    pos.getX()+ 0.5f, pos.getY() + 1f, pos.getZ()+ 0.5f, 2,
                    0, 0, 0, 0.1f);
        }

        for (BlockPos pos : linkedRelays){

            BlockEntity entity = level.getBlockEntity(pos);

            if (entity instanceof FairyPowerRelayBlockEntity){
                ((FairyPowerRelayBlockEntity) entity).animateParticles((ServerLevel) pLevel, pos);
            }
        }
    }



    public boolean canRun(Level pLevel){
        return altarIsValid() && getEnchantedRelayCount(pLevel) > 0 && hasRecipe(pLevel);
    }

    public int getEnchantedRelayCount(Level level){
        int i = 0;
        for (BlockPos pos : linkedRelays){
            if (isRelayEnchanted(level, pos)){
                i++;
            }
        }
        return i;
    }

    public boolean isRelayEnchanted(Level level, BlockPos pos){
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof FairyPowerRelayBlockEntity){
            return ((FairyPowerRelayBlockEntity) entity).isEnchanted();
        }
        return false;
    }

    public abstract void craftItem(Level pLevel);

    public abstract boolean hasRecipe(Level pLevel);

    public abstract Optional<?> getCurrentRecipe(Level pLevel);

    protected boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    protected void increaseProgress(Level level) {
        this.progress += getEnchantedRelayCount(level);
    }

    protected void resetProgress() {
        this.progress = 0;
    }

    protected abstract void checkAltarValidity(Level level, BlockPos pos);

    protected ItemStack getItemFromPedestal(Level level, BlockPos pos){
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof InfusionPedestalBlockEntity){
            return ((InfusionPedestalBlockEntity) entity).getItem();
        }
        return ItemStack.EMPTY;
    }

    protected void extractItemFromPedestal(Level level, BlockPos pos){
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof InfusionPedestalBlockEntity){
            ((InfusionPedestalBlockEntity) entity).extractItem();
        }
    }

    protected void checkPedestalPlacement(Level level, BlockPos pos){
        BlockState state = level.getBlockState(pos);
        if (state.is(ModBlocks.INFUSION_PEDESTAL.get())){
            if (!infusionPedestals.contains(pos)){
                infusionPedestals.add(pos);
            }
        }
    }

    protected abstract boolean altarIsValid();


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