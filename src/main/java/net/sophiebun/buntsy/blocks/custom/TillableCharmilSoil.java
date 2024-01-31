package net.sophiebun.buntsy.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class TillableCharmilSoil extends SnowyDirtBlock {

    public TillableCharmilSoil(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).is(ItemTags.HOES) && !pLevel.isClientSide()){
            pLevel.setBlock(pPos, ModBlocks.CHARMIL_FARMLAND.get().defaultBlockState(), 11);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, ModBlocks.CHARMIL_FARMLAND.get().defaultBlockState()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
