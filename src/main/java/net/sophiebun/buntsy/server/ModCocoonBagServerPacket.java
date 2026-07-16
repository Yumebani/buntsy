package net.sophiebun.buntsy.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ModCocoonBagServerPacket {

    private final CompoundTag itemHandlerTag;
    private final int id;
    private final boolean getUpdate;
    private final boolean unregister;

    public ModCocoonBagServerPacket(int id, CompoundTag itemHandlerTag, boolean getUpdate, boolean unregister) {
        this.id = id;
        this.itemHandlerTag = itemHandlerTag;
        this.getUpdate = getUpdate;
        this.unregister = unregister;
    }

    public static ModCocoonBagServerPacket read(FriendlyByteBuf buf) {
        int id = buf.readInt();
        CompoundTag tag = buf.readNbt();
        boolean getUpdate = buf.readBoolean();
        boolean unregister = buf.readBoolean();

        return new ModCocoonBagServerPacket(id, tag, getUpdate, unregister);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeInt(id);
        buf.writeNbt(itemHandlerTag);
        buf.writeBoolean(getUpdate);
        buf.writeBoolean(unregister);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {

            ServerPlayer player = supplier.get().getSender();
            GiantCocoonSavedData data = GiantCocoonSavedData.computeIfAbsent(player.getServer());
            if (getUpdate){
                data.distributePacket(id, player);
                data.registerNewPlayer(id, player);
            }
            else if (unregister){
                data.unregisterNewPlayer(id, player);
            }
            else {
                data.packetUpdatePlayer(id, itemHandlerTag, player);
            }
        });
            
        ctx.setPacketHandled(true);
    }
}
