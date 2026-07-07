package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.entity.ai.util.LandRandomPos;
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
    public void addAdditionalSaveData(CompoundTag pTag) {
        super.addAdditionalSaveData(pTag);

        pTag.putInt("clockwork_maiden.clockwork_tier", this.clockworkTier.ordinal());
        pTag.putBoolean("clockwork_maiden.has_upgrade_item", upgradeItem != null);
        if (upgradeItem != null){
            pTag.put("clockwork_maiden.upgrade_item", upgradeItem.serializeNBT());
        }

        pTag.putInt("clockwork_maiden.carried_items_size", carriedItems.size());
        for (int i = 0; i < carriedItems.size(); i++){
            MaidenTask task = ((MaidenTask) carriedItems.keySet().toArray()[i]);
            pTag.put("clockwork_maiden.carried_items_task_" + i, task.getCompound());
            pTag.put("clockwork_maiden.carried_items_item_" + i, carriedItems.get(task).getFirst().serializeNBT());
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
    }

    @Override
    public void load(CompoundTag pTag) {

        this.clockworkTier = ClockworkTier.values()[pTag.getInt("clockwork_maiden.clockwork_tier")];
        if (pTag.getBoolean("clockwork_maiden.has_upgrade_item")){
            this.upgradeItem = ItemStack.EMPTY;
            this.upgradeItem.deserializeNBT(pTag.getCompound("clockwork_maiden.upgrade_item"));
        } else {
            this.upgradeItem = null;
        }

        int size = pTag.getInt("clockwork_maiden.carried_items_size");
        for (int i = 0; i < size; i++){
            ItemStack in = ItemStack.EMPTY;
            in.deserializeNBT(pTag.getCompound("clockwork_maiden.carried_items_item_" + i));
            this.carriedItems.put(
                    MaidenTask.parseCompound(pTag.getCompound("clockwork_maiden.carried_items_task_" + i)),
                    Pair.of(in,
                            MaidenInteractionConfig.parseCompound(pTag.getCompound("clockwork_maiden.carried_items_config_" + i))));
        }

        if (pTag.getBoolean("clockwork_maiden.has_current_task")){
            this.currentTask = MaidenTask.parseCompound(pTag.getCompound("clockwork_maiden.current_task"));
        }

        if (pTag.getBoolean("clockwork_maiden.has_terminal")){
            this.terminal = NbtUtils.readBlockPos(pTag.getCompound("clockwork_maiden.terminal"));
        }

        super.load(pTag);

        reAssesGoals();
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
        if (terminal != null){
            this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollWhileCloseGoal(this, terminal, 1.0D));
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
        this.reAssesGoals();
    }

    @Override
    public void remove(RemovalReason pReason) {
        this.clearBlockEntityData(level());
        super.remove(pReason);
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
        public boolean canContinueToUse() {
            return maiden.currentTask != null;
        }

        @Override
        public boolean canUse() {
            if (isTerminalValid() && maiden.currentTask == null){
                System.out.println("Trying to use");
                if (!tasksQueue.isEmpty()){
                    for (int i = 0; i < tasksQueue.size(); i++){
                        MaidenTask task = tasksQueue.removeFirst();
                        Pair<ItemStack, MaidenInteractionConfig> objective = maiden.carriedItems.get(maiden.currentTask);
                        if (MaidenTask.tryPlace(maiden.level(), objective.getSecond(), objective.getFirst(), true) < objective.getFirst().getCount()){
                            maiden.currentTask = task;
                            return true;
                        } else if (level().isLoaded(objective.getSecond().getPos()) && level().getBlockEntity(objective.getSecond().getPos()) != null) {
                            tasksQueue.addLast(task);
                        } else if (!level().isLoaded(objective.getSecond().getPos())){
                            tasksQueue.addLast(task);
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
                    Pair<ItemStack, MaidenInteractionConfig> objective = maiden.currentTask.getNextDelivery(maiden.level());
                    if (objective != null){
                        playItemSound();
                        this.maiden.carriedItems.put(maiden.currentTask, objective);
                        this.setTarget(objective.getSecond().getPos());
                    } else {
                        maiden.currentTask = null;
                    }
                } else {
                    Pair<ItemStack, MaidenInteractionConfig> objective = maiden.carriedItems.get(maiden.currentTask);
                    int totalSpace = objective.getFirst().getCount() - MaidenTask.tryPlace(maiden.level(), objective.getSecond(), objective.getFirst(), false);
                    if (totalSpace > 0){
                        playItemSound();
                    }
                    objective.getFirst().setCount(objective.getFirst().getCount() - totalSpace);
                    if (objective.getFirst().isEmpty()){
                        maiden.carriedItems.remove(maiden.currentTask);
                        maiden.currentTask = null;
                    } else {
                        this.tasksQueue.addLast(maiden.currentTask);
                        maiden.currentTask = null;
                    }
                }
            }
        }
    }

    protected class WaterAvoidingRandomStrollWhileCloseGoal extends RandomStrollGoal {
        public static final float PROBABILITY = 0.001F;
        protected final float probability;
        protected BlockPos terminal;

        public WaterAvoidingRandomStrollWhileCloseGoal(PathfinderMob pMob, BlockPos terminal, double pSpeedModifier) {
            this(pMob, terminal, pSpeedModifier, 0.001F);
        }

        public WaterAvoidingRandomStrollWhileCloseGoal(PathfinderMob pMob, BlockPos terminal, double pSpeedModifier, float pProbability) {
            super(pMob, pSpeedModifier);
            this.probability = pProbability;
            this.terminal = terminal;
        }

        @Override
        public boolean canUse() {
            return mob.getNavigation().isDone();
        }

        @Nullable
        protected Vec3 getPosition() {
            if (this.mob.isInWaterOrBubble()) {
                Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
                while (vec3.distanceTo(this.terminal.getCenter()) > 12){
                    vec3 = LandRandomPos.getPos(this.mob, 15, 7);
                }
                return vec3 == null ? super.getPosition() : vec3;
            } else {
                if (this.mob.getRandom().nextFloat() >= this.probability){
                    Vec3 vec3 = LandRandomPos.getPos(this.mob, 10, 7);
                    while (vec3.distanceTo(this.terminal.getCenter()) > 12){
                        vec3 = LandRandomPos.getPos(this.mob, 15, 7);
                    }
                    return vec3 == null ? super.getPosition() : vec3;
                } else return super.getPosition();
            }
        }
    }
}
