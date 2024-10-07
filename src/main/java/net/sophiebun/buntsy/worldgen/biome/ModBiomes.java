package net.sophiebun.buntsy.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.item.custom.FairyStaff;
import net.sophiebun.buntsy.worldgen.ModPlacedFeatures;

import javax.swing.*;

public class ModBiomes {

    public static final DeferredRegister<Biome> biomeRegister =
            DeferredRegister.create(ForgeRegistries.BIOMES, BuntsyMod.MODID);

    public static final ResourceKey<Biome> CUTERLY_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"cuterly_biome"));

    public static void bootstrap(BootstapContext<Biome> context){
        context.register(CUTERLY_BIOME, cutelyBiome(context));
    }

    public static void cuterlyGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
    }

    private static Biome cutelyBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.creatureGenerationProbability(10);

        MobCategory cat = MobCategory.create("ambient", "custom", 10, true, true, 128);

        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(ModEntities.SILKBUN_ENTITY.get(), 1, 2, 3));
        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(ModEntities.FAIRY_ENTITY.get(), 1, 2, 3));

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        cuterlyGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Trees
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GENTLIT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BRAVOT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GROUNDED_GENTLIT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GROUNDED_BRAVOT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_LOVESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_GLOWSHROOM_PLACED_KEY);

        //Plants
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PINK_CHARMIL_GRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLUE_CHARMIL_GRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PINK_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLUE_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.LOVESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GLOWSHROOM_PLACED_KEY);

        Biome biome = new Biome.BiomeBuilder()
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

        //biomeRegister.register("cuterly_biome", () -> biome);

        return biome;
    }
}
