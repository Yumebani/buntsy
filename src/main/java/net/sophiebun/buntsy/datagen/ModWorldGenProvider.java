package net.sophiebun.buntsy.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.ModBiomeModifiers;
import net.sophiebun.buntsy.worldgen.ModConfiguredFeatures;
import net.sophiebun.buntsy.worldgen.ModPlacedFeatures;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;
import net.sophiebun.buntsy.worldgen.dimension.BuntsyDimension;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, BuntsyDimension::bootstrapType)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.LEVEL_STEM, BuntsyDimension::bootstrapStem);
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(BuntsyMod.MODID));
    }
}
