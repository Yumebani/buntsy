package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;

public class SyrupExtractorBlock extends Block{

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape[] SHAPE = new VoxelShape[]{Block.box(4, 2, 0, 12, 10, 7),
                                                            Block.box(0, 2, 4, 7, 10, 12),
                                                            Block.box(4, 2, 9, 12, 10, 16),
                                                            Block.box(9, 2, 4, 16, 10, 12)};


    public SyrupExtractorBlock(Properties pProperties) {
        super(pProperties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LEVEL, Integer.valueOf(0)));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if (pPlayer.getItemInHand(pHand).is(Items.GLASS_BOTTLE) && pState.getValue(SyrupExtractorBlock.LEVEL) == 3){
                pPlayer.getItemInHand(pHand).shrink(1);
                pPlayer.addItem(new ItemStack(ModItems.GENTLIT_SYRUP.get()));
                pLevel.setBlockAndUpdate(pPos, this.defaultBlockState()
                        .setValue(HorizontalDirectionalBlock.FACING, pState.getValue(HorizontalDirectionalBlock.FACING))
                        .setValue(SyrupExtractorBlock.LEVEL, 0));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }

        super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(SyrupExtractorBlock.LEVEL) != 3;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getBlockState(pPos.relative(pState.getValue(SyrupExtractorBlock.FACING).getOpposite(), 1)).is(ModTags.Blocks.GENTLIT_LOGS)){
            pLevel.setBlockAndUpdate(pPos, this.defaultBlockState()
                    .setValue(HorizontalDirectionalBlock.FACING, pState.getValue(HorizontalDirectionalBlock.FACING))
                    .setValue(SyrupExtractorBlock.LEVEL, pState.getValue(SyrupExtractorBlock.LEVEL) + 1));
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace()).setValue(LEVEL, Integer.valueOf(0));
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch (pState.getValue(HorizontalDirectionalBlock.FACING)){
            case EAST:
                return SHAPE[1];
            case SOUTH:
                return SHAPE[0];
            case WEST:
                return SHAPE[3];
            default:
                return SHAPE[2];
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

}
