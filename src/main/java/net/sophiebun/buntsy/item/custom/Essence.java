package net.sophiebun.buntsy.item.custom;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sophiebun.buntsy.components.ModDataComponents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Essence extends Item {

    public static final String[] EssenceTypes = {
            "crystalline",
            "metallic",
            "valuable",
            "fauna_sustenance",
            "fauna",
            "flora_sustenance",
            "flora",
            "musician_1",
            "musician_2",
            "coral",
            "oceanic",
            "overworld_sapling",
            "overworld_creature",
            "nether_creature",
            "nether_flora",
            "nether_valuables",
            "end_creature",
            "end_flora",
            "dense_matter"
    };

    public static ItemColor getTint(){

        return ((pStack, pTintIndex) -> {
            if (!pStack.has(ModDataComponents.ESSENCE_TYPE)) return 0xFFFFFFFF;
            switch (pStack.get(ModDataComponents.ESSENCE_TYPE)) {
                case "crystalline" -> {return 0xFF5cf1ff;
                }
                case "metallic" -> {return 0xFFd9dbde;
                }
                case "valuable" -> {return 0xFFffef5c;
                }
                case "fauna_sustenance" -> {return 0xFFff7e73;
                }
                case "fauna" -> {return 0xFFFFFFFF;
                }
                case "flora_sustenance" -> {return 0xFFffaf2e;
                }
                case "flora" -> {return 0xFFa3f03e;
                }
                case "musician_1" -> {return 0xFFfca71e;
                }
                case "musician_2" -> {return 0xFF1ee3fc;
                }
                case "coral" -> {return 0xFFf9a1ff;
                }
                case "oceanic" -> {return 0xFF00c8ff;
                }
                case "overworld_sapling" -> {return 0xFF25b827;
                }
                case "overworld_creature" -> {return 0xFF90ab90;
                }
                case "nether_creature" -> {return 0xFFf54242;
                }
                case "nether_flora" -> {return 0xFFe9f542;
                }
                case "nether_valuables" -> {return 0xFFe86b23;
                }
                case "end_creature" -> {return 0xFFed82f5;
                }
                case "end_flora" -> {return 0xFF85228c;
                }
                case "dense_matter" -> {return 0xFF220f24;
                }
            }

            return 0xFFFFFFFF;
        });
    }

    public Essence(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (stack.has(ModDataComponents.ESSENCE_TYPE)){
            tooltipComponents.add(Component.translatable("essence.buntsy." + stack.get(ModDataComponents.ESSENCE_TYPE)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
