package net.sophiebun.buntsy.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEffectTicker {

    @SubscribeEvent
    public static void TickEffects(TickEvent.ServerTickEvent event){
        MinecraftServer server = event.getServer();
        PrismaticBeaconSavedData data = PrismaticBeaconSavedData.computeIfAbsent(server);

        if (data.getLastTick() < 20){
            data.tickUp();
        }
        else{
            Map<Integer, Tuple<UUID, List<Tuple<MobEffect, Integer>>>> playerEffects = data.getPlayerEffects();
            Map<Integer, Boolean> valid = data.getValid();
            /*
            Map<Integer, Boolean> loaded = data.getLoaded();
            Map<Integer, Boolean> updated = data.getUpdated();

            for (int id : updated.keySet().stream().toList()){
                if (loaded.get(id)){
                    if (updated.get(id)){
                        updated.put(id, false);
                    }
                    else {
                        updated.remove(id);
                        playerEffects.remove(id);
                    }
                }
            }

             */

            for (int id : playerEffects.keySet()){
                if (valid.get(id)){
                    ServerPlayer player = server.getPlayerList().getPlayer(playerEffects.get(id).getA());

                    if (player != null) {
                        for (Tuple<MobEffect, Integer> effect : playerEffects.get(id).getB()){
                            player.addEffect(new MobEffectInstance(effect.getA(), 100, effect.getB() - 1));
                        }
                    }
                }
            }

            data.resetLastTick();
        }
    }
}
