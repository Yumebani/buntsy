package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.item.ClockworkTier;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.ClockworkUpgradeItem;

import javax.annotation.Nullable;
import java.util.*;

public class ClockworkMaiden extends PathfinderMob {

    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState windUpAnimationState = new AnimationState();
    public AnimationState ItemCarryStanceAnimationState = new AnimationState();

    protected ClockworkTier clockworkTier = ClockworkTier.NONE;
    private ItemStack upgradeItem = null;

    private final Map<MaidenTask, Pair<ItemStack, MaidenInteractionConfig>> carriedItems = new HashMap<>();
    private MaidenTask currentTask = null;
    private BlockPos terminal = null;
    private BlockPos target = null;

    boolean animInit = false;

    private static final EntityDataAccessor<ItemStack> CARRIED_ITEM =
            SynchedEntityData.defineId(ClockworkMaiden.class, EntityDataSerializers.ITEM_STACK);

    public ClockworkMaiden(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CARRIED_ITEM, ItemStack.EMPTY);
    }

    public ItemStack getCarriedItem() {
        return this.entityData.get(CARRIED_ITEM);
    }

    public void setCarriedItem(ItemStack stack) {
        this.entityData.set(CARRIED_ITEM, stack);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pTag) {
        super.addAdditionalSaveData(pTag);

        pTag.putInt("clockwork_maiden.clockwork_tier", this.clockworkTier.ordinal());
        pTag.putBoolean("clockwork_maiden.has_upgrade_item", upgradeItem != null);
        if (upgradeItem != null){
            CompoundTag tag = new CompoundTag();
            upgradeItem.save(tag);
            pTag.put("clockwork_maiden.upgrade_item", tag);
        }

        pTag.putInt("clockwork_maiden.carried_items_size", carriedItems.size());
        for (int i = 0; i < carriedItems.size(); i++){
            MaidenTask task = ((MaidenTask) carriedItems.keySet().toArray()[i]);
            CompoundTag tag = new CompoundTag();
            carriedItems.get(task).getFirst().save(tag);
            pTag.put("clockwork_maiden.carried_items_task_" + i, task.getCompound());
            pTag.put("clockwork_maiden.carried_items_item_" + i, tag);
            pTag.put("clockwork_maiden.carried_items_config_" + i, carriedItems.get(task).getSecond().getCompound());
        }

        pTag.putBoolean("clockwork_maiden.has_current_task", this.currentTask != null);
        if (this.currentTask != null){
            pTag.put("clockwork_maiden.current_task", this.currentTask.getCompound());
        }

        pTag.putBoolean("clockwork_maiden.has_terminal", this.terminal != null);
        if (this.terminal != null){
            pTag.put("clockwork_maiden.terminal", NbtUtils.writeBlockPos(this.terminal));
        }

        pTag.putBoolean("clockwork_maiden.has_target", this.target != null);
        if (this.target != null){
            pTag.put("clockwork_maiden.target", NbtUtils.writeBlockPos(this.target));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pTag) {

        this.clockworkTier = ClockworkTier.values()[pTag.getInt("clockwork_maiden.clockwork_tier")];
        if (pTag.getBoolean("clockwork_maiden.has_upgrade_item")){
            this.upgradeItem = ItemStack.of(pTag.getCompound("clockwork_maiden.upgrade_item"));
        } else {
            this.upgradeItem = null;
        }

        int size = pTag.getInt("clockwork_maiden.carried_items_size");
        for (int i = 0; i < size; i++){
            this.carriedItems.put(
                    MaidenTask.parseCompound(pTag.getCompound("clockwork_maiden.carried_items_task_" + i)),
                    Pair.of(ItemStack.of(pTag.getCompound("clockwork_maiden.carried_items_item_" + i)),
                            MaidenInteractionConfig.parseCompound(pTag.getCompound("clockwork_maiden.carried_items_config_" + i))));
        }

        if (pTag.getBoolean("clockwork_maiden.has_current_task")){
            this.currentTask = MaidenTask.parseCompound(pTag.getCompound("clockwork_maiden.current_task"));
        }

        if (pTag.getBoolean("clockwork_maiden.has_terminal")){
            this.terminal = NbtUtils.readBlockPos(pTag.getCompound("clockwork_maiden.terminal"));
        }

        if (pTag.getBoolean("clockwork_maiden.has_target")){
            this.target = NbtUtils.readBlockPos(pTag.getCompound("clockwork_maiden.target"));
        }

        super.readAdditionalSaveData(pTag);

        reAssesGoals();
    }

    @Override
    public void tick() {
        super.tick();
        animate();
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public InteractionResult putUpgrade(ItemStack potentialUpgrade, Level level, Player player, InteractionHand hand){

        if (potentialUpgrade.getItem() instanceof ClockworkUpgradeItem){

            this.ejectItem(level, player);
            this.upgradeItem = potentialUpgrade;
            this.clockworkTier = ((ClockworkUpgradeItem) this.upgradeItem.getItem()).getTier();
            player.setItemInHand(hand, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void ejectItem(Level level, Player player){
        if (this.upgradeItem != null){
            level.addFreshEntity(new ItemEntity(level, player.position().x, player.position().y + 1f, player.position().z, this.upgradeItem));
            this.clockworkTier = ClockworkTier.NONE;
            this.upgradeItem = ItemStack.EMPTY;
        }
    }

    protected double getMoveSpeed(){
        return switch (clockworkTier){
            case NONE -> 1.0;
            case SIMPLE -> 1.5;
            case INTRICATE -> 2.0;
            case COMPLEX -> 2.5;
        };
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {

        if (pPlayer.getItemInHand(pHand).getItem() instanceof ClockworkUpgradeItem){
            putUpgrade(pPlayer.getItemInHand(pHand), pPlayer.level(), pPlayer, pHand);
            return InteractionResult.SUCCESS;
        } else if (pPlayer.isCrouching() && pPlayer.getItemInHand(pHand).isEmpty()){
            ejectItem(pPlayer.level(), pPlayer);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(pPlayer, pHand);
    }

    private void animate(){

        if (this.level().isClientSide()){
            if (!animInit){
                if (!windUpAnimationState.isStarted()){
                    windUpAnimationState.start(this.tickCount);
                }

                if (!idleAnimationState.isStarted()){
                    idleAnimationState.start(this.tickCount);
                }
                animInit = true;
            }

            if (!getCarriedItem().isEmpty()){
                ItemCarryStanceAnimationState.start(this.tickCount);
            } else {
                ItemCarryStanceAnimationState.stop();
            }
        }
    }

    protected void registerGoals() {
        reAssesGoals();
    }

    protected void reAssesGoals(){
        this.goalSelector.removeAllGoals(goal -> true);
        if (terminal != null){
            this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(10, new StayWithinPremises(this, terminal, 1.0D, 16));
            this.goalSelector.addGoal(20, new MaidenTaskGoal(this, terminal));
        } else {
            this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
            this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.CALCITE_PLACE, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.COPPER_HIT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.COPPER_BREAK;
    }

    public boolean canFreeze() {
        return false;
    }

    public void aiStep() {
        super.aiStep();
    }
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 1.4F;
    }

    public double getMyRidingOffset() {
        return -0.6D;
    }

    public void clearBlockEntityData(Level level) {
        this.terminal = null;
        this.currentTask = null;
        for (MaidenTask key : this.carriedItems.keySet()){
            level.addFreshEntity(new ItemEntity(level, this.position().x, this.position().y, this.position().z, this.carriedItems.get(key).getFirst()));
        }
        this.carriedItems.clear();
        this.reAssesGoals();
    }

    public void clearCarriedData(Level level, MaidenTask task) {
        this.currentTask = null;
        for (MaidenTask key : this.carriedItems.keySet().stream().toList()){
            if (task.equals(key)){
                level.addFreshEntity(new ItemEntity(level, this.position().x, this.position().y, this.position().z, this.carriedItems.get(key).getFirst()));
                this.carriedItems.remove(key);
            }
        }
    }

    public void removeCarriedItem(MaidenTask task, Level level){
        level.addFreshEntity(new ItemEntity(level, this.position().x, this.position().y, this.position().z, this.carriedItems.get(task).getFirst()));
        this.carriedItems.remove(task);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        Level level = level();
        this.clearBlockEntityData(level);
        if (!this.upgradeItem.isEmpty()){
            level.addFreshEntity(new ItemEntity(level, this.position().x, this.position().y, this.position().z, this.upgradeItem));
        }
        level.addFreshEntity(new ItemEntity(level, this.position().x, this.position().y, this.position().z, new ItemStack(ModItems.CLOCKWORK_MAIDEN.get())));
    }

    public boolean containsTerminal(ClockworkMaidenTerminalEntity blockEntity) {
        return this.terminal != null;
    }

    public void registerTerminal(ClockworkMaidenTerminalEntity blockEntity, Level level) {
        this.clearBlockEntityData(level);
        this.terminal = blockEntity.getBlockPos();
        this.reAssesGoals();
    }

    protected class MaidenTaskGoal extends Goal{

        private final ClockworkMaiden maiden;
        private final BlockPos terminal;

        private final Deque<MaidenTask> tasksQueue = new ArrayDeque<>();

        public MaidenTaskGoal(ClockworkMaiden maiden, BlockPos terminal) {
            this.maiden = maiden;
            this.terminal = terminal;
        }

        private boolean isTerminalValid(){
            if (maiden.level().isLoaded(terminal)){
                BlockEntity blockEntity = maiden.level().getBlockEntity(terminal);
                if (blockEntity != null && (blockEntity instanceof ClockworkMaidenTerminalEntity)){
                    return true;
                }
            }
            return false;
        }

        private boolean isBlockEntityValid(BlockPos pos){
            if (maiden.level().isLoaded(pos)){
                BlockEntity blockEntity = maiden.level().getBlockEntity(pos);
                if (blockEntity != null){
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            if (maiden.currentTask != null){
                if (!isBlockEntityValid(target)){
                    maiden.currentTask = null;
                    if (!getCarriedItem().isEmpty()){
                        setCarriedItem(ItemStack.EMPTY);
                    }
                }
            }
            return maiden.currentTask != null ;
        }

        @Override
        public boolean canUse() {

            if (maiden.currentTask != null && navigation.isDone()){
                setTarget(this.maiden.target);
                return true;
            }
            else if (isTerminalValid() && maiden.currentTask == null){
                if (!tasksQueue.isEmpty()){
                    for (int i = 0; i < tasksQueue.size(); i++){
                        MaidenTask task = tasksQueue.removeFirst();
                        Pair<ItemStack, MaidenInteractionConfig> objective = maiden.carriedItems.get(task);

                        if (maiden.level().isLoaded(objective.getSecond().getPos())) {

                            if (isBlockEntityValid(objective.getSecond().getPos())){
                                if (MaidenTask.tryPlace(maiden.level(), objective.getSecond(), objective.getFirst(), true).getCount() < objective.getFirst().getCount()
                                    && this.setTarget(objective.getSecond().getPos())) {
                                    setCarriedItem(objective.getFirst());
                                    maiden.currentTask = task;
                                    return true;
                                } else {
                                    tasksQueue.addLast(task);
                                }
                            } else {
                                removeCarriedItem(task, maiden.level());
                            }

                        } else {
                            tasksQueue.addLast(task);
                        }
                    }
                }

                ClockworkMaidenTerminalEntity terminalEntity = ((ClockworkMaidenTerminalEntity) maiden.level().getBlockEntity(terminal));
                for (int i = 0; i < terminalEntity.getTotalTasks(); i++){
                    MaidenTask task = terminalEntity.getTask();
                    if (task != null && !this.tasksQueue.contains(task) && isBlockEntityValid(task.getExtractPos()) && task.achievable(maiden.level())
                        && this.setTarget(task.getExtractPos())){
                        maiden.currentTask = task;
                        return true;
                    }
                }
            }
            return false;
        }

        public BlockPos getValidAdjacentPos(Level level, BlockPos targetPos) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos sidePos = targetPos.relative(direction);

                if (isValidStandSpace(level, sidePos)) {
                    return sidePos;
                }
            }
            return targetPos.above();
        }

        private boolean isValidStandSpace(Level level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            BlockState aboveState = level.getBlockState(pos.above());
            BlockState belowState = level.getBlockState(pos.below());

            return state.getCollisionShape(level, pos).isEmpty()
                    && aboveState.getCollisionShape(level, pos.above()).isEmpty()
                    && !belowState.getCollisionShape(level, pos.below()).isEmpty();
        }

        private boolean setTarget(BlockPos pos){
            this.maiden.target = pos;
            Path path = maiden.getNavigation().createPath(getValidAdjacentPos(maiden.level(), pos), 1);
            if (path != null){
                this.maiden.getNavigation().moveTo(path, this.maiden.getMoveSpeed());
                return true;
            }
            else return false;
        }

        void playItemSound() {
            this.maiden.level().playSound(null, this.maiden.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL,
                    1f, this.maiden.random.nextInt(8,12) * 0.1f);
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void tick() {

            if (maiden.getNavigation().isDone() && maiden.position().distanceTo(this.maiden.target.getCenter()) <= 3f){
                if (!maiden.carriedItems.containsKey(maiden.currentTask)){

                    Pair<ItemStack, MaidenInteractionConfig> objective = maiden.currentTask.getNextDelivery(maiden.level());
                    if (objective != null){
                        playItemSound();
                        this.maiden.carriedItems.put(maiden.currentTask, objective);
                        this.tasksQueue.addFirst(currentTask);
                    }
                    maiden.currentTask = null;
                    this.maiden.target = null;

                } else {

                    Pair<ItemStack, MaidenInteractionConfig> objective = maiden.carriedItems.get(maiden.currentTask);
                    if (isBlockEntityValid(objective.getSecond().getPos())){
                        ItemStack remainder = MaidenTask.tryPlace(maiden.level(), objective.getSecond(), objective.getFirst(), false);
                        if (remainder.getCount() != objective.getFirst().getCount()){
                            playItemSound();
                        }

                        if (remainder.isEmpty()){
                            maiden.carriedItems.remove(maiden.currentTask);
                            maiden.currentTask = null;
                            this.maiden.target = null;
                            setCarriedItem(ItemStack.EMPTY);
                        } else {
                            this.maiden.carriedItems.put(maiden.currentTask, Pair.of(remainder, objective.getSecond()));
                            this.tasksQueue.addLast(maiden.currentTask);
                            maiden.currentTask = null;
                            this.maiden.target = null;
                            setCarriedItem(ItemStack.EMPTY);
                        }
                    }
                    else {
                        clearCarriedData(maiden.level(), this.maiden.currentTask);
                    }
                }
            } else if (maiden.getNavigation().isDone()){
                setTarget(this.maiden.target);
            }
        }
    }

    protected static class StayWithinPremises extends Goal {

        protected final BlockPos terminal;
        protected final double speedModifier;
        protected final int maxDistance;
        protected final ClockworkMaiden maiden;

        public StayWithinPremises(ClockworkMaiden maiden, BlockPos terminal, double speedModifier, int maxDistance) {
            this.terminal = terminal;
            this.speedModifier = speedModifier;
            this.maxDistance = maxDistance;
            this.maiden = maiden;
        }

        public BlockPos getValidAdjacentPos(Level level, BlockPos targetPos) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos sidePos = targetPos.relative(direction);

                if (isValidStandSpace(level, sidePos)) {
                    return sidePos;
                }
            }
            return targetPos.above();
        }

        private boolean isValidStandSpace(Level level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            BlockState aboveState = level.getBlockState(pos.above());
            BlockState belowState = level.getBlockState(pos.below());

            return state.getCollisionShape(level, pos).isEmpty()
                    && aboveState.getCollisionShape(level, pos.above()).isEmpty()
                    && !belowState.getCollisionShape(level, pos.below()).isEmpty();
        }

        private boolean setTarget(BlockPos pos){
            Path path = maiden.getNavigation().createPath(getValidAdjacentPos(maiden.level(), pos), 2);
            if (path != null){
                this.maiden.getNavigation().moveTo(path, this.maiden.getMoveSpeed());
                return true;
            }
            else return false;
        }

        @Override
        public boolean canUse() {
            Vec3 distance = terminal.getCenter().subtract(maiden.position());
            return Math.abs(distance.x()) > maxDistance || Math.abs(distance.y()) > maxDistance || Math.abs(distance.z()) > maxDistance;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.maiden.navigation.isDone();
        }

        @Override
        public void start() {
            setTarget(terminal);
        }

        @Override
        public boolean isInterruptable() {
            return true;
        }
    }
}
