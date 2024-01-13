package net.sophiebun.buntsy.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BowlFoodFairyFoodItem extends FairyFoodItem {
    public BowlFoodFairyFoodItem(Properties pProperties, int foodTick, float terrariumChanceMult) {
        super(pProperties, foodTick, terrariumChanceMult);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack $$3 = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        return pEntityLiving instanceof Player && ((Player)pEntityLiving).getAbilities().instabuild ? $$3 : new ItemStack(Items.BOWL);
    }
}
