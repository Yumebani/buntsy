package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;

public class SugardewCrop extends CropBlock {

    public SugardewCrop(Properties pProperties) {
        super(pProperties);
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.SUGARDEW_SEEDS.get();
    }

    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(this.getBaseSeedId());
    }
}
