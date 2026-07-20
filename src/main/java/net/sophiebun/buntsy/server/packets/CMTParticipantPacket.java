package net.sophiebun.buntsy.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.screen.clockwork.CMTParticipantMenu;
import net.sophiebun.buntsy.server.CMTParticipantPacketOperation;

import java.util.List;
import java.util.Optional;

public record CMTParticipantPacket (
        Optional<CompoundTag> data,
        Optional<BlockPos> terminal,
        Optional<BlockPos> target,
        Optional<List<ItemStack>> filter,
        CMTParticipantPacketOperation operation

) implements CustomPacketPayload {

    public CMTParticipantPacket(Optional<CompoundTag> data, Optional<BlockPos> terminal, Optional<BlockPos> target, Optional<List<ItemStack>> filter, int operation){
        this(data, terminal, target, filter, CMTParticipantPacketOperation.values()[operation]);
    }

    public int operationTypeOrdinal(){
        return operation.ordinal();
    }

    public static final Type<CMTParticipantPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "cmt_participant_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CMTParticipantPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.COMPOUND_TAG), CMTParticipantPacket::data,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), CMTParticipantPacket::terminal,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), CMTParticipantPacket::target,
            ByteBufCodecs.optional(ItemStack.LIST_STREAM_CODEC), CMTParticipantPacket::filter,
            ByteBufCodecs.VAR_INT, CMTParticipantPacket::operationTypeOrdinal,
            CMTParticipantPacket::new
    );

    @Override
    public Type<CMTParticipantPacket> type() {
        return TYPE;
    }

    public static void handle(CMTParticipantPacket packet, IPayloadContext context) {

        context.enqueueWork(() -> {

            ServerLevel level = ((ServerLevel) context.player().level());

            if (packet.operation() == CMTParticipantPacketOperation.SET_DATA){
                ClockworkMaidenTerminalEntity entity = ((ClockworkMaidenTerminalEntity) level.getBlockEntity(packet.terminal().get()));
                entity.updateData(CMTParticipantData.parseCompound(packet.data().get(), level.registryAccess()), packet.target().get());
            } else if (packet.operation() == CMTParticipantPacketOperation.LOAD_FILTER){
                CMTParticipantMenu menu = ((CMTParticipantMenu) ((ServerPlayer) context.player()).containerMenu);
                for (int i = 0; i < 12; i++){
                    menu.slots.get(i + 36).set(packet.filter().get().get(i));
                }
                menu.broadcastChanges();
            }

        });


    }
}
