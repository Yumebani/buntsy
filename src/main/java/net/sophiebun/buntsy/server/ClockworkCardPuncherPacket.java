package net.sophiebun.buntsy.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ClockworkMaidenTerminalBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkFairyTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.clockwork_maiden.ClockworkMaiden;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClockworkCardPuncherPacket {

    private final int maidenId;
    private final BlockPos terminalBlock;
    private final BlockPos block;
    private final FairyStaffOperationType operationType;

    public ClockworkCardPuncherPacket(int maidenId, BlockPos block, FairyStaffOperationType operationType) {
        this.maidenId = maidenId;
        this.terminalBlock = null;
        this.block = block;
        this.operationType = operationType;
    }

    public ClockworkCardPuncherPacket(BlockPos terminalBlock, BlockPos block, FairyStaffOperationType operationType) {
        this.terminalBlock = terminalBlock;
        this.maidenId = -1;
        this.block = block;
        this.operationType = operationType;
    }

    public static ClockworkCardPuncherPacket read(FriendlyByteBuf buf) {

        int maidenId;
        BlockPos terminalBlock;
        if (buf.readBoolean()){
            maidenId = buf.readInt();
            terminalBlock = null;
        } else {
            maidenId = -1;
            terminalBlock = buf.readBlockPos();
        }

        BlockPos block = null;
        if (buf.readBoolean()){
            block = buf.readBlockPos();
        }
        FairyStaffOperationType operationType = FairyStaffOperationType.values()[buf.readInt()];

        return terminalBlock == null ? new ClockworkCardPuncherPacket(maidenId, block, operationType) : new ClockworkCardPuncherPacket(terminalBlock, block, operationType);
    }

    public void write(FriendlyByteBuf buf){

        if (terminalBlock != null){
            buf.writeBoolean(false);
            buf.writeBlockPos(terminalBlock);
        } else {
            buf.writeBoolean(true);
            buf.writeInt(maidenId);
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
                handleMaidenOperations(ctx);
            } else {
                handleMaidenTerminalOperations(ctx);
            }

        });
            
        ctx.setPacketHandled(true);
    }

    private void handleMaidenTerminalOperations(NetworkEvent.Context ctx){
        ServerPlayer player = ctx.getSender();
        ServerLevel level = ctx.getSender().serverLevel();

        BlockEntity entity = level.getBlockEntity(this.terminalBlock);

        if (entity == null || !(entity instanceof ClockworkMaidenTerminalEntity)) return;

        ClockworkMaidenTerminalEntity maidenTerminal = ((ClockworkMaidenTerminalEntity) entity);

        if (operationType == FairyStaffOperationType.CLEAR_DATA){
            maidenTerminal.clearData(level);
            finishSuccess(player, "Cleared maiden terminal data");
        }
        else if (operationType == FairyStaffOperationType.SET_BLOCK){

            BlockEntity blockEntity = level.getBlockEntity(this.block);
            if (blockEntity != null) {
                handleInteractableBlockTerminal(level, player, maidenTerminal, blockEntity);
            } else {
                finishSuccess(player, "Cleared selection");
            }
        }
    }

    private void handleMaidenOperations(NetworkEvent.Context ctx){
        ServerPlayer player = ctx.getSender();
        ServerLevel level = ctx.getSender().serverLevel();

        ClockworkMaiden maiden = ((ClockworkMaiden) level.getEntity(this.maidenId));

        if (maiden == null || !maiden.isAlive()) return;

        if (operationType == FairyStaffOperationType.CLEAR_DATA){
            maiden.clearBlockEntityData(level);
            finishSuccess(player, "Cleared maiden data");
        }
        else if (operationType == FairyStaffOperationType.SET_BLOCK){

            BlockEntity blockEntity = level.getBlockEntity(this.block);
            if (blockEntity != null && (blockEntity instanceof  ClockworkMaidenTerminalEntity)) {
                
                if (maiden.containsTerminal((ClockworkMaidenTerminalEntity) blockEntity)){
                    maiden.clearBlockEntityData(level);
                    finishSuccess(player, "Removed terminal");
                }
                else{
                    maiden.registerTerminal((ClockworkMaidenTerminalEntity) blockEntity, level);
                    finishSuccess(player, "New terminal registered");
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

    private  void handleInteractableBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkMaidenTerminalEntity maidenTerminal, BlockEntity blockEntity){

        if (maidenTerminal.hasBlock(blockEntity.getBlockPos())){
            List<Direction> validSides = new ArrayList<>();

            for (Direction dir : Direction.values()){
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, dir).ifPresent(handler -> {
                    validSides.add(dir);
                });
            }

            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    new CMTParticipantPacket(maidenTerminal.getData(block),
                            terminalBlock, block, validSides));
        }
        else {
            if (!maidenTerminal.isBlockEntityInRange(blockEntity)){
                finishFail(player, "Block too far away from terminal");
            }
            else {
                if (maidenTerminal.canRegisterNewBlock(blockEntity)){
                    maidenTerminal.addNewBlock(blockEntity);
                    finishSuccess(player, "New block registered");
                } else {
                    finishFail(player, "Terminal at limit");
                }
            }
        }
    }

    private void finishSuccess(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§a" + text), true);
    }
    private void finishFail(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§c" + text), true);
    }
}
