package net.sophiebun.buntsy.blocks.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FilterSlot extends SlotItemHandler {

    private final int itemHandlerIndex;

    public FilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
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

    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
