package net.sophiebun.buntsy.entity.animals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.sophiebun.buntsy.blocks.entity.FairyCollectionTrayBlockEntity;
import net.sophiebun.buntsy.blocks.entity.FairyInfusionBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.OfferingBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Fairy extends TamableAnimal implements FlyingAnimal {

    //Everything is server side
    private static final int MAX_UTIL_BLOCK_POOL = 4;

    private Map<BlockPos, Integer> registeredUtilBlockEntityPos = new HashMap<BlockPos, Integer>();
    private int currentWeight;
    private boolean hungry;
    private boolean updateBlocksFlag;
    private ItemStack carriedItem;

    BlockPos offeringBenchPos;
    private int food;
    private float foodModifier;
    private float consumptionRate;
    private float toConsume;

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

    public static AttributeSupplier.Builder createAtributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 24);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(ModTags.Items.FAIRY_FOOD), false));
        this.goalSelector.addGoal(1, new UpdateFoodConsumptionGoal(this, 0.1f));
        this.goalSelector.addGoal(2, new FairyEatFromOfferings(this, 0.6f, 30));
        this.goalSelector.addGoal(3, new FairyUpdateRegisteredBlocks(this));
        this.goalSelector.addGoal(5, new FairyCollectResources(this, 0.45f, 8, 15));
        this.goalSelector.addGoal(10, new FairyEnchantResources(this, 0.6f, 30));
        this.goalSelector.addGoal(15, new FairyWanderGoal(this, 8, 0.3f));
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

    public boolean hasofferingBench(){
        return this.offeringBenchPos != null && this.level().getBlockEntity(offeringBenchPos) != null;
    }

    public OfferingBenchBlockEntity getofferingBench() {
        return (OfferingBenchBlockEntity)level().getBlockEntity(offeringBenchPos);
    }

    public BlockPos getOfferingBenchPos() {
        return offeringBenchPos;
    }

    public BlockPos[] getRegisteredBlockPosArray(){
        return (BlockPos[]) this.registeredUtilBlockEntityPos.keySet().toArray();
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

    public void clearBlockEntityData(){
        this.offeringBenchPos = null;
        this.registeredUtilBlockEntityPos = new HashMap<>();
    }

    public void registerNewOfferingBench(OfferingBenchBlockEntity blockEntity){
        this.offeringBenchPos = blockEntity.getBlockPos();
    }

    public boolean canRegisterNewBlock(BlockEntity blockEntity){
        return ((FairyInteractBlockEntity) blockEntity).getFairyWeight() + this.currentWeight < Fairy.MAX_UTIL_BLOCK_POOL;
    }

    public void registerNewBlock(BlockEntity blockEntity){
        int fairyWeight = ((FairyInteractBlockEntity) blockEntity).getFairyWeight();
        this.currentWeight += fairyWeight;
        this.registeredUtilBlockEntityPos.put(blockEntity.getBlockPos(), fairyWeight);
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

        for (int i = 0; i < this.registeredUtilBlockEntityPos.size(); i++){
            BlockPos pos = (BlockPos) this.registeredUtilBlockEntityPos.keySet().toArray()[i];
            pCompound.put("fairy.registered_block_entity_pos_" + i, NbtUtils.writeBlockPos(pos));
            pCompound.putInt("fairy.registered_block_entity_weight_" + i, this.registeredUtilBlockEntityPos.get(pos));
        }

        pCompound.putInt("fairy.registered_block_entity_count", this.registeredUtilBlockEntityPos.size());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        int loopCount = pCompound.getInt("fairy.registered_block_entity_count");

        for (int i = loopCount - 1; i >= 0; i--){
            this.registeredUtilBlockEntityPos.put(
                    NbtUtils.readBlockPos(pCompound.getCompound("fairy.registered_block_entity_pos_" + i)),
                    pCompound.getInt("fairy.registered_block_entity_weight_" + i));
        }

        if (pCompound.contains("fairy.offering_bench")){
            this.offeringBenchPos = NbtUtils.readBlockPos(pCompound.getCompound("fairy.offering_bench"));
        }

        this.foodModifier = pCompound.getFloat("fairy.food_modifier");
        this.food = pCompound.getInt("fairy.food");
        this.currentWeight = pCompound.getInt("fairy.current_weight");

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
    }

    private boolean canEatFromOfferings(){
        return this.goalSelector.getAvailableGoals().stream().filter((goal) -> goal.getGoal().getClass() == FairyEatFromOfferings.class)
                .anyMatch(WrappedGoal::canUse);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.isTame() && !this.hasCarriedItem() && this.getFood() <= 0 && !canEatFromOfferings()){
            setHungry(true);
            setUpdateBlocksFlag(true);
        }

        if (this.isTame() && this.getFood() > 0){
            this.toConsume += this.getConsumptionRate();
            this.decrementFood(Mth.floor(this.toConsume));
            this.toConsume %= 1;
        }
    }

    public class FairyEatFromOfferings extends Goal {
        private final Fairy fairy;

        private final float SPEED_MODIFIER;
        private final int EAT_TIME;

        private int eatingTime;
        private boolean hasEaten;

        public FairyEatFromOfferings(Fairy fairy, float speedModifier, int eatingTicks) {
            this.fairy = fairy;
            this.SPEED_MODIFIER = speedModifier;
            this.EAT_TIME = eatingTicks;
        }

        private boolean checkBenchValidity(){

            OfferingBenchBlockEntity offeringBenchPos = fairy.getofferingBench();
            return offeringBenchPos.isValidForInteraction() && offeringBenchPos.hasFood();
        }

        @Override
        public boolean canUse() {
            if (this.fairy.isTame() && !this.fairy.hasCarriedItem() && this.fairy.hasofferingBench() && this.fairy.getFood() <= 0){
                return checkBenchValidity();
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.hasEaten && fairy.hasofferingBench() && checkBenchValidity();
        }

        @Override
        public void start() {
            super.start();
            this.moveToBlock(fairy.getofferingBench().getBlockPos().above(1));
            this.eatingTime = 0;
            this.hasEaten = false;
        }

        private void moveToBlock(BlockPos pos){
            fairy.moveControl.setWantedPosition(pos.getX(), pos.getY(), pos.getZ(), this.SPEED_MODIFIER);
        }

        private boolean hasArrived(){
            return fairy.getofferingBench().getBlockPos().above(1).closerToCenterThan(this.fairy.position(), 0.5f);
        }

        private void makeItemParticle(Item item){
            Vec3 fairyPos = this.fairy.position();
            fairy.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)),
                    fairyPos.x, fairyPos.y, fairyPos.z, 1, 1, 1);
        }

        @Override
        public void tick() {
            super.tick();

            if (hasArrived() && this.eatingTime < this.EAT_TIME){
                if (this.eatingTime % 15 == 0){
                    Item foodItem = fairy.getofferingBench().getNextFoodItem();
                    makeItemParticle(foodItem);
                }
                this.eatingTime++;
            }
            else if (this.eatingTime >= this.EAT_TIME){
                OfferingBenchBlockEntity offeringBenchPos = fairy.getofferingBench();
                Item foodItem = offeringBenchPos.getNextFoodItem();
                offeringBenchPos.consumeFood();

                this.fairy.setFood(OfferingBenchBlockEntity.getFoodTick(foodItem));
                this.fairy.setFoodModifier(OfferingBenchBlockEntity.getChanceModifier(foodItem));

                this.hasEaten = true;

                if (this.fairy.isHungry()){
                    this.fairy.setHungry(false);
                    this.fairy.setUpdateBlocksFlag(true);
                }
            }
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
            return this.fairy.isTame() && (getUpdateBlocksFlag() || requiresUpdate());
        }

        @Override
        public void start() {
            super.start();

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
            return this.fairy.navigation.isDone() && this.fairy.getNoActionTime() >= 100 && this.fairy.random.nextInt(10) == 0;
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
            int radius = 8;
            if (this.fairy.isTame() && this.fairy.hasofferingBench() && !this.fairy.getOfferingBenchPos().closerThan(this.fairy.blockPosition(), MAX_OFFERING_BENCH_DISTANCE)) {
                center = Vec3.atCenterOf(this.fairy.getOfferingBenchPos());
            } else {
                center = this.fairy.getViewVector(0.0F);
            }

            if (this.fairy.isTame() && this.fairy.hasofferingBench()){
                radius = this.MAX_OFFERING_BENCH_DISTANCE / 2;
            }

            Vec3 finalPos = HoverRandomPos.getPos(this.fairy, radius, radius / 2, center.x, center.z, 1.5f, 3, 1);
            return finalPos != null ? finalPos : AirAndWaterRandomPos.getPos(this.fairy, radius, radius / 2, -2, center.x, center.z, 1.5f);
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

    private class FairyCollectResources extends MoveToBlockGoal {
        protected final Fairy fairy;

        private static final BlockEntityType TYPE = ModBlockEntities.FAIRY_COLLECTION_TRAY_BLOCK_ENTITY.get();
        protected static final TagKey<Block> VALID_BLOCK_TAG = ModTags.Blocks.FAIRY_HARVESTABLES;
        private final float SPEED_MODIFIER;
        private final int COLLECT_TIME;

        private boolean hasReachedTarget;
        private boolean hasDeposited;
        private int collectionTime;

        public FairyCollectResources(Fairy fairy, float speedModifier, int searchRange, int collectTime) {
            super(fairy, speedModifier, searchRange);
            this.fairy = fairy;
            this.SPEED_MODIFIER = speedModifier;
            this.COLLECT_TIME = collectTime;
        }

        public boolean hasValidBlockEntity(){
            BlockEntity block = this.fairy.level().getBlockEntity(this.fairy.getRegisteredBlockPosArray()[0]);
            return block != null && block.getType() == this.TYPE && ((FairyInteractBlockEntity) block).isValidForInteraction();
        }

        @Override
        public boolean canUse() {
            return this.fairy.isTame() && this.fairy.getFood() > 0 && this.fairy.random.nextInt(10) == 0 && !this.fairy.registeredUtilBlockEntityPos.isEmpty()
                && hasValidBlockEntity();
        }

        @Override
        public boolean canContinueToUse() {
            return ((!this.hasDeposited && this.fairy.getFood() > 0
                    && hasValidBlockEntity())
                    || (!this.hasDeposited && this.fairy.hasCarriedItem())) && super.canContinueToUse();
        }

        private void moveToBlock(BlockPos pos){
            fairy.moveControl.setWantedPosition(pos.getX(), pos.getY(), pos.getZ(), this.SPEED_MODIFIER);
        }

        private boolean hasArrivedAtBlockEntity(){
            return this.fairy.getRegisteredBlockPosArray()[0].above(1).closerToCenterThan(this.fairy.position(), 0.5f);
        }

        @Override
        public void start() {
            super.start();
            this.hasDeposited = false;
            this.hasReachedTarget = false;
            this.collectionTime = 0;
        }

        @Override
        public void tick() {
            super.tick();

            if (this.isReachedTarget() && !this.hasReachedTarget){
                this.hasReachedTarget = true;
            }

            if (this.hasReachedTarget && this.collectionTime < this.COLLECT_TIME){
                this.collectionTime++;
                if (this.collectionTime >= this.COLLECT_TIME){
                    collectFromBlock();
                    this.moveToBlock(this.fairy.getRegisteredBlockPosArray()[0]);
                }
            }

            if (this.hasArrivedAtBlockEntity() && this.fairy.hasCarriedItem()){
                depositItem();
                this.hasDeposited = true;
            }
        }

        protected void depositItem(){
            ((FairyCollectionTrayBlockEntity) this.fairy.level().getBlockEntity(this.fairy.getRegisteredBlockPosArray()[0])).depositItem(
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

                this.fairy.level().setBlock(this.blockPos, ((FairyMineralClusterBlock) blockState).getStateInSequence(0));
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
            return levelReader.getBlockState(blockPos).is(this.VALID_BLOCK_TAG);
        }
    }

    private class FairyEnchantResources extends Goal {
        private final Fairy fairy;

        private final static BlockEntityType TYPE = ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get();
        private final float SPEED_MODIFIER;
        private final int USE_TIME;

        private int usingTime;
        private boolean hasUsed;
        private BlockPos currentBench;

        public FairyEnchantResources(Fairy fairy, float speedModifier, int useTicks) {
            this.fairy = fairy;
            this.SPEED_MODIFIER = speedModifier;
            this.USE_TIME = useTicks;
        }

        public BlockPos getRandomValidBenchPos(){
            List<BlockPos> posList = new ArrayList<>();
            for (BlockPos pos : this.fairy.getRegisteredBlockPosArray()){
                BlockEntity blockEntity = this.fairy.level().getBlockEntity(pos);
                if (blockEntity != null && blockEntity.getType() == this.TYPE){
                    posList.add(pos);
                }
            }
            return posList.isEmpty() ? null : posList.get(this.fairy.random.nextInt(posList.size()));
        }

        public  FairyInfusionBenchBlockEntity getBench(BlockPos pos){
            return ((FairyInfusionBenchBlockEntity) this.fairy.level().getBlockEntity(pos))
        }

        public boolean hasValidBlockEntity(){
            return getRandomValidBenchPos() != null;
        }

        @Override
        public boolean canUse() {
            return this.fairy.isTame() && this.fairy.random.nextInt(10) == 0 && !this.fairy.hasCarriedItem() && this.fairy.getFood() > 0
                    && hasValidBlockEntity();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.hasUsed && this.fairy.getFood() > 0 && hasValidBlockEntity();
        }

        @Override
        public void start() {
            super.start();
            this.currentBench = getRandomValidBenchPos();
            this.moveToBlock(this.currentBench.above(1));
            this.usingTime = 0;
            this.hasUsed = false;
        }

        private void moveToBlock(BlockPos pos){
            fairy.moveControl.setWantedPosition(pos.getX(), pos.getY(), pos.getZ(), this.SPEED_MODIFIER);
        }

        private boolean hasArrived(){
            return getBench(this.currentBench).getBlockPos().above(1).closerToCenterThan(this.fairy.position(), 0.5f);
        }

        private void makeGlowParticle(){
            Vec3 fairyPos = this.fairy.position();
            fairy.level().addParticle(ParticleTypes.GLOW,
                    fairyPos.x, fairyPos.y, fairyPos.z, 1, 1, 1);
        }

        @Override
        public void tick() {
            super.tick();

            if (hasArrived() && this.usingTime < this.USE_TIME){
                if (this.usingTime % 15 == 0){
                    makeGlowParticle();
                }
                this.usingTime++;
            }
            else if (this.usingTime >= this.USE_TIME){
                getBench(this.currentBench).infuse();

                this.hasUsed = true;
            }
        }
    }
}
