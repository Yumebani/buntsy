package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkWinderBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.screen.clockwork.ClockworkWinderMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;
import java.util.List;

public class ClockworkWinderEntity extends ClockworkBlockEntity implements MenuProvider, GeoBlockEntity {

    public final ItemStackHandler inventoryItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    protected final ContainerData data;

    private int burnTicks = 0;
    private int maxBurnTicks = -1;

    private List<BlockPos> registeredBlocks = new ArrayList<>();
    private int totalWeight = 0;

    private final int BASE_WEIGHT = 2;

    private final int BASE_RANGE = 8;

    private int nextCheckTick = 0;
    private int nextRegisterTick = 0;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<ClockworkWinderEntity> controller;

    private PlayState predicate(AnimationState<ClockworkWinderEntity> clockworkFairyTerminalEntityAnimationState) {
        clockworkFairyTerminalEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.clockwork_winder.running", Animation.LoopType.LOOP));
        return getBlockState().getValue(ClockworkWinderBlock.BURNING) ? PlayState.CONTINUE : PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controller = new AnimationController<>(this, "controller", 2, this::predicate);
        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.buntsy.clockwork_winder");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player pPlayer) {
        return new ClockworkWinderMenu(i, inventory, this, this.data);
    }

    public ClockworkWinderEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_WINDER_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch(i) {
                    case 0 -> ClockworkWinderEntity.this.burnTicks;
                    case 1 -> ClockworkWinderEntity.this.maxBurnTicks;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> ClockworkWinderEntity.this.burnTicks = i1;
                    case 1 -> ClockworkWinderEntity.this.maxBurnTicks = i1;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public ItemStackHandler getItemHandler(Direction side) {
        return inventoryItemHandler;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, inventoryItemHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.put("clockwork_winder.inputInventory", inventoryItemHandler.serializeNBT(registries));
        pTag.putInt("clockwork_winder.burn_ticks", this.burnTicks);
        pTag.putInt("clockwork_winder.max_burn_ticks", this.maxBurnTicks);

        pTag.putInt("clockwork_winder.registered_block_count", this.registeredBlocks.size());
        for (int i = 0; i < this.registeredBlocks.size(); i++){
            pTag.put("clockwork_winder.registered_block_" + i, NbtUtils.writeBlockPos(this.registeredBlocks.get(i)));
        }

        pTag.putInt("clockwork_winder.total_weight", this.totalWeight);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        this.inventoryItemHandler.deserializeNBT(registries, pTag.getCompound("clockwork_winder.inputInventory"));
        this.burnTicks = pTag.getInt("clockwork_winder.burn_ticks");
        this.maxBurnTicks = pTag.getInt("clockwork_winder.max_burn_ticks");

        int size = pTag.getInt("clockwork_winder.registered_block_count");
        for (int i = 0; i < size; i++){
            this.registeredBlocks.add(NbtUtils.readBlockPos(pTag, "clockwork_winder.registered_block_" + i).get());
        }

        this.totalWeight = pTag.getInt("clockwork_winder.total_weight");
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

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if (this.burnTicks > 0) {

            this.burnTicks -= getBurn();

            if (nextRegisterTick <= 0){
                registerBlocks();
            } else {
                nextRegisterTick--;
            }

            if (nextCheckTick <= 0){
                windupBlocks();
            } else {
                nextCheckTick--;
            }

        } else if (this.maxBurnTicks != -1){
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(ClockworkWinderBlock.BURNING, false));
            this.maxBurnTicks = -1;
        }

        if (this.maxBurnTicks == -1 && inventoryItemHandler.getStackInSlot(0).getBurnTime(RecipeType.SMELTING) > 0) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(ClockworkWinderBlock.BURNING, true));
            consumeFuel();
        }
    }

    private void windupBlocks(){
        nextCheckTick = 5;
        this.totalWeight = 0;
        for (BlockPos pos : registeredBlocks){
            if (this.level.isLoaded(pos) && this.level.getBlockEntity(pos) instanceof WindupClockworkEntity){
                WindupClockworkEntity entity = ((WindupClockworkEntity) this.level.getBlockEntity(pos));
                entity.windup(this.getBlockPos());
                this.totalWeight += entity.getWindupWeight();
            }
        }
    }

    private void consumeFuel(){
        this.maxBurnTicks = inventoryItemHandler.getStackInSlot(0).getBurnTime(RecipeType.SMELTING) * getBurnTimeModifier();
        this.burnTicks = this.maxBurnTicks;

        ItemStack remainderStack = inventoryItemHandler.getStackInSlot(0).getCraftingRemainingItem();
        if (remainderStack.isEmpty()){
            inventoryItemHandler.getStackInSlot(0).shrink(1);
        } else {
            inventoryItemHandler.setStackInSlot(0, remainderStack);
        }
    }

    private void registerBlocks(){
        nextRegisterTick = 30;

        int range = getMaxDistance();

        for (BlockPos pos : BlockPos.withinManhattan(this.getBlockPos(), range, range / 2, range)){
            if (this.level.isLoaded(pos) && !registeredBlocks.contains(pos) && this.level.getBlockEntity(pos) instanceof WindupClockworkEntity
            && !(((WindupClockworkEntity) this.level.getBlockEntity(pos)).isWindedUpBy())){
                this.registeredBlocks.add(pos.offset(0, 0, 0));
            }
        }
    }

    protected int getBurn(){
        return BASE_WEIGHT + totalWeight;
    }

    protected int getBurnTimeModifier(){
        return switch (clockworkTier){
            case NONE -> 4;
            case SIMPLE -> 6;
            case INTRICATE -> 8;
            case COMPLEX -> 12;
        };
    }

    protected int getMaxDistance(){
        return BASE_RANGE + getClockworkRangeAmount();
    }

    protected int getClockworkRangeAmount() {
        return switch (clockworkTier){
            case NONE -> 0;
            case SIMPLE -> 2;
            case INTRICATE -> 4;
            case COMPLEX -> 8;
        };
    }

    public boolean isBurning() {
        return maxBurnTicks != -1 && burnTicks > maxBurnTicks;
    }
}
