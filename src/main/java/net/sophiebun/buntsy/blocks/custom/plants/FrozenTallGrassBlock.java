package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.custom.farmland.ModFarmland;
import net.sophiebun.buntsy.tag.ModTags;

public class FrozenTallGrassBlock extends ModTallGrassBlock{

    public FrozenTallGrassBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(ModTags.Blocks.FREEZWEET_BLOCKS);
    }
}
