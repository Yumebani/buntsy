package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.screen.ClockworkMaidenTerminalParticipantScreen;

import java.util.UUID;
import java.util.function.Supplier;

public class CMTParticipantPacket {

    private final CMTParticipantData data;

    private final BlockPos terminal;

    private final BlockPos target;
    private final UUID player;

    public CMTParticipantPacket(CMTParticipantData data, BlockPos terminal, BlockPos target, UUID player) {
        this.data = data;
        this.target = target;

        this.terminal = terminal;

        this.player = player;
    }

    public CMTParticipantPacket(CMTParticipantData data, BlockPos terminal, BlockPos target) {
        this.data = data;
        this.target = target;

        this.terminal = terminal;

        this.player = null;
    }

    public CMTParticipantPacket(BlockPos terminal, BlockPos target, UUID player) {
        this.data = null;
        this.target = null;

        this.terminal = terminal;

        this.player = null;
    }

    public static CMTParticipantPacket read(FriendlyByteBuf buf) {

        CMTParticipantData data = null;
        if (buf.readBoolean()){
            data = CMTParticipantData.parseCompound(buf.readNbt());
        }

        BlockPos terminal = buf.readBlockPos();

        BlockPos target = null;
        UUID player = null;
        if (buf.readBoolean()){
            target = buf.readBlockPos();
            player = buf.readUUID();
        }

        return new CMTParticipantPacket(data, terminal, target, player);
    }

    public void write(FriendlyByteBuf buf){

        buf.writeBoolean(data != null);
        if (data != null){
            buf.writeNbt(data.getCompound());
        }

        buf.writeBlockPos(terminal);

        buf.writeBoolean(this.target != null);
        if (this.target != null){
            buf.writeBlockPos(this.target);
            buf.writeUUID(player);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {

            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT){
                handlePacketClient(ctx);
            } else {
                handlePacketServer(ctx);
            }
        });
            
        ctx.setPacketHandled(true);
    }

    private void handlePacketServer(NetworkEvent.Context ctx) {

        if (this.player != null){
            ClockworkMaidenTerminalEntity entity = ((ClockworkMaidenTerminalEntity) ctx.getSender().level().getBlockEntity(this.terminal));
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.getSender()),
                    new CMTParticipantPacket(entity.getData(target), terminal, target, player));
        } else {
            ClockworkMaidenTerminalEntity entity = ((ClockworkMaidenTerminalEntity) ctx.getSender().level().getBlockEntity(this.terminal));
            entity.updateData(this.data, target);
        }
    }

    private void handlePacketClient(NetworkEvent.Context ctx) {
        Minecraft.getInstance().setScreen(
                new ClockworkMaidenTerminalParticipantScreen(new CMTParticipantMenu(),
                        Minecraft.getInstance().player.getInventory(),
                        )
        );
    }
}
