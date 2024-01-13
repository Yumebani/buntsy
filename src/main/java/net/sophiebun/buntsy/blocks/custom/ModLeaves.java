package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModLeaves extends LeavesBlock {

    public static final int DECAY_DISTANCE = 12;

    public ModLeaves(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    @Override
    protected boolean decaying(BlockState pState) {
        return !(Boolean)pState.getValue(PERSISTENT) && (Integer)pState.getValue(DISTANCE) == DECAY_DISTANCE;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return (Integer)pState.getValue(DISTANCE) == DECAY_DISTANCE && !(Boolean)pState.getValue(PERSISTENT);
    }
}
