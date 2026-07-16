package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class TillableModSoil extends SpreadingSnowyDirtBlock {

    private final Block FARMLAND;

    public TillableModSoil(Properties pProperties, Block farmland) {
        super(pProperties);
        this.FARMLAND = farmland;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).is(ItemTags.HOES) && !pLevel.isClientSide()){
            pLevel.setBlock(pPos, FARMLAND.defaultBlockState(), 11);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, FARMLAND.defaultBlockState()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
