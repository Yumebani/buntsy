package net.sophiebun.buntsy.item.custom;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FumeBottle extends Item {

    public enum FumeType {
        PRODUCTIVITY,
        EFFICIENCY,
        ACCELERATION,
        GROWTH,
        CHANGE,
        REJUVENATION,
        SIN,
        GLUTTONY,
        SLOTH
    }

    public static ItemColor getTint(){

        return ((pStack, pTintIndex) -> {
            if (!pStack.hasTag()) return pTintIndex == 0 ? 0x00fcba03 : 0xFFFFFFFF;
            int fumeId = pStack.getTag().getInt("buntsy.fumeType");
            switch (FumeType.values()[fumeId]) {
                case PRODUCTIVITY -> {return pTintIndex == 0 ? 0x00fcba03 : 0xFFFFFFFF;
                }
                case EFFICIENCY -> {return pTintIndex == 0 ? 0x00baf50a : 0xFFFFFFFF;
                }
                case ACCELERATION -> {return pTintIndex == 0 ? 0x006cdef5 : 0xFFFFFFFF;
                }
                case GROWTH -> {return pTintIndex == 0 ? 0x00356b1c : 0xFFFFFFFF;
                }
                case CHANGE -> {return pTintIndex == 0 ? 0x004f1452 : 0xFFFFFFFF;
                }
                case REJUVENATION -> {return pTintIndex == 0 ? 0x00f5a6e4 : 0xFFFFFFFF;
                }
                case SIN -> {return pTintIndex == 0 ? 0x00cc2f3f : 0xFFFFFFFF;
                }
                case GLUTTONY -> {return pTintIndex == 0 ? 0x00d6891e : 0xFFFFFFFF;
                }
                case SLOTH -> {return pTintIndex == 0 ? 0x001d28a1 : 0xFFFFFFFF;
                }
            }

            return pTintIndex == 0 ? 0x00fcba03 : 0xFFFFFFFF;
        });
    }

    public FumeBottle(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if (pStack.hasTag()){
            int currentFume = pStack.getTag().getInt("buntsy.fumeType");
            int level = pStack.getTag().getInt("buntsy.fumeLevel");
            pTooltipComponents.add(Component.literal(
                    Component.translatable("fume.buntsy." + FumeType.values()[currentFume].toString().toLowerCase()).getString() + " " +
                    Component.translatable("fume.buntsy.level." + level).getString()));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
