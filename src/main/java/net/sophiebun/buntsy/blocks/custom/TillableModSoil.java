package net.sophiebun.buntsy.blocks.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.sophiebun.buntsy.blocks.custom.entityblocks.ChocolateGeyserBlock;

public class TillableModSoil extends SpreadingSnowyDirtBlock {

    public static final MapCodec<TillableModSoil> CODEC = RecordCodecBuilder.mapCodec(tillableModSoilInstance -> {
        return tillableModSoilInstance.group(
                propertiesCodec(),
                Block.CODEC.forGetter(TillableModSoil::getFarmland)
        ).apply(tillableModSoilInstance, TillableModSoil::new);
    });
    private final Block FARMLAND;

    public TillableModSoil(Properties pProperties, Block farmland) {
        super(pProperties);
        this.FARMLAND = farmland;
    }

    public Block getFarmland() {
        return FARMLAND;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).is(ItemTags.HOES) && !pLevel.isClientSide()){
            pLevel.setBlock(pPos, FARMLAND.defaultBlockState(), 11);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, FARMLAND.defaultBlockState()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected MapCodec<? extends SpreadingSnowyDirtBlock> codec() {
        return CODEC;
    }
}
