package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.function.Supplier;

public class FrozenFlowerBlock extends FlowerBlock {

    public FrozenFlowerBlock(Supplier<MobEffect> effectSupplier, int pEffectDuration, Properties pProperties) {
        super(effectSupplier, pEffectDuration, pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(ModTags.Blocks.FREEZWEET_BLOCKS);
    }
}
