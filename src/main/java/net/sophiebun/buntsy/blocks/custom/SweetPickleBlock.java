package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class SweetPickleBlock extends SeaPickleBlock {
    public SweetPickleBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        if (!isDead(pState) && pLevel.getBlockState(pPos.below()).is(BlockTags.CORAL_BLOCKS)) {
            int i = 5;
            int j = 1;
            int k = 2;
            int l = 0;
            int i1 = pPos.getX() - 2;
            int j1 = 0;

            for(int k1 = 0; k1 < 5; ++k1) {
                for(int l1 = 0; l1 < j; ++l1) {
                    int i2 = 2 + pPos.getY() - 1;

                    for(int j2 = i2 - 2; j2 < i2; ++j2) {
                        BlockPos blockpos = new BlockPos(i1 + k1, j2, pPos.getZ() - j1 + l1);
                        if (blockpos != pPos && pRandom.nextInt(6) == 0 && pLevel.getBlockState(blockpos).is(Blocks.WATER)) {
                            BlockState blockstate = pLevel.getBlockState(blockpos.below());
                            if (blockstate.is(BlockTags.CORAL_BLOCKS)) {
                                pLevel.setBlock(blockpos, ModBlocks.SWEET_PICKLE.get().defaultBlockState().setValue(PICKLES, Integer.valueOf(pRandom.nextInt(4) + 1)), 3);
                            }
                        }
                    }
                }

                if (l < 2) {
                    j += 2;
                    ++j1;
                } else {
                    j -= 2;
                    --j1;
                }

                ++l;
            }

            pLevel.setBlock(pPos, pState.setValue(PICKLES, Integer.valueOf(4)), 2);
        }

    }
}
