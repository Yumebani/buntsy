package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.*;

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


    public ItemStack tryExtract(Level level){

        if (level.isLoaded(extractBlock.getPos())){
            BlockEntity entity = level.getBlockEntity(extractBlock.getPos());
            LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, extractBlock.getSide());

            List<ItemStack> extractableSpace = new ArrayList<>();
            capability.ifPresent(itemHandler -> {
                extractableSpace.add(getExtractableCount(itemHandler));
            });

            if (extractableSpace.isEmpty()){
                return ItemStack.EMPTY;
            } else return extractableSpace.get(0);
        }
    }

    private ItemStack getExtractableCount(IItemHandler handler){
        ItemStack toFilter = null;
        int total = 0;
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack content = handler.getStackInSlot(i);
            if (extractBlock.matchesFilter(content)){

                if (toFilter == null){
                    toFilter = content;
                }

                if (toFilter.is(content.getItem())){
                    if (content.getCount() + total < extractBlock.getExtractCount()){
                        total += content.getCount();
                    } else {
                        return new ItemStack(content.getItem(), extractBlock.getExtractCount());
                    }
                }
            }
        }
    }

    public Pair<BlockPos, Integer> getNextBlock(Level level, ItemStack stackToMove){

        return switch (extractBlock.getSelectionRegime()){
            case NEAREST -> getNextBlockNearest(level, stackToMove);
            case ROUND_ROBIN -> getNextBlockRoundRobin(level, stackToMove);
            case PRIORITY -> getNextBlockPriority(level, stackToMove);
        };
    }

    private Pair<BlockPos, Integer> getNextBlockNearest(Level level, ItemStack stackToMove){
        for (MaidenInteractionConfig config : insertBlocks){
            if (level.isLoaded(config.getPos()) && config.matchesFilter(stackToMove)){
                int availableSpace = tryPlace(level, config, stackToMove);
                if (availableSpace > 0){
                    return Pair.of(config.getPos(), availableSpace);
                }
            }
        }
        return null;
    }

    private Pair<BlockPos, Integer> getNextBlockRoundRobin(Level level, ItemStack stackToMove){
        int roundRobinOrigin = roundRobinSelector;
        Pair<BlockPos, Integer> returnValue = null;
        while (roundRobinOrigin < roundRobinSelector + 1){
            MaidenInteractionConfig config = insertBlocks.get(roundRobinSelector);
            if (level.isLoaded(config.getPos()) && config.matchesFilter(stackToMove)){
                int availableSpace = tryPlace(level, config, stackToMove);
                if (availableSpace > 0){
                    returnValue = Pair.of(config.getPos(), availableSpace);
                }
            }
            roundRobinSelector++;
            if (roundRobinSelector >= insertBlocks.size()){
                roundRobinSelector = 0;
            }

            if (returnValue != null){
                return returnValue;
            }
        }
        return null;
    }

    private Pair<BlockPos, Integer> getNextBlockPriority(Level level, ItemStack stackToMove){
        for (MaidenInteractionConfig config : insertBlocks){
            if (level.isLoaded(config.getPos()) && config.matchesFilter(stackToMove)){
                int availableSpace = tryPlace(level, config, stackToMove);
                if (availableSpace > 0){
                    return Pair.of(config.getPos(), availableSpace);
                }
            }
        }
        return null;
    }

    private Integer tryPlace(Level level, MaidenInteractionConfig config, ItemStack itemStack){
        BlockEntity entity = level.getBlockEntity(config.getPos());
        LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, config.getSide());

        List<Integer> availableSpace = new ArrayList<>();
        capability.ifPresent(itemHandler -> {
            availableSpace.add(getAvailableSpace(itemHandler, itemStack));
        });

        if (availableSpace.isEmpty()){
            return 0;
        } else return availableSpace.get(0);
    }

    private Integer getAvailableSpace(IItemHandler handler, ItemStack itemStack){
        int total = itemStack.getCount();
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack content = handler.getStackInSlot(i);
            if (content.is(itemStack.getItem())){
                if (content.getCount() + total > content.getMaxStackSize()){
                    total -= content.getMaxStackSize() - content.getCount();
                } else {
                    return itemStack.getCount();
                }
            } else if (content.isEmpty()){
                return itemStack.getCount();
            }
        }
        return itemStack.getCount() - total;
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
        MaidenInteractionConfig extractBlock = MaidenInteractionConfig.parseCompound(tag.getCompound("maiden_task.terminal_loc"));
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
}
