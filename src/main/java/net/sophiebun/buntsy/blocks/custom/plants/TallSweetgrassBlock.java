package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class TallSweetgrassBlock extends TallSeagrassBlock {
    public TallSweetgrassBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304988_, BlockPos p_154750_, BlockState p_154751_) {
        return new ItemStack(ModBlocks.SWEETGRASS.get());
    }
}
