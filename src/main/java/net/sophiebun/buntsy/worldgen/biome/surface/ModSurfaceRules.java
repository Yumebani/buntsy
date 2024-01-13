package net.sophiebun.buntsy.worldgen.biome.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;

public class ModSurfaceRules {

    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource PINK_GRASS_BLOCK = makeStateRule(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get());

    public static SurfaceRules.RuleSource makeRules()
    {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.RuleSource pinkGrassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, PINK_GRASS_BLOCK), DIRT);

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.CUTELY_BIOME),
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, pinkGrassSurface))
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block)
    {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
