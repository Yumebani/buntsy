package net.sophiebun.buntsy.item.custom;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Catalyst extends Item {

    public Catalyst(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if (pStack.hasTag()){
            int currentFume = pStack.getTag().getInt("buntsy.fumeType");
            String type = pStack.getTag().getString("buntsy.catalystType");
            pTooltipComponents.add(Component.literal(
                    Component.translatable("fume.buntsy." + FumeBottle.FumeType.values()[currentFume].toString().toLowerCase()).getString() + " " +
                    Component.translatable("item.buntsy.catalystType." + type).getString()));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
