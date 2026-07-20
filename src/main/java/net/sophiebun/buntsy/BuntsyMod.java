package net.sophiebun.buntsy;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.client.*;
import net.sophiebun.buntsy.client.particle.ModParticleTypes;
import net.sophiebun.buntsy.components.ModDataComponents;
import net.sophiebun.buntsy.dispenser.DispenserBehaviourAditions;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.client.*;
import net.sophiebun.buntsy.fluids.ModFluidTypes;
import net.sophiebun.buntsy.fluids.ModFluids;
import net.sophiebun.buntsy.item.CreativeModeTabs;
import net.sophiebun.buntsy.item.ModArmorMats;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.recipe.ModRecipes;
import net.sophiebun.buntsy.screen.*;
import net.sophiebun.buntsy.screen.clockwork.*;
import net.sophiebun.buntsy.worldgen.biome.surface.ModSurfaceRules;
import net.sophiebun.buntsy.worldgen.feature.ModFeatures;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BuntsyMod.MODID)
public class BuntsyMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "buntsy";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public BuntsyMod(IEventBus modEventBus)
    {

        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, (pSource, pStack) -> DispenserBehaviourAditions.interactWithSExtractor(pSource, pStack));

        ModArmorMats.register(modEventBus);
        ModDataComponents.register(modEventBus);

        ModParticleTypes.register(modEventBus);

        CreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModEntities.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModTrunkPlacerTypes.register(modEventBus);
        ModFoliagePlacers.register(modEventBus);

        ModFeatures.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        //modEventBus.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.PINK_BLOOM.getId(), ModBlocks.POTTED_PINK_BLOOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.BLUE_BLOOM.getId(), ModBlocks.POTTED_BLUE_BLOOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.ABYSSAL_BLOOM.getId(), ModBlocks.POTTED_ABYSSAL_BLOOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.FROZEN_BLOOM.getId(), ModBlocks.POTTED_FROZEN_BLOOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.ORIGAMI_FERN.getId(), ModBlocks.POTTED_ORIGAMI_FERN);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.GENTLIT_SAPLING.getId(), ModBlocks.POTTED_GENTLIT_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.BRAVOT_SAPLING.getId(), ModBlocks.POTTED_BRAVOT_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MALVOR_SAPLING.getId(), ModBlocks.POTTED_MALVOR_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.LOVESHROOM.getId(), ModBlocks.POTTED_LOVESHROOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.GLOWSHROOM.getId(), ModBlocks.POTTED_GLOWSHROOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.PALESHROOM.getId(), ModBlocks.POTTED_PALESHROOM);

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModSurfaceRules.makeRules());
        });
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void commonSetup(final FMLCommonSetupEvent event){
            EntityRenderers.register(ModEntities.SILKBUN_ENTITY.get(), SilkbunRenderer::new);
            EntityRenderers.register(ModEntities.FAIRY_ENTITY.get(), FairyRenderer::new);
            EntityRenderers.register(ModEntities.HOOTCAT_ENTITY.get(), HootcatRenderer::new);
            EntityRenderers.register(ModEntities.MARIONETTE.get(), MarionetteRenderer::new);
            EntityRenderers.register(ModEntities.CLOCKWORK_MAIDEN_ENTITY.get(), ClockworkMaidenRenderer::new);

            BlockEntityRenderers.register(ModBlockEntities.THREAD_REELER_BLOCK_ENTITY.get(), ThreadReelerBlockRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.GRINDING_WHEEL_BLOCK_ENTITY.get(), GrindingWheelBlockRenderer::new);

            BlockEntityRenderers.register(ModBlockEntities.CLOCKWORK_FAIRY_TERMINAL_ENTITY.get(), ClockworkFairyTerminalRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.CLOCKWORK_MAIDEN_TERMINAL_ENTITY.get(), ClockworkMaidenTerminalRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.CLOCKWORK_WINDER_ENTITY.get(), ClockworkWinderRenderer::new);

            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SWICE.get(), RenderType.translucent());
            });
        }

        @SubscribeEvent
        public static void menuScreensEvent(RegisterMenuScreensEvent event)
        {
            event.register(ModMenuTypes.FAIRY_OFFERING_BENCH_MENU.get(), FairyOfferingBenchScreen::new);
            event.register(ModMenuTypes.GRINDING_WHEEL_MENU.get(), GrindingWheelScreen::new);
            event.register(ModMenuTypes.THREAD_REELER_MENU.get(), ThreadReelerScreen::new);
            event.register(ModMenuTypes.FAIRY_COLLECTION_TRAY_MENU.get(), FairyCollectionTrayScreen::new);
            event.register(ModMenuTypes.FAIRY_INFUSION_BENCH_MENU.get(), FairyInfusionBenchScreen::new);
            event.register(ModMenuTypes.MAGIC_CRYSTALIZER_MENU.get(), MagicCrystalizerScreen::new);
            event.register(ModMenuTypes.FUME_DISTILLERY_MENU.get(), FumeDistilleryScreen::new);
            event.register(ModMenuTypes.FUME_SPREADER_MENU.get(), FumeSpreaderScreen::new);
            event.register(ModMenuTypes.GIANT_COCOON_MENU.get(), GiantCocoonScreen::new);
            event.register(ModMenuTypes.COCOON_BAG_MENU.get(), CocoonBagScreen::new);
            event.register(ModMenuTypes.MIXER_MENU.get(), MixerScreen::new);

            event.register(ModMenuTypes.CLOCKWORK_SYRUP_EXTRACTOR_MENU.get(), ClockworkSyrupExtractorScreen::new);
            event.register(ModMenuTypes.CLOCKWORK_GEYSER_COLLECTOR_MENU.get(), ClockworkGeyserCollectorScreen::new);
            event.register(ModMenuTypes.CLOCKWORK_POWDERED_SUGAR_COLLECTOR_MENU.get(), ClockworkPowderedSugarCollectorScreen::new);
            event.register(ModMenuTypes.CLOCKWORK_FISHER_MENU.get(), ClockworkFisherScreen::new);
            event.register(ModMenuTypes.CLOCKWORK_CRAFTER_MENU.get(), ClockworkCrafterScreen::new);
            event.register(ModMenuTypes.CLOCKWORK_WINDER_MENU.get(), ClockworkWinderScreen::new);
            event.register(ModMenuTypes.CLOCKWORK_FAIRY_TERMINAL_MENU.get(), ClockworkFairyTerminalScreen::new);

            event.register(ModMenuTypes.CMT_PARTICIPANT_MENU.get(), CMTParticipantScreen::new);
        }
    }
}
