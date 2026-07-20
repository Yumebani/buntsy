package net.sophiebun.buntsy.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.server.GiantCocoonSavedData;

public record GiantCocoonServerPacket (
        CompoundTag itemHandlerTag,
        int id,
        BlockPos origin
) implements CustomPacketPayload {

    public static final Type<GiantCocoonServerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "giant_cocoon_server_packet"));

    public static final StreamCodec<FriendlyByteBuf, GiantCocoonServerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, GiantCocoonServerPacket::itemHandlerTag,
            ByteBufCodecs.INT, GiantCocoonServerPacket::id,
            BlockPos.STREAM_CODEC, GiantCocoonServerPacket::origin,
            GiantCocoonServerPacket::new
    );

    @Override
    public Type<GiantCocoonServerPacket> type() {
        return TYPE;
    }

    public static void handle(GiantCocoonServerPacket packet, IPayloadContext context){

        context.enqueueWork(() -> {

            ServerPlayer player = ((ServerPlayer) context.player());
            GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(player.getServer());
            data.packetUpdate(player.level().registryAccess(), packet.id(), packet.itemHandlerTag(), packet.origin());

        });

    }
}
