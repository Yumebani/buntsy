package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class SweetgrassBlock extends SeagrassBlock {
    public SweetgrassBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void performBonemeal(ServerLevel p_222423_, RandomSource p_222424_, BlockPos p_222425_, BlockState p_222426_) {
        BlockState blockstate = ModBlocks.TALL_SWEETGRASS.get().defaultBlockState();
        BlockState blockstate1 = blockstate.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
        BlockPos blockpos = p_222425_.above();
        if (p_222423_.getBlockState(blockpos).is(Blocks.WATER)) {
            p_222423_.setBlock(p_222425_, blockstate, 2);
            p_222423_.setBlock(blockpos, blockstate1, 2);
        }

    }
}
