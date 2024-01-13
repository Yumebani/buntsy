package net.sophiebun.buntsy.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class ModOverworldRegion extends Region {


    public ModOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        // Overlap Vanilla's parameters with our own for our COLD_BLUE biome.
        // The parameters for this biome are chosen arbitrarily.
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(ParameterUtils.Temperature.span(ParameterUtils.Temperature.NEUTRAL, ParameterUtils.Temperature.WARM))
                .humidity(ParameterUtils.Humidity.span(ParameterUtils.Humidity.NEUTRAL, ParameterUtils.Humidity.HUMID))
                .continentalness(ParameterUtils.Continentalness.MID_INLAND, ParameterUtils.Continentalness.FAR_INLAND)
                .erosion(ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_5))
                .depth(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.FLOOR)
                .weirdness(ParameterUtils.Weirdness.FULL_RANGE)
                .build().forEach(point -> builder.add(point, ModBiomes.CUTELY_BIOME));

        // Add our points to the mapper
        builder.build().forEach(mapper);
    }
}
