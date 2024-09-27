package net.sophiebun.buntsy.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.SyrupExtractorBlock;
import net.sophiebun.buntsy.item.ModItems;

public class DispenserBehaviourAditions {

    public static ItemStack interactWithSExtractor(BlockSource pSource,ItemStack pStack){

        ServerLevel level = pSource.getLevel();
        BlockPos pos = pSource.getPos().relative(pSource.getEntity().getBlockState().getValue(DispenserBlock.FACING), 1);
        BlockState targetBlock = level.getBlockState(pos);

        if (targetBlock.is(ModBlocks.SYRUP_EXTRACTOR.get()) &&
                targetBlock.getValue(SyrupExtractorBlock.LEVEL).intValue() == 3){

            level.setBlockAndUpdate(pos, targetBlock.getBlock().defaultBlockState()
                    .setValue(HorizontalDirectionalBlock.FACING, targetBlock.getValue(HorizontalDirectionalBlock.FACING))
                    .setValue(SyrupExtractorBlock.LEVEL, 0));

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.GENTLIT_SYRUP.get()));

            return new ItemStack(Items.GLASS_BOTTLE, pStack.getCount() - 1);
        }
        return new ItemStack(Items.GLASS_BOTTLE, pStack.getCount());
    }
}
