package net.sophiebun.buntsy.item.custom;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sophiebun.buntsy.components.ModDataComponents;

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
        SLOTH,
        WRATH
    }

    public static ItemColor getTint(){

        return ((pStack, pTintIndex) -> {
            if (!pStack.has(ModDataComponents.FUME_TYPE)) return pTintIndex == 0 ? 0x00fcba03 : 0xFFFFFFFF;
            int fumeId = pStack.get(ModDataComponents.FUME_TYPE).type();
            switch (FumeType.values()[fumeId]) {
                case PRODUCTIVITY -> {return pTintIndex == 0 ? 0xFFfcba03 : 0xFFFFFFFF;
                }
                case EFFICIENCY -> {return pTintIndex == 0 ? 0xFFbaf50a : 0xFFFFFFFF;
                }
                case ACCELERATION -> {return pTintIndex == 0 ? 0xFF6cdef5 : 0xFFFFFFFF;
                }
                case GROWTH -> {return pTintIndex == 0 ? 0xFF356b1c : 0xFFFFFFFF;
                }
                case CHANGE -> {return pTintIndex == 0 ? 0xFF4f1452 : 0xFFFFFFFF;
                }
                case REJUVENATION -> {return pTintIndex == 0 ? 0xFFf5a6e4 : 0xFFFFFFFF;
                }
                case SIN -> {return pTintIndex == 0 ? 0xFFcc2f3f : 0xFFFFFFFF;
                }
                case GLUTTONY -> {return pTintIndex == 0 ? 0xFFd6891e : 0xFFFFFFFF;
                }
                case SLOTH -> {return pTintIndex == 0 ? 0xFF1d28a1 : 0xFFFFFFFF;
                }
                case WRATH -> {return pTintIndex == 0 ? 0xFFde2a1d : 0xFFFFFFFF;
                }
            }

            return pTintIndex == 0 ? 0xFFfcba03 : 0xFFFFFFFF;
        });
    }

    public FumeBottle(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (stack.has(ModDataComponents.FUME_TYPE)){
            net.sophiebun.buntsy.codec.FumeType fumeType = stack.get(ModDataComponents.FUME_TYPE);
            tooltipComponents.add(Component.literal(
                    Component.translatable("fume.buntsy." + FumeType.values()[fumeType.type()].toString().toLowerCase()).getString() + " " +
                            Component.translatable("fume.buntsy.level." + fumeType.level()).getString()));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
