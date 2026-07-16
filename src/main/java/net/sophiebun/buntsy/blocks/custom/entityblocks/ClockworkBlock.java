package net.sophiebun.buntsy.blocks.custom.entityblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkBlockEntity;

public abstract class ClockworkBlock  extends BaseEntityBlock {

    public ClockworkBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        
        if (pPlayer.getItemInHand(pHand).isEmpty()){
            ((ClockworkBlockEntity) pLevel.getBlockEntity(pPos)).ejectItem(pLevel, pPlayer);
            return InteractionResult.SUCCESS;
        } else {
            return ((ClockworkBlockEntity) pLevel.getBlockEntity(pPos)).putUpgrade(pPlayer.getItemInHand(pHand), pLevel, pPlayer, pHand);
        }
    }
}
