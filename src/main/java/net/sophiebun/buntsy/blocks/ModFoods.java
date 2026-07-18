package net.sophiebun.buntsy.blocks;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties WINTER_ROOT = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.5f)
            .build();
    public static final FoodProperties BOWL_OF_CARAMEL = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.8f)
            .build();
    public static final FoodProperties BOWL_OF_ROCKCANDY = new FoodProperties.Builder().nutrition(4)
            .saturationMod(1.2f)
            .build();
    public static final FoodProperties STRAWBERRY = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.5f)
            .build();
    public static final FoodProperties CARAMEL_STRAWBERRIES = new FoodProperties.Builder().nutrition(7)
            .saturationMod(0.8f)
            .build();
    public static final FoodProperties CHOCOLATE_STRAWBERRIES = new FoodProperties.Builder().nutrition(7)
            .saturationMod(0.8f)
            .build();
    public static final FoodProperties CHOCOLATE = new FoodProperties.Builder().nutrition(3)
            .saturationMod(0.5f)
            .build();
    public static final FoodProperties ICECREAM = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.6f)
            .build();
    public static final FoodProperties CHOCOLATE_ICECREAM = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.6f)
            .build();
    public static final FoodProperties CARAMEL_ICECREAM = new FoodProperties.Builder().nutrition(5)
            .saturationMod(0.6f)
            .build();
    public static final FoodProperties TRIPLE_SHOT_ICECREAM = new FoodProperties.Builder().nutrition(9)
            .saturationMod(0.8f)
            .build();
    public static final FoodProperties GOLDEN_STRAWBERRY = new FoodProperties.Builder().nutrition(2).fast()
            .saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1200), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200), 1f)
            .build();
}
