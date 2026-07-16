package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.sophiebun.buntsy.blocks.entity.custom.GiantCocoonBlockEntity;

import java.awt.*;
import java.util.function.Supplier;

public class ModGiantCocoonClientPacket {

    private final CompoundTag itemHandlerTag;
    private final BlockPos giantCocoonBlock;

    public ModGiantCocoonClientPacket(CompoundTag itemHandlerTag, BlockPos giantCocoonBlock) {
        this.itemHandlerTag = itemHandlerTag;
        this.giantCocoonBlock = giantCocoonBlock;
    }

    public static ModGiantCocoonClientPacket read(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        BlockPos giantCocoonBlock = buf.readBlockPos();

        return new ModGiantCocoonClientPacket(tag, giantCocoonBlock);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeNbt(itemHandlerTag);
        buf.writeBlockPos(giantCocoonBlock);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {

            DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> handleClientSide(supplier));

        });
            
        ctx.setPacketHandled(true);
    }

    public DistExecutor.SafeRunnable handleClientSide(Supplier<NetworkEvent.Context> supplier){
        return new DistExecutor.SafeRunnable(){
            @Override
            public void run() {
                ClientLevel level = Minecraft.getInstance().level;
                BlockEntity block = level.getBlockEntity(giantCocoonBlock);
                if (block instanceof GiantCocoonBlockEntity){

                    GiantCocoonBlockEntity cocoon = (GiantCocoonBlockEntity)block;
                    cocoon.setContentItemHandler(itemHandlerTag);
                }
            }
        };
    }
}
