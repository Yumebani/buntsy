package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class SweetPickleFeature extends Feature<CountConfiguration> {
    public SweetPickleFeature(Codec<CountConfiguration> p_66754_) {
        super(p_66754_);
    }

    public boolean place(FeaturePlaceContext<CountConfiguration> p_160316_) {
        int i = 0;
        RandomSource randomsource = p_160316_.random();
        WorldGenLevel worldgenlevel = p_160316_.level();
        BlockPos blockpos = p_160316_.origin();
        int j = p_160316_.config().count().sample(randomsource);

        for(int k = 0; k < j; ++k) {
            int l = randomsource.nextInt(8) - randomsource.nextInt(8);
            int i1 = randomsource.nextInt(8) - randomsource.nextInt(8);
            int j1 = worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockpos.getX() + l, blockpos.getZ() + i1);
            BlockPos blockpos1 = new BlockPos(blockpos.getX() + l, j1, blockpos.getZ() + i1);
            BlockState blockstate = ModBlocks.SWEET_PICKLE.get().defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(randomsource.nextInt(4) + 1));
            if (worldgenlevel.getBlockState(blockpos1).is(Blocks.WATER) && blockstate.canSurvive(worldgenlevel, blockpos1)) {
                worldgenlevel.setBlock(blockpos1, blockstate, 2);
                ++i;
            }
        }

        return i > 0;
    }
}
