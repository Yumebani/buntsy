package net.sophiebun.buntsy.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sophiebun.buntsy.BuntsyMod;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BuntsyMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register(){

        INSTANCE.registerMessage(0, ModFairyStaffPacket.class,
                ModFairyStaffPacket::write,
                ModFairyStaffPacket::read,
                ModFairyStaffPacket::handle);
    }
}
