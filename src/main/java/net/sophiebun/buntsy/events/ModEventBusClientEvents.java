package net.sophiebun.buntsy.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.client.FairyCollectionTrayRenderer;
import net.sophiebun.buntsy.blocks.entity.client.FairyInfusionBenchRenderer;
import net.sophiebun.buntsy.blocks.entity.client.FairyOfferingBenchRenderer;
import net.sophiebun.buntsy.entity.client.FairyModel;
import net.sophiebun.buntsy.entity.client.ModModelLayers;
import net.sophiebun.buntsy.entity.client.SilkbunModel;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.SILKBUN_LOCATION, SilkbunModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FAIRY_LAYER, FairyModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderes(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(ModBlockEntities.OFFERING_BENCH_BLOCK_ENTITY.get(),
                FairyOfferingBenchRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FAIRY_COLLECTION_TRAY_BLOCK_ENTITY.get(),
                FairyCollectionTrayRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get(),
                FairyInfusionBenchRenderer::new);
    }
}
