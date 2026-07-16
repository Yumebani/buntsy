package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class HangingClockworkFeature extends HangingBlockFeature{

    public HangingClockworkFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public Block getBlock() {
        return ModBlocks.HANGING_CLOCKWORK.get();
    }
}
