package net.sophiebun.buntsy.entity.animals;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.sophiebun.buntsy.blocks.entity.OfferingBenchBlockEntity;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Fairy extends TamableAnimal implements FlyingAnimal {

    //Everything is server side
    private static final int MAX_UTIL_BLOCK_POOL = 4;

    List<BlockEntity> registeredUtilBlockEntities;
    private int currentWeight;

    BlockEntity offeringBench;
    private int food;
    private float foodModifier;

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
        this.goalSelector.addGoal(2, new FairyEatFromOfferings(this, 0.6f, 30));
        this.goalSelector.addGoal(2, new FairyUpdateRegisteredBlocks(this));
        this.goalSelector.addGoal(3, new FairyWanderGoal(this));
        this.goalSelector.addGoal(5, new FairyCollectResources(this));
        this.goalSelector.addGoal(10, new FairyEnchantResources(this));
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getFood() {
        return food;
    }

    public void setFoodModifier(float foodModifier) {
        this.foodModifier = foodModifier;
    }

    public float getFoodModifier() {
        return foodModifier;
    }

    public boolean hasOfferingBench(){
        return !(this.offeringBench == null);
    }

    public BlockEntity getOfferingBench() {
        return offeringBench;
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
        Item item = itemstack.getItem();
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

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
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

            OfferingBenchBlockEntity offeringBench = (OfferingBenchBlockEntity)fairy.getOfferingBench();
            return offeringBench.isValid() && offeringBench.hasFood();
        }

        @Override
        public boolean canUse() {
            if (fairy.hasOfferingBench() && fairy.getFood() <= 0){
                return checkBenchValidity();
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.hasEaten && fairy.hasOfferingBench() && checkBenchValidity();
        }

        @Override
        public void start() {
            super.start();
            this.moveToBlock(fairy.getOfferingBench().getBlockPos().above(1));
            this.eatingTime = 0;
            this.hasEaten = false;
        }

        private void moveToBlock(BlockPos pos){
            fairy.moveControl.setWantedPosition(pos.getX(), pos.getY(), pos.getZ(), this.SPEED_MODIFIER);
        }

        private boolean hasArrived(){
            return fairy.getOfferingBench().getBlockPos().closerToCenterThan(this.fairy.position(), 0.5f);
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
                    Item foodItem = ((OfferingBenchBlockEntity)fairy.getOfferingBench()).getNextFoodItem();
                    makeItemParticle(foodItem);
                }
                this.eatingTime++;
            }
            else if (this.eatingTime >= this.EAT_TIME){
                OfferingBenchBlockEntity offeringBench = (OfferingBenchBlockEntity)fairy.getOfferingBench();
                Item foodItem = offeringBench.getNextFoodItem();
                offeringBench.consumeFood();

                this.fairy.setFood(OfferingBenchBlockEntity.getFoodTick(foodItem));
                this.fairy.setFoodModifier(OfferingBenchBlockEntity.getChanceModifier(foodItem));

                this.hasEaten = true;
            }
        }
    }
}
