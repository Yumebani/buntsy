package net.sophiebun.buntsy.entity.animals;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;
import java.util.function.Predicate;

public class Silkbun extends Animal {

    private static final int JUMP_DURATION_TIME = 30;
    private static final int WAKE_UP_TIME = 30;

    private static final EntityDataAccessor<Integer> DATA_TYPE_ID =
            SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_SLEEPING =
            SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_WAKING_UP =
            SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_JUMPING =
            SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.BOOLEAN);

    public  AnimationState initAnimationState = new AnimationState();
    public  AnimationState idleAnimationState = new AnimationState();
    public  AnimationState jumpAnimationState = new AnimationState();
    public  AnimationState startSleepAnimationState = new AnimationState();
    public  AnimationState endSleepAnimationState = new AnimationState();

    private int nextSleepShift;

    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;

    //Animation useful
    private boolean wasSleeping = false;
    private boolean hasJumped = false;
    private boolean initialized = false;
    private int wakupTime = 0;

    public Silkbun(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.jumpControl = new SilkbunJumpControl(this);
        this.moveControl = new SilkbunMoveControl(this);
    }

    public static boolean canSpawn(EntityType<Silkbun> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return level.getBlockState(pos.below()).is(ModTags.Blocks.CUTERLY_SPAWNER);
    }

    protected void registerGoals() {
        if (!getIsSleeping()){
            resetGoals();
            this.goalSelector.addGoal(1, new FloatGoal(this));
            this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
            this.goalSelector.addGoal(1, new SilkbunPanicGoal(this, 2.2));
            this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
            this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, Ingredient.of(new ItemLike[]{ModItems.STRAWBERRY.get(), ModItems.CARAMEL_STRAWBERRIES.get(), ModItems.GOLDEN_STRAWBERRY.get()}), false));
            this.goalSelector.addGoal(4, new FollowParentGoal(this, 0.8f));
            this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
            this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
            this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        }
        else{
            resetGoals();
            this.goalSelector.addGoal(1, new SilkbunSleep(this));
        }
    }

    protected void resetGoals(){
        this.goalSelector.removeAllGoals(new Predicate<Goal>() {
            @Override
            public boolean test(Goal goal) {
                return true;
            }
        });
    }

    public static AttributeSupplier.Builder createAtributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 24);
    }

    public int getNextSleepShift() {
        return nextSleepShift;
    }

    @Override
    public void tick() {
        super.tick();
        animate();
    }

    public void animate() {

        if (this.level().isClientSide()) {

            if (!initialized){
                this.initAnimationState.start(this.tickCount);
                this.initialized = true;
            }
            else{

                if (isIdle() && !this.idleAnimationState.isStarted()){
                    stopAllAnimations();
                    this.idleAnimationState.start(this.tickCount);
                }

                if (!getIsSleeping() && !getDataIsWakingUp()){
                    if (getDataIsJumping() && !this.hasJumped){
                        this.hasJumped = true;
                        stopAllAnimations();
                        this.jumpAnimationState.start(this.tickCount);
                    }
                    else if (!getDataIsJumping() && this.hasJumped){
                        this.hasJumped = false;
                    }
                }

                if (getIsSleeping() && !this.wasSleeping) {
                    this.wasSleeping = true;
                    stopAllAnimations();
                    this.startSleepAnimationState.start(this.tickCount);
                }
                else if (!getIsSleeping() && this.wasSleeping) {
                    this.startSleepAnimationState.stop();
                    this.wakupTime = WAKE_UP_TIME;
                    this.wasSleeping = false;
                    stopAllAnimations();
                    this.endSleepAnimationState.start(this.tickCount);
                }

                if (getDataIsWakingUp()){
                    this.wakupTime--;
                    if (this.wakupTime <= 0){
                        setDataIsWakingUp(false);
                    }
                }
            }
        }
    }

    public void stopAllAnimations(){
        if (initAnimationState.isStarted()) {initAnimationState.stop();}
        if (idleAnimationState.isStarted()) {idleAnimationState.stop();}
        if (startSleepAnimationState.isStarted()) {startSleepAnimationState.stop();}
        if (endSleepAnimationState.isStarted()) {endSleepAnimationState.stop();}
        if (jumpAnimationState.isStarted()) {jumpAnimationState.stop();}

    }

    public boolean isIdle(){
        return !getIsSleeping() && !getDataIsWakingUp() && !isInWaterOrBubble() && !getDataIsJumping() && !this.hasJumped;
    }

    public boolean getDataIsJumping(){
        return this.entityData.get(DATA_IS_JUMPING);
    }

    public void setDataIsJumping(boolean bool){
        this.entityData.set(DATA_IS_JUMPING, bool);
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        this.walkAnimation.update(1f, pPartialTick);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(ModItems.STRAWBERRY.get()) || pStack.is(ModItems.GOLDEN_STRAWBERRY.get());
    }

    protected float getJumpPower() {
        float f = 0.4F;
        if (this.horizontalCollision || this.moveControl.hasWanted() && this.moveControl.getWantedY() > this.getY() + 0.5) {
            f = 0.5F;
        }

        Path path = this.navigation.getPath();
        if (path != null && !path.isDone()) {
            Vec3 vec3 = path.getNextEntityPos(this);
            if (vec3.y > this.getY() + 0.5) {
                f = 0.5F;
            }
        }

        return f + this.getJumpBoostPower();
    }

    protected void jumpFromGround() {
        super.jumpFromGround();
        double d0 = this.moveControl.getSpeedModifier();
        if (d0 > 0.0) {
            double d1 = this.getDeltaMovement().horizontalDistanceSqr();
            if (d1 < 0.001) {
                this.moveRelative(0.2F, new Vec3(0.0, 0.0, 1.0));
            }
        }

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)1);
        }
    }

    public void setSpeedModifier(double pSpeedModifier) {
        this.getNavigation().setSpeedModifier(pSpeedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), pSpeedModifier + 0.1f);
    }

    public void setJumping(boolean pJumping) {
        super.setJumping(pJumping);
        if (pJumping) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
        setDataIsJumping(pJumping);
    }

    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = JUMP_DURATION_TIME;
        this.jumpTicks = 0;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, Variant.WHITE.id);
        this.entityData.define(DATA_IS_SLEEPING, false);
        this.entityData.define(DATA_IS_WAKING_UP, false);
        this.entityData.define(DATA_IS_JUMPING, this.jumping);
    }

    public boolean getIsSleeping(){
        return this.entityData.get(DATA_IS_SLEEPING);
    }

    public void setSleeping(boolean bool){
        this.entityData.set(DATA_IS_SLEEPING, bool);
    }

    public boolean getDataIsWakingUp(){
        return this.entityData.get(DATA_IS_WAKING_UP);
    }

    public void setDataIsWakingUp(boolean bool){
        this.entityData.set(DATA_IS_WAKING_UP, bool);
    }

    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.spawnAtLocation(ModItems.MOLTED_MOTH_WINGS.get(), 1);
        }

    }

    public void customServerAiStep() {

        if (!this.isBaby()){
            if (this.nextSleepShift > 0){
                this.nextSleepShift--;
            }
            else if (goalSelector.getRunningGoals().count() <= 0 && !this.moveControl.hasWanted()){
                this.nextSleepShift = this.random.nextInt(14000,18000);
                setSleeping(!this.getIsSleeping());

                if (!getIsSleeping()){
                    setDataIsWakingUp(true);
                    if (!level().isClientSide()){
                        Containers.dropItemStack(level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.COCOON.get()));
                    }
                }

                registerGoals();
            }

            if (getIsSleeping() && getEnabledJumpControl()){
                disableJumpControl();
            }
            else if (!getDataIsWakingUp() && !getEnabledJumpControl()){
                enableJumpControl();
            }
        }

        if (this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }

        if (this.onGround()) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            Silkbun.SilkbunJumpControl silkbunJumpControl = (Silkbun.SilkbunJumpControl)this.jumpControl;
            if (!silkbunJumpControl.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    Path path = this.navigation.getPath();
                    Vec3 vec3 = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (path != null && !path.isDone()) {
                        vec3 = path.getNextEntityPos(this);
                    }

                    this.facePoint(vec3.x, vec3.z);
                    this.startJumping();
                }
            } else if (!silkbunJumpControl.canJump()) {
                this.enableJumpControl();
            }
        }
        else {
            Vec3 vector = this.getDeltaMovement();
            if (vector.y < 0){
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.7, 1.0));
            }

            //Temp fix for bunny not jumping over block sometimes (dunno why)
            if (this.horizontalCollision || this.moveControl.hasWanted() && this.moveControl.getWantedY() > this.getY() + 0.5){
                this.moveRelative(0.01F, new Vec3(0.0, 0.0, 1.0));
            }
        }

        this.wasOnGround = this.onGround();
    }

    public boolean canSpawnSprintParticle() {
        return false;
    }

    private void facePoint(double pX, double pZ) {
        this.setYRot((float)(Mth.atan2(pZ - this.getZ(), pX - this.getX()) * 57.2957763671875) - 90.0F);
    }

    private void enableJumpControl() {
        ((Silkbun.SilkbunJumpControl)this.jumpControl).setCanJump(true);
    }

    private void disableJumpControl() {
        ((Silkbun.SilkbunJumpControl)this.jumpControl).setCanJump(false);
    }

    private boolean getEnabledJumpControl() {
        return ((SilkbunJumpControl)this.jumpControl).canJump;
    }

    private void setLandingDelay() {
        if (this.moveControl.getSpeedModifier() < 2.2) {
            this.jumpDelayTicks = 10;
        } else {
            this.jumpDelayTicks = 1;
        }

    }

    private void checkLandingDelay() {
        this.setLandingDelay();
        this.disableJumpControl();
    }

    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("SilkbunType", this.getVariant().id);
        pCompound.putInt("NextSleepShift", this.nextSleepShift);

        pCompound.putBoolean("IsSleeping", this.getIsSleeping());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(Silkbun.Variant.byId(pCompound.getInt("SilkbunType")));
        this.nextSleepShift = pCompound.getInt("NextSleepShift");

        this.setSleeping(pCompound.getBoolean("IsSleeping"));

        registerGoals();
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.RABBIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    @Nullable
    public Silkbun getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {

        Silkbun silkbun = ModEntities.SILKBUN_ENTITY.get().create(pLevel);

        if (silkbun != null) {
            Silkbun.Variant silkbunVariant = getRandomSilkbunVariant(pLevel, this.blockPosition());

            if (this.random.nextInt(20) != 0) {
                varientGet: {

                    if (pOtherParent instanceof Silkbun) {
                        Silkbun silkbunParent = (Silkbun) pOtherParent;

                        if (this.random.nextBoolean()) {
                            silkbunVariant = silkbunParent.getVariant();
                            break varientGet;
                        }
                    }

                    silkbunVariant = this.getVariant();
                }
            }

            silkbun.setVariant(silkbunVariant);
        }

        return silkbun;
    }

    public Silkbun.Variant getVariant() {
        return Silkbun.Variant.byId(this.entityData.get(DATA_TYPE_ID));
    }

    public void setVariant(Silkbun.Variant pVariant) {
        this.entityData.set(DATA_TYPE_ID, pVariant.id);
    }

        private static Silkbun.Variant getRandomSilkbunVariant(LevelAccessor pLevel, BlockPos pPos) {
            return Silkbun.Variant.byId(pLevel.getRandom().nextInt(0,3));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {

        Silkbun.Variant silkbunVariant = getRandomSilkbunVariant(pLevel, this.blockPosition());

        if (pSpawnData instanceof Silkbun.SilkbunGroupData) {
            silkbunVariant = ((Silkbun.SilkbunGroupData)pSpawnData).variant;
        }
        else {
            pSpawnData = new Silkbun.SilkbunGroupData(silkbunVariant);
        }

        this.setVariant(silkbunVariant);
        this.setDataIsWakingUp(false);
        this.setDataIsJumping(false);
        this.setSleeping(false);
        this.nextSleepShift = this.random.nextInt(14000,18000);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 1) {
            this.spawnSprintParticle();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleEntityEvent(pId);
        }

    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public static class SilkbunJumpControl extends JumpControl {
        private final Silkbun silkbun;
        private boolean canJump;

        public SilkbunJumpControl(Silkbun silkbun) {
            super(silkbun);
            this.silkbun = silkbun;
        }

        public boolean wantJump() {
            return this.jump;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean pCanJump) {
            this.canJump = pCanJump;
        }

        public void tick() {
            if (this.jump) {
                this.silkbun.startJumping();
                this.jump = false;
            }
        }
    }

    static class SilkbunMoveControl extends MoveControl {
        private final Silkbun silkbun;
        private double nextJumpSpeed;

        public SilkbunMoveControl(Silkbun silkbun) {
            super(silkbun);
            this.silkbun = silkbun;
        }

        public void tick() {
            if (this.silkbun.onGround() && !this.silkbun.jumping && !((Silkbun.SilkbunJumpControl)this.silkbun.jumpControl).wantJump()) {
                this.silkbun.setSpeedModifier(0.0);
            } else if (this.hasWanted()) {
                this.silkbun.setSpeedModifier(this.nextJumpSpeed);
            }

            super.tick();
        }

        public void setWantedPosition(double pX, double pY, double pZ, double pSpeed) {
            if (this.silkbun.isInWater()) {
                pSpeed = 1.5;
            }

            super.setWantedPosition(pX, pY, pZ, pSpeed);
            if (pSpeed > 0.0) {
                this.nextJumpSpeed = pSpeed;
            }
        }
    }

    static class SilkbunSleep extends Goal{
        private final Silkbun silkbun;

        public SilkbunSleep(Silkbun silkbun) {
            super();
            this.silkbun = silkbun;
        }

        @Override
        public boolean canUse() {
            return false;
        }
    }

    static class SilkbunPanicGoal extends PanicGoal {
        private final Silkbun silkbun;

        public SilkbunPanicGoal(Silkbun silkbun, double pSpeedModifier) {
            super(silkbun, pSpeedModifier);
            this.silkbun = silkbun;
        }

        public void tick() {
            super.tick();
            this.silkbun.setSpeedModifier(this.speedModifier);
        }
    }

    public static enum Variant implements StringRepresentable {
        WHITE(0, "white"),
        PINK(1, "pink"),
        BLUE(2, "blue");

        private static final IntFunction<Silkbun.Variant> BY_ID = ByIdMap.sparse(Silkbun.Variant::id, values(), WHITE);
        public static final Codec<Silkbun.Variant> CODEC = StringRepresentable.fromEnum(Silkbun.Variant::values);
        final int id;
        private final String name;

        private Variant(int pId, String pName) {
            this.id = pId;
            this.name = pName;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public int id() {
            return this.id;
        }

        public static Silkbun.Variant byId(int pId) {
            return (Silkbun.Variant)BY_ID.apply(pId);
        }
    }

    public static class SilkbunGroupData extends AgeableMob.AgeableMobGroupData {
        public final Silkbun.Variant variant;

        public SilkbunGroupData(Silkbun.Variant pVariant) {
            super(1.0F);
            this.variant = pVariant;
        }
    }
}
