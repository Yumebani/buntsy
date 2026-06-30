package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;

public class SwiceSpikeFeature extends Feature<NoneFeatureConfiguration> {

    public SwiceSpikeFeature(Codec<NoneFeatureConfiguration > pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();

        int size = randomsource.nextInt(2, 4);
        int baseSize = size - 1;
        int height = randomsource.nextInt(2, 6);

        for (int x = -baseSize; x <= baseSize; x++){
            for (int z = -baseSize; z <= baseSize; z++){
                for (int y = -2; y <= height; y++) {
                    BlockState state = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
                    if (isInRadius2D(baseSize, x + (Math.abs(Math.min(0, y))), z + (Math.abs(Math.min(0, y)))) && (state.isAir() || state.is(ModTags.Blocks.FREEZWEET_BLOCKS))) {
                        worldgenlevel.setBlock(blockpos.offset(x, y, z), ModBlocks.SWICE.get().defaultBlockState(), 2);
                    }
                }
            }
        }

        for (int x = -size; x <= size; x++){
            for (int z = -size; z <= size; z++){
                for (int y = 0; y <= size * 5; y++) {
                    BlockState state = worldgenlevel.getBlockState(blockpos.offset(x, y + height, z));
                    if (isInRadius2D(size * 5, (x * 5) + (y * (x < 0 ? -1 : 1)), (z * 5) + (y * (z < 0 ? -1 : 1))) && (state.isAir() || state.is(ModTags.Blocks.FREEZWEET_BLOCKS))) {
                        worldgenlevel.setBlock(blockpos.offset(x, y + height, z), ModBlocks.SWICE.get().defaultBlockState(), 2);
                    }
                }
            }
        }

        return true;
    }

    private boolean isInRadius2D(int radius, int x, int z){
        return Math.pow(x, 2) + Math.pow(z, 2) <= Math.pow(radius, 2);
    }
}
