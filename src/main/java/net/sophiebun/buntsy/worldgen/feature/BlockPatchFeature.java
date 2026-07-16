package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.sophiebun.buntsy.blocks.custom.plants.ModLeaves;

public class BlockPatchFeature extends Feature<BlockStateConfiguration> {

    public BlockPatchFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> config) {
        RandomSource randomsource = config.random();
        BlockPos blockpos = config.origin();
        WorldGenLevel worldgenlevel = config.level();
        BlockState placeState = config.config().state;

        int y = 0;
        BlockState state = worldgenlevel.getBlockState(blockpos.offset(0, y, 0). below());
        while (state.isAir() || (state.getBlock() instanceof ModLeaves)){
            y--;
            state = worldgenlevel.getBlockState(blockpos.offset(0, y, 0));
        }

        int i = 0;
        int j = 6;
        int k = 1;

        for(int l = 0; l < 24; ++l) {
            BlockPos pos = blockpos.offset(randomsource.nextInt(j) - randomsource.nextInt(j), y + randomsource.nextInt(k) - randomsource.nextInt(k), randomsource.nextInt(j) - randomsource.nextInt(j));
            BlockState state1 =  worldgenlevel.getBlockState(pos.below());
            if (state1.getBlock().isCollisionShapeFullBlock(state1, worldgenlevel, pos.below()) && worldgenlevel.getBlockState(pos).isAir()){
                worldgenlevel.setBlock(pos, placeState, 2);
                ++i;
            }
        }

        return i > 0;
    }
}
