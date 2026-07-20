package net.sophiebun.buntsy.server.packets;

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

import java.util.Optional;

public record CocoonBagServerPacket (
        Optional<CompoundTag> itemHandlerTag,
        int id,
        boolean getUpdate,
        boolean unregister
) implements CustomPacketPayload {

    public static final Type<CocoonBagServerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "cocoon_bag_server_packet"));

    public static final StreamCodec<FriendlyByteBuf, CocoonBagServerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.COMPOUND_TAG), CocoonBagServerPacket::itemHandlerTag,
            ByteBufCodecs.INT, CocoonBagServerPacket::id,
            ByteBufCodecs.BOOL, CocoonBagServerPacket::getUpdate,
            ByteBufCodecs.BOOL, CocoonBagServerPacket::unregister,
            CocoonBagServerPacket::new
    );

    @Override
    public Type<CocoonBagServerPacket> type() {
        return TYPE;
    }

    public static void handle(CocoonBagServerPacket packet, IPayloadContext context){

        context.enqueueWork(() -> {

            ServerPlayer player = ((ServerPlayer) context.player());
            GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(player.getServer());

            if (packet.getUpdate()){
                data.distributePacket(player.level().registryAccess(), packet.id(), player);
                data.registerNewPlayer(packet.id(), player);
            }
            else if (packet.unregister()){
                data.unregisterNewPlayer(packet.id(), player);
            }
            else {
                data.packetUpdatePlayer(player.level().registryAccess(), packet.id(), packet.itemHandlerTag().get(), player);
            }

        });

    }
}
