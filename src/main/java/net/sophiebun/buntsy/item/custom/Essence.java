package net.sophiebun.buntsy.item.custom;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
            if (!pStack.hasTag()) return 0xFFFFFFFF;
            switch (pStack.getTag().getString("buntsy.essenceType")) {
                case "crystalline" -> {return 0x5cf1ff;
                }
                case "metallic" -> {return 0xd9dbde;
                }
                case "valuable" -> {return 0xffef5c;
                }
                case "fauna_sustenance" -> {return 0xff7e73;
                }
                case "fauna" -> {return 0xFFFFFFFF;
                }
                case "flora_sustenance" -> {return 0xffaf2e;
                }
                case "flora" -> {return 0xa3f03e;
                }
                case "musician_1" -> {return 0xfca71e;
                }
                case "musician_2" -> {return 0x1ee3fc;
                }
                case "coral" -> {return 0xf9a1ff;
                }
                case "oceanic" -> {return 0x00c8ff;
                }
                case "overworld_sapling" -> {return 0x25b827;
                }
                case "overworld_creature" -> {return 0x90ab90;
                }
                case "nether_creature" -> {return 0xf54242;
                }
                case "nether_flora" -> {return 0xe9f542;
                }
                case "nether_valuables" -> {return 0xe86b23;
                }
                case "end_creature" -> {return 0xed82f5;
                }
                case "end_flora" -> {return 0x85228c;
                }
                case "dense_matter" -> {return 0x220f24;
                }
            }

            return 0xFFFFFFFF;
        });
    }

    public Essence(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if (pStack.hasTag()){
            String essenceType = pStack.getTag().getString("buntsy.essenceType");
            pTooltipComponents.add(Component.translatable("essence.buntsy." + essenceType));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
