package net.sophiebun.buntsy.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.sophiebun.buntsy.blocks.entity.custom.GiantCocoonBlockEntity;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;

import java.util.function.Supplier;

public class ModCocoonBagClientPacket {

    private final CompoundTag itemHandlerTag;

    public ModCocoonBagClientPacket(CompoundTag itemHandlerTag) {
        this.itemHandlerTag = itemHandlerTag;
    }

    public static ModCocoonBagClientPacket read(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();

        return new ModCocoonBagClientPacket(tag);
    }

    public void write(FriendlyByteBuf buf){
        buf.writeNbt(itemHandlerTag);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {

            DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> handleClientSide(supplier));
        });
            
        ctx.setPacketHandled(true);
    }

    public DistExecutor.SafeRunnable handleClientSide(Supplier<NetworkEvent.Context> supplier){
        return () -> {
            LocalPlayer player = Minecraft.getInstance().player;

            CompoundTag tag = new CompoundTag();
            tag.put("uro_contents", itemHandlerTag);
            tag.putBoolean("in_update", true);

            ItemStack handItem = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();

            if (!handItem.isEmpty() && handItem.hasTag() && handItem.is(ModItems.COCOON_BAG.get())){
                tag.putInt("buntsy.uro_id", CocoonBag.getUroId(handItem));
                handItem.setTag(tag);
            }
            else {
                tag.putInt("buntsy.uro_id", CocoonBag.getUroId(offHand));
                offHand.setTag(tag);
            }
        };
    }
}
