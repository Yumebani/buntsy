package net.sophiebun.buntsy.server;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.*;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.entity.clockwork_maiden.ClockworkMaiden;
import net.sophiebun.buntsy.screen.CMTParticipantMenu;
import net.sophiebun.buntsy.screen.CMTParticipantScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClockworkCardPuncherPacket {

    private final int maidenId;
    private final BlockPos terminalBlock;
    private final BlockPos block;
    private final ConfigureStaffOperationType operationType;

    public ClockworkCardPuncherPacket(int maidenId, BlockPos block, ConfigureStaffOperationType operationType) {
        this.maidenId = maidenId;
        this.terminalBlock = null;
        this.block = block;
        this.operationType = operationType;
    }

    public ClockworkCardPuncherPacket(BlockPos terminalBlock, BlockPos block, ConfigureStaffOperationType operationType) {
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
        ConfigureStaffOperationType operationType = ConfigureStaffOperationType.values()[buf.readInt()];

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

        if (operationType == ConfigureStaffOperationType.CLEAR_DATA){
            maidenTerminal.clearData(level);
            finishSuccess(player, "Cleared maiden terminal data");
        }
        else if (operationType == ConfigureStaffOperationType.SET_BLOCK){

            BlockEntity blockEntity = level.getBlockEntity(this.block);
            if (blockEntity != null) {
                handleBindingBlockTerminal(level, player, maidenTerminal, blockEntity);
            }
        }
        else if (operationType == ConfigureStaffOperationType.EDIT_DATA){
            BlockEntity blockEntity = level.getBlockEntity(this.block);
            if (blockEntity != null) {
                handleEditBlockTerminal(level, player, maidenTerminal, blockEntity);
            }
        }
    }

    private void handleMaidenOperations(NetworkEvent.Context ctx){
        ServerPlayer player = ctx.getSender();
        ServerLevel level = ctx.getSender().serverLevel();

        ClockworkMaiden maiden = ((ClockworkMaiden) level.getEntity(this.maidenId));

        if (maiden == null || !maiden.isAlive()) return;

        if (operationType == ConfigureStaffOperationType.CLEAR_DATA){
            maiden.clearBlockEntityData(level);
            finishSuccess(player, "Cleared maiden data");
        }
        else if (operationType == ConfigureStaffOperationType.SET_BLOCK){

            BlockEntity blockEntity = level.getBlockEntity(this.block);
            if (blockEntity instanceof ClockworkMaidenTerminalEntity) {
                
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

    private  void handleEditBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkMaidenTerminalEntity maidenTerminal, BlockEntity blockEntity){

        if (maidenTerminal.hasBlock(blockEntity.getBlockPos())){
            List<Direction> validSides = new ArrayList<>();

            for (Direction dir : Direction.values()){
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, dir).ifPresent(handler -> {
                    validSides.add(dir);
                });
            }

            MenuProvider containerProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("screen.cmt_participant");
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    return new CMTParticipantMenu(containerId, playerInventory, block, terminalBlock, maidenTerminal.getData(block), validSides);
                }
            };

            NetworkHooks.openScreen(player, containerProvider, buf -> {
                buf.writeBlockPos(block);
                buf.writeBlockPos(terminalBlock);
                buf.writeNbt(maidenTerminal.getData(block).getCompound());

                buf.writeInt(validSides.size());
                for (int i = 0; i < validSides.size(); i++){
                    buf.writeInt(validSides.get(i).ordinal());
                }
            });

        }
        else {
            finishFail(player, "Block not registered to selected terminal");
        }
    }

    private  void handleBindingBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkMaidenTerminalEntity maidenTerminal, BlockEntity blockEntity){

        if (maidenTerminal.hasBlock(blockEntity.getBlockPos())){
            maidenTerminal.removeBlock(blockEntity);
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
