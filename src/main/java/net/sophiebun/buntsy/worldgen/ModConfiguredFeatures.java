package net.sophiebun.buntsy.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.bravot.SquishedBlobFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.GentlitTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.PieFoliagePlacer;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> GENTLIT_KEY = registerKey("gentlit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BRAVOT_KEY = registerKey("bravot");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context){

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
                new SquishedBlobFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0,2)).build());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(BuntsyMod.MODID, name));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
    BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
