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
            if (!pStack.has(ModDataComponents.PRISM_TYPE)) return 0xFFFFFFFF;
            switch (pStack.get(ModDataComponents.PRISM_TYPE)) {
                case "mineral" -> {return 0xFF7bede7;
                }
                case "fauna" -> {return 0xFFffaf2e;
                }
                case "flora" -> {return 0xFFa3f03e;
                }
                case "collectors" -> {return 0xFF1e3cfc;
                }
                case "overworld" -> {return 0xFF66ff66;
                }
                case "nether" -> {return 0xFFbf1d1d;
                }
                case "end" -> {return 0xFFbf26c9;
                }
                case "dense_matter" -> {return 0xFF220f24;
                }
            }

            return 0xFFFFFFFF;
        });
    }

    public Prism(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (stack.has(ModDataComponents.PRISM_TYPE)){
            tooltipComponents.add(Component.translatable("prism.buntsy." + stack.get(ModDataComponents.PRISM_TYPE)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
