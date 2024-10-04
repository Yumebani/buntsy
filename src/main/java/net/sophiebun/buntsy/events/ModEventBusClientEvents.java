package net.sophiebun.buntsy.events;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.client.*;
import net.sophiebun.buntsy.entity.client.FairyModel;
import net.sophiebun.buntsy.entity.client.HootCatModel;
import net.sophiebun.buntsy.entity.client.ModModelLayers;
import net.sophiebun.buntsy.entity.client.SilkbunModel;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.item.custom.CocoonBag;
import net.sophiebun.buntsy.item.custom.FumeBottle;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.SILKBUN_LOCATION, SilkbunModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FAIRY_LAYER, FairyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HOOTCAT_LAYER, HootCatModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HOOTCAT_COLLAR_LAYER, HootCatModel::createBodyLayer);
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
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderes(RegisterColorHandlersEvent.Item event){
        event.register(FumeBottle.getTint(), ModItems.FUME_BOTTLE.get());
        event.register(FumeBottle.getTint(), ModItems.CATALYST.get());
    }
}
