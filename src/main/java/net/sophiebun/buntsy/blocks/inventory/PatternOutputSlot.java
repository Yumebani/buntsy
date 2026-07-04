package net.sophiebun.buntsy.blocks.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PatternOutputSlot extends SlotItemHandler implements IHideableSlot {
    public PatternOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
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
