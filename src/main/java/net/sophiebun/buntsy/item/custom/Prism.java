package net.sophiebun.buntsy.item.custom;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Prism extends Item {

    public static final String[] PrismTypes = {
            "mineral",
            "fauna",
            "flora",
            "collectors",
            "overworld",
            "nether",
            "end",
            "dense_matter"
    };

    public static ItemColor getTint(){

        return ((pStack, pTintIndex) -> {
            if (!pStack.hasTag()) return 0xFFFFFFFF;
            switch (pStack.getTag().getString("buntsy.prismType")) {
                case "mineral" -> {return 0x7bede7;
                }
                case "fauna" -> {return 0xffaf2e;
                }
                case "flora" -> {return 0xa3f03e;
                }
                case "collectors" -> {return 0x1e3cfc;
                }
                case "overworld" -> {return 0x66ff66;
                }
                case "nether" -> {return 0xbf1d1d;
                }
                case "end" -> {return 0xbf26c9;
                }
                case "dense_matter" -> {return 0x220f24;
                }
            }

            return 0xFFFFFFFF;
        });
    }

    public Prism(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if (pStack.hasTag()){
            String prismType = pStack.getTag().getString("buntsy.prismType");
            pTooltipComponents.add(Component.translatable("prism.buntsy." + prismType));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
