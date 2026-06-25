package net.sophiebun.buntsy;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.SyrupExtractorBlock;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.client.GrindingWheelBlockRenderer;
import net.sophiebun.buntsy.blocks.entity.client.ThreadReelerBlockRenderer;
import net.sophiebun.buntsy.datagen.ModBlockTagGenerator;
import net.sophiebun.buntsy.dispenser.DispenserBehaviourAditions;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.client.ClockworkMaidenRenderer;
import net.sophiebun.buntsy.entity.client.FairyRenderer;
import net.sophiebun.buntsy.entity.client.HootcatRenderer;
import net.sophiebun.buntsy.entity.client.SilkbunRenderer;
import net.sophiebun.buntsy.item.CreativeModeTabs;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.recipe.ModRecipes;
import net.sophiebun.buntsy.screen.*;
import net.sophiebun.buntsy.server.ModPacketHandler;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;
import net.sophiebun.buntsy.worldgen.biome.ModTerrablender;
import net.sophiebun.buntsy.worldgen.biome.surface.ModSurfaceRules;
import net.sophiebun.buntsy.worldgen.feature.ModFeatures;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;
import terrablender.api.SurfaceRuleManager;

import java.nio.channels.NetworkChannel;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BuntsyMod.MODID)
public class BuntsyMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "buntsy";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public BuntsyMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();

        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, (pSource, pStack) -> DispenserBehaviourAditions.interactWithSExtractor(pSource, pStack));

        ModPacketHandler.register();

        CreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModEntities.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModTrunkPlacerTypes.register(modEventBus);
        ModFoliagePlacers.register(modEventBus);

        ModFeatures.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

        event.enqueueWork(() -> {
            ModTerrablender.registerBiomes();

            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.PINK_BLOOM.getId(), ModBlocks.POTTED_PINK_BLOOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.BLUE_BLOOM.getId(), ModBlocks.POTTED_BLUE_BLOOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.GENTLIT_SAPLING.getId(), ModBlocks.POTTED_GENTLIT_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.BRAVOT_SAPLING.getId(), ModBlocks.POTTED_BRAVOT_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MALVOR_SAPLING.getId(), ModBlocks.POTTED_MALVOR_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.LOVESHROOM.getId(), ModBlocks.POTTED_LOVESHROOM);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.GLOWSHROOM.getId(), ModBlocks.POTTED_GLOWSHROOM);

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModSurfaceRules.makeRules());
        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.FAIRY_OFFERING_BENCH_MENU.get(), FairyOfferingBenchScreen::new);
            MenuScreens.register(ModMenuTypes.GRINDING_WHEEL_MENU.get(), GrindingWheelScreen::new);
            MenuScreens.register(ModMenuTypes.THREAD_REELER_MENU.get(), ThreadReelerScreen::new);
            MenuScreens.register(ModMenuTypes.FAIRY_COLLECTION_TRAY_MENU.get(), FairyCollectionTrayScreen::new);
            MenuScreens.register(ModMenuTypes.FAIRY_INFUSION_BENCH_MENU.get(), FairyInfusionBenchScreen::new);
            MenuScreens.register(ModMenuTypes.MAGIC_CRYSTALIZER_MENU.get(), MagicCrystalizerScreen::new);
            MenuScreens.register(ModMenuTypes.FUME_DISTILLERY_MENU.get(), FumeDistilleryScreen::new);
            MenuScreens.register(ModMenuTypes.FUME_SPREADER_MENU.get(), FumeSpreaderScreen::new);
            MenuScreens.register(ModMenuTypes.GIANT_COCOON_MENU.get(), GiantCocoonScreen::new);
            MenuScreens.register(ModMenuTypes.COCOON_BAG_MENU.get(), CocoonBagScreen::new);
            MenuScreens.register(ModMenuTypes.MIXER_MENU.get(), MixerScreen::new);

            EntityRenderers.register(ModEntities.SILKBUN_ENTITY.get(), SilkbunRenderer::new);
            EntityRenderers.register(ModEntities.FAIRY_ENTITY.get(), FairyRenderer::new);
            EntityRenderers.register(ModEntities.HOOTCAT_ENTITY.get(), HootcatRenderer::new);
            EntityRenderers.register(ModEntities.CLOCKWORK_MAIDEN_ENTITY.get(), ClockworkMaidenRenderer::new);

            BlockEntityRenderers.register(ModBlockEntities.THREAD_REELER_BLOCK_ENTITY.get(), ThreadReelerBlockRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.GRINDING_WHEEL_BLOCK_ENTITY.get(), GrindingWheelBlockRenderer::new);
        }
    }
}
