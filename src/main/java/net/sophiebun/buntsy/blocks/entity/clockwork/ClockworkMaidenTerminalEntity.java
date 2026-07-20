package net.sophiebun.buntsy.blocks.entity.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sophiebun.buntsy.blocks.custom.entityblocks.WindupClockworkBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.entity.clockwork_maiden.MaidenInteractionConfig;
import net.sophiebun.buntsy.entity.clockwork_maiden.MaidenTask;
import net.sophiebun.buntsy.screen.clockwork.CMTParticipantScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockworkMaidenTerminalEntity extends WindupClockworkEntity implements GeoBlockEntity {

    private final Map<BlockPos, CMTParticipantData> registeredConfigs = new HashMap<>();
    private final List<MaidenTask> maidenTasks = new ArrayList<>();

    private int tasksRoundRobin = 0;

    private final int BASE_BLOCK_COUNT = 12;
    private final int BASE_RANGE_COUNT = 8;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<ClockworkMaidenTerminalEntity> controller;

    private PlayState predicate(AnimationState<ClockworkMaidenTerminalEntity> clockworkFairyTerminalEntityAnimationState) {
        clockworkFairyTerminalEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.clockwork_windup.running", Animation.LoopType.LOOP));
        return getBlockState().getValue(WindupClockworkBlock.RUNNING)  ? PlayState.CONTINUE : PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controller = new AnimationController<>(this, "controller", 2, this::predicate);
        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public ClockworkMaidenTerminalEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CLOCKWORK_MAIDEN_TERMINAL_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);

        pTag.putInt("clockwork_maiden_terminal.data_count", registeredConfigs.size());
        for (int i = 0; i < registeredConfigs.size(); i++){
            pTag.put("clockwork_maiden_terminal.data_pos_" + i, NbtUtils.writeBlockPos(((BlockPos) registeredConfigs.keySet().toArray()[i])));
            pTag.put("clockwork_maiden_terminal.data_" + i, registeredConfigs.get(registeredConfigs.keySet().toArray()[i]).getCompound(registries));
        }

        pTag.putInt("clockwork_maiden_terminal.task_count", maidenTasks.size());
        for (int i = 0; i < maidenTasks.size(); i++){
            pTag.put("clockwork_maiden_terminal.task_" + i, maidenTasks.get(i).getCompound(registries));
        }

        pTag.putInt("clockwork_maiden_terminal.task_round_robin", tasksRoundRobin);

    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);

        int regCount = pTag.getInt("clockwork_maiden_terminal.data_count");
        for (int i = 0; i < regCount; i++){
            this.registeredConfigs.put(
                    NbtUtils.readBlockPos(pTag, "clockwork_maiden_terminal.data_pos_" + i).get(),
                    CMTParticipantData.parseCompound(pTag.getCompound("clockwork_maiden_terminal.data_" + i), registries));
        }

        int taskCount = pTag.getInt("clockwork_maiden_terminal.task_count");
        for (int i = 0; i < taskCount; i++){
            maidenTasks.add(MaidenTask.parseCompound(pTag.getCompound("clockwork_maiden_terminal.task_" + i), registries));
        }

        this.tasksRoundRobin = pTag.getInt("clockwork_maiden_terminal.task_round_robin");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void addNewBlock(BlockEntity block){
        this.registeredConfigs.put(block.getBlockPos(), new CMTParticipantData());
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public MaidenTask getTask(){

        if (isWoundUp()){
            boolean recompile = false;
            for (BlockPos pos : registeredConfigs.keySet().stream().toList()){
                if (level.isLoaded(pos) && level.getBlockEntity(pos) == null){
                    registeredConfigs.remove(pos);
                    recompile = true;
                }
            }
            if (recompile) recompileTasks();

            if (tasksRoundRobin >= maidenTasks.size()){
                tasksRoundRobin = 0;
            }

            if (!this.maidenTasks.isEmpty()){
                return this.maidenTasks.get(tasksRoundRobin++);
            } else return null;
        }
        else return null;
    }

    public void recompileTasks(){
        maidenTasks.clear();
        this.tasksRoundRobin = 0;
        int i = 0;
        int x = 0;
        for (BlockPos pos : registeredConfigs.keySet()){
            for (int channel = 0; channel < CMTParticipantScreen.MAX_CHANNELS; channel++){
                if (registeredConfigs.get(pos).isEnabled(false, channel)){
                    MaidenInteractionConfig configInQuestion = registeredConfigs.get(pos).getConfig(false, channel);
                    List<MaidenInteractionConfig> targets = new ArrayList<>();
                    int y = 0;
                    for (BlockPos target : registeredConfigs.keySet()){
                        if (registeredConfigs.get(target).isEnabled(true, channel)){
                            targets.add(registeredConfigs.get(target).getConfig(true, channel));
                        }
                        y++;
                        if (y >= getClockworkBlockCount()) break;
                    }
                    if (!targets.isEmpty()){
                        this.maidenTasks.add(new MaidenTask(level.random.nextInt(), this.getBlockPos(), configInQuestion, targets));
                        i++;
                    }
                }
            }
            x++;
            if (x >= getClockworkBlockCount()) break;
        }
    }

    public CMTParticipantData getData(BlockPos target) {
        return this.registeredConfigs.get(target);
    }


    public void updateData(CMTParticipantData data, BlockPos target) {

        boolean recompileTasks = false;
        for (Integer channel : data.getChanged().keySet().stream().toList()){
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

        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public void clearData(ServerLevel level) {
        this.maidenTasks.clear();
        this.registeredConfigs.clear();
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public boolean hasBlock(BlockPos blockPos) {
        return this.registeredConfigs.containsKey(blockPos);
    }

    public boolean isBlockEntityInRange(BlockEntity blockEntity) {
        Vec3 distance = this.getBlockPos().getCenter().subtract(blockEntity.getBlockPos().getCenter());
        int maxDistance = getMaxDistance();
        return Math.abs(distance.x()) < maxDistance && Math.abs(distance.y()) < maxDistance && Math.abs(distance.z()) < maxDistance;
    }

    protected int getMaxDistance(){
        return BASE_RANGE_COUNT + getClockworkRangeAmount();
    }

    protected int getClockworkRangeAmount() {
        return switch (clockworkTier){
            case NONE -> 0;
            case SIMPLE -> 2;
            case INTRICATE -> 4;
            case COMPLEX -> 8;
        };
    }

    public boolean canRegisterNewBlock(BlockEntity blockEntity) {
        return this.registeredConfigs.size() < (getClockworkBlockCount());
    }

    protected int getClockworkBlockCount() {
        return switch (clockworkTier){
            case NONE -> 12;
            case SIMPLE -> 16;
            case INTRICATE -> 22;
            case COMPLEX -> 28;
        };
    }

    public int getTotalTasks() {
        return maidenTasks.size();
    }

    public void removeBlock(BlockEntity blockEntity) {
        this.registeredConfigs.remove(blockEntity.getBlockPos());
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState){

        if (isWoundUp()){
            tickWindup();
        }
    }

    @Override
    public int getWindupWeight() {
        return 4;
    }
}
