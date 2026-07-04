package net.sophiebun.buntsy.blocks.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PatternSlot extends SlotItemHandler  implements IHideableSlot {

    private final int itemHandlerIndex;

    public PatternSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        itemHandlerIndex = index;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        getItemHandler().insertItem(itemHandlerIndex, new ItemStack(stack.getItem(), 1), false);
        return false;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        getItemHandler().extractItem(itemHandlerIndex, 1, false);
        return false;
    }

    private boolean hidden = false;

    @Override
    public void setHidden(boolean set) {
        hidden = set;
    }

    @Override
    public boolean hasItem() {
        return !hidden && super.hasItem();
    }
}
