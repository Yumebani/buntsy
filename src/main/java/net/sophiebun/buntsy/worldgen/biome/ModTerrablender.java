package net.sophiebun.buntsy.worldgen.biome;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.sophiebun.buntsy.BuntsyMod;
import terrablender.api.Regions;

public class ModTerrablender {

    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(BuntsyMod.MODID, "overworld_1"), 1));
    }
}
