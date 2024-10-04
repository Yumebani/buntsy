package net.sophiebun.buntsy.blocks.custom.entityblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.network.NetworkHooks;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.blocks.entity.basicfairy.GrindingWheelBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.GiantCocoonBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionPedestalBlockEntity;
import net.sophiebun.buntsy.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class GiantCocoonBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public GiantCocoonBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GiantCocoonBlockEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GiantCocoonBlockEntity) {
                ((GiantCocoonBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GiantCocoonBlockEntity) {

                GiantCocoonBlockEntity giantCocoon = (GiantCocoonBlockEntity) blockEntity;

                if (!giantCocoon.hasUro() && !pPlayer.getItemInHand(pHand).isEmpty() && pPlayer.getItemInHand(pHand).is(ModItems.URO.get())){
                    ItemStack stackInHand = pPlayer.getItemInHand(pHand);
                    ItemStack itemToAdd = stackInHand.copy();
                    itemToAdd.setCount(1);
                    stackInHand.shrink(1);
                    giantCocoon.setUro(itemToAdd, pLevel);
                }
                else if (giantCocoon.hasUro() && pPlayer.isCrouching()){
                    pPlayer.addItem(giantCocoon.extractUro());
                }
                else if (giantCocoon.hasUro()){
                    NetworkHooks.openScreen((ServerPlayer) pPlayer,(GiantCocoonBlockEntity) blockEntity, pPos);
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
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GIANT_COCOON_BLOCK_ENTITY.get(),
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
