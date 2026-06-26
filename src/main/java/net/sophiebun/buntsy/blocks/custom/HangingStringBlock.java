package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingStringBlock extends Block {

    public static final EnumProperty<HangingStringEnding> TYPE = EnumProperty.create("hanging_string_ending", HangingStringEnding.class);
    protected static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public HangingStringBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, HangingStringEnding.TOP));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {

        BlockState below = pLevel.getBlockState(pPos.below());
        if (pLevel.getBlockState(pPos.above()).is(pState.getBlock())){
            if (below.is(pState.getBlock())){
                return this.defaultBlockState().setValue(TYPE, HangingStringEnding.MIDDLE);
            } else if (!below.isSolidRender(pLevel, pPos.below()) && !(below.getBlock() instanceof HangingObjectBlock)){
                return this.defaultBlockState().setValue(TYPE, HangingStringEnding.MIDDLE_ENDING);
            } else {
                return this.defaultBlockState().setValue(TYPE, HangingStringEnding.GRAB);
            }
        } else {
            if (below.is(pState.getBlock())){
                return this.defaultBlockState().setValue(TYPE, HangingStringEnding.TOP);
            } else if (!below.isSolidRender(pLevel, pPos.below()) && !(below.getBlock() instanceof HangingObjectBlock)){
                return this.defaultBlockState().setValue(TYPE, HangingStringEnding.TOP_ENDING);
            } else {
                return this.defaultBlockState().setValue(TYPE, HangingStringEnding.TOP_GRAB);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return !pLevel.getBlockState(pPos.above()).isAir();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TYPE);
    }
}
