package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;

public class WinterRootsCrop extends CropBlock {

    public WinterRootsCrop(Properties pProperties) {
        super(pProperties);
    }

    protected static float getGrowthSpeed(Block pBlock, BlockGetter pLevel, BlockPos pPos) {
        float f = 1.0F;
        BlockPos blockpos = pPos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = pLevel.getBlockState(blockpos.offset(i, 0, j));
                if (blockstate.canSustainPlant(pLevel, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, (net.minecraftforge.common.IPlantable) pBlock)) {
                    f1 = 3.0F;
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pPos.north();
        BlockPos blockpos2 = pPos.south();
        BlockPos blockpos3 = pPos.west();
        BlockPos blockpos4 = pPos.east();
        boolean flag = pLevel.getBlockState(blockpos3).is(pBlock) || pLevel.getBlockState(blockpos4).is(pBlock);
        boolean flag1 = pLevel.getBlockState(blockpos1).is(pBlock) || pLevel.getBlockState(blockpos2).is(pBlock);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = pLevel.getBlockState(blockpos3.north()).is(pBlock) || pLevel.getBlockState(blockpos4.north()).is(pBlock) || pLevel.getBlockState(blockpos4.south()).is(pBlock) || pLevel.getBlockState(blockpos3.south()).is(pBlock);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / f) + 1) == 0)) {
                    pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }

    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return (pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos)) && pLevel.getBlockState(pPos.below()).is(ModBlocks.FROZEN_POWDER_BLOCK.get());
    }

    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(ModBlocks.FROZEN_POWDER_BLOCK.get());
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.WINTER_ROOT.get();
    }

    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(this.getBaseSeedId());
    }
}
