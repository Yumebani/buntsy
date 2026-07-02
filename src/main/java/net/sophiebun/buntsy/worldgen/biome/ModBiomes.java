package net.sophiebun.buntsy.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
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
    public static final ResourceKey<Biome> CANDY_CRAGS_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"candy_crags_biome"));
    public static final ResourceKey<Biome> POWDERY_TUNDRA_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"powdery_tundra_biome"));
    public static final ResourceKey<Biome> CHOCOLATE_SPRINGS_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"chocolate_springs_biome"));
    public static final ResourceKey<Biome> CLOCKWORK_CANOPY_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"clockwork_canopy_biome"));
    public static final ResourceKey<Biome> SWEET_OCEAN_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"sweet_ocean_biome"));
    public static final ResourceKey<Biome> WARM_SWEET_OCEAN_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"warm_sweet_ocean_biome"));
    public static final ResourceKey<Biome> COLD_SWEET_OCEAN_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"cold_sweet_ocean_biome"));
    public static final ResourceKey<Biome> ORIGAMI_BEACH_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"origami_beach_biome"));
    public static final ResourceKey<Biome> WARM_ORIGAMI_BEACH_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"warm_origami_beach_biome"));
    public static final ResourceKey<Biome> COLD_ORIGAMI_BEACH_BIOME = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(),
            new ResourceLocation(BuntsyMod.MODID,"cold_origami_beach_biome"));

    public static void bootstrap(BootstapContext<Biome> context){
        context.register(CUTERLY_BIOME, cutelyBiome(context));
        context.register(CANDY_CRAGS_BIOME, candyCragsBiome(context));
        context.register(POWDERY_TUNDRA_BIOME, powderyTundra(context));
        context.register(CHOCOLATE_SPRINGS_BIOME, chocolateSprings(context));
        context.register(CLOCKWORK_CANOPY_BIOME, clockworkCanopy(context));
        context.register(SWEET_OCEAN_BIOME, sweetOcean(context));
        context.register(WARM_SWEET_OCEAN_BIOME, warmSweetOcean(context));
        context.register(COLD_SWEET_OCEAN_BIOME, coldSweetOcean(context));
        context.register(ORIGAMI_BEACH_BIOME, origamiBeach(context));
        context.register(WARM_ORIGAMI_BEACH_BIOME, warmOrigamiBeach(context));
        context.register(COLD_ORIGAMI_BEACH_BIOME, coldOrigamiBeach(context));
    }

    public static void standardGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addDefaultSeagrass(builder);
    }

    private static Biome cutelyBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 10));

        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(ModEntities.SILKBUN_ENTITY.get(), 20, 2, 4));
        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(ModEntities.FAIRY_ENTITY.get(), 10, 2, 3));

        spawnBuilder.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Trees
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GENTLIT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BRAVOT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_LOVESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_GLOWSHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GROUNDED_GENTLIT_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GROUNDED_BRAVOT_PLACED_KEY);

        //Plants
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PINK_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLUE_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.LOVESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GLOWSHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WILD_STRAWBERRY_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WILD_HOOTNIP_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PINK_CHARMIL_GRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLUE_CHARMIL_GRASS_PLACED_KEY);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.CHARMING_LOTUS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BRAVE_LOTUS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MOD_CORAL_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEETGRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEET_PICKLE_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COTTON_VINE_PLACED_KEY);

        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.7f)
                .downfall(0.8f)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x45adf2)
                        .waterFogColor(0x08263D)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2)
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();

        return biome;
    }

    private static Biome candyCragsBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(ModEntities.FAIRY_ENTITY.get(), 20, 2, 3));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Candy crag
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.CANDY_CRAG_PILE_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.CANDY_BOULDER_PLACED_KEY);

        //Trees
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_LOVESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_GLOWSHROOM_PLACED_KEY);

        //Plants
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.LOVESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GLOWSHROOM_PLACED_KEY);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MOD_CORAL_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEETGRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEET_PICKLE_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COTTON_VINE_PLACED_KEY);

        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.8f)
                .temperature(1f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x45adf2)
                        .waterFogColor(0x08263D)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2)
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome powderyTundra(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Plants
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FROZEN_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FROZEN_GRASS_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.SWICE_SPIKE_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.CRYSTALIZED_TREE_PLACED_KEY);


        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(-0.2f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x7DEBFA)
                        .waterFogColor(0x196069)
                        .skyColor(0x94B9EB)
                        .grassColorOverride(0xB2E5FF)
                        .foliageColorOverride(0x4D98A7)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome chocolateSprings(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.CHOCOLATE_SPRING_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.CHOCOLATE_GEYSER_PLACED_KEY);

        //Plants
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FROZEN_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FROZEN_GRASS_PLACED_KEY);

        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(-0.2f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x7DEBFA)
                        .waterFogColor(0x196069)
                        .skyColor(0x94B9EB)
                        .grassColorOverride(0xB2E5FF)
                        .foliageColorOverride(0x4D98A7)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome clockworkCanopy(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.CLOCKWORK_MAIDEN_ENTITY.get(), 20, 2, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 40, 2, 4));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Ground
        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacedFeatures.MUD_PATCH_PLACED_KEY);

        //Plants
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MALVOR_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GIANT_PALESHROOM_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.GROUNDED_MALVOR_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.LUMINUM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALESHROOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ABYSSAL_BLOOM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALEGRASS_PLACED_KEY);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MALIUM_LOTUS_PLACED_KEY);

        //Finalizing
        biomeBuilder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModPlacedFeatures.HANGING_CLOCKWORK_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModPlacedFeatures.HANGING_LUMINUM_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModPlacedFeatures.HANGING_STRING_PLACED_KEY);

        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(-0.2f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x79A9C9)
                        .waterFogColor(0x243145)
                        .skyColor(0x94B9EB)
                        .grassColorOverride(0xA39CB5)
                        .foliageColorOverride(0x8CADA2)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome sweetOcean(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        BiomeDefaultFeatures.oceanSpawns(spawnBuilder, 10, 3, 15);

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEETGRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COTTON_VINE_PLACED_KEY);


        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.2f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x45adf2)
                        .waterFogColor(0x08263D)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2)
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome warmSweetOcean(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        spawnBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
        spawnBuilder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 5, 2, 4));
        spawnBuilder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MOD_CORAL_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEETGRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEET_PICKLE_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COTTON_VINE_PLACED_KEY);


        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x59ECF7)
                        .waterFogColor(0x0A3E4D)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2)
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome coldSweetOcean(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        BiomeDefaultFeatures.oceanSpawns(spawnBuilder, 10, 3, 15);

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x7DEBFA)
                        .waterFogColor(0x196069)
                        .skyColor(0x94B9EB)
                        .grassColorOverride(0xB2E5FF)
                        .foliageColorOverride(0x4D98A7)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome origamiBeach(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        BiomeDefaultFeatures.oceanSpawns(spawnBuilder, 10, 3, 15);

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Rocks
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.SWEET_LIMESTONE_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ORIGAMI_PALM_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SEASHELLS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ORIGAMI_FERN_PLACED_KEY);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEETGRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COTTON_VINE_PLACED_KEY);


        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.2f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x45adf2)
                        .waterFogColor(0x08263D)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2)
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome warmOrigamiBeach(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        spawnBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
        spawnBuilder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 5, 2, 4));
        spawnBuilder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Rocks
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.SUNNY_LIMESTONE_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ORIGAMI_PALM_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SEASHELLS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ORIGAMI_FERN_PLACED_KEY);

        //Ocean Stuff
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MOD_CORAL_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEETGRASS_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SWEET_PICKLE_PLACED_KEY);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COTTON_VINE_PLACED_KEY);


        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x59ECF7)
                        .waterFogColor(0x0A3E4D)
                        .skyColor(0x6eb1ff)
                        .grassColorOverride(0xffb7b2)
                        .foliageColorOverride(0x71a74d)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }

    private static Biome coldOrigamiBeach(BootstapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        BiomeDefaultFeatures.oceanSpawns(spawnBuilder, 10, 3, 15);

        standardGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //Rocks
        biomeBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.FROZEN_LIMESTONE_PLACED_KEY);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SEASHELLS_PLACED_KEY);

        Biome biome = new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x7DEBFA)
                        .waterFogColor(0x196069)
                        .skyColor(0x94B9EB)
                        .grassColorOverride(0xB2E5FF)
                        .foliageColorOverride(0x4D98A7)
                        .fogColor(0xc0d8ff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();

        return biome;
    }
}
