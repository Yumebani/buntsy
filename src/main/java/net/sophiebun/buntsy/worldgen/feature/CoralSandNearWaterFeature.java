package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.Optional;

public class CoralSandNearWaterFeature extends Feature<NoneFeatureConfiguration> {
    public CoralSandNearWaterFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        RandomSource randomsource = pContext.random();
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        return this.placeFeature(worldgenlevel, randomsource, blockpos);
    }

    protected boolean placeFeature(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos){
        for (int x = -10; x <= 10; x++){
            for (int z = -10; z <= 10; z++){
                for (int y = -3; y <= 3; y++){
                    BlockPos newPos = pPos.relative(Direction.Axis.X, x).relative(Direction.Axis.Y, y).relative(Direction.Axis.Z, z);
                    if (pLevel.getBlockState(newPos).is(BlockTags.DIRT) && canPlaceSand(pLevel, pRandom, newPos)){
                        setBlock(pLevel, newPos, ModBlocks.SWEET_CORAL_SAND.get().defaultBlockState());
                    }
                }
            }
        }

        return true;
    }

    protected boolean canPlaceSand(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos) {
        for(BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-3, 0, -3), pPos.offset(3, 1, 3))) {
            if (pLevel.getBlockState(blockpos).is(Blocks.WATER)){
                return true;
            }
        }
        return false;
    }
}
