package net.sophiebun.buntsy.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FairyFoodItem extends Item {

    private final int foodTick;
    private final float terrariumChanceMult;

    public FairyFoodItem(Properties pProperties, int foodTick, float terrariumChanceMult) {
        super(pProperties);
        this.foodTick = foodTick;
        this.terrariumChanceMult = terrariumChanceMult;
    }

    public int getFoodTick(){
        return this.foodTick;
    }

    public float getTerrariumChanceMult() {
        return terrariumChanceMult;
    }
}
