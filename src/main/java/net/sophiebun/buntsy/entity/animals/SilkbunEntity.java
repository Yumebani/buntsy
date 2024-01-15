package net.sophiebun.buntsy.entity.animals;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class SilkbunEntity extends Rabbit {
    public SilkbunEntity(EntityType<? extends Rabbit> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(1, new RabbitPanicGoal(this, 2.2));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, Ingredient.of(new ItemLike[]{ModItems.STRAWBERRY.get(), ModItems.CARAMEL_STRAWBERRIES.get(), ModItems.GOLDEN_STRAWBERRY.get()}), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 0.8f));
        this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal(this, Wolf.class, 10.0F, 2.2, 2.2));
        this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal(this, Monster.class, 4.0F, 2.2, 2.2));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAtributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 24);
    }

    @Nullable
    @Override
    public SilkbunEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.SILKBUN_ENTITY.get().create(pLevel);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(ModItems.STRAWBERRY.get()) || pStack.is(ModItems.GOLDEN_STRAWBERRY.get());
    }
}
