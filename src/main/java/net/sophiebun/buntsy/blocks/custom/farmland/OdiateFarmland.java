package net.sophiebun.buntsy.blocks.custom.farmland;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sophiebun.buntsy.blocks.ModBlocks;

import javax.annotation.Nullable;

public class OdiateFarmland extends ModFarmland {

    public OdiateFarmland(Properties pProperties) {
        super(pProperties);
    }

    public static void turnToDirt(@Nullable Entity pEntity, BlockState pState, Level pLevel, BlockPos pPos) {
        BlockState blockstate = pushEntitiesUp(pState, ModBlocks.ODIATE_SOIL.get().defaultBlockState(), pLevel, pPos);
        pLevel.setBlockAndUpdate(pPos, blockstate);
        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pEntity, blockstate));
    }
}
