package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.entity.clockwork_maiden.MaidenInteractionConfig;
import net.sophiebun.buntsy.entity.clockwork_maiden.MaidenTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClockworkMaidenTerminalEntity extends ClockworkBlockEntity {

    protected final ContainerData data;

    private final Map<BlockPos, CMTParticipantData> registeredConfigs = new HashMap<>();
    private final List<MaidenTask> maidenTasks = new ArrayList<>();

    private int tasksRoundRobin = 0;
    private int maidenEntityId = -1;

    public ClockworkMaidenTerminalEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {return 0;}

            @Override
            public void set(int i, int i1) {}

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {

        pTag.putInt("clockwork_maiden_terminal.block_count", registeredBlocks.size());
        for (int i = 0; i < registeredBlocks.size(); i++){
            pTag.put("clockwork_maiden_terminal.block_" + i, NbtUtils.writeBlockPos(registeredBlocks.get(i)));
        }

        pTag.putInt("clockwork_maiden_terminal.task_count", maidenTasks.size());
        for (int i = 0; i < maidenTasks.size(); i++){
            pTag.put("clockwork_maiden_terminal.task_" + i, maidenTasks.get(i).getCompound());
        }

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        int regCount = pTag.getInt("clockwork_maiden_terminal.block_count");
        for (int i = 0; i < regCount; i++){
            registeredBlocks.add(NbtUtils.readBlockPos(pTag.getCompound("clockwork_maiden_terminal.block_" + i)));
        }

        int taskCount = pTag.getInt("clockwork_maiden_terminal.task_count");
        for (int i = 0; i < taskCount; i++){
            maidenTasks.add(MaidenTask.parseCompound(pTag.getCompound("clockwork_maiden_terminal.task_" + i)));
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    public void addMaiden(int maidenEntityId){
        this.maidenEntityId = maidenEntityId;
    }

    public void clearMaiden(){
        this.maidenEntityId = -1;
    }

    public void addMaidenTask(MaidenTask task){
        this.maidenTasks.add(task);
    }

    public void removeMaidenTask(MaidenTask task){
        this.maidenTasks.remove(task);
    }

    public void addNewBlock(BlockEntity block){
        this.registeredConfigs.put(block.getBlockPos(), new CMTParticipantData());
    }

    public void removeBlock(BlockEntity block){
        this.registeredConfigs.remove(block.getBlockPos());
    }

    public boolean hasTask(){
        return !maidenTasks.isEmpty();
    }

    public MaidenTask getTask(){
        if (tasksRoundRobin >= maidenTasks.size()){
            tasksRoundRobin = 0;
        }

        return this.maidenTasks.get(tasksRoundRobin++);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

    }

    public CMTParticipantData getData(BlockPos target) {
        return this.registeredConfigs.get(target);
    }


    public void updateData(CMTParticipantData data, BlockPos target) {

        boolean recompileTasks = false;
        for (Integer channel : data.getChanged().keySet()){
            boolean[] changes = data.getChanged().get(channel);

            if (changes[0]){
                this.registeredConfigs.get(target).setEnabled(true, channel, data.isEnabled(true, channel));
                this.registeredConfigs.get(target).setConfig(true, channel, data.getConfig(true, channel));
                recompileTasks = true;
            }

            if (changes[1]){
                this.registeredConfigs.get(target).setEnabled(false, channel, data.isEnabled(false, channel));
                this.registeredConfigs.get(target).setConfig(false, channel, data.getConfig(false, channel));
                recompileTasks = true;
            }
        }

        if (recompileTasks) this.recompileTasks();
    }
}
