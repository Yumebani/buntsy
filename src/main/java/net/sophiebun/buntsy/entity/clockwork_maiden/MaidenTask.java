package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MaidenTask {

    private final BlockPos terminalLoc;

    private final MaidenInteractionConfig extractBlock;
    private final List<MaidenInteractionConfig> insertBlocks;
    private final MaidenSelectionRegime selectionRegime;

    private int roundRobinSelector = 0;

    MaidenTask(BlockPos terminalLoc, MaidenInteractionConfig extractBlock, List<MaidenInteractionConfig> insertBlocks, MaidenSelectionRegime selectionRegime){
        this.terminalLoc = terminalLoc;
        this.extractBlock = extractBlock;
        this.insertBlocks = insertBlocks.stream().sorted(selectionRegime == MaidenSelectionRegime.PRIORITY ?
                Comparator.comparingInt(MaidenInteractionConfig::priority) :
                Comparator.comparingInt(config -> Math.toIntExact(Math.round(config.pos().getCenter().distanceTo(terminalLoc.getCenter()))))).toList();
        this.selectionRegime = selectionRegime;
    }

    public Pair<BlockPos, Integer> getNextBlock(Level level, ItemStack stackToMove){

        return switch (selectionRegime){
            case NEAREST -> getNextBlockNearest(level, stackToMove);
            case ROUND_ROBIN -> getNextBlockRoundRobin(level, stackToMove);
            case PRIORITY -> getNextBlockPriority(level, stackToMove);
        };
    }

    private Pair<BlockPos, Integer> getNextBlockNearest(Level level, ItemStack stackToMove){
        for (MaidenInteractionConfig config : insertBlocks){
            if (level.isLoaded(config.pos()) && config.matchesFilter(stackToMove)){
                int availableSpace = tryPlace(level, config, stackToMove);
                if (availableSpace > 0){
                    return Pair.of(config.pos(), availableSpace);
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
            if (level.isLoaded(config.pos()) && config.matchesFilter(stackToMove)){
                int availableSpace = tryPlace(level, config, stackToMove);
                if (availableSpace > 0){
                    returnValue = Pair.of(config.pos(), availableSpace);
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
            if (level.isLoaded(config.pos()) && config.matchesFilter(stackToMove)){
                int availableSpace = tryPlace(level, config, stackToMove);
                if (availableSpace > 0){
                    return Pair.of(config.pos(), availableSpace);
                }
            }
        }
        return null;
    }

    private Integer tryPlace(Level level, MaidenInteractionConfig config, ItemStack itemStack){
        BlockEntity entity = level.getBlockEntity(config.pos());
        LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, config.side());

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
}
