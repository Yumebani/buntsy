package net.sophiebun.buntsy.blocks.custom.entityblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarAdvancedBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBasicBlockEntity;
import org.jetbrains.annotations.Nullable;

public class InfusionAltarAdvanced extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 12, 15);

    public InfusionAltarAdvanced(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfusionAltarAdvancedBlockEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof InfusionAltarAdvancedBlockEntity) {
                ((InfusionAltarAdvancedBlockEntity) blockEntity).drops();
                ((InfusionAltarAdvancedBlockEntity) blockEntity).removeAllRelays(pLevel);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof InfusionAltarAdvancedBlockEntity) {

                InfusionAltarAdvancedBlockEntity infusePedestal = (InfusionAltarAdvancedBlockEntity) blockEntity;

                if (infusePedestal.getItem().isEmpty() && !pPlayer.getItemInHand(pHand).isEmpty()){
                    ItemStack stackInHand = pPlayer.getItemInHand(pHand);
                    ItemStack itemToAdd = stackInHand.copy();
                    itemToAdd.setCount(1);
                    stackInHand.shrink(1);
                    infusePedestal.depositItem(itemToAdd);
                }
                else {
                    pPlayer.addItem(infusePedestal.extractItem());
                }

            }
            else {
                throw new IllegalStateException("No container provider.");
            }
        }

        super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.INFUSION_ALTAR_ADVANCED_BLOCK_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
