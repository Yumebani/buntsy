package net.sophiebun.buntsy.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;
import net.sophiebun.buntsy.recipe.InfusionAltarBasicRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class InfusionAltarBasicBlockEntity extends BlockEntity {

    private final List<BlockPos> linkedRelays = new ArrayList<>();
    private final List<BlockPos> infusionPedestals = new ArrayList<>();
    private int maxProgress = 999;
    private int progress = 0;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final Integer randomRotation;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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

    public InfusionAltarBasicBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INFUSION_ALTAR_BASIC_BLOCK_ENTITY.get(), pPos, pBlockState);

        Random random = new Random();
        randomRotation = random.nextInt(0, 270);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (itemHandler.getStackInSlot(0).isEmpty()){
                return lazyItemHandler.cast();
            }
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
        inventory.setItem(0, itemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
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

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("infusion_altar.progress");
        this.maxProgress = pTag.getInt("infusion_altar.max_progress");

        int loopCount = pTag.getInt("infusion_altar.linked_relays_count");

        for (int i = 0; i < loopCount; i++){
            this.linkedRelays.add(NbtUtils.readBlockPos(pTag.getCompound("infusion_altar.linked_relay_" + i)));
        }

        loopCount = pTag.getInt("infusion_altar.infusion_pedestals_count");

        for (int i = 0; i < loopCount; i++){
            this.infusionPedestals.add(NbtUtils.readBlockPos(pTag.getCompound("infusion_altar.infusion_pedestal_" + i)));
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

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        checkAltarValidity(pLevel, pPos);

        if (canRun(pLevel)){

            maxProgress = getCurrentRecipe(pLevel).get().getMaxProgress();
            animateParticles(pLevel, pPos);
            increaseProgress(pLevel);

            if (hasProgressFinished()){
                resetProgress();
                craftItem(pLevel);
                tick(pLevel, pPos, pState);
            }
        }
        else{
            resetProgress();
        }
    }

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
            ((ServerLevel) pLevel).sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX()+ 0.5f, pos.getY() + 1f, pos.getZ()+ 0.5f, 2,
                    0, 0, 0, 0.1f);
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

    public void craftItem(Level pLevel) {
        InfusionAltarBasicRecipe recipe = getCurrentRecipe(pLevel).get();
        ItemStack result = recipe.getResultItem(null);

        this.itemHandler.extractItem(0, 1, false);
        for (int i = 0; i < 4; i++){
            extractItemFromPedestal(pLevel, infusionPedestals.get(i));
        }

        itemHandler.setStackInSlot(0, result);
    }

    public boolean hasRecipe(Level pLevel) {
        Optional<InfusionAltarBasicRecipe> recipe = getCurrentRecipe(pLevel);

        return !recipe.isEmpty();
    }

    public Optional<InfusionAltarBasicRecipe> getCurrentRecipe(Level pLevel) {
        SimpleContainer inventory = new SimpleContainer(5);
        inventory.setItem(0, itemHandler.getStackInSlot(0));
        for (int i = 1; i <= 4; i++){
            inventory.setItem(i, getItemFromPedestal(pLevel, infusionPedestals.get(i - 1)));
        }

        return this.level.getRecipeManager().getRecipeFor(InfusionAltarBasicRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseProgress(Level level) {
        this.progress += getEnchantedRelayCount(level);
    }

    private void resetProgress() {
        this.progress = 0;
    }


    private void checkAltarValidity(Level level, BlockPos pos) {
        checkPedestalPlacement(level, pos.relative(Direction.NORTH, 2));
        checkPedestalPlacement(level, pos.relative(Direction.EAST, 2));
        checkPedestalPlacement(level, pos.relative(Direction.SOUTH, 2));
        checkPedestalPlacement(level, pos.relative(Direction.WEST, 2));
    }

    private ItemStack getItemFromPedestal(Level level, BlockPos pos){
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof InfusionPedestalBlockEntity){
            return ((InfusionPedestalBlockEntity) entity).getItem();
        }
        return ItemStack.EMPTY;
    }

    private void extractItemFromPedestal(Level level, BlockPos pos){
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof InfusionPedestalBlockEntity){
            ((InfusionPedestalBlockEntity) entity).extractItem();
        }
    }

    private void checkPedestalPlacement(Level level, BlockPos pos){
        BlockState state = level.getBlockState(pos);
        if (state.is(ModBlocks.INFUSION_PEDESTAL.get())){
            if (!infusionPedestals.contains(pos)){
                infusionPedestals.add(pos);
            }
        }
    }

    private boolean altarIsValid() {
        return infusionPedestals.size() == 4;
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