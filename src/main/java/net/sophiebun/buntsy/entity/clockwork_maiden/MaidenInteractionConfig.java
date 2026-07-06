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
    private List<ItemStack> filter;
    private boolean whiteList;
    private boolean exact;

    private MaidenFillRegime fillRegime = null;
    private int priority = -1;

    private MaidenSelectionRegime selectionRegime = null;
    private int extractSize = -1;

    public MaidenInteractionConfig(BlockPos pos, Direction side, List<ItemStack> filter, boolean whiteList, boolean exact, int priority, MaidenFillRegime fillRegime, int extractSize, MaidenSelectionRegime selectionRegime){
        this.pos = pos;
        this.side = side;
        this.filter = filter;
        this.whiteList = whiteList;
        this.exact = exact;

        this.priority = priority;
        this.fillRegime = fillRegime;

        this.extractSize = extractSize;
        this.selectionRegime = selectionRegime;
    }

    public MaidenInteractionConfig(BlockPos pos, Direction side, List<ItemStack> filter, boolean whiteList, boolean exact, int priority, MaidenFillRegime fillRegime){
        this.pos = pos;
        this.side = side;
        this.filter = filter;
        this.whiteList = whiteList;
        this.exact = exact;

        this.priority = priority;
        this.fillRegime = fillRegime;
    }

    public MaidenInteractionConfig(BlockPos pos, Direction side, boolean whiteList, boolean exact, int priority, MaidenFillRegime fillRegime){
        this.pos = pos;
        this.side = side;
        this.whiteList = whiteList;
        this.exact = exact;

        this.filter = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            this.filter.add(ItemStack.EMPTY);
        }

        this.priority = priority;
        this.fillRegime = fillRegime;
    }

    public MaidenInteractionConfig(BlockPos pos, Direction side, List<ItemStack> filter, boolean whiteList, boolean exact, int extractSize, MaidenSelectionRegime selectionRegime){
        this.pos = pos;
        this.side = side;
        this.filter = filter;
        this.whiteList = whiteList;
        this.exact = exact;

        this.extractSize = extractSize;
        this.selectionRegime = selectionRegime;
    }

    public MaidenInteractionConfig(BlockPos pos, Direction side, boolean whiteList, boolean exact, int extractSize, MaidenSelectionRegime selectionRegime){
        this.pos = pos;
        this.side = side;
        this.whiteList = whiteList;
        this.exact = exact;

        this.filter = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            this.filter.add(ItemStack.EMPTY);
        }

        this.extractSize = extractSize;
        this.selectionRegime = selectionRegime;
    }

    public static MaidenInteractionConfig makeNewConfig(boolean inserting, BlockPos pos){
        return inserting ?
                new MaidenInteractionConfig(pos, Direction.NORTH, false, false, 0, MaidenFillRegime.STACK):
                new MaidenInteractionConfig(pos, Direction.NORTH,  false, false, 64, MaidenSelectionRegime.NEAREST);
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
        tag.putInt("maiden_config.filter_size", filter.size());
        for (int i = 0; i < filter.size(); i++){
            tag.put("maiden_config.filter_item_" + i, filter.get(i).serializeNBT());
        }
        tag.putBoolean("maiden_config.whitelist", whiteList);
        tag.putBoolean("maiden_config.exact", exact);

        tag.putBoolean("maiden_config.has_fill_regime", this.fillRegime != null);
        if (this.fillRegime != null){
            tag.putInt("maiden_config.fill_regime", this.fillRegime.ordinal());
        }

        tag.putBoolean("maiden_config.has_priority", this.priority > -1);
        if (this.fillRegime != null){
            tag.putInt("maiden_config.priority", this.priority);
        }

        tag.putBoolean("maiden_config.has_selection_regime", this.selectionRegime != null);
        if (this.fillRegime != null){
            tag.putInt("maiden_config.selection_regime", this.selectionRegime.ordinal());
        }

        tag.putBoolean("maiden_config.has_extract_size", this.extractSize > -1);
        if (this.fillRegime != null){
            tag.putInt("maiden_config.extract_size", this.extractSize);
        }

        return tag;
    }

    public static MaidenInteractionConfig parseCompound(CompoundTag tag) {
        BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("maiden_config.pos"));
        Direction side = Direction.values()[tag.getInt("maiden_config.direction")];
        List<ItemStack> filter = new ArrayList<>();
        int filterSize = tag.getInt("maiden_config.filter_size");
        for (int i = 0; i < filterSize; i++){
            ItemStack stack = ItemStack.EMPTY;
            stack.deserializeNBT(tag.getCompound("maiden_config.filter_item_" + i));
            filter.add(stack);
        }
        boolean whiteList = tag.getBoolean("maiden_config.whitelist");
        boolean exact = tag.getBoolean("maiden_config.exact");

        MaidenFillRegime regime = null;
        if (tag.getBoolean("maiden_config.has_fill_regime")){
            regime = MaidenFillRegime.values()[tag.getInt("maiden_config.fill_regime")];
        }

        int priority = -1;
        if (tag.getBoolean("maiden_config.has_priority")){
            priority = tag.getInt("maiden_config.priority");
        }

        MaidenSelectionRegime regime2 = null;
        if (tag.getBoolean("maiden_config.has_selection_regime")){
            regime2 = MaidenSelectionRegime.values()[tag.getInt("maiden_config.selection_regime")];
        }

        int extractSize = -1;
        if (tag.getBoolean("maiden_config.has_extract_size")){
            extractSize = tag.getInt("maiden_config.extract_size");
        }

        return new MaidenInteractionConfig(pos, side, filter, whiteList, exact, priority, regime, extractSize, regime2);
    }

    public MaidenInteractionConfig copy() {
        return new MaidenInteractionConfig(pos, side, filter.stream().toList(), whiteList, exact, priority, fillRegime, extractSize, selectionRegime);
    }

    public void cycleFillRegime() {
        int regime = this.fillRegime.ordinal();
        if (++regime >= MaidenFillRegime.values().length){
            regime = 0;
        }
        this.fillRegime = MaidenFillRegime.values()[regime];
    }

    public MaidenFillRegime getFillRegime() {
        return fillRegime;
    }

    public void cycleSelectionRegime() {
        int regime = this.selectionRegime.ordinal();
        if (++regime >= MaidenSelectionRegime.values().length){
            regime = 0;
        }
        this.selectionRegime = MaidenSelectionRegime.values()[regime];
    }

    public MaidenSelectionRegime getSelectionRegime() {
        return selectionRegime;
    }

    public void setExtractCount(int extractSize) {
        this.extractSize = extractSize;
    }

    public int getExtractCount() {
        return extractSize;
    }
}
