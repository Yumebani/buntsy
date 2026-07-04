package net.sophiebun.buntsy.entity.clockwork_maiden;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record MaidenInteractionConfig(BlockPos pos, Direction side, int priority, List<ItemStack> filter, boolean whiteList, boolean exact) {

    public boolean matchesFilter(ItemStack stackToMove) {
        for (ItemStack stack : filter){
            if (stack.is(stackToMove.getItem())){
                if (!exact || (exact && stack.getTag().equals(stackToMove.getTag()))){
                    return whiteList;
                }
            }
        }
        return false;
    }
}
