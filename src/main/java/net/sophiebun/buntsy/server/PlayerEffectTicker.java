package net.sophiebun.buntsy.server;

import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.sophiebun.buntsy.BuntsyMod;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = BuntsyMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEffectTicker {

    @SubscribeEvent
    public static void TickEffects(ServerTickEvent.Pre event){
        MinecraftServer server = event.getServer();
        PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(server);

        if (data.getLastTick() < 20){
            data.tickUp();
        }
        else{
            Map<Integer, Tuple<UUID, List<Tuple<Holder<MobEffect>, Integer>>>> playerEffects = data.getPlayerEffects();
            Map<Integer, Boolean> valid = data.getValid();

            for (int id : playerEffects.keySet()){
                if (valid.get(id)){
                    ServerPlayer player = server.getPlayerList().getPlayer(playerEffects.get(id).getA());

                    if (player != null) {
                        for (Tuple<Holder<MobEffect>, Integer> effect : playerEffects.get(id).getB()){
                            player.addEffect(new MobEffectInstance(effect.getA(), 100, effect.getB() - 1));
                        }
                    }
                }
            }

            data.resetLastTick();
        }
    }
}
