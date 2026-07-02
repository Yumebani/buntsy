package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class SandySaplingBlock extends SaplingBlock {
    public SandySaplingBlock(AbstractTreeGrower pTreeGrower, Properties pProperties) {
        super(pTreeGrower, pProperties);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return super.canSurvive(pState, pLevel, pPos) || pLevel.getBlockState(pPos.below()).is(Tags.Blocks.SAND);
    }
}
