package net.sophiebun.buntsy.worldgen.biome.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;

public class ModSurfaceRules {

    private static final SurfaceRules.RuleSource CHARMIL_SOIL = makeStateRule(ModBlocks.CHARMIL_SOIL.get());
    private static final SurfaceRules.RuleSource SWEET_CORAL_SAND = makeStateRule(ModBlocks.SWEET_CORAL_SAND.get());
    private static final SurfaceRules.RuleSource CHARMIL_SOIL_PINK_FLUF = makeStateRule(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get());

    public static SurfaceRules.RuleSource makeRules()
    {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource isAboveGround = SurfaceRules.abovePreliminarySurface();

        SurfaceRules.RuleSource pinkGrassSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.CUTERLY_BIOME),
                        SurfaceRules.ifTrue(isAboveGround,
                                SurfaceRules.ifTrue(isAtOrAboveWaterLevel,
                                        SurfaceRules.sequence(
                                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, CHARMIL_SOIL_PINK_FLUF),
                                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, CHARMIL_SOIL)
                                        )))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.CUTERLY_BIOME),
                        SurfaceRules.ifTrue(isAboveGround,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SWEET_CORAL_SAND),
                                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SWEET_CORAL_SAND)))));

        SurfaceRules.RuleSource candyCragsSand = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.CANDY_CRAGS_BIOME),
                        SurfaceRules.ifTrue(isAboveGround,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SWEET_CORAL_SAND),
                                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SWEET_CORAL_SAND)))));

        return SurfaceRules.sequence(pinkGrassSurface, candyCragsSand);
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block)
    {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
