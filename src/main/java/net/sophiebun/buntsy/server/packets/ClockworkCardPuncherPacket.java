package net.sophiebun.buntsy.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.ClockworkMaiden;
import net.sophiebun.buntsy.screen.clockwork.CMTParticipantMenu;
import net.sophiebun.buntsy.server.ConfigureStaffOperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record ClockworkCardPuncherPacket(
        int maidenId,
        Optional<BlockPos> terminalBlock,
        Optional<BlockPos> block,
        ConfigureStaffOperationType operationType
) implements CustomPacketPayload {

    public ClockworkCardPuncherPacket(int maidenId, Optional<BlockPos> terminalBlock, Optional<BlockPos> block, int operationType){
        this(maidenId, terminalBlock, block, ConfigureStaffOperationType.values()[operationType]);
    }

    public int operationTypeOrdinal(){
        return operationType.ordinal();
    }

    public static final Type<ClockworkCardPuncherPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "clockwork_card_puncher_packet"));

    public static final StreamCodec<FriendlyByteBuf, ClockworkCardPuncherPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClockworkCardPuncherPacket::maidenId,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), ClockworkCardPuncherPacket::terminalBlock,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), ClockworkCardPuncherPacket::block,
            ByteBufCodecs.VAR_INT, ClockworkCardPuncherPacket::operationTypeOrdinal,
            ClockworkCardPuncherPacket::new
    );

    @Override
    public Type<ClockworkCardPuncherPacket> type() {
        return TYPE;
    }

    public static void handle(ClockworkCardPuncherPacket packet, IPayloadContext context){

        context.enqueueWork(() -> {

            ServerLevel level = ((ServerLevel) context.player().level());

            if (level == null || (packet.block.isPresent() && !level.hasChunkAt(packet.block.get()))) return;

            if (!packet.terminalBlock.isPresent()){
                handleMaidenOperations(packet, context);
            } else {
                handleMaidenTerminalOperations(packet, context);
            }

        });
    }

    private static void handleMaidenTerminalOperations(ClockworkCardPuncherPacket packet, IPayloadContext context){
        ServerPlayer player = ((ServerPlayer) context.player());
        ServerLevel level = player.serverLevel();

        BlockEntity entity = level.getBlockEntity(packet.terminalBlock.get());

        if (entity == null || !(entity instanceof ClockworkMaidenTerminalEntity)) return;

        ClockworkMaidenTerminalEntity maidenTerminal = ((ClockworkMaidenTerminalEntity) entity);

        if (packet.operationType == ConfigureStaffOperationType.CLEAR_DATA){
            maidenTerminal.clearData(level);
            finishSuccess(player, "Cleared maiden terminal data");
        }
        else if (packet.operationType == ConfigureStaffOperationType.SET_BLOCK){

            BlockEntity blockEntity = level.getBlockEntity(packet.block.get());
            if (blockEntity != null) {
                handleBindingBlockTerminal(level, player, maidenTerminal, blockEntity);
            }
        }
        else if (packet.operationType == ConfigureStaffOperationType.EDIT_DATA){
            BlockEntity blockEntity = level.getBlockEntity(packet.block.get());
            if (blockEntity != null) {
                handleEditBlockTerminal(level, player, maidenTerminal, blockEntity);
            }
        }
    }

    private static void handleMaidenOperations(ClockworkCardPuncherPacket packet, IPayloadContext context){
        ServerPlayer player = ((ServerPlayer) context.player());
        ServerLevel level = player.serverLevel();

        ClockworkMaiden maiden = ((ClockworkMaiden) level.getEntity(packet.maidenId));

        if (maiden == null || !maiden.isAlive()) return;

        if (packet.operationType == ConfigureStaffOperationType.CLEAR_DATA){
            maiden.clearBlockEntityData(level);
            finishSuccess(player, "Cleared maiden data");
        }
        else if (packet.operationType == ConfigureStaffOperationType.SET_BLOCK){

            BlockEntity blockEntity = level.getBlockEntity(packet.block.get());
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

    private static void handleEditBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkMaidenTerminalEntity maidenTerminal, BlockEntity blockEntity){

        if (maidenTerminal.hasBlock(blockEntity.getBlockPos())){
            List<Direction> validSides = new ArrayList<>();

            for (Direction dir : Direction.values()){
                IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), dir);
                if (handler != null){
                    validSides.add(dir);
                }
            }

            MenuProvider containerProvider = new MenuProvider() {

                ItemStackHandler stackHandler = new ItemStackHandler(12);

                @Override
                public Component getDisplayName() {
                    return Component.translatable("screen.cmt_participant");
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    return new CMTParticipantMenu(containerId, playerInventory, stackHandler, blockEntity.getBlockPos(), maidenTerminal.getBlockPos(), maidenTerminal.getData(blockEntity.getBlockPos()), validSides);
                }
            };

           player.openMenu(containerProvider, buf -> {
                buf.writeBlockPos(blockEntity.getBlockPos());
                buf.writeBlockPos(maidenTerminal.getBlockPos());
                buf.writeNbt(maidenTerminal.getData(blockEntity.getBlockPos()).getCompound(level.registryAccess()));

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

    private static void handleBindingBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkMaidenTerminalEntity maidenTerminal, BlockEntity blockEntity){

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

    private static void finishSuccess(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§a" + text), true);
    }
    private static void finishFail(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§c" + text), true);
    }
}
