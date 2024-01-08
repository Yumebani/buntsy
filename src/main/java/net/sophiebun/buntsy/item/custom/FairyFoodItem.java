package net.sophiebun.buntsy.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FairyFoodItem extends Item {

    private final int foodTick;

    public static final int HONEY_BOTTLE_FOOD_TICK = 1600;
    public static final int SUGAR_FOOD_TICK = 400;

    public FairyFoodItem(Properties pProperties, int foodTick) {
        super(pProperties);
        this.foodTick = foodTick;
    }

    public int getFoodTick(){
        return this.foodTick;
    }
}
