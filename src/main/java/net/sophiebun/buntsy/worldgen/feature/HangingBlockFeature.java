package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.ModLeaves;

public class HangingBlockFeature extends Feature<NoneFeatureConfiguration> {

    public HangingBlockFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public Block getBlock() {
        return ModBlocks.HANGING_STRING.get();
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();

        int y = worldgenlevel.getHeight(Heightmap.Types.MOTION_BLOCKING, blockpos.getX(), blockpos.getZ()) - 1;

        BlockPos newPos = new BlockPos(blockpos.getX(), 0, blockpos.getZ());

        if (!(worldgenlevel.getBlockState(newPos.offset(0, y, 0)).getBlock() instanceof ModLeaves)){
            return false;
        }

        while (worldgenlevel.getBlockState(newPos.offset(0, y, 0)).getBlock() instanceof ModLeaves){
            y--;
        }

        if (worldgenlevel.getBlockState(newPos.offset(0, y, 0)).isAir()
            && worldgenlevel.getBlockState(newPos.offset(0, y - 1, 0)).isAir()){

            boolean cutoff = false;
            int chance = 15;
            while (worldgenlevel.getBlockState(newPos.offset(0, y - 2, 0)).isAir() && !cutoff){

                if (randomsource.nextInt(0, chance) < 3){
                    cutoff = true;
                } else {
                    BlockPos pos = newPos.offset(0, y, 0);
                    worldgenlevel.setBlock(pos, ModBlocks.HANGING_STRING.get().defaultBlockState(), 2);
                    worldgenlevel.getChunk(pos).markPosForPostprocessing(pos);
                    y--;
                }
            }

            BlockPos pos = newPos.offset(0, y, 0);
            worldgenlevel.setBlock(pos, getBlock().defaultBlockState(), 2);
            worldgenlevel.getChunk(pos).markPosForPostprocessing(pos);

            return true;
        }

        return false;
    }
}
