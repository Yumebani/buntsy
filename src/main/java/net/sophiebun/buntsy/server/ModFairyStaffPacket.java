package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.function.Supplier;

public class ModFairyStaffPacket {

    private final int fairyId;
    private final BlockPos block;
    private final FairyStaffOperationType operationType;

    public ModFairyStaffPacket(int fairyId, BlockPos block, FairyStaffOperationType operationType) {
        this.fairyId = fairyId;
        this.block = block;
        this.operationType = operationType;
    }

    public static ModFairyStaffPacket read(FriendlyByteBuf buf) {
        int fairyId = buf.readInt();
        BlockPos block = null;
        if (buf.readBoolean()){
            block = buf.readBlockPos();
        }
        FairyStaffOperationType operationType = FairyStaffOperationType.values()[buf.readInt()];

        return new ModFairyStaffPacket(fairyId, block, operationType);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeInt(fairyId);
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

            ServerPlayer player = ctx.getSender();
            ServerLevel level = ctx.getSender().serverLevel();

            if (level == null || (block != null && !level.hasChunkAt(block))) return;

            Fairy fairy = ((Fairy) level.getEntity(this.fairyId));

            if (fairy == null || !fairy.isAlive()) return;

            if (operationType == FairyStaffOperationType.CLEAR_DATA){
                fairy.clearBlockEntityData();
                finishSuccess(player, "Cleared fairy data");
            }
            else if (operationType == FairyStaffOperationType.SET_BLOCK){

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
        });
            
        ctx.setPacketHandled(true);
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
            else if (fairy.canRegisterNewBlock(blockEntity)){
                fairy.registerNewBlock(blockEntity);
                finishSuccess(player, "New station registered");
            }
            else{
                finishFail(player, "Fairy at limit");
            }
        }
        else{
            finishFail(player, "No offering bench registered");
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
