package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class CottonvineBlock extends KelpBlock {
    public CottonvineBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected Block getBodyBlock() {
        return ModBlocks.COTTON_VINE_PLANT.get();
    }
}
