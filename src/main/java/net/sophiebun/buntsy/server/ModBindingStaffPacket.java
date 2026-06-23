package net.sophiebun.buntsy.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;

import java.util.function.Supplier;

public class ModBindingStaffPacket {

    private final BlockPos binding;
    private final BlockPos master;

    public ModBindingStaffPacket(BlockPos binding, BlockPos master) {
        this.binding = binding;
        this.master = master;
    }

    public static ModBindingStaffPacket read(FriendlyByteBuf buf) {
        BlockPos binding = buf.readBlockPos();
        BlockPos master = buf.readBlockPos();

        return new ModBindingStaffPacket(binding, master);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeBlockPos(binding);
        buf.writeBlockPos(master);
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
