package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.item.ClockworkTier;
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

    boolean animInit = false;

    public ClockworkMaiden(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void load(CompoundTag pCompound) {
        super.load(pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        animate();
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

    public ItemStack getAnimationStack(){
        if (currentTask != null && carriedItems.containsKey(currentTask)){
            return carriedItems.get(currentTask).getFirst();
        } else {
            return ItemStack.EMPTY;
        }
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

            if (currentTask != null && carriedItems.containsKey(currentTask)){
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
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        if (terminal != null){
            this.goalSelector.addGoal(20, new MaidenTaskGoal(this, terminal));
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
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

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
    }

    public boolean containsTerminal(ClockworkMaidenTerminalEntity blockEntity) {
        return this.terminal != null;
    }

    public void registerTerminal(ClockworkMaidenTerminalEntity blockEntity, Level level) {
        this.clearBlockEntityData(level);
        this.terminal = blockEntity.getBlockPos();
    }

    protected class MaidenTaskGoal extends Goal{

        private final ClockworkMaiden maiden;
        private final BlockPos terminal;

        private final Deque<MaidenTask> tasksQueue = new ArrayDeque<>();

        private BlockPos target;

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

        @Override
        public boolean canUse() {
            if (isTerminalValid() && maiden.currentTask != null){
                if (!tasksQueue.isEmpty()){
                    for (int i = 0; i < tasksQueue.size(); i++){
                        MaidenTask task = tasksQueue.removeFirst();
                        if (MaidenTask.tryPlace(maiden.level(), carriedItems.get(task).getSecond(), carriedItems.get(task).getFirst(), true) > 0){
                            maiden.currentTask = task;
                            return true;
                        }
                    }
                }

                ClockworkMaidenTerminalEntity terminalEntity = ((ClockworkMaidenTerminalEntity) maiden.level().getBlockEntity(terminal));
                for (int i = 0; i < terminalEntity.getTotalTasks(); i++){
                    MaidenTask task = terminalEntity.getTask();
                    if (!this.tasksQueue.contains(task)){
                        maiden.currentTask = task;
                        return true;
                    }
                }
            }
            return false;
        }

        private void setTarget(BlockPos pos){
            this.target = pos;
            Vec3 goal = pos.getCenter();
            this.maiden.getNavigation().moveTo(goal.x(), goal.y(), goal.z(), this.maiden.getMoveSpeed());
        }

        void playItemSound() {
            this.maiden.level().playSound(null, this.maiden.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL,
                    1f, this.maiden.random.nextInt(8,12) * 0.1f);
        }

        @Override
        public void start() {

            if (!maiden.carriedItems.keySet().contains(maiden.currentTask)){
                this.setTarget(maiden.currentTask.getExtractPos());
            } else {
                this.setTarget(maiden.carriedItems.get(maiden.currentTask).getSecond().getPos());
            }
        }

        @Override
        public void tick() {

            if (maiden.position().distanceTo(this.target.getCenter()) <= 1.5f){
                maiden.getNavigation().stop();
                if (!maiden.carriedItems.keySet().contains(maiden.currentTask)){
                    playItemSound();
                    Pair<ItemStack, MaidenInteractionConfig> objective = maiden.currentTask.getNextDelivery(maiden.level());
                    this.maiden.carriedItems.put(maiden.currentTask, objective);
                    this.setTarget(objective.getSecond().getPos());
                } else {
                    Pair<ItemStack, MaidenInteractionConfig> objective = maiden.carriedItems.get(maiden.currentTask);
                    int totalSpace = MaidenTask.tryPlace(maiden.level(), objective.getSecond(), objective.getFirst(), false);
                    if (totalSpace > 0){
                        playItemSound();
                    }
                    objective.getFirst().setCount(objective.getFirst().getCount() - totalSpace);
                    if (objective.getFirst().isEmpty()){
                        maiden.carriedItems.remove(maiden.currentTask);
                        maiden.currentTask = null;
                    } else {
                        maiden.currentTask = null;
                    }
                }
            }
        }
    }
}
