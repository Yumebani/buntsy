package net.sophiebun.buntsy.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModGiantCocoonServerPacket {

    private final CompoundTag itemHandlerTag;
    private final int id;
    private final BlockPos origin;

    public ModGiantCocoonServerPacket(int id, CompoundTag itemHandlerTag, BlockPos origin) {
        this.id = id;
        this.itemHandlerTag = itemHandlerTag;
        this.origin = origin;
    }

    public static ModGiantCocoonServerPacket read(FriendlyByteBuf buf) {
        int id = buf.readInt();
        CompoundTag tag = buf.readNbt();
        BlockPos origin = buf.readBlockPos();

        return new ModGiantCocoonServerPacket(id, tag, origin);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeInt(id);
        buf.writeNbt(itemHandlerTag);
        buf.writeBlockPos(origin);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {

            ServerPlayer player = supplier.get().getSender();
            GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(player.getServer());
            data.packetUpdate(id, itemHandlerTag, origin);
        });
            
        ctx.setPacketHandled(true);
    }
}
