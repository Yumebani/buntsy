package net.sophiebun.buntsy.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.ModPlacedFeatures;

public class ModBiomes {

    public static final ResourceKey<Biome> CUTELY_BIOME = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(BuntsyMod.MODID,"cutely_biome"));

    public static void bootstrap(BootstapContext<Biome> context){
        context.register(CUTELY_BIOME, cutelyBiome(context));
    }

    public static void cutelyGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
    }

    private static Biome cutelyBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        //spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.RHINO.get(), 2, 3, 5));

        //spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));

        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        //BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        cutelyGeneration(biomeBuilder);
        BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GENTLIT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BRAVOT_PLACED_KEY);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x45adf2)
                        .waterFogColor(0x041633)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2) //b6db61 green - ffd7d4 pink - f0abe1 purple - ffe27c yellow
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
}
