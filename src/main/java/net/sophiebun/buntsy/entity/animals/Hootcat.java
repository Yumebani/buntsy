package net.sophiebun.buntsy.entity.animals;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.client.HootcatRenderer;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class Hootcat extends TamableAnimal {

    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(ModItems.HOOTNIP.get());
    private static final Ingredient TEMPT_INGREDIENT_PHELINIX = Ingredient.of(ModItems.BLAZING_HOOTNIP.get());
    private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE = SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FEATHER_DROP_COOLDOWN = SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_PHELINIX = SynchedEntityData.defineId(Silkbun.class, EntityDataSerializers.BOOLEAN);
    @javax.annotation.Nullable
    private TemptGoal temptGoal;
    private float relaxStateOneAmount;
    private float relaxStateOneAmountO;
    private int netherExposure = 80;
    public  AnimationState sittingAnimationState = new AnimationState();

    public Hootcat(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static boolean canSpawn(EntityType<Hootcat> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    protected void registerGoals() {
        this.temptGoal = new Hootcat.CatTemptGoal(this, 0.6D, TEMPT_INGREDIENT, true);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, this.temptGoal);
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.addGoal(10, new BreedGoal(this, 0.8D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1.0000001E-5F));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    public void setRelaxStateOne(boolean pRelaxStateOne) {
        this.entityData.set(RELAX_STATE_ONE, pRelaxStateOne);
    }

    public boolean isRelaxStateOne() {
        return this.entityData.get(RELAX_STATE_ONE);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor pColor) {
        this.entityData.set(DATA_COLLAR_COLOR, pColor.getId());
    }

    public int getFeatherDropCD(){
        return this.entityData.get(FEATHER_DROP_COOLDOWN);
    }

    public void resetFeatherDropCD(){
        this.entityData.set(FEATHER_DROP_COOLDOWN, this.random.nextInt(1600, 6400));
    }

    public void decreaseFeatherDropCD(){
        this.entityData.set(FEATHER_DROP_COOLDOWN, getFeatherDropCD() - 1);
    }

    public void setFeatherCD(int cd){
        this.entityData.set(FEATHER_DROP_COOLDOWN, cd);
    }

    public boolean isPhelinix(){
        return this.entityData.get(IS_PHELINIX);
    }

    public void setPhelinix(boolean bool){
        this.entityData.set(IS_PHELINIX, bool);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RELAX_STATE_ONE, false);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(FEATHER_DROP_COOLDOWN, this.random.nextInt(1600, 6400));
        this.entityData.define(IS_PHELINIX, false);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        pCompound.putInt("featherCD", this.getFeatherDropCD());
        pCompound.putBoolean("isPhelinix", this.isPhelinix());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(pCompound.getInt("CollarColor")));
        }
        this.setFeatherCD(pCompound.getInt("featherCD"));
        this.setPhelinix(pCompound.getBoolean("isPhelinix"));

    }

    public void customServerAiStep() {

        this.decreaseFeatherDropCD();
        if (this.getFeatherDropCD() <= 0){
            this.resetFeatherDropCD();
            ItemStack droppedFeather;
            if (isPhelinix()){
                droppedFeather = new ItemStack(ModItems.PHELINIX_FEATHER.get());
            }
            else {
                droppedFeather = new ItemStack(ModItems.HOOTCAT_FEATHER.get());
            }
            Containers.dropItemStack(this.level(), this.position().x(), this.position().y(), this.position().z(), droppedFeather);
        }

        if(this.level().dimension().equals(Level.NETHER)){
            if (this.netherExposure > 0){
                this.netherExposure--;
            }

            if (!this.isTame() && !this.isPhelinix() && this.netherExposure <= 0){
                setPhelinix(true);
            }
        }

    }

    @javax.annotation.Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isTame()) {
            if (this.isInLove()) {
                return SoundEvents.CAT_PURR;
            } else {
                return this.random.nextInt(4) == 0 ? SoundEvents.CAT_PURREOW : SoundEvents.CAT_AMBIENT;
            }
        } else {
            return SoundEvents.CAT_STRAY_AMBIENT;
        }
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getAmbientSoundInterval() {
        return 120;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    protected void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack) {
        if (this.isFood(pStack)) {
            this.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
        }

        super.usePlayerItem(pPlayer, pHand, pStack);
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return pEntity.hurt(this.damageSources().mobAttack(this), this.getAttackDamage());
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.isTame() && this.tickCount % 100 == 0) {
            this.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }

        this.handleLieDown();
        this.animate();
    }

    public void animate() {

        if (this.level().isClientSide()) {

            if (isInSittingPose() && !this.sittingAnimationState.isStarted()){
                this.sittingAnimationState.start(this.tickCount);
            }
            else if (!isInSittingPose() && this.sittingAnimationState.isStarted()){
                this.sittingAnimationState.stop();
            }
        }
    }

    private void handleLieDown() {
        if ((this.isRelaxStateOne()) && this.tickCount % 5 == 0) {
            this.playSound(SoundEvents.CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
        }

        this.updateRelaxStateOneAmount();
    }

    private void updateRelaxStateOneAmount() {
        this.relaxStateOneAmountO = this.relaxStateOneAmount;
        if (this.isRelaxStateOne()) {
            this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
        } else {
            this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
        }

    }

    public float getRelaxStateOneAmount(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, this.relaxStateOneAmountO, this.relaxStateOneAmount);
    }

    @javax.annotation.Nullable
    public Hootcat getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        Hootcat hootcat = ModEntities.HOOTCAT_ENTITY.get().create(pLevel);
        if (hootcat != null && pOtherParent instanceof Hootcat hootcat1) {

            if (this.isPhelinix()){
                hootcat.setPhelinix(true);
            }

            if (this.isTame()) {
                hootcat.setOwnerUUID(this.getOwnerUUID());
                hootcat.setTame(true);
                if (this.random.nextBoolean()) {
                    hootcat.setCollarColor(this.getCollarColor());
                } else {
                    hootcat.setCollarColor(hootcat1.getCollarColor());
                }
            }
        }

        return hootcat;
    }

    /**
     * Returns {@code true} if the mob is currently able to mate with the specified mob.
     */
    public boolean canMate(Animal pOtherAnimal) {
        if (!this.isTame()) {
            return false;
        } else if (!(pOtherAnimal instanceof Hootcat)) {
            return false;
        } else if (this.isPhelinix() && ((Hootcat) pOtherAnimal).isPhelinix()) {
            return ((Hootcat) pOtherAnimal).isTame();
        }else if (!this.isPhelinix() && !((Hootcat) pOtherAnimal).isPhelinix()) {
            return ((Hootcat) pOtherAnimal).isTame();
        }
        else {
            return false;
        }
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        return pSpawnData;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        if (this.level().isClientSide) {
            if (this.isTame() && this.isOwnedBy(pPlayer)) {
                return InteractionResult.SUCCESS;
            } else {
                return !this.isFood(itemstack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        } else {
            if (this.isTame()) {
                if (this.isOwnedBy(pPlayer)) {
                    if (!(item instanceof DyeItem)) {
                        if (item.isEdible() && this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                            this.heal((float)itemstack.getFoodProperties(this).getNutrition());
                            this.usePlayerItem(pPlayer, pHand, itemstack);
                            return InteractionResult.CONSUME;
                        }

                        InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
                        if (!interactionresult.consumesAction() || this.isBaby()) {
                            this.setOrderedToSit(!this.isOrderedToSit());
                        }

                        return interactionresult;
                    }

                    DyeColor dyecolor = ((DyeItem)item).getDyeColor();
                    if (dyecolor != this.getCollarColor()) {
                        this.setCollarColor(dyecolor);
                        if (!pPlayer.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }

                        this.setPersistenceRequired();
                        return InteractionResult.CONSUME;
                    }
                }
            } else if (this.isFood(itemstack)) {
                this.usePlayerItem(pPlayer, pHand, itemstack);
                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                    this.tame(pPlayer);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }

                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }

            InteractionResult interactionresult1 = super.mobInteract(pPlayer, pHand);
            if (interactionresult1.consumesAction()) {
                this.setPersistenceRequired();
            }

            return interactionresult1;
        }
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isFood(ItemStack pStack) {
        return this.isPhelinix() ? TEMPT_INGREDIENT_PHELINIX.test(pStack) : TEMPT_INGREDIENT.test(pStack);
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.5F;
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return !this.isTame() && this.tickCount > 2400;
    }

    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }
    static class CatTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Hootcat cat;

        public CatTemptGoal(Hootcat pCat, double pSpeedModifier, Ingredient pItems, boolean pCanScare) {
            super(pCat, pSpeedModifier, pItems, pCanScare);
            this.cat = pCat;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
                this.selectedPlayer = this.player;
            } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
                this.selectedPlayer = null;
            }

        }

        protected boolean canScare() {
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.player) ? false : super.canScare();
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && !this.cat.isTame();
        }
    }
}
