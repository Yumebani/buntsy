package net.sophiebun.buntsy.server.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.custom.GiantCocoonBlockEntity;

public record GiantCocoonClientPacket(
        CompoundTag itemHandlerTag,
        BlockPos giantCocoonBlock
) implements CustomPacketPayload {

    public static final Type<GiantCocoonClientPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "giant_cocoon_client_packet"));

    public static final StreamCodec<FriendlyByteBuf, GiantCocoonClientPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, GiantCocoonClientPacket::itemHandlerTag,
            BlockPos.STREAM_CODEC, GiantCocoonClientPacket::giantCocoonBlock,
            GiantCocoonClientPacket::new
    );

    @Override
    public Type<GiantCocoonClientPacket> type() {
        return TYPE;
    }

    public static void handle(GiantCocoonClientPacket packet, IPayloadContext context){
        ClientLevel level = Minecraft.getInstance().level;
        BlockEntity block = level.getBlockEntity(packet.giantCocoonBlock());
        if (block instanceof GiantCocoonBlockEntity){

            GiantCocoonBlockEntity cocoon = (GiantCocoonBlockEntity)block;
            cocoon.setContentItemHandler(level.registryAccess(), packet.itemHandlerTag());
        }
    }
}
