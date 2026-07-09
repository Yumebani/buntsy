package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.screen.CMTParticipantMenu;
import net.sophiebun.buntsy.screen.CMTParticipantScreen;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class CMTParticipantPacket {

    private final CMTParticipantPacketOperation operation;

    private CMTParticipantData data;
    private BlockPos terminal;
    private BlockPos target;
    private List<ItemStack> filter;

    public CMTParticipantPacket(CMTParticipantPacketOperation operation, CMTParticipantData data, BlockPos terminal, BlockPos target) {
        this.operation = operation;
        this.data = data;
        this.target = target;
        this.terminal = terminal;
    }

    public CMTParticipantPacket(CMTParticipantPacketOperation operation, List<ItemStack> filter) {
        this.operation = operation;
        this.filter = filter;
    }

    public static CMTParticipantPacket read(FriendlyByteBuf buf) {

        CMTParticipantPacketOperation operation = CMTParticipantPacketOperation.values()[buf.readInt()];

        if (operation == CMTParticipantPacketOperation.SET_DATA){
            CMTParticipantData data = CMTParticipantData.parseCompound(buf.readNbt());
            BlockPos terminal = buf.readBlockPos();
            BlockPos target = buf.readBlockPos();
            return new CMTParticipantPacket(operation, data, terminal, target);
        }
        else {
            List<ItemStack> filter = new ArrayList<>();
            for (int i = 0; i < 12; i++){
                filter.add(buf.readItem());
            }
            return new CMTParticipantPacket(operation, filter);
        }
    }

    public void write(FriendlyByteBuf buf){

        buf.writeInt(operation.ordinal());

        if (operation == CMTParticipantPacketOperation.SET_DATA){
            buf.writeNbt(data.getCompound());
            buf.writeBlockPos(terminal);
            buf.writeBlockPos(this.target);
        }
        else {
            for (ItemStack stack : this.filter){
                buf.writeItem(stack);
            }
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
                handlePacketServer(ctx);
        });
            
        ctx.setPacketHandled(true);
    }

    private void handlePacketServer(NetworkEvent.Context ctx) {

        if (operation == CMTParticipantPacketOperation.SET_DATA){
            ClockworkMaidenTerminalEntity entity = ((ClockworkMaidenTerminalEntity) ctx.getSender().level().getBlockEntity(this.terminal));
            entity.updateData(this.data, target);
        } else if (operation == CMTParticipantPacketOperation.LOAD_FILTER){
            CMTParticipantMenu menu = ((CMTParticipantMenu) ctx.getSender().containerMenu);
            for (int i = 0; i < 12; i++){
                menu.slots.get(i + 36).set(filter.get(i));
            }
            menu.broadcastChanges();
        }
    }
}
