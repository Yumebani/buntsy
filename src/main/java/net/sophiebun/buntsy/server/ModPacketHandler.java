package net.sophiebun.buntsy.server;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.server.packets.*;

@EventBusSubscriber(modid = BuntsyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModPacketHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToServer(
                ClockworkCardPuncherPacket.TYPE,
                ClockworkCardPuncherPacket.STREAM_CODEC,
                ClockworkCardPuncherPacket::handle
        );

        registrar.playToServer(
                FairyStaffPacket.TYPE,
                FairyStaffPacket.STREAM_CODEC,
                FairyStaffPacket::handle
        );

        registrar.playToServer(
                BindingStaffPacket.TYPE,
                BindingStaffPacket.STREAM_CODEC,
                BindingStaffPacket::handle
        );

        registrar.playToServer(
                GiantCocoonServerPacket.TYPE,
                GiantCocoonServerPacket.STREAM_CODEC,
                GiantCocoonServerPacket::handle
        );

        registrar.playToServer(
                CocoonBagServerPacket.TYPE,
                CocoonBagServerPacket.STREAM_CODEC,
                CocoonBagServerPacket::handle
        );

        registrar.playToServer(
                CMTParticipantPacket.TYPE,
                CMTParticipantPacket.STREAM_CODEC,
                CMTParticipantPacket::handle
        );


        registrar.playToClient(
                GiantCocoonClientPacket.TYPE,
                GiantCocoonClientPacket.STREAM_CODEC,
                GiantCocoonClientPacket::handle
        );

        registrar.playToClient(
                CocoonBagClientPacket.TYPE,
                CocoonBagClientPacket.STREAM_CODEC,
                CocoonBagClientPacket::handle
        );
    }
}
