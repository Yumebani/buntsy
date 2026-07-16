package net.sophiebun.buntsy.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.client.*;
import net.sophiebun.buntsy.client.particle.ChocolateDustParticle;
import net.sophiebun.buntsy.client.particle.ModParticleTypes;
import net.sophiebun.buntsy.entity.client.*;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.Essence;
import net.sophiebun.buntsy.item.custom.FumeBottle;
import net.sophiebun.buntsy.item.custom.Prism;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.CHOCOLATE_DUST_PARTICLE.get(), ChocolateDustParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.SILKBUN_LOCATION, SilkbunModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FAIRY_LAYER, FairyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HOOTCAT_LAYER, HootCatModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HOOTCAT_COLLAR_LAYER, HootCatModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MARIONETTE_LAYER, MarionetteModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CLOCKWORK_MAIDEN_LAYER, ClockworkMaidenModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderes(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(ModBlockEntities.OFFERING_BENCH_BLOCK_ENTITY.get(),
                FairyOfferingBenchRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FAIRY_COLLECTION_TRAY_BLOCK_ENTITY.get(),
                FairyCollectionTrayRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get(),
                FairyInfusionBenchRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.INFUSION_PEDESTAL_BLOCK_ENTITY.get(),
                InfusionPedestalRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.INFUSION_ALTAR_BASIC_BLOCK_ENTITY.get(),
                InfusionAltarBasicRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.INFUSION_ALTAR_ADVANCED_BLOCK_ENTITY.get(),
                InfusionAltarAdvancedRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderes(RegisterColorHandlersEvent.Item event){
        event.register(FumeBottle.getTint(), ModItems.FUME_BOTTLE.get());
        event.register(FumeBottle.getTint(), ModItems.CATALYST.get());
        event.register(Essence.getTint(), ModItems.ESSENCE.get());
        event.register(Prism.getTint(), ModItems.PRISM.get());
    }
}
