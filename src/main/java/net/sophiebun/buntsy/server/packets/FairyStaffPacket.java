package net.sophiebun.buntsy.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkFairyTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.custom.FairyInteractBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.server.ConfigureStaffOperationType;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.Optional;

public record FairyStaffPacket(
        int fairyId,
        Optional<BlockPos> terminalBlock,
        Optional<BlockPos> block,
        ConfigureStaffOperationType operationType
) implements CustomPacketPayload {

    public FairyStaffPacket(int maidenId, Optional<BlockPos> terminalBlock, Optional<BlockPos> block, int operationType){
        this(maidenId, terminalBlock, block, ConfigureStaffOperationType.values()[operationType]);
    }

    public int operationTypeOrdinal(){
        return operationType.ordinal();
    }

    public static final Type<FairyStaffPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "fairy_staff_packet"));

    public static final StreamCodec<FriendlyByteBuf, FairyStaffPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FairyStaffPacket::fairyId,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), FairyStaffPacket::terminalBlock,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), FairyStaffPacket::block,
            ByteBufCodecs.VAR_INT, FairyStaffPacket::operationTypeOrdinal,
            FairyStaffPacket::new
    );

    @Override
    public Type<FairyStaffPacket> type() {
        return TYPE;
    }

    public static void handle(FairyStaffPacket packet, IPayloadContext context){

        context.enqueueWork(() -> {

            ServerLevel level = ((ServerLevel) context.player().level());

            if (level == null || (packet.block().isPresent() && !level.hasChunkAt(packet.block().get()))) return;

            if (!packet.terminalBlock().isPresent()){
                handleFairyOperations(packet, context);
            } else {
                handleFairyTerminalOperations(packet, context);
            }

        });
    }

    private static void handleFairyTerminalOperations(FairyStaffPacket packet, IPayloadContext context){
        ServerPlayer player = ((ServerPlayer) context.player());
        ServerLevel level = player.serverLevel();

        BlockEntity entity = level.getBlockEntity(packet.terminalBlock().get());

        if (entity == null || !(entity instanceof ClockworkFairyTerminalEntity)) return;

        ClockworkFairyTerminalEntity fairyTerminal = ((ClockworkFairyTerminalEntity) entity);

        if (packet.operationType() == ConfigureStaffOperationType.CLEAR_DATA){
            fairyTerminal.clearBlockEntityData(level);
            finishSuccess(player, "Cleared fairy terminal data");
        }
        else if (packet.operationType() == ConfigureStaffOperationType.SET_BLOCK){

            BlockState blockState = level.getBlockState(packet.block().get());
            if (blockState.is(ModTags.Blocks.FAIRY_INTERACTABLE_BLOCK_ENTITY)) {

                BlockEntity blockEntity = level.getBlockEntity(packet.block().get());

                handleInteractableBlockTerminal(level, player, fairyTerminal, blockEntity);
            } else {
                finishSuccess(player, "Cleared selection");
            }
        }
    }

    private static void handleFairyOperations(FairyStaffPacket packet, IPayloadContext context){
        ServerPlayer player = ((ServerPlayer) context.player());
        ServerLevel level = player.serverLevel();

        Fairy fairy = ((Fairy) level.getEntity(packet.fairyId()));

        if (fairy == null || !fairy.isAlive()) return;

        if (packet.operationType() == ConfigureStaffOperationType.CLEAR_DATA){
            fairy.clearBlockEntityData();
            finishSuccess(player, "Cleared fairy data");
        }
        else if (packet.operationType() == ConfigureStaffOperationType.SET_BLOCK){

            BlockState blockState = level.getBlockState(packet.block().get());
            if (blockState.is(ModTags.Blocks.FAIRY_INTERACTABLE_BLOCK_ENTITY)) {

                BlockEntity blockEntity = level.getBlockEntity(packet.block().get());

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

    private static  void handleInteractableBlock(ServerPlayer player, Fairy fairy, BlockEntity blockEntity){

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

    private static void handleInteractableBlockTerminal(ServerLevel level, ServerPlayer player, ClockworkFairyTerminalEntity fairyTerminal, BlockEntity blockEntity){

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

    private static void handleOfferingBench(ServerPlayer player, Fairy fairy, FairyOfferingBenchBlockEntity offeringBench){

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

    private static void finishSuccess(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§a" + text), true);
    }
    private static void finishFail(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§c" + text), true);
    }
}
