package net.sophiebun.buntsy.entity.clockwork_maiden;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

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
            IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, extractBlock.getPos(), extractBlock.getSide());
            
            List<ItemStack> extractableSpace = new ArrayList<>();

            if (itemHandler != null){
                ItemStack target = null;
                int total = 0;

                for (int i = 0; i < itemHandler.getSlots(); i++){
                    ItemStack content = itemHandler.extractItem(i, extractBlock.getExtractCount(), true);
                    if (!content.isEmpty() && extractBlock.matchesCombinedFilter(nextConfig, content)){
                        if (target == null || extractBlock.matchItems(content, target)){

                            int possibleTotal = content.getCount() + total;
                            int amountInContainer = getCountInStorage(level, nextConfig, content);
                            possibleTotal = calibrateToAmountSettings(nextConfig, possibleTotal, amountInContainer);

                            if (possibleTotal > 0){
                                ItemStack test = new ItemStack(content.getItem(), possibleTotal);
                                test.applyComponents(content.getComponents());
                                int remainder = tryPlace(level, nextConfig, test, true).getCount();

                                if (remainder == 0){
                                    if (possibleTotal < Math.min(extractBlock.getExtractCount(), content.getMaxStackSize())){
                                        total = possibleTotal;
                                        if (target == null){
                                            target = content;
                                        }
                                    } else {
                                        total = Math.min(extractBlock.getExtractCount(), content.getMaxStackSize());
                                        target = content;
                                        break;
                                    }
                                } else {
                                    total = possibleTotal - remainder;
                                    target = content;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (target != null){
                    ItemStack stack = target.copy();
                    stack.setCount(total);
                    extractableSpace.add(stack);
                }
            }

            if (extractableSpace.isEmpty()){
                return ItemStack.EMPTY;
            } else return extractableSpace.get(0);
        }
        return ItemStack.EMPTY;
    }

    private int calibrateToAmountSettings(MaidenInteractionConfig config, int possibleTotal, int amountInContainer) {

        return switch (config.getFillRegime()){
            case ONE -> Math.max(0, Math.min(1 - amountInContainer, possibleTotal));
            case STACK -> Math.max(0, Math.min(64 - amountInContainer, possibleTotal));
            case FILL -> possibleTotal;
        };
    }

    private int getCountInStorage(Level level, MaidenInteractionConfig config, ItemStack itemStack) {
        BlockEntity entity = level.getBlockEntity(config.getPos());
        IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, extractBlock.getPos(), extractBlock.getSide());

        List<Integer> count = new ArrayList<>();
        count.add(0);

        if (itemHandler != null){
            for (int i = 0; i < itemHandler.getSlots(); i++){
                ItemStack content = itemHandler.getStackInSlot(i);
                if (MaidenInteractionConfig.matchExactly(content, itemStack)){
                    count.set(0, content.getCount() + count.get(0));
                }
            }
        }

        return count.get(0);
    }

    public static ItemStack tryPlace(Level level, MaidenInteractionConfig config, ItemStack itemStack, boolean simulated){

        IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, config.getPos(), config.getSide());

        ItemStack localStack = itemStack.copy();
        List<ItemStack> stackHolder = new ArrayList<>();
        stackHolder.add(localStack);

        if (itemHandler != null){
            for (int i = 0; i < itemHandler.getSlots(); i++){
                stackHolder.set(0, itemHandler.insertItem(i, stackHolder.get(0), simulated));
            }
        }

        return stackHolder.get(0);
    }

    public Pair<ItemStack, MaidenInteractionConfig> getNextDelivery(Level level){

        ItemStack nextStack = ItemStack.EMPTY;
        MaidenInteractionConfig config = null;

        int startConfig = getNextConfig(false);
        for (int i = startConfig; i < (extractBlock.getSelectionRegime() != MaidenSelectionRegime.ROUND_ROBIN ? insertBlocks.size() : startConfig + 1) ; i++){
            config = insertBlocks.get(i);

            if (!level.isLoaded(this.extractBlock.getPos()) || level.getBlockEntity(extractBlock.getPos()) == null ||
                    !level.isLoaded(config.getPos()) || level.getBlockEntity(config.getPos()) == null){
                return null;
            }

            nextStack = getExtractable(level, config);

            if (!nextStack.isEmpty()) break;
        }

        if (!nextStack.isEmpty()){

            IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, extractBlock.getPos(), extractBlock.getSide());
            ItemStack finalNextStack = nextStack;

            if (itemHandler != null){
                int total = 0;
                for (int ii = 0; ii < itemHandler.getSlots(); ii++){
                    if (MaidenInteractionConfig.matchExactly(itemHandler.getStackInSlot(ii), finalNextStack)){
                        ItemStack stack = itemHandler.getStackInSlot(ii);
                        if (total + stack.getCount() < finalNextStack.getCount()){
                            total += stack.getCount();
                            itemHandler.extractItem(ii, stack.getCount(), false);
                        } else {
                            itemHandler.extractItem(ii, finalNextStack.getCount() - total, false);
                            break;
                        }
                    }
                }
            }

            return Pair.of(nextStack, config);
        }

        return null;
    }

    public int getNextConfig(boolean simulate){

        if (extractBlock.getSelectionRegime() == MaidenSelectionRegime.ROUND_ROBIN){
            if (roundRobinSelector >= insertBlocks.size()){
                roundRobinSelector = 0;
            }
            return simulate ? roundRobinSelector : roundRobinSelector++;
        }

        return 0;
    }

    public CompoundTag getCompound(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("maiden_task.id", this.id);
        tag.put("maiden_task.terminal_loc", NbtUtils.writeBlockPos(this.terminalLoc));
        tag.put("maiden_task.extract_config", this.extractBlock.getCompound(registries));
        tag.putInt("maiden_task.insert_config_count", this.insertBlocks.size());
        for (int i = 0; i < this.insertBlocks.size(); i++){
            tag.put("maiden_task.insert_config_" + i, this.insertBlocks.get(i).getCompound(registries));
        }
        tag.putInt("maiden_task.round_robin", this.roundRobinSelector);

        return tag;
    }

    public static MaidenTask parseCompound(CompoundTag tag, HolderLookup.Provider registries) {

        int id = tag.getInt("maiden_task.id");
        BlockPos terminalLoc = NbtUtils.readBlockPos(tag, "maiden_task.terminal_loc").get();
        MaidenInteractionConfig extractBlock = MaidenInteractionConfig.parseCompound(tag.getCompound("maiden_task.extract_config"), registries);
        List<MaidenInteractionConfig> insertBlocks = new ArrayList<>();
        int configCount = tag.getInt("maiden_task.insert_config_count");
        for (int i = 0; i < configCount; i++){
            insertBlocks.add(MaidenInteractionConfig.parseCompound(tag.getCompound("maiden_task.insert_config_" + i), registries));
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

    public boolean achievable(Level level) {
        ItemStack nextStack = ItemStack.EMPTY;
        MaidenInteractionConfig config = null;

        int startConfig = getNextConfig(false);
        for (int i = startConfig; i < (extractBlock.getSelectionRegime() != MaidenSelectionRegime.ROUND_ROBIN ? insertBlocks.size() : startConfig + 1) ; i++){
            config = insertBlocks.get(i);

            if (!level.isLoaded(this.extractBlock.getPos()) || level.getBlockEntity(extractBlock.getPos()) == null ||
                    !level.isLoaded(config.getPos()) || level.getBlockEntity(config.getPos()) == null){
                return false;
            }

            nextStack = getExtractable(level, config);

            if (!nextStack.isEmpty()) break;
        }

        return !nextStack.isEmpty();
    }
}
