package net.sophiebun.buntsy.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.feature.ModFeatures;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.GentlitTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.PieFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.grounded_trunk.GroundedTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.grounded_trunk.SphereFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.malvor.MalvorFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.malvor.MalvorTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.origami_palm.OrigamiPalmFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.origami_palm.OrigamiPalmTrunkPlacer;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> GENTLIT_KEY = registerKey("gentlit_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BRAVOT_KEY = registerKey("bravot_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MALVOR_KEY = registerKey("malvor_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORIGAMI_PALM_KEY = registerKey("origami_palm_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTALIZED_TREE_KEY = registerKey("crystalized_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GROUNDED_GENTLIT_KEY = registerKey("grounded_gentlit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GROUNDED_BRAVOT_KEY = registerKey("grounded_bravot");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GROUNDED_MALVOR_KEY = registerKey("grounded_malvor");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_LOVESHROOM_KEY = registerKey("giant_loveshroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_GLOWSHROOM_KEY = registerKey("giant_glowshroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_PALESHROOM_KEY = registerKey("giant_paleshroom");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CHARMING_LOTUS_KEY = registerKey("charming_lotus_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BRAVE_LOTUS_KEY = registerKey("brave_lotus_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MALIUM_LOTUS_KEY = registerKey("malium_lotus_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SWEETGRASS_KEY = registerKey("sweetgrass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SWEET_PICKLE_KEY = registerKey("sweet_pickle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> COTTON_VINE_KEY = registerKey("cotton_vine");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOD_CORAL_KEY = registerKey("mod_coral_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORAL_SAND_NEAR_WATER_KEY = registerKey("coral_sand_near_water_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CANDY_CRAG_PILE_KEY = registerKey("candy_crag_pile_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CANDY_BOULDER_KEY = registerKey("candy_boulder_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SWICE_SPIKE_KEY = registerKey("swice_spike_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MUD_PATCH_KEY = registerKey("mud_patch_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HANGING_STRING_KEY = registerKey("hanging_string_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HANGING_CLOCKWORK_KEY = registerKey("hanging_clockwork_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HANGING_LUMINUM_KEY = registerKey("hanging_luminum_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SWEET_LIMESTONE_KEY = registerKey("sweet_limestone_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SUNNY_LIMESTONE_KEY = registerKey("sunny_limestone_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FROZEN_LIMESTONE_KEY = registerKey("frozen_limestone_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_STRAWBERRY_KEY = registerKey("wild_strawberry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_HOOTNIP_KEY = registerKey("wild_hootnip");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINK_CHARMIL_GRASS_KEY = registerKey("pink_charmil_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_CHARMIL_GRASS_KEY = registerKey("blue_charmil_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINK_BLOOM_KEY = registerKey("pink_bloom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_BLOOM_KEY = registerKey("blue_bloom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LOVESHROOM_KEY = registerKey("loveshroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSHROOM_KEY = registerKey("glowshroom");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALESHROOM_KEY = registerKey("paleshroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_BLOOM_KEY = registerKey("abyssal_bloom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALEGRASS_KEY = registerKey("palegrass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMINUM_KEY = registerKey("luminum");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FROZEN_GRASS_KEY = registerKey("frozen_grass_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FROZEN_BLOOM_KEY = registerKey("frozen_bloom_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_KEY = registerKey("freeze_top_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORIGAMI_FERN_KEY = registerKey("origami_fern_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SEASHELLS_KEY = registerKey("seashells_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CHOCOLATE_GEYSER_KEY = registerKey("chocolate_geyser_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CHOCOLATE_SPRING_KEY = registerKey("chocolate_spring_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CHARMIL_BONEMEAL_KEY = registerKey("charmil_bonemeal_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ODIATE_BONEMEAL_KEY = registerKey("odiate_bonemeal_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CLOCKWORK_STRUCTURES_KEY = registerKey("clockwork_structures_key");

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

        register(context, MALVOR_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.MALVOR_LOG.get()),
                new MalvorTrunkPlacer(16, 0, 4),
                BlockStateProvider.simple(ModBlocks.MALVOR_LEAVES.get()),
                new MalvorFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());

        register(context, ORIGAMI_PALM_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.ORIGAMI_PALM_LOG.get()),
                new OrigamiPalmTrunkPlacer(6, 0, 4),
                BlockStateProvider.simple(ModBlocks.ORIGAMI_PALM_LEAVES.get()),
                new OrigamiPalmFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());

        register(context, CRYSTALIZED_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CRYSTALLIZED_LOG.get()),
                new BravotTrunkPlacer(10, 0, 6),
                BlockStateProvider.simple(ModBlocks.CRYSTALLIZED_LEAVES.get()),
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

        register(context, GROUNDED_MALVOR_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.MALVOR_LOG.get()),
                new GroundedTrunkPlacer(1, 0, 2),
                BlockStateProvider.simple(ModBlocks.MALVOR_LEAVES.get()),
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

        register(context, GIANT_PALESHROOM_KEY, ModFeatures.GIANT_GLOWSHROOM_FEATURE.get(), new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(ModBlocks.PALESHROOM_BLOCK.get()),
                BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)
                        .setValue(HugeMushroomBlock.DOWN, false)), 0));

        //Lotus
        register(context, CHARMING_LOTUS_KEY, ModFeatures.WATER_PATCH_FEATURE.get(), new RandomPatchConfiguration(
                5, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CHARMING_LOTUS.get())))));

        register(context, BRAVE_LOTUS_KEY, ModFeatures.WATER_PATCH_FEATURE.get(), new RandomPatchConfiguration(
                5, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BRAVE_LOTUS.get())))));

        register(context, MALIUM_LOTUS_KEY, ModFeatures.WATER_PATCH_FEATURE.get(), new RandomPatchConfiguration(
                8, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MALIUM_LOTUS.get())))));

        //Sea Stuff Patches
        register(context, SWEETGRASS_KEY, ModFeatures.SWEETGRASS_FEATURE.get(), new ProbabilityFeatureConfiguration(0.4F));
        register(context, SWEET_PICKLE_KEY, ModFeatures.SWEETPICKLE_FEATURE.get(), new CountConfiguration(20));
        register(context, COTTON_VINE_KEY, ModFeatures.COTTON_VINE_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, CORAL_SAND_NEAR_WATER_KEY, ModFeatures.CORAL_SAND_NEAR_WATER.get(), new NoneFeatureConfiguration());
        register(context, MOD_CORAL_KEY, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(
                HolderSet.direct(PlacementUtils.inlinePlaced(ModFeatures.MOD_CORAL_MUSHROOM.get(), FeatureConfiguration.NONE),
                        PlacementUtils.inlinePlaced(ModFeatures.MOD_CORAL_TREE.get(), FeatureConfiguration.NONE),
                        PlacementUtils.inlinePlaced(ModFeatures.MOD_CORAL_CLAW.get(), FeatureConfiguration.NONE))));

        //Candy crag
        register(context, CANDY_CRAG_PILE_KEY, ModFeatures.CANDY_CRAG_PILE.get(), new NoneFeatureConfiguration());
        register(context, CANDY_BOULDER_KEY, ModFeatures.CANDY_BOULDER.get(), new NoneFeatureConfiguration());

        //Other
        register(context, CLOCKWORK_STRUCTURES_KEY, ModFeatures.CLOCKWORK_STRUCTURE_FEATURE.get(), new NoneFeatureConfiguration());

        register(context, FREEZE_TOP_KEY, ModFeatures.SNOW_FREEZE_FEATURE.get(), new NoneFeatureConfiguration());

        register(context, MUD_PATCH_KEY, ModFeatures.MUD_PATCH_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, SWICE_SPIKE_KEY, ModFeatures.SWICE_SPIKE_FEATURE.get(), new NoneFeatureConfiguration());

        register(context, CHOCOLATE_GEYSER_KEY, ModFeatures.CHOCOLATE_GEYSER_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, CHOCOLATE_SPRING_KEY, ModFeatures.CHOCOLATE_SPRING_FEATURE.get(), new NoneFeatureConfiguration());

        //Hanging string
        register(context, HANGING_STRING_KEY, ModFeatures.HANGING_STRING_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, HANGING_CLOCKWORK_KEY, ModFeatures.HANGING_CLOCKWORK_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, HANGING_LUMINUM_KEY, ModFeatures.HANGING_LUMINUM_FEATURE.get(), new NoneFeatureConfiguration());

        //Limestone
        register(context, SUNNY_LIMESTONE_KEY, ModFeatures.LIMESTONE_FEATURE.get(), new BlockStateConfiguration(
                ModBlocks.SUNNY_LIMESTONE.get().defaultBlockState()));
        register(context, SWEET_LIMESTONE_KEY, ModFeatures.LIMESTONE_FEATURE.get(), new BlockStateConfiguration(
                ModBlocks.SWEET_LIMESTONE.get().defaultBlockState()));
        register(context, FROZEN_LIMESTONE_KEY, ModFeatures.LIMESTONE_FEATURE.get(), new BlockStateConfiguration(
                ModBlocks.FROZEN_LIMESTONE.get().defaultBlockState()));

        //Bonemeal Patches
        register(context, CHARMIL_BONEMEAL_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                20, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(new WeightedStateProvider(
                        SimpleWeightedRandomList.<BlockState>builder()
                                .add(ModBlocks.PINK_CHARMIL_GRASS.get().defaultBlockState(), 20)
                                .add(ModBlocks.BLUE_CHARMIL_GRASS.get().defaultBlockState(), 20)
                                .add(ModBlocks.PINK_BLOOM.get().defaultBlockState(), 5)
                                .add(ModBlocks.BLUE_BLOOM.get().defaultBlockState(), 5)
                                .add(ModBlocks.LOVESHROOM.get().defaultBlockState(), 3)
                                .add(ModBlocks.GLOWSHROOM.get().defaultBlockState(), 3)
                                .build())))));

        register(context, ODIATE_BONEMEAL_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                20, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(new WeightedStateProvider(
                        SimpleWeightedRandomList.<BlockState>builder()
                                .add(ModBlocks.PALEGRASS.get().defaultBlockState(), 20)
                                .add(ModBlocks.ABYSSAL_BLOOM.get().defaultBlockState(), 5)
                                .add(ModBlocks.PALESHROOM.get().defaultBlockState(), 3)
                                .build())))));

        //Plant Patches
        register(context, WILD_STRAWBERRY_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_STRAWBERRY.get())))));

        register(context, WILD_HOOTNIP_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_HOOTNIP.get())))));

        register(context, PINK_CHARMIL_GRASS_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                48, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PINK_CHARMIL_GRASS.get())))));

        register(context, BLUE_CHARMIL_GRASS_KEY, Feature.FLOWER, new RandomPatchConfiguration(
                48, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BLUE_CHARMIL_GRASS.get())))));

        register(context, PALEGRASS_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                128, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PALEGRASS.get())))));

        register(context, FROZEN_GRASS_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                128, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.FROZEN_GRASS.get())))));

        register(context, LUMINUM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.LUMINUM.get())))));

        register(context, PINK_BLOOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                32, 7, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PINK_BLOOM.get())))));

        register(context, BLUE_BLOOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                32, 7, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BLUE_BLOOM.get())))));

        register(context, ABYSSAL_BLOOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                32, 7, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ABYSSAL_BLOOM.get())))));

        register(context, FROZEN_BLOOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 7, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.FROZEN_BLOOM.get())))));

        register(context, ORIGAMI_FERN_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 7, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ORIGAMI_FERN.get())))));

        register(context, SEASHELLS_KEY, ModFeatures.BLOCK_PATCH_FEATURE.get(), new BlockStateConfiguration(
                ModBlocks.SEA_SHELLS.get().defaultBlockState()));

        register(context, LOVESHROOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.LOVESHROOM.get())))));

        register(context, GLOWSHROOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GLOWSHROOM.get())))));

        register(context, PALESHROOM_KEY, ModFeatures.PATCH_FEATURE.get(), new RandomPatchConfiguration(
                16, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.PALESHROOM.get())))));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(BuntsyMod.MODID, name));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
    BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
