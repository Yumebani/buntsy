package net.sophiebun.buntsy.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkFairyTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.function.Supplier;

public class ModFairyStaffPacket {

    private final int fairyId;
    private final BlockPos terminalBlock;
    private final BlockPos block;
    private final ConfigureStaffOperationType operationType;

    public ModFairyStaffPacket(int fairyId, BlockPos block, ConfigureStaffOperationType operationType) {
        this.fairyId = fairyId;
        this.terminalBlock = null;
        this.block = block;
        this.operationType = operationType;
    }

    public ModFairyStaffPacket(BlockPos terminalBlock, BlockPos block, ConfigureStaffOperationType operationType) {
        this.terminalBlock = terminalBlock;
        this.fairyId = -1;
        this.block = block;
        this.operationType = operationType;
    }

    public static ModFairyStaffPacket read(FriendlyByteBuf buf) {

        int fairyId;
        BlockPos terminalBlock;
        if (buf.readBoolean()){
            fairyId = buf.readInt();
            terminalBlock = null;
        } else {
            fairyId = -1;
            terminalBlock = buf.readBlockPos();
        }

        BlockPos block = null;
        if (buf.readBoolean()){
            block = buf.readBlockPos();
        }
        ConfigureStaffOperationType operationType = ConfigureStaffOperationType.values()[buf.readInt()];

        return terminalBlock == null ? new ModFairyStaffPacket(fairyId, block, operationType) : new ModFairyStaffPacket(terminalBlock, block, operationType);
    }

    public void write(FriendlyByteBuf buf){

        if (terminalBlock != null){
            buf.writeBoolean(false);
            buf.writeBlockPos(terminalBlock);
        } else {
            buf.writeBoolean(true);
            buf.writeInt(fairyId);
        }

        if (block != null){
            buf.writeBoolean(true);
            buf.writeBlockPos(block);
        }
        else {
            buf.writeBoolean(false);
        }
        buf.writeInt(operationType.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {

            ServerLevel level = ctx.getSender().serverLevel();

            if (level == null || (block != null && !level.hasChunkAt(block))) return;

            if (this.terminalBlock == null){
                handleFairyOperations(ctx);
            } else {
                handleFairyTerminalOperations(ctx);
            }

        });
            
        ctx.setPacketHandled(true);
    }

    private void handleFairyTerminalOperations(NetworkEvent.Context ctx){
        ServerPlayer player = ctx.getSender();
        ServerLevel level = ctx.getSender().serverLevel();

        BlockEntity entity = level.getBlockEntity(this.terminalBlock);

        if (entity == null || !(entity instanceof ClockworkFairyTerminalEntity)) return;

        ClockworkFairyTerminalEntity fairyTerminal = ((ClockworkFairyTerminalEntity) entity);

        if (operationType == ConfigureStaffOperationType.CLEAR_DATA){
            fairyTerminal.clearBlockEntityData(level);
            finishSuccess(player, "Cleared fairy terminal data");
        }
        else if (operationType == ConfigureStaffOperationType.SET_BLOCK){

            BlockState blockState = level.getBlockState(this.block);
            if (blockState.is(ModTags.Blocks.FAIRY_INTERACTABLE_BLOCK_ENTITY)) {

                BlockEntity blockEntity = level.getBlockEntity(this.block);

                handleInteractableBlockTerminal(level, player, fairyTerminal, blockEntity);
            } else {
                finishSuccess(player, "Cleared selection");
            }
        }
    }

    private void handleFairyOperations(NetworkEvent.Context ctx){
        ServerPlayer player = ctx.getSender();
        ServerLevel level = ctx.getSender().serverLevel();

        Fairy fairy = ((Fairy) level.getEntity(this.fairyId));

        if (fairy == null || !fairy.isAlive()) return;

        if (operationType == ConfigureStaffOperationType.CLEAR_DATA){
            fairy.clearBlockEntityData();
            finishSuccess(player, "Cleared fairy data");
        }
        else if (operationType == ConfigureStaffOperationType.SET_BLOCK){

            BlockState blockState = level.getBlockState(this.block);
            if (blockState.is(ModTags.Blocks.FAIRY_INTERACTABLE_BLOCK_ENTITY)) {

                BlockEntity blockEntity = level.getBlockEntity(this.block);

                if (blockEntity.getType() == ModBlockEntities.OFFERING_BENCH_BLOCK_ENTITY.get()) {
                    handleOfferingBench(player, fairy, (FairyOfferingBenchBlockEntity) blockEntity);
                } else {
                    handleInteractableBlock(player, fairy, blockEntity);
                }
            } else {
                finishSuccess(player, "Cleared selection");
            }
        }
    }

    private  void handleInteractableBlock(ServerPlayer player, Fairy fairy, BlockEntity blockEntity){

        if (((FairyInteractBlockEntity) blockEntity).isWatched()){
            if (fairy.isBlockRegistered(blockEntity)){
                fairy.unregisterBlock(blockEntity);
                finishSuccess(player, "Removed station");
            }
            else {
                finishFail(player, "Block is watched by another fairy");
            }
        }
        else if (fairy.hasofferingBench()){
            if (!fairy.isBlockEntityInRange(blockEntity)){
                finishFail(player, "Station too far away from offering bench");
            }
            else {
                int result = fairy.canRegisterNewBlock(blockEntity);
                if (result == 0){
                    fairy.registerNewBlock(blockEntity);
                    finishSuccess(player, "New station registered");
                } else if (result == -2){
                    finishFail(player, "Fairy already has a titular station");
                }
                else{
                    finishFail(player, "Fairy at limit");
                }
            }
        }
        else{
            finishFail(player, "No offering bench registered");
        }
    }

    private  void handleInteractableBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkFairyTerminalEntity fairyTerminal, BlockEntity blockEntity){

        if (((FairyInteractBlockEntity) blockEntity).isWatched()){
            if (fairyTerminal.isBlockRegistered(blockEntity)){
                fairyTerminal.unregisterBlock(blockEntity, level);
                finishSuccess(player, "Removed station");
            }
            else {
                finishFail(player, "Block is watched by another fairy");
            }
        }
        else {
            if (!fairyTerminal.isBlockEntityInRange(blockEntity)){
                finishFail(player, "Station too far away from offering bench");
            }
            else {
                int result = fairyTerminal.canRegisterNewBlock(blockEntity);
                if (result == 0){
                    fairyTerminal.registerNewBlock(blockEntity);
                    finishSuccess(player, "New station registered");
                } else if (result == -2){
                    finishFail(player, "Fairy already has a titular station");
                }
                else{
                    finishFail(player, "Fairy at limit");
                }
            }
        }
    }

    private void handleOfferingBench(ServerPlayer player, Fairy fairy, FairyOfferingBenchBlockEntity offeringBench){

        if (offeringBench.isWatched()){
            if (fairy.hasofferingBench() && fairy.isBenchRegistered(offeringBench)){
                fairy.clearBlockEntityData();
                finishSuccess(player, "Removed offering bench");
            }
            else {
                finishFail(player, "Offering bench is watched by another fairy");
            }
        }
        else{
            fairy.registerNewOfferingBench(offeringBench);
            finishSuccess(player, "New offering bench registered");
        }
    }

    private void finishSuccess(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§a" + text), true);
    }
    private void finishFail(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§c" + text), true);
    }
}
