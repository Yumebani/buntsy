package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.screen.CMTParticipantMenu;
import net.sophiebun.buntsy.screen.CMTParticipantScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class CMTParticipantPacket {

    private final CMTParticipantData data;

    private final BlockPos terminal;

    private final BlockPos target;
    private final List<Direction> validSides;

    public CMTParticipantPacket(CMTParticipantData data, BlockPos terminal, BlockPos target, List<Direction> validSides) {
        this.data = data;
        this.target = target;

        this.terminal = terminal;

        this.validSides = validSides;
    }

    public CMTParticipantPacket(CMTParticipantData data, BlockPos terminal, BlockPos target) {
        this.data = data;
        this.target = target;

        this.terminal = terminal;

        this.validSides = new ArrayList<>();
    }

    public static CMTParticipantPacket read(FriendlyByteBuf buf) {

        CMTParticipantData data = CMTParticipantData.parseCompound(buf.readNbt());
        BlockPos terminal = buf.readBlockPos();

        BlockPos target = buf.readBlockPos();
        List<Direction> validSides = new ArrayList<>();
        int count = buf.readInt();
        for (int x = 0; x < count; x++){
            validSides.add(Direction.values()[buf.readInt()]);
        }

        return new CMTParticipantPacket(data, terminal, target, validSides);
    }

    public void write(FriendlyByteBuf buf){

        buf.writeNbt(data.getCompound());
        buf.writeBlockPos(terminal);

        buf.writeBlockPos(this.target);
        buf.writeInt(this.validSides.size());
        for (Direction dir : validSides){
            buf.writeInt(dir.ordinal());
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

        ClockworkMaidenTerminalEntity entity = ((ClockworkMaidenTerminalEntity) ctx.getSender().level().getBlockEntity(this.terminal));
        entity.updateData(this.data, target);
    }

    private void handlePacketClient(NetworkEvent.Context ctx) {
        Minecraft.getInstance().setScreen(
                new CMTParticipantScreen(new CMTParticipantMenu(0, Minecraft.getInstance().player),
                        Minecraft.getInstance().player.getInventory(),
                        target,
                        terminal,
                        data,
                        validSides)
        );
    }
}
