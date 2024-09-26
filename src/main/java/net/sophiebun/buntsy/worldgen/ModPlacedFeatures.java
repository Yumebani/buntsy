package net.sophiebun.buntsy.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ModPlacedFeatures {

    public static ResourceKey<PlacedFeature> GENTLIT_PLACED_KEY = registerKey("gentlit_placed");
    public static ResourceKey<PlacedFeature> BRAVOT_PLACED_KEY = registerKey("bravot_placed");

    public static ResourceKey<PlacedFeature> GROUNDED_GENTLIT_PLACED_KEY = registerKey("grounded_gentlit_placed");
    public static ResourceKey<PlacedFeature> GROUNDED_BRAVOT_PLACED_KEY = registerKey("grounded_bravot_placed");

    public static final ResourceKey<PlacedFeature> GIANT_LOVESHROOM_PLACED_KEY = registerKey("giant_loveshroom_placed");
    public static final ResourceKey<PlacedFeature> GIANT_GLOWSHROOM_PLACED_KEY = registerKey("giant_glowshroom_placed");

    public static final ResourceKey<PlacedFeature> WILD_STRAWBERRY_PLACED_KEY = registerKey("wild_strawberry_placed");
    public static final ResourceKey<PlacedFeature> WILD_HOOTNIP_PLACED_KEY = registerKey("wild_hootnip_placed");
    public static final ResourceKey<PlacedFeature> PINK_CHARMIL_GRASS_PLACED_KEY = registerKey("pink_charmil_grass_placed");
    public static final ResourceKey<PlacedFeature> BLUE_CHARMIL_GRASS_PLACED_KEY = registerKey("blue_charmil_grass_placed");
    public static final ResourceKey<PlacedFeature> PINK_BLOOM_PLACED_KEY = registerKey("pink_bloom_placed");
    public static final ResourceKey<PlacedFeature> BLUE_BLOOM_PLACED_KEY = registerKey("blue_bloom_placed");
    public static final ResourceKey<PlacedFeature> LOVESHROOM_PLACED_KEY = registerKey("loveshroom_placed");
    public static final ResourceKey<PlacedFeature> GLOWSHROOM_PLACED_KEY = registerKey("glowshroom_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        //Trees
        register(context, GENTLIT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GENTLIT_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(6, 0.1f, 2),
                        ModBlocks.GENTLIT_SAPLING.get()));
        register(context, BRAVOT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BRAVOT_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1f, 2),
                        ModBlocks.BRAVOT_SAPLING.get()));

        //Grounded logs
        register(context, GROUNDED_GENTLIT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GROUNDED_GENTLIT_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.5f, 2),
                        ModBlocks.GENTLIT_SAPLING.get()));
        register(context, GROUNDED_BRAVOT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GROUNDED_BRAVOT_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.5f, 2),
                        ModBlocks.BRAVOT_SAPLING.get()));

        //Mushroom trees
        register(context, GIANT_LOVESHROOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_LOVESHROOM_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.5f, 2),
                        ModBlocks.LOVESHROOM.get()));
        register(context, GIANT_GLOWSHROOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_GLOWSHROOM_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.5f, 2),
                        ModBlocks.GLOWSHROOM.get()));

        //Plants
        register(context, WILD_STRAWBERRY_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.WILD_STRAWBERRY_KEY),
                List.of(PlacementUtils.countExtra(0, 0.025f, 6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        register(context, WILD_HOOTNIP_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.WILD_HOOTNIP_KEY),
                List.of(PlacementUtils.countExtra(0, 0.025f, 6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

        register(context, PINK_CHARMIL_GRASS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PINK_CHARMIL_GRASS_KEY),
                List.of(CountPlacement.of(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        register(context, BLUE_CHARMIL_GRASS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLUE_CHARMIL_GRASS_KEY),
                List.of(CountPlacement.of(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

        register(context, PINK_BLOOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PINK_BLOOM_KEY),
                List.of(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        register(context, BLUE_BLOOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLUE_BLOOM_KEY),
                List.of(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

        register(context, LOVESHROOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LOVESHROOM_KEY),
                List.of(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        register(context, GLOWSHROOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GLOWSHROOM_KEY),
                List.of(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(BuntsyMod.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
