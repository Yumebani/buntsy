package net.sophiebun.buntsy.events;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.client.ModModelLayers;
import net.sophiebun.buntsy.entity.client.SilkbunModel;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.SILKBUN_LOCATION, SilkbunModel::createBodyLayer);
    }
}
