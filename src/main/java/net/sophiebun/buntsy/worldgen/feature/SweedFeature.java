package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.plants.Sweeds;

public class SweedFeature extends Feature<NoneFeatureConfiguration> {

    public SweedFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        RandomSource randomsource = pContext.random();
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        return this.placeFeature(worldgenlevel, randomsource, blockpos);
    }

    protected boolean placeFeature(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos){

        int i = 0;

        for (BlockPos pos : BlockPos.withinManhattan(pPos, 3, 1, 3)){

            if (pLevel.getBlockState(pos).isAir() && ModBlocks.SWEEDS.get().canSurvive(pLevel.getBlockState(pos), pLevel, pos)){
                int startHeight = pRandom.nextInt(0, 3);
                for (int height = startHeight; height <= 4; height++){
                    pLevel.setBlock(pos.offset(0, height - startHeight, 0), ModBlocks.SWEEDS.get().defaultBlockState().setValue(Sweeds.HEIGHT, height), 2);
                }
                i++;
            }

            if (i >= 3) break;
        }

        return i > 0;
    }
}
