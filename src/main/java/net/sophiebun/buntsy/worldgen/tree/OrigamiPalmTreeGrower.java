package net.sophiebun.buntsy.worldgen.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.sophiebun.buntsy.worldgen.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class OrigamiPalmTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean b) {
        return ModConfiguredFeatures.ORIGAMI_PALM_KEY;
    }
}
