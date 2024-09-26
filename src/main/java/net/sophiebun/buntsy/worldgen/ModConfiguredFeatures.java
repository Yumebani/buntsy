package net.sophiebun.buntsy.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.feature.ModFeatures;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.GentlitTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.PieFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.grounded_trunk.GroundedTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.grounded_trunk.SphereFoliagePlacer;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> GENTLIT_KEY = registerKey("gentlit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BRAVOT_KEY = registerKey("bravot");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GROUNDED_GENTLIT_KEY = registerKey("grounded_gentlit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GROUNDED_BRAVOT_KEY = registerKey("grounded_bravot");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_LOVESHROOM_KEY = registerKey("giant_loveshroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_GLOWSHROOM_KEY = registerKey("giant_glowshroom");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_STRAWBERRY_KEY = registerKey("wild_strawberry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_HOOTNIP_KEY = registerKey("wild_hootnip");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINK_CHARMIL_GRASS_KEY = registerKey("pink_charmil_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_CHARMIL_GRASS_KEY = registerKey("blue_charmil_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINK_BLOOM_KEY = registerKey("pink_bloom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_BLOOM_KEY = registerKey("blue_bloom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LOVESHROOM_KEY = registerKey("loveshroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSHROOM_KEY = registerKey("glowshroom");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context){


        //Trees
        register(context, GENTLIT_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.GENTLIT_LOG.get()),
                new GentlitTrunkPlacer(18, 0, 8),
                BlockStateProvider.simple(ModBlocks.GENTLIT_LEAVES.get()),
                new PieFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());

        register(context, BRAVOT_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BRAVOT_LOG.get()),
                new BravotTrunkPlacer(10, 0, 6),
                BlockStateProvider.simple(ModBlocks.BRAVOT_LEAVES.get()),
                new BravotFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());


        //Grounded logs
        register(context, GROUNDED_GENTLIT_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.GENTLIT_LOG.get()),
                new GroundedTrunkPlacer(1, 0, 2),
                BlockStateProvider.simple(ModBlocks.GENTLIT_LEAVES.get()),
                new SphereFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());

        register(context, GROUNDED_BRAVOT_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BRAVOT_LOG.get()),
                new GroundedTrunkPlacer(1, 0, 2),
                BlockStateProvider.simple(ModBlocks.BRAVOT_LEAVES.get()),
                new SphereFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());

        //Mushroom trees
        register(context, GIANT_LOVESHROOM_KEY, ModFeatures.GIANT_LOVESHROOM_FEATURE.get(), new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(ModBlocks.LOVESHROOM_BLOCK.get()),
                BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)
                        .setValue(HugeMushroomBlock.DOWN, false)), 0));

        register(context, GIANT_GLOWSHROOM_KEY, ModFeatures.GIANT_GLOWSHROOM_FEATURE.get(), new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(ModBlocks.GLOWSHROOM_BLOCK.get()),
                BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)
                        .setValue(HugeMushroomBlock.DOWN, false)), 0));


        //Plant Patches
        register(context, WILD_STRAWBERRY_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                16, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_STRAWBERRY.get())))));

        register(context, WILD_HOOTNIP_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                16, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_HOOTNIP.get())))));

        register(context, PINK_CHARMIL_GRASS_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                128, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PINK_CHARMIL_GRASS.get())))));

        register(context, BLUE_CHARMIL_GRASS_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                128, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BLUE_CHARMIL_GRASS.get())))));

        register(context, PINK_BLOOM_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                32, 5, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PINK_BLOOM.get())))));

        register(context, BLUE_BLOOM_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                32, 5, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BLUE_BLOOM.get())))));

        register(context, LOVESHROOM_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                16, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.LOVESHROOM.get())))));

        register(context, GLOWSHROOM_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                16, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GLOWSHROOM.get())))));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(BuntsyMod.MODID, name));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
    BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
