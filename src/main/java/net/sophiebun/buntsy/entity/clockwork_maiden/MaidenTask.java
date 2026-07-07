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
                    ItemStack content = itemHandler.extractItem(i, extractBlock.getExtractCount(), true);
                    if (!content.isEmpty() && extractBlock.matchesCombinedFilter(nextConfig, content)){
                        if (target == null || extractBlock.matchItems(content, target)){

                            int possibleTotal = content.getCount() + total;

                            ItemStack test = ItemStack.EMPTY;
                            test.deserializeNBT(content.serializeNBT());
                            test.setCount(possibleTotal);
                            int remainder = tryPlace(level, nextConfig, test, true).getCount();
                            System.out.println("remainder " + remainder);

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

    public static ItemStack tryPlace(Level level, MaidenInteractionConfig config, ItemStack itemStack, boolean simulated){

        BlockEntity entity = level.getBlockEntity(config.getPos());
        LazyOptional<IItemHandler> capability = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, config.getSide());

        ItemStack localStack = new ItemStack(itemStack.getItem(), itemStack.getCount());
        if (itemStack.hasTag()){localStack.setTag(itemStack.getTag());}
        List<ItemStack> stackHolder = new ArrayList<>();
        stackHolder.add(localStack);

        capability.ifPresent(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++){
                stackHolder.set(0, itemHandler.insertItem(i, stackHolder.get(0), simulated));
            }
        });

        return stackHolder.get(0);
    }

    public Pair<ItemStack, MaidenInteractionConfig> getNextDelivery(Level level){

        for (int i = 0; i < insertBlocks.size(); i++){

            int configPos = getNextConfig();
            MaidenInteractionConfig config = insertBlocks.get(configPos);

            if (!level.isLoaded(this.extractBlock.getPos()) || level.getBlockEntity(extractBlock.getPos()) == null ||
                !level.isLoaded(config.getPos()) || level.getBlockEntity(config.getPos()) == null){
                return null;
            }

            ItemStack nextStack = getExtractable(level, config);
            System.out.println("should extract " + nextStack);

            if (!nextStack.isEmpty()){

                BlockEntity entity = level.getBlockEntity(extractBlock.getPos());
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
