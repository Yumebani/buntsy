package net.sophiebun.buntsy.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sophiebun.buntsy.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class FairyInBottleSlot extends SlotItemHandler {

    public FairyInBottleSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() == ModItems.FAIRY_IN_A_BOTTLE.get();
    }
}
