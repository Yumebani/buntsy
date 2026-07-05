package net.sophiebun.buntsy.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;

import java.util.UUID;
import java.util.function.Supplier;

public class CMTParticipantServerPacket {

    private final CMTParticipantData data;
    private final BlockPos terminal;

    private final BlockPos target;
    private final UUID player;

    public CMTParticipantServerPacket(CMTParticipantData data, BlockPos terminal, BlockPos target, UUID player) {
        this.data = data;
        this.terminal = terminal;

        this.target = target;
        this.player = player;
    }

    public CMTParticipantServerPacket(CMTParticipantData data, BlockPos terminal) {
        this.data = data;
        this.terminal = terminal;

        this.target = null;
        this.player = null;
    }

    public static CMTParticipantServerPacket read(FriendlyByteBuf buf) {
        CMTParticipantData data = CMTParticipantData.parseCompound(buf.readNbt());
        BlockPos terminal = buf.readBlockPos();

        BlockPos target = null;
        UUID player = null;
        if (buf.readBoolean()){
            target = buf.readBlockPos();
            player = buf.readUUID();
        }

        return new CMTParticipantServerPacket(data, terminal, target, player);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeNbt(data.getCompound());
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

            ServerPlayer player = ctx.getSender();
            ServerLevel level = ctx.getSender().serverLevel();

            if (level == null || (binding != null && !level.hasChunkAt(binding)) || (master != null && !level.hasChunkAt(master))) return;

            if (level.getBlockEntity(master) instanceof InfusionAltarBlockEntity &&
                    level.getBlockEntity(binding) instanceof FairyPowerRelayBlockEntity){

                if (getDistanceToBlock(master, binding) < 6){
                    FairyPowerRelayBlockEntity fairyRelay = (FairyPowerRelayBlockEntity) level.getBlockEntity(binding);
                    InfusionAltarBlockEntity basicAltar = (InfusionAltarBlockEntity) level.getBlockEntity(master);

                    if (fairyRelay.getLinked() != null) fairyRelay.removeLinked(level);

                    fairyRelay.setLinked(master);
                    basicAltar.addRelay(binding);

                    finishSuccess(player, "Binded relay to altar");
                }
                else {
                    finishFail(player, "Blocks too far away");
                }
            }
        });
            
        ctx.setPacketHandled(true);
    }

    public static double getDistanceToBlock(BlockPos first, BlockPos second) {
        double deltaX = first.getX() - second.getX();
        double deltaY = first.getY() - second.getY();
        double deltaZ = first.getZ() - second.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    private void finishSuccess(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§a" + text), true);
    }
    private void finishFail(ServerPlayer player, String text){
        player.displayClientMessage(Component.literal("§c" + text), true);
    }
}
