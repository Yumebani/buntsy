package net.sophiebun.buntsy.entity.animals;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyCollectionTrayBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyInfusionBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Fairy extends TamableAnimal implements FlyingAnimal {

    private static final int MAX_UTIL_BLOCK_POOL = 4;
    private static final int MAX_OFFERING_BENCH_RANGE = 8;

    private final Map<BlockPos, Integer> registeredUtilBlockEntityPos = new HashMap<BlockPos, Integer>();
    private int currentWeight;
    private boolean hungry;
    private boolean updateBlocksFlag;
    private ItemStack carriedItem;

    BlockPos offeringBenchPos;
    private int food;
    private float foodModifier;
    private float consumptionRate;
    private float toConsume;

    private boolean busy;

    //Client side
    public AnimationState flyAnimationState = new AnimationState();


    public Fairy(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    @Override
    public void tick() {
        super.tick();

        playAnimations();
    }

    public void playAnimations(){
        if (level().isClientSide() && !flyAnimationState.isStarted()){
            flyAnimationState.start(this.tickCount);
        }
    }

    public static AttributeSupplier.Builder createAtributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.FLYING_SPEED, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 24);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(ModTags.Items.FAIRY_FOOD), false));
        this.goalSelector.addGoal(1, new UpdateFoodConsumptionGoal(this, 0.01f));
        this.goalSelector.addGoal(1, new FairyUpdateRegisteredBlocks(this));
        this.goalSelector.addGoal(2, new FairyEatFromOfferings(this, 0.8f, 30));
        this.goalSelector.addGoal(5, new FairyCollectResources(this, 0.6f, 8, 45));
        this.goalSelector.addGoal(10, new FairyEnchantResources(this, 0.6f, 30));
        this.goalSelector.addGoal(15, new FairyWanderGoal(this, 8, 0.4f));
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);

        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public void setUpdateBlocksFlag(boolean updateBlocksFlag) {
        this.updateBlocksFlag = updateBlocksFlag;
    }


    public boolean getUpdateBlocksFlag() {
        return updateBlocksFlag;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    public void setHungry(boolean hungry) {
        this.hungry = hungry;
    }

    public boolean isHungry() {
        return hungry;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getFood() {
        return food;
    }

    public void decrementFood(int food){
        this.food -= food;
    }

    public void setFoodModifier(float foodModifier) {
        this.foodModifier = foodModifier;
    }

    public float getFoodModifier() {
        return foodModifier;
    }

    public float getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(float consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean hasofferingBench(){
        return this.offeringBenchPos != null && this.level().getBlockEntity(offeringBenchPos) != null;
    }

    public FairyOfferingBenchBlockEntity getofferingBench() {
        return (FairyOfferingBenchBlockEntity)level().getBlockEntity(offeringBenchPos);
    }

    public BlockPos getOfferingBenchPos() {
        return offeringBenchPos;
    }

    public List<BlockPos> getRegisteredBlockPosArray(){
        return this.registeredUtilBlockEntityPos.keySet().stream().toList();
    }

    public boolean hasCarriedItem(){
        return this.carriedItem != null;
    }

    public void setCarriedItem(ItemStack carriedItem) {
        this.carriedItem = carriedItem;
    }

    public ItemStack getCarriedItem() {
        return carriedItem;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.level().isClientSide) {
            boolean flag = itemstack.is(ModTags.Items.FAIRY_FOOD) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        else if (itemstack.is(ModTags.Items.FAIRY_FOOD) && !this.isTame()) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                this.tame(pPlayer);
                this.navigation.stop();
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    private boolean isInRangeOfOfferingBench(BlockPos pos){
        return pos.closerThan(this.offeringBenchPos, MAX_OFFERING_BENCH_RANGE);
    }

    public void clearBlockEntityData(){
        if (this.offeringBenchPos != null && getofferingBench() != null){
            getofferingBench().setEnchanted(false);
            getofferingBench().setWatched(false);
            this.offeringBenchPos = null;
        }

        if (this.registeredUtilBlockEntityPos.size() != 0){
            for (BlockPos pos : getRegisteredBlockPosArray()){
                BlockEntity blockEntity = level().getBlockEntity(pos);
                if (blockEntity != null){
                    FairyInteractBlockEntity entity = (FairyInteractBlockEntity) level().getBlockEntity(pos);
                    entity.setEnchanted(false);
                    entity.setWatched(false);
                }
            }
            this.registeredUtilBlockEntityPos.clear();
        }

        this.setUpdateBlocksFlag(true);
    }

    public void registerNewOfferingBench(FairyOfferingBenchBlockEntity blockEntity){
        this.clearBlockEntityData();
        blockEntity.setWatched(true);
        this.offeringBenchPos = blockEntity.getBlockPos();
    }

    public boolean isBenchRegistered(FairyOfferingBenchBlockEntity blockEntity){
        return this.getOfferingBenchPos().equals(blockEntity.getBlockPos());
    }

    public boolean isBlockEntityInRange(BlockEntity entity){
        return isInRangeOfOfferingBench(entity.getBlockPos());
    }

    public boolean canRegisterNewBlock(BlockEntity blockEntity){
        return ((FairyInteractBlockEntity) blockEntity).getFairyWeight() + this.currentWeight <= Fairy.MAX_UTIL_BLOCK_POOL;
    }

    public boolean isBlockRegistered(BlockEntity blockEntity){
        for (BlockPos pos : getRegisteredBlockPosArray()){
            if (pos != null && pos.equals(blockEntity.getBlockPos())){
                return true;
            }
        }
        return false;
    }

    public void registerNewBlock(BlockEntity blockEntity){
        FairyInteractBlockEntity entity = (FairyInteractBlockEntity) blockEntity;
        entity.setWatched(true);
        int fairyWeight = entity.getFairyWeight();
        this.currentWeight += fairyWeight;
        this.registeredUtilBlockEntityPos.put(blockEntity.getBlockPos(), fairyWeight);
        this.setUpdateBlocksFlag(true);
    }

    public void unregisterBlock(BlockEntity blockEntity){
        for (BlockPos pos : getRegisteredBlockPosArray()){
            if (pos != null && pos.equals(blockEntity.getBlockPos())){
                ((FairyInteractBlockEntity) level().getBlockEntity(pos)).setWatched(false);
                ((FairyInteractBlockEntity) level().getBlockEntity(pos)).setEnchanted(false);
                this.currentWeight -= this.registeredUtilBlockEntityPos.get(pos);
                this.registeredUtilBlockEntityPos.remove(pos);
                this.setUpdateBlocksFlag(true);
            }
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        clearBlockEntityData();
        super.remove(pReason);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
        pCompound.putInt("fairy.current_weight", this.currentWeight);
        pCompound.putInt("fairy.food", this.food);
        pCompound.putFloat("fairy.food_modifier", this.foodModifier);

        if (this.hasofferingBench()){
            pCompound.put("fairy.offering_bench", NbtUtils.writeBlockPos(this.offeringBenchPos));
        }

        pCompound.putInt("fairy.registered_block_entity_count", this.registeredUtilBlockEntityPos.size());

        for (int i = 0; i < this.registeredUtilBlockEntityPos.size(); i++){
            BlockPos pos = (BlockPos) this.registeredUtilBlockEntityPos.keySet().toArray()[i];
            pCompound.put("fairy.registered_block_entity_pos_" + i, NbtUtils.writeBlockPos(pos));
            pCompound.putInt("fairy.registered_block_entity_weight_" + i, this.registeredUtilBlockEntityPos.get(pos));
        }
    }

    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {

        UUID uuid;
        if (pCompound.hasUUID("Owner")) {
            uuid = pCompound.getUUID("Owner");
        } else {
            String s = pCompound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable var4) {
                this.setTame(false);
            }
        }

        this.currentWeight = pCompound.getInt("fairy.current_weight");
        this.food = pCompound.getInt("fairy.food");
        this.foodModifier = pCompound.getFloat("fairy.food_modifier");

        if (pCompound.contains("fairy.offering_bench")){
            this.offeringBenchPos = NbtUtils.readBlockPos(pCompound.getCompound("fairy.offering_bench"));
        }

        int loopCount = pCompound.getInt("fairy.registered_block_entity_count");

        for (int i = 0; i < loopCount; i++){
            this.registeredUtilBlockEntityPos.put(
                    NbtUtils.readBlockPos(pCompound.getCompound("fairy.registered_block_entity_pos_" + i)),
                    pCompound.getInt("fairy.registered_block_entity_weight_" + i));
        }

        super.readAdditionalSaveData(pCompound);
    }

    private boolean canEatFromOfferings(){
        return this.goalSelector.getAvailableGoals().stream().filter((goal) -> goal.getGoal().getClass() == FairyEatFromOfferings.class)
                .anyMatch(WrappedGoal::canUse);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.isTame() && !this.hasCarriedItem() && this.getFood() <= 0 && !canEatFromOfferings()){

            if (!isHungry()){
                setHungry(true);
                setUpdateBlocksFlag(true);
            }

            if (this.hasofferingBench()){
                this.getofferingBench().setEnchanted(false);
            }
        }

        if (this.isTame() && this.getFood() > 0){
            this.toConsume += this.getConsumptionRate();
            this.decrementFood(Mth.floor(this.toConsume));
            this.toConsume %= 1;
        }
    }

    public abstract class FairyUseGoal extends Goal {
        protected final Fairy fairy;

        private final float SPEED_MODIFIER;
        private final int USE_TIME;

        private int usageTime;
        private boolean hasUsed;
        protected BlockPos target;

        private int repathTicks;

        public FairyUseGoal(Fairy fairy, float speedModifier, int useTime) {
            this.fairy = fairy;
            this.SPEED_MODIFIER = speedModifier;
            this.USE_TIME = useTime;
        }

        abstract boolean canUseExtra();
        abstract boolean canContinueUseExtra();
        abstract BlockPos getTarget();
        abstract void playUseParticle();
        abstract void playUseSound();
        abstract void useBlock();

        @Override
        public boolean canUse() {
            return this.fairy.isTame() && !this.fairy.hasCarriedItem() && canUseExtra();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.hasUsed && canContinueUseExtra();
        }

        @Override
        public void start() {
            this.target = this.getTarget();
            this.usageTime = 0;
            this.hasUsed = false;
            this.repathTicks = 0;
            this.fairy.setBusy(true);

            this.moveToGoal();
        }

        private void moveToBlock(BlockPos pos){
            fairy.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), this.SPEED_MODIFIER);
        }

        private boolean hasArrived(){
            return this.target.closerToCenterThan(this.fairy.position(), 1.75f);
        }

        protected void makeItemParticle(Item item){
            Vec3 pos = fairy.position();
            ((ServerLevel) fairy.level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)), pos.x, pos.y, pos.z, fairy.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void makeBlockParticle(BlockState block, Vec3 pos, int count){
            ((ServerLevel) fairy.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block), pos.x, pos.y, pos.z, fairy.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void makeGlowParticle(){
            Vec3 pos = fairy.position();
            ((ServerLevel) fairy.level()).sendParticles(ParticleTypes.GLOW, pos.x, pos.y, pos.z, fairy.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void makeSparkParticle(){
            Vec3 pos = fairy.position();
            ((ServerLevel) fairy.level()).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.x, pos.y, pos.z, fairy.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        private void moveToGoal(){
            this.moveToBlock(this.target);
        }

        private void moveAway(){
            BlockPos pos = this.target;
            this.moveToBlock(pos.offset(fairy.random.nextInt(-2, 2),
                    fairy.random.nextInt(-1, 1),
                    fairy.random.nextInt(-2, 2)));
        }

        @Override
        public void tick() {
            super.tick();

            if (repathTicks > 0){
                this.repathTicks--;
            }
            else if (repathTicks == 0){
                moveToGoal();
                repathTicks = -1;
            }
            else if (fairy.navigation.isDone()) {

                if (!hasArrived()){
                    moveAway();
                    repathTicks = 30;
                }
                else if (hasArrived() && this.usageTime < this.USE_TIME) {
                    if (this.usageTime % 15 == 0) {
                        this.playUseParticle();
                        this.playUseSound();;
                    }
                    this.usageTime++;
                }
                else if (this.usageTime >= this.USE_TIME) {
                    this.useBlock();

                    this.hasUsed = true;
                    this.fairy.setBusy(false);
                }
            }
        }
    }

    public class FairyEatFromOfferings extends FairyUseGoal {

        public FairyEatFromOfferings(Fairy fairy, float speedModifier, int eatingTicks) {
            super(fairy, speedModifier, eatingTicks);
        }

        @Override
        boolean canUseExtra() {
            if (this.fairy.hasofferingBench() && this.fairy.getFood() <= 0) {
                FairyOfferingBenchBlockEntity offeringBenchPos = this.fairy.getofferingBench();
                return offeringBenchPos.isValidForInteraction() && offeringBenchPos.hasFood();
            }
            return false;
        }

        @Override
        boolean canContinueUseExtra() {
            return canUseExtra();
        }

        @Override
        BlockPos getTarget() {
            return this.fairy.getOfferingBenchPos();
        }

        @Override
        void playUseParticle() {
            Item foodItem = fairy.getofferingBench().getNextFoodItem();
            this.makeItemParticle(foodItem);
        }

        @Override
        void playUseSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        void playFinishUseSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        @Override
        void useBlock() {
            this.playFinishUseSound();

            FairyOfferingBenchBlockEntity offeringBench = fairy.getofferingBench();
            Item foodItem = offeringBench.getNextFoodItem();
            offeringBench.consumeFood();

            this.fairy.setFood(FairyOfferingBenchBlockEntity.getFoodTick(foodItem));
            this.fairy.setFoodModifier(FairyOfferingBenchBlockEntity.getChanceModifier(foodItem));

            this.fairy.getofferingBench().setEnchanted(true);

            if (this.fairy.isHungry()) {
                this.fairy.setHungry(false);
                this.fairy.setUpdateBlocksFlag(true);
            }
        }
    }

    private class FairyEnchantResources extends FairyUseGoal {

        private final static BlockEntityType TYPE = ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get();

        private int nextUseTicks = 0;

        public FairyEnchantResources(Fairy fairy, float speedModifier, int useTime) {
            super(fairy, speedModifier, useTime);
        }

        public BlockPos getRandomValidBenchPos(){
            List<BlockPos> posList = new ArrayList<>();
            for (BlockPos pos : this.fairy.getRegisteredBlockPosArray()){
                BlockEntity blockEntity = this.fairy.level().getBlockEntity(pos);
                if (blockEntity != null && blockEntity.getType() == this.TYPE && ((FairyInfusionBenchBlockEntity) blockEntity).hasInfusion()){
                    posList.add(pos);
                }
            }
            return posList.isEmpty() ? null : posList.get(this.fairy.random.nextInt(posList.size()));
        }

        public  FairyInfusionBenchBlockEntity getBench(BlockPos pos){
            return ((FairyInfusionBenchBlockEntity) this.fairy.level().getBlockEntity(pos));
        }

        public boolean hasValidBlockEntity(){
            return getRandomValidBenchPos() != null;
        }

        public boolean isValidBlockEntity(BlockPos pos){
            BlockEntity blockEntity = this.fairy.level().getBlockEntity(pos);
            return blockEntity != null && blockEntity.getType().equals(TYPE) && ((FairyInfusionBenchBlockEntity) blockEntity).hasInfusion();
        }

        @Override
        protected boolean canUseExtra() {
            if (nextUseTicks > 0){
                nextUseTicks -= fairy.foodModifier;
            }
            else{
                return this.fairy.getFood() > 0 && hasValidBlockEntity();
            }
            return false;
        }

        @Override
        boolean canContinueUseExtra() {
            return this.fairy.getFood() > 0 && isValidBlockEntity(this.target);
        }

        @Override
        public void start() {
            super.start();
            this.nextUseTicks = 1000 + fairy.random.nextInt(500);
        }

        @Override
        protected BlockPos getTarget() {
            return getRandomValidBenchPos();
        }

        @Override
        protected void playUseParticle() {
            this.makeSparkParticle();
        }

        @Override
        protected void playUseSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        protected void playFinishUseParticle() {
            this.makeSparkParticle();
        }

        protected void playFinishUseSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        @Override
        protected void useBlock() {
            getBench(this.target).infuse();
            this.playFinishUseParticle();
            this.playFinishUseSound();
        }
    }

    private class FairyCollectResources extends MoveToBlockGoal {
        protected final Fairy fairy;

        private static final BlockEntityType TYPE = ModBlockEntities.FAIRY_COLLECTION_TRAY_BLOCK_ENTITY.get();
        protected static final TagKey<Block> VALID_BLOCK_TAG = ModTags.Blocks.FAIRY_HARVESTABLE;
        private final float SPEED_MODIFIER;
        private final int COLLECT_TIME;

        private boolean hasReachedTarget;
        private int collectionTime;

        private BlockPos targetTray;
        private boolean hasDeposited;

        private BlockPos nextTarget;
        private int repathTicks;

        public FairyCollectResources(Fairy fairy, float speedModifier, int searchRange, int collectTime) {
            super(fairy, speedModifier, searchRange);
            this.fairy = fairy;
            this.SPEED_MODIFIER = speedModifier;
            this.COLLECT_TIME = collectTime;
        }

        public boolean hasValidBlockEntity(BlockPos pos){
            BlockEntity block = this.fairy.level().getBlockEntity(pos);
            return block != null && block.getType() == TYPE && ((FairyInteractBlockEntity) block).isValidForInteraction();
        }

        @Override
        public boolean canUse() {
            if (this.nextStartTick > 0){
                this.nextStartTick -= fairy.foodModifier;
            }
            else if (this.fairy.isTame() && this.fairy.getFood() > 0 && !this.fairy.registeredUtilBlockEntityPos.isEmpty()
                    && hasValidBlockEntity(this.fairy.getRegisteredBlockPosArray().get(0))) {

                if (this.findNearestBlock()) {
                    return true;
                }
                else{
                    this.nextStartTick = 200 + this.fairy.random.nextInt(100);
                }
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return (this.fairy.getFood() > 0 && hasValidBlockEntity(this.targetTray) && collectionTime < COLLECT_TIME && isValidTarget(fairy.level(), blockPos))
                    || (!this.hasDeposited && this.fairy.hasCarriedItem() && hasValidBlockEntity(this.targetTray));
        }

        private void moveToBlock(BlockPos pos){
            fairy.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), this.SPEED_MODIFIER);
        }

        private boolean hasArrivedAtTray(){
            return this.targetTray.closerToCenterThan(this.fairy.position(), 2f);
        }

        private boolean hasArrivedAtHarvest(){
            return this.blockPos.closerToCenterThan(this.fairy.position(), 1.75f);
        }

        @Override
        public void start() {
            super.start();
            this.fairy.setBusy(true);
            this.nextStartTick = 200 + this.fairy.random.nextInt(100);

            this.hasReachedTarget = false;
            this.collectionTime = 0;

            this.targetTray = this.fairy.getRegisteredBlockPosArray().get(0);
            this.hasDeposited = false;

            this.nextTarget = blockPos;
            this.repathTicks = 0;
        }

        private void moveToGoal(){
            this.moveToBlock(this.nextTarget);
        }

        private void moveAway(){
            BlockPos pos = this.nextTarget;
            this.moveToBlock(pos.offset(fairy.random.nextInt(-2, 2),
                    fairy.random.nextInt(-1, 1),
                    fairy.random.nextInt(-2, 2)));
        }

        protected void playMineralMiningSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        protected void playMineralBreakSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        protected void playFlowerCopySound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        protected void makeBlockParticle(BlockState block, Vec3 pos, int count){
            ((ServerLevel) fairy.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block), pos.x, pos.y, pos.z, fairy.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }
        protected void makeSparkleParticle(int count){
            Vec3 pos = fairy.position();
            ((ServerLevel) fairy.level()).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.x, pos.y, pos.z, fairy.random.nextInt(50,75),
                    0.25f, 0.25f, 0.25f, 0.1f);
        }

        protected void playStoreSound() {
            this.fairy.level().playSound(null, this.fairy.blockPosition(), SoundEvents.BARREL_CLOSE, SoundSource.NEUTRAL,
                    1f, this.fairy.random.nextInt(8,12) * 0.1f);
        }

        @Override
        public void tick() {
            super.tick();

            if (repathTicks > 0){
                this.repathTicks--;
            }
            else if (repathTicks == 0){
                moveToGoal();
                repathTicks = -1;
            }
            else if (fairy.navigation.isDone()) {

                if (hasArrivedAtTray() && fairy.hasCarriedItem()){
                    playStoreSound();
                    depositItem();
                    this.hasDeposited = true;
                    this.fairy.setBusy(false);
                }

                if ((!hasArrivedAtHarvest() && !hasReachedTarget) || (!hasArrivedAtTray() && fairy.hasCarriedItem())){
                    moveAway();
                    repathTicks = 30;
                }
                else if (hasArrivedAtHarvest() && !hasReachedTarget){
                    hasReachedTarget = true;
                }

                if (hasReachedTarget && collectionTime < COLLECT_TIME){
                    collectionTime++;
                    if (collectionTime % 5 == 0){
                        BlockState block = fairy.level().getBlockState(blockPos);
                        if (block.is(BlockTags.FLOWERS)){
                            makeSparkleParticle(3);
                        }
                        else{
                            playMineralMiningSound();
                            makeBlockParticle(block, blockPos.getCenter(), 3);
                        }
                    }
                    if (collectionTime >= COLLECT_TIME){
                        BlockState block = fairy.level().getBlockState(blockPos);
                        if (block.is(BlockTags.FLOWERS)){
                            playFlowerCopySound();
                            makeSparkleParticle(25);
                        }
                        else{
                            playMineralBreakSound();
                            makeBlockParticle(block, blockPos.getCenter(), 25);
                        }

                        collectFromBlock();
                        nextTarget = targetTray;
                        moveToGoal();
                    }
                }
            }
        }

        protected void depositItem(){
            ((FairyCollectionTrayBlockEntity) this.fairy.level().getBlockEntity(this.fairy.getRegisteredBlockPosArray().get(0))).depositItem(
                    this.fairy.getCarriedItem());
            this.fairy.setCarriedItem(null);
        }
        protected void collectFromBlock(){

            BlockState blockState = this.fairy.level().getBlockState(this.blockPos);

            if (blockState.is(BlockTags.FLOWERS)){
                this.fairy.setCarriedItem(new ItemStack(blockState.getBlock().asItem()));
            }
            else {
                LootParams.Builder params = new LootParams.Builder(((ServerLevel) this.fairy.level()))
                        .withParameter(LootContextParams.ORIGIN, new Vec3(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()))
                        .withParameter(LootContextParams.BLOCK_STATE, blockState)
                        .withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE));

                this.fairy.setCarriedItem(blockState.getDrops(params).get(0));

                ModGrowableMineral mineral = ((ModGrowableMineral) blockState.getBlock());
                BlockState newBlockState = mineral.getStages().get(this.fairy.random.nextInt(2)).get().defaultBlockState();

                fairy.level().setBlockAndUpdate(this.blockPos, newBlockState
                        .setValue(AmethystClusterBlock.FACING, blockState.getValue(AmethystClusterBlock.FACING))
                        .setValue(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getType() == Fluids.WATER));
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
            BlockState blockState = levelReader.getBlockState(blockPos);
            if (blockState.is(VALID_BLOCK_TAG) && isInRangeOfOfferingBench(blockPos)){
                if (blockState.is(BlockTags.FLOWERS)){
                    return fairy.random.nextInt(32) == 0;
                }
                return true;
            }
            return false;
        }
    }

    private class FairyWanderGoal extends Goal {
        private final Fairy fairy;

        private final int MAX_OFFERING_BENCH_DISTANCE;
        public final float SPEED_MODIFIER;

        public FairyWanderGoal(Fairy fairy, int offeringBenchDistance, float speedModifier) {
            this.fairy = fairy;
            this.MAX_OFFERING_BENCH_DISTANCE = offeringBenchDistance;
            this.SPEED_MODIFIER = speedModifier;
        }

        @Override
        public boolean canUse() {
            return !this.fairy.isBusy() && this.fairy.navigation.isDone() && this.fairy.random.nextInt(20) == 0;
        }

        public boolean canContinueToUse() {
            return this.fairy.navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                this.fairy.navigation.moveTo(this.fairy.navigation.createPath(BlockPos.containing(vec3), 1), this.SPEED_MODIFIER);
            }

        }

        private Vec3 findPos() {
            Vec3 center;
            int radius = 4;
            if (this.fairy.isTame() && this.fairy.hasofferingBench() && !this.fairy.getOfferingBenchPos().closerThan(this.fairy.blockPosition(), MAX_OFFERING_BENCH_DISTANCE)) {
                center = Vec3.atCenterOf(this.fairy.getOfferingBenchPos());
            } else {
                center = this.fairy.getPosition(0);
            }

            if (this.fairy.isTame() && this.fairy.hasofferingBench()){
                radius = this.MAX_OFFERING_BENCH_DISTANCE / 2;
            }

            RandomSource random = this.fairy.random;
            return new Vec3(center.x + random.nextInt(-radius, radius), center.y + random.nextInt(-1, 1), center.z + random.nextInt(-radius, radius));
        }
    }

    private class FairyUpdateRegisteredBlocks extends Goal {
        private final Fairy fairy;

        public FairyUpdateRegisteredBlocks(Fairy fairy) {
            this.fairy = fairy;
        }

        public boolean requiresUpdate(){

            for (BlockPos pos : this.fairy.getRegisteredBlockPosArray()){
                if (this.fairy.level().getBlockEntity(pos) == null){
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean canUse() {
            return this.fairy.isTame() && (this.fairy.getUpdateBlocksFlag() || requiresUpdate());
        }

        @Override
        public boolean canContinueToUse() {
            return this.fairy.getUpdateBlocksFlag();
        }

        @Override
        public void start() {
            this.fairy.setUpdateBlocksFlag(true);
        }

        @Override
        public void tick() {

            for (BlockPos pos : this.fairy.getRegisteredBlockPosArray()){
                if (this.fairy.level().getBlockEntity(pos) == null){
                    this.fairy.currentWeight -= this.fairy.registeredUtilBlockEntityPos.get(pos);
                    this.fairy.registeredUtilBlockEntityPos.remove(pos);
                }
                else if (this.fairy.isHungry()){
                    ((FairyInteractBlockEntity) this.fairy.level().getBlockEntity(pos)).setEnchanted(false);
                }
                else{
                    ((FairyInteractBlockEntity) this.fairy.level().getBlockEntity(pos)).setEnchanted(true);
                }
            }

            this.fairy.setUpdateBlocksFlag(false);
        }
    }

    private class UpdateFoodConsumptionGoal extends Goal {
        private final Fairy fairy;

        private final float BASE_CONSUMPTION;

        public UpdateFoodConsumptionGoal(Fairy fairy, float baseConsumption) {
            this.fairy = fairy;
            this.BASE_CONSUMPTION = baseConsumption;
            this.fairy.setConsumptionRate(baseConsumption);
        }

        @Override
        public boolean canUse() {
            return this.fairy.isTame() && this.fairy.random.nextInt(5) == 0 &&
                    (!this.fairy.registeredUtilBlockEntityPos.isEmpty() || this.fairy.getConsumptionRate() > this.BASE_CONSUMPTION + 0.1f);
        }

        @Override
        public void start() {
            super.start();

            float finalConsumption = this.BASE_CONSUMPTION;
            for (BlockPos pos : this.fairy.getRegisteredBlockPosArray()){
                if (this.fairy.level().getBlockEntity(pos) != null){
                    finalConsumption += ((FairyInteractBlockEntity) this.fairy.level().getBlockEntity(pos)).getConsumption();
                }
            }
            this.fairy.setConsumptionRate(finalConsumption);
        }
    }
}
