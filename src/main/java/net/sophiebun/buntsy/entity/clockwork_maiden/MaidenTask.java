package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MaidenTask {

    private final int id;

    private final BlockPos terminalLoc;

    private final MaidenInteractionConfig extractBlock;
    private final List<MaidenInteractionConfig> insertBlocks;

    private int roundRobinSelector = 0;

    public MaidenTask(int id, BlockPos terminalLoc, MaidenInteractionConfig extractBlock, List<MaidenInteractionConfig> insertBlocks){
        this.id = id;
        this.terminalLoc = terminalLoc;
        this.extractBlock = extractBlock;
        this.insertBlocks = insertBlocks.stream().sorted(extractBlock.getSelectionRegime() == MaidenSelectionRegime.PRIORITY ?
                Comparator.comparingInt(MaidenInteractionConfig::getPriority) :
                Comparator.comparingInt(config -> Math.toIntExact(Math.round(config.getPos().getCenter().distanceTo(terminalLoc.getCenter()))))).toList();
    }

    private MaidenTask(int id, BlockPos terminalLoc, MaidenInteractionConfig extractBlock, List<MaidenInteractionConfig> insertBlocks, int roundRobinSelector){
        this.id = id;
        this.terminalLoc = terminalLoc;
        this.extractBlock = extractBlock;
        this.insertBlocks = insertBlocks.stream().sorted(extractBlock.getSelectionRegime() == MaidenSelectionRegime.PRIORITY ?
                Comparator.comparingInt(MaidenInteractionConfig::getPriority) :
                Comparator.comparingInt(config -> Math.toIntExact(Math.round(config.getPos().getCenter().distanceTo(terminalLoc.getCenter()))))).toList();
        this.roundRobinSelector = roundRobinSelector;
    }

    private ItemStack getExtractable(Level level, MaidenInteractionConfig nextConfig){

        if (!extractBlock.areFiltersCompatible(nextConfig)) return ItemStack.EMPTY;


        if (level.isLoaded(extractBlock.getPos())){

            if (level.isLoaded(extractBlock.getPos())) return ItemStack.EMPTY;
            BlockEntity entity = level.getBlockEntity(extractBlock.getPos());
            if (entity == null) return ItemStack.EMPTY;
            LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, extractBlock.getSide());

            List<ItemStack> extractableSpace = new ArrayList<>();

            capability.ifPresent(itemHandler -> {

                ItemStack target = null;
                int total = 0;

                for (int i = 0; i < itemHandler.getSlots(); i++){
                    ItemStack content = itemHandler.getStackInSlot(i);
                    if (!content.isEmpty() && extractBlock.matchesCombinedFilter(nextConfig, content)){
                        if (target == null || extractBlock.matchItems(content, target)){

                            int possibleTotal = content.getCount() + total;

                            ItemStack test = ItemStack.EMPTY;
                            test.deserializeNBT(content.serializeNBT());
                            test.setCount(possibleTotal);
                            int availableSpace = possibleTotal - tryPlace(level, nextConfig, test, true);

                            if (availableSpace > 0){

                                if (possibleTotal < Math.min(extractBlock.getExtractCount(), content.getMaxStackSize())){
                                    total = possibleTotal;
                                    if (target == null){
                                        target = content;
                                    }

                                    if (availableSpace < possibleTotal){
                                        break;
                                    }
                                }
                                else {
                                    total = Math.min(extractBlock.getExtractCount(), content.getMaxStackSize());
                                    target = content;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (target != null){
                    extractableSpace.add(
                            target.hasTag() ? new ItemStack(target.getItem(), total, target.getTag()):
                                    new ItemStack(target.getItem(), total));
                }

            });

            if (extractableSpace.isEmpty()){
                return ItemStack.EMPTY;
            } else return extractableSpace.get(0);
        }
        return ItemStack.EMPTY;
    }

    public static Integer tryPlace(Level level, MaidenInteractionConfig config, ItemStack itemStack, boolean simulated){
        if (level.isLoaded(config.getPos())) return itemStack.getCount();
        BlockEntity entity = level.getBlockEntity(config.getPos());
        if (entity == null) return itemStack.getCount();
        LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, config.getSide());

        List<Integer> availableSpace = new ArrayList<>();
        capability.ifPresent(itemHandler -> {
            availableSpace.add(getAvailableSpace(itemHandler, config, itemStack, simulated));
        });

        if (availableSpace.isEmpty()){
            return itemStack.getCount();
        } else return availableSpace.get(0);
    }

    public static Integer getAvailableSpace(IItemHandler handler, MaidenInteractionConfig config, ItemStack itemStack, boolean simulated){
        int total = itemStack.getCount();
        ItemStack matched = null;
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack content = handler.getStackInSlot(i);
            if (!content.isEmpty() && (matched == null && config.matchItems(itemStack, content)) || (matched != null && config.matchExactly(matched, content))){
                if (content.getCount() + total > itemStack.getMaxStackSize()){
                    total -= content.getMaxStackSize() - content.getCount();
                    if (!simulated) {
                        ItemStack newStack = new ItemStack(itemStack.getItem(), itemStack.getMaxStackSize());
                        if (itemStack.hasTag()){newStack.setTag(itemStack.getTag());}
                        handler.insertItem(i, newStack, false);
                    }
                    matched = content;
                } else {
                    ItemStack newStack = new ItemStack(itemStack.getItem(), itemStack.getCount());
                    if (itemStack.hasTag()){newStack.setTag(itemStack.getTag());}
                    if (!simulated) handler.insertItem(i, newStack, false);
                    return itemStack.getCount();
                }
            } else if (content.isEmpty()){
                ItemStack newStack = new ItemStack(itemStack.getItem(), itemStack.getCount());
                if (itemStack.hasTag()){newStack.setTag(itemStack.getTag());}
                if (!simulated) handler.insertItem(i, newStack, false);
                return itemStack.getCount();
            }
        }
        return itemStack.getCount() - total;
    }

    public Pair<ItemStack, MaidenInteractionConfig> getNextDelivery(Level level){

        for (int i = 0; i < insertBlocks.size(); i++){

            int configPos = getNextConfig();
            MaidenInteractionConfig config = insertBlocks.get(configPos);
            ItemStack nextStack = getExtractable(level, config);

            if (!nextStack.isEmpty()){

                if (level.isLoaded(config.getPos())) return null;
                BlockEntity entity = level.getBlockEntity(extractBlock.getPos());
                if (entity == null) return null;
                LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, extractBlock.getSide());


                capability.ifPresent(itemHandler -> {
                    int total = 0;
                    for (int ii = 0; ii < itemHandler.getSlots(); ii++){
                        if (MaidenInteractionConfig.matchExactly(itemHandler.getStackInSlot(ii), nextStack)){
                            ItemStack stack = itemHandler.getStackInSlot(ii);
                            if (total + stack.getCount() < nextStack.getCount()){
                                total += stack.getCount();
                                itemHandler.extractItem(ii, stack.getCount(), false);
                            } else {
                                itemHandler.extractItem(ii, nextStack.getCount() - total, false);
                                break;
                            }
                        }
                    }
                });

                return Pair.of(nextStack, config);
            }
        }

        return null;
    }

    public int getNextConfig(){

        if (extractBlock.getSelectionRegime() == MaidenSelectionRegime.ROUND_ROBIN){
            if (roundRobinSelector >= insertBlocks.size()){
                roundRobinSelector = 0;
            }
            return roundRobinSelector++;
        }

        return 0;
    }

    public CompoundTag getCompound() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("maiden_task.id", this.id);
        tag.put("maiden_task.terminal_loc", NbtUtils.writeBlockPos(this.terminalLoc));
        tag.put("maiden_task.extract_config", this.extractBlock.getCompound());
        tag.putInt("maiden_task.insert_config_count", this.insertBlocks.size());
        for (int i = 0; i < this.insertBlocks.size(); i++){
            tag.put("maiden_task.insert_config_" + i, this.insertBlocks.get(i).getCompound());
        }
        tag.putInt("maiden_task.round_robin", this.roundRobinSelector);

        return tag;
    }

    public static MaidenTask parseCompound(CompoundTag tag) {

        int id = tag.getInt("maiden_task.id");
        BlockPos terminalLoc = NbtUtils.readBlockPos(tag.getCompound("maiden_task.terminal_loc"));
        MaidenInteractionConfig extractBlock = MaidenInteractionConfig.parseCompound(tag.getCompound("maiden_task.extract_config"));
        List<MaidenInteractionConfig> insertBlocks = new ArrayList<>();
        int configCount = tag.getInt("maiden_task.insert_config_count");
        for (int i = 0; i < configCount; i++){
            insertBlocks.add(MaidenInteractionConfig.parseCompound(tag.getCompound("maiden_task.insert_config_" + i)));
        }
        int roundRobinSelector = tag.getInt("maiden_task.round_robin");

        return new MaidenTask(id, terminalLoc, extractBlock, insertBlocks, roundRobinSelector);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MaidenTask){
            return this.id == ((MaidenTask) obj).id;
        }
        return super.equals(obj);
    }

    public BlockPos getExtractPos() {
        return extractBlock.getPos();
    }
}
