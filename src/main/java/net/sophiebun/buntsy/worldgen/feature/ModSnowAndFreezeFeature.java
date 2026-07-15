package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;

public class ModSnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
    public ModSnowAndFreezeFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> config) {
        WorldGenLevel worldgenlevel = config.level();
        BlockPos blockpos = config.origin();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                int k = blockpos.getX() + i;
                int l = blockpos.getZ() + j;
                int i1 = worldgenlevel.getHeight(Heightmap.Types.MOTION_BLOCKING, k, l);
                mutableBlockPos.set(k, i1, l);
                mutableBlockPos1.set(mutableBlockPos).move(Direction.DOWN, 1);
                Biome biome = worldgenlevel.getBiome(mutableBlockPos).value();
                if (biome.shouldFreeze(worldgenlevel, mutableBlockPos1, false)) {
                    worldgenlevel.setBlock(mutableBlockPos1, Blocks.ICE.defaultBlockState(), 2);
                }

                if (biome.shouldSnow(worldgenlevel, mutableBlockPos) && !worldgenlevel.getBlockState(mutableBlockPos.below()).is(ModTags.Blocks.NO_SNOW)) {
                    worldgenlevel.setBlock(mutableBlockPos, ModBlocks.FROZEN_POWDER_LAYER.get().defaultBlockState(), 2);
                    BlockState blockstate = worldgenlevel.getBlockState(mutableBlockPos1);
                    if (blockstate.hasProperty(SnowyDirtBlock.SNOWY)) {
                        worldgenlevel.setBlock(mutableBlockPos1, blockstate.setValue(SnowyDirtBlock.SNOWY, Boolean.valueOf(true)), 2);
                    }
                }
            }
        }

        return true;
    }
}