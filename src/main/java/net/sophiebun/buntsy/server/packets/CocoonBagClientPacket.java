package net.sophiebun.buntsy.server.packets;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.codec.UroContent;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.item.ModItems;

public record CocoonBagClientPacket (
        CompoundTag itemHandlerTag
) implements CustomPacketPayload {

    public static final Type<CocoonBagClientPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "cocoon_bag_client_packet"));

    public static final StreamCodec<FriendlyByteBuf, CocoonBagClientPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, CocoonBagClientPacket::itemHandlerTag,
            CocoonBagClientPacket::new
    );

    @Override
    public Type<CocoonBagClientPacket> type() {
        return TYPE;
    }

    public static void handle(CocoonBagClientPacket packet, IPayloadContext context){
        LocalPlayer player = Minecraft.getInstance().player;

        ItemStack handItem = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (!handItem.isEmpty() && handItem.is(ModItems.COCOON_BAG.get())){
            handItem.set(ModDataComponents.URO_CONTENT, new UroContent(packet.itemHandlerTag(), true, false));
        }
        else {
            offHand.set(ModDataComponents.URO_CONTENT, new UroContent(packet.itemHandlerTag(),true, false));
        }
    }
}
