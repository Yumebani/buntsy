package net.sophiebun.buntsy.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;

public record BindingStaffPacket(
        BlockPos binding,
        BlockPos master
) implements CustomPacketPayload {

    public static final Type<BindingStaffPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "binding_staff_packet"));

    public static final StreamCodec<FriendlyByteBuf, BindingStaffPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BindingStaffPacket::binding,
            BlockPos.STREAM_CODEC, BindingStaffPacket::master,
            BindingStaffPacket::new
    );

    @Override
    public Type<BindingStaffPacket> type() {
        return TYPE;
    }

    public static void handle(BindingStaffPacket packet, IPayloadContext context){

        context.enqueueWork(() -> {

            ServerPlayer player = ((ServerPlayer) context.player());
            ServerLevel level = player.serverLevel();

            if (level == null || !level.hasChunkAt(packet.binding()) || !level.hasChunkAt(packet.master())) return;

            if (level.getBlockEntity(packet.master()) instanceof InfusionAltarBlockEntity &&
                    level.getBlockEntity(packet.binding()) instanceof FairyPowerRelayBlockEntity){

                if (getDistanceToBlock(packet.master(), packet.binding()) < 6){
                    FairyPowerRelayBlockEntity fairyRelay = (FairyPowerRelayBlockEntity) level.getBlockEntity(packet.binding());
                    InfusionAltarBlockEntity basicAltar = (InfusionAltarBlockEntity) level.getBlockEntity(packet.master());

                    if (fairyRelay.getLinked() != null) fairyRelay.removeLinked(level);

                    fairyRelay.setLinked(packet.master());
                    basicAltar.addRelay(packet.binding());

                    finishSuccess(player, "Bound relay to altar");
                }
                else {
                    finishFail(player, "Blocks too far away");
                }
            }

        });

    }

    public static double getDistanceToBlock(BlockPos first, BlockPos second) {
        double deltaX = first.getX() - second.getX();
        double deltaY = first.getY() - second.getY();
        double deltaZ = first.getZ() - second.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    private static void finishSuccess(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§a" + text), true);
    }
    private static void finishFail(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§c" + text), true);
    }
}
