package net.sophiebun.buntsy.entity.clockwork_maiden;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;

import javax.annotation.Nullable;
import java.util.*;

public class ClockworkMaiden extends PathfinderMob {

    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState windUpAnimationState = new AnimationState();
    public AnimationState ItemCarryStanceAnimationState = new AnimationState();
    public ItemStack animationStack = ItemStack.EMPTY;

    private final Map<MaidenTask, List<ItemStack>> carriedItems = new HashMap<>();
    private MaidenTask currentTask = null;
    private BlockPos terminal = null;

    boolean animInit = false;

    public ClockworkMaiden(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        animate();
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

            if (currentTask != null && carriedItems.containsKey(currentTask) && !carriedItems.get(currentTask).isEmpty()){
                ItemCarryStanceAnimationState.start(this.tickCount);
                animationStack = carriedItems.get(currentTask).get(0);
            } else {
                ItemCarryStanceAnimationState.stop();
                animationStack = ItemStack.EMPTY;
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
            this.goalSelector.addGoal(20, new MaidenTaskGoal(this, 1, terminal));
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

    protected class MaidenTaskGoal extends Goal{

        private final ClockworkMaiden maiden;
        private final BlockPos terminal;

        private Deque<MaidenTask> tasksQueue = new ArrayDeque<>();

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
            return !tasksQueue.isEmpty() || isTerminalValid();
        }

        @Override
        public void start() {
            if (!tasksQueue.isEmpty()){
                for (int i = 0; i < tasksQueue.size(); i++){
                    MaidenTask task = tasksQueue.removeFirst();
                    if (maiden.carriedItems.get(task).isEmpty()){
                        if (task.canStart()){
                            maiden.currentTask = task;
                            return;
                        }
                    } else {
                        if (task.canContinue(maiden.carriedItems.get(task).get(0))){
                            maiden.currentTask = task;
                            return;
                        }
                    }
                    tasksQueue.addLast(task);
                }
            }

            ClockworkMaidenTerminalEntity terminalEntity = ((ClockworkMaidenTerminalEntity) maiden.level().getBlockEntity(terminal));
            for (int i = 0; i < terminalEntity.getTotalTasks(); i++){
                MaidenTask task = terminalEntity.getTask();
                if (task.canStart())
            }
        }
    }
}
