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
            BlockEntity entity = level.getBlockEntity(extractBlock.getPos());
            LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, extractBlock.getSide());

            List<ItemStack> extractableSpace = new ArrayList<>();

            capability.ifPresent(itemHandler -> {

                ItemStack target = null;
                int total = 0;

                for (int i = 0; i < itemHandler.getSlots(); i++){
                    ItemStack content = itemHandler.getStackInSlot(i);
                    if (extractBlock.matchesCombinedFilter(nextConfig, content)){
                        if (target == null || extractBlock.matchItems(content, target)){

                            int possibleTotal = content.getCount() + total;

                            ItemStack test = ItemStack.EMPTY;
                            test.deserializeNBT(content.serializeNBT());
                            test.setCount(total);
                            int availableSpace = tryPlace(level, nextConfig, test);

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
                    ItemStack returnStack = ItemStack.EMPTY;
                    returnStack.deserializeNBT(target.serializeNBT());
                    returnStack.setCount(total);
                    extractableSpace.add(returnStack);
                }

            });

            if (extractableSpace.isEmpty()){
                return ItemStack.EMPTY;
            } else return extractableSpace.get(0);
        }
        return ItemStack.EMPTY;
    }

    private Integer tryPlace(Level level, MaidenInteractionConfig config, ItemStack itemStack){
        BlockEntity entity = level.getBlockEntity(config.getPos());
        LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, config.getSide());

        List<Integer> availableSpace = new ArrayList<>();
        capability.ifPresent(itemHandler -> {
            availableSpace.add(getAvailableSpace(itemHandler, config, itemStack));
        });

        if (availableSpace.isEmpty()){
            return 0;
        } else return availableSpace.get(0);
    }

    private Integer getAvailableSpace(IItemHandler handler, MaidenInteractionConfig config, ItemStack itemStack){
        int total = itemStack.getCount();
        ItemStack matched = null;
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack content = handler.getStackInSlot(i);
            if ((matched == null && config.matchItems(itemStack, content)) || (matched != null && config.matchExactly(matched, content))){
                if (content.getCount() + total > content.getMaxStackSize()){
                    total -= content.getMaxStackSize() - content.getCount();
                    matched = content;
                } else {
                    return itemStack.getCount();
                }
            } else if (content.isEmpty()){
                return itemStack.getCount();
            }
        }
        return itemStack.getCount() - total;
    }

    public Pair<ItemStack, BlockPos> getNextDelivery(Level level){

        for (int i = 0; i < insertBlocks.size(); i++){

            int configPos = getNextConfig();
            MaidenInteractionConfig config = insertBlocks.get(configPos);
            ItemStack nextStack = getExtractable(level, config);

            if (!nextStack.isEmpty()){
                return Pair.of(nextStack, config.getPos());
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
