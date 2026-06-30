package net.sophiebun.buntsy.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.OptionalLong;

public class BuntsyDimension {

    public static final ResourceKey<LevelStem> BUNTSY_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(BuntsyMod.MODID, "buntsydim"));
    public static final ResourceKey<Level> BUNTSY_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(BuntsyMod.MODID, "buntsydim"));
    public static final ResourceKey<DimensionType> BUNTSY_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(BuntsyMod.MODID, "buntsydim"));


    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(BUNTSY_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -64, // minY
                384, // height
                384, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        Holder.Reference<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS).getOrThrow(NoiseGeneratorSettings.OVERWORLD);

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(-0.2f, 0.5f),
                                                Climate.Parameter.span(-0.5f, 0.5f),
                                                Climate.Parameter.span(-0.2f, 0.4f),
                                                Climate.Parameter.span(-0.5f, 0.5f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(0.0f, 1.0f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.CUTERLY_BIOME)),
                                Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(0.4f, 1f),
                                                Climate.Parameter.span(-1f, 0f),
                                                Climate.Parameter.span(0.25f, 1f),
                                                Climate.Parameter.span(-0.2f, 1.0f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(-1f, 1f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.CANDY_CRAGS_BIOME)),
                                Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(-1f, -0.2f),
                                                Climate.Parameter.span(-1f, 1f),
                                                Climate.Parameter.span(-0.2f, 1f),
                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(-1f, 0.2f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.POWDERY_TUNDRA_BIOME)),
                                Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(0.2f, 0.8f),
                                                Climate.Parameter.span(-0.2f, 1f),
                                                Climate.Parameter.span(-0.2f, 1f),
                                                Climate.Parameter.span(-1.0f, 0.5f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(-1f, 1f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.CLOCKWORK_CANOPY_BIOME)),
                                Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(-0.5f, 0.5f),
                                                Climate.Parameter.span(-1f, 1f),
                                                Climate.Parameter.span(-1f, -0.3f),
                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(-1f, 1f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.SWEET_OCEAN_BIOME)),
                                Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(0.5f, 1f),
                                                Climate.Parameter.span(-1f, 1f),
                                                Climate.Parameter.span(-1f, -0.3f),
                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(-1f, 1f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.WARM_SWEET_OCEAN_BIOME)),
                                Pair.of(
                                        Climate.parameters(
                                                Climate.Parameter.span(-1f, -0.5f),
                                                Climate.Parameter.span(-1f, 1f),
                                                Climate.Parameter.span(-1f, -0.3f),
                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                Climate.Parameter.point(0f),
                                                Climate.Parameter.span(-1f, 1f),
                                                0f), biomeRegistry.getOrThrow(ModBiomes.COLD_SWEET_OCEAN_BIOME))
                                ))),
                noiseGenSettings);

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(BuntsyDimension.BUNTSY_DIM_TYPE), noiseBasedChunkGenerator);

        context.register(BUNTSY_KEY, stem);
    }
}
