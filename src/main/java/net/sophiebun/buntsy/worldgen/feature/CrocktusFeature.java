package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.plants.Sweeds;

public class CrocktusFeature extends Feature<NoneFeatureConfiguration> {

    public CrocktusFeature(Codec<NoneFeatureConfiguration> pCodec) {
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
        while (pRandom.nextInt(0, (i + 1)) <= 5 - i){
            if (pLevel.getBlockState(pPos.above(i)).isAir() && ModBlocks.CROCKTUS.get().canSurvive(pLevel.getBlockState(pPos.above(i)), pLevel, pPos.above(i))){
                pLevel.setBlock(pPos.above(i), ModBlocks.CROCKTUS.get().defaultBlockState(), 2);
                i++;
            } else {
                break;
            }
        }

        return true;
    }
}
