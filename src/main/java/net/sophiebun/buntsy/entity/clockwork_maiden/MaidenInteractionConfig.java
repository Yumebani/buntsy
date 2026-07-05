package net.sophiebun.buntsy.entity.clockwork_maiden;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class MaidenInteractionConfig {

    private BlockPos pos;
    private Direction side;
    private int priority;
    private List<ItemStack> filter;
    private boolean whiteList;
    private boolean exact;

    public MaidenInteractionConfig(BlockPos pos, Direction side, int priority, List<ItemStack> filter, boolean whiteList, boolean exact){
        this.pos = pos;
        this.side = side;
        this.priority = priority;
        this.filter = filter;
        this.whiteList = whiteList;
        this.exact = exact;
    }

    public MaidenInteractionConfig(BlockPos pos, Direction side, int priority, boolean whiteList, boolean exact){
        this.pos = pos;
        this.side = side;
        this.priority = priority;
        this.whiteList = whiteList;
        this.exact = exact;

        this.filter = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            this.filter.add(ItemStack.EMPTY);
        }
    }

    public void setWhiteList(boolean whiteList) {
        this.whiteList = whiteList;
    }

    public boolean getWhiteList() {
        return whiteList;
    }

    public void setExact(boolean exact) {
        this.exact = exact;
    }

    public boolean getExact() {
        return exact;
    }

    public BlockPos getPos() {
        return pos;
    }

    public List<ItemStack> getFilter() {
        return filter;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Direction getSide() {
        return side;
    }

    public void setSide(Direction side) {
        this.side = side;
    }

    public boolean matchesFilter(ItemStack stackToMove) {
        for (ItemStack stack : filter){
            if (!stack.isEmpty() && stack.is(stackToMove.getItem())){
                if (!exact || (exact && stack.getTag().equals(stackToMove.getTag()))){
                    return whiteList;
                }
            }
        }
        return false;
    }

    public CompoundTag getCompound() {
        CompoundTag tag = new CompoundTag();

        tag.put("maiden_config.pos", NbtUtils.writeBlockPos(pos));
        tag.putInt("maiden_config.direction", side.ordinal());
        tag.putInt("maiden_config.priority", priority);
        tag.putInt("maiden_config.filter_size", filter.size());
        for (int i = 0; i < filter.size(); i++){
            tag.put("maiden_config.filter_item_" + i, filter.get(i).serializeNBT());
        }
        tag.putBoolean("maiden_config.whitelist", whiteList);
        tag.putBoolean("maiden_config.exact", exact);

        return tag;
    }

    public static MaidenInteractionConfig parseCompound(CompoundTag tag) {
        BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("maiden_config.pos"));
        Direction side = Direction.values()[tag.getInt("maiden_config.direction")];
        int priority = tag.getInt("maiden_config.priority");
        List<ItemStack> filter = new ArrayList<>();
        int filterSize = tag.getInt("maiden_config.filter_size");
        for (int i = 0; i < filterSize; i++){
            ItemStack stack = ItemStack.EMPTY;
            stack.deserializeNBT(tag.getCompound("maiden_config.filter_item_" + i));
            filter.add(stack);
        }
        boolean whiteList = tag.getBoolean("maiden_config.whitelist");
        boolean exact = tag.getBoolean("maiden_config.exact");

        return new MaidenInteractionConfig(pos, side, priority, filter, whiteList, exact);
    }
}
