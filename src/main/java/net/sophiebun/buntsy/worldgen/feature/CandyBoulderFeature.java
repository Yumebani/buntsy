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

import java.util.ArrayList;
import java.util.List;

public class CandyBoulderFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockState SWEET = ModBlocks.SWEET_CANDY_ROCK.get().defaultBlockState();
    private static final BlockState SOUR = ModBlocks.SOUR_CANDY_ROCK.get().defaultBlockState();
    private static final BlockState BITTER = ModBlocks.BITTER_CANDY_ROCK.get().defaultBlockState();

    private static final BlockState[] BLOCK_RANGE = {SWEET, SOUR, BITTER};
    public CandyBoulderFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        RandomSource randomsource = pContext.random();
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        return this.placeFeature(worldgenlevel, randomsource, blockpos);
    }

    protected boolean placeFeature(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos){

        if (pLevel.getBlockState(pPos).isAir()){

            int size = pRandom.nextInt(1, 3);
            BlockState blockToPlace = BLOCK_RANGE[pRandom.nextInt(0, 3)];

            for (int y = -size; y <= size; y++){
                for (int x = -size; x <= size; x++){
                    for (int z = -size; z <= size; z++){
                        if (isInSphere(size, x, y, z) && pLevel.getBlockState(pPos.offset(x, y, z)).isAir()){
                            pLevel.setBlock(pPos.offset(x, y, z), blockToPlace, 3);
                        }
                    }
                }
            }

            return true;
        }
        else return false;
    }

    private boolean isInSphere(float radius, int x, int y, int z){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) <= radius;
    }
}
