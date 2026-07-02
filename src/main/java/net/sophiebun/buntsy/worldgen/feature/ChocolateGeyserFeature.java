package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.Tags;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.fluids.ModFluidTypes;
import net.sophiebun.buntsy.fluids.ModFluids;
import net.sophiebun.buntsy.tag.ModTags;

public class ChocolateGeyserFeature extends Feature<NoneFeatureConfiguration> {

    public ChocolateGeyserFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();

        for (int x = -2; x <= 2; x++){
            for (int z = -2; z <= 2; z++){
                for (int y = 1; y >= -6; y--) {

                    BlockState check = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
                    FluidState checkFluid = check.getFluidState();

                    if (isInRadius2D(25, x * 10, z * 10) && (check.is(ModTags.Blocks.FREEZWEET_BLOCKS) || check.isAir() || !checkFluid.isEmpty() || !checkFluid.is(ModFluids.SOURCE_HOT_CHOCOLATE.get().getSource()))){
                        worldgenlevel.setBlock(blockpos.offset(x, y, z), ModBlocks.PETRIFIED_CHOCOLATE.get().defaultBlockState(), 2);
                    }
                }
            }
        }

        worldgenlevel.setBlock(blockpos.offset(0, 1,0), ModBlocks.CHOCOLATE_GEYSER.get().defaultBlockState(), 2);

        return true;
    }

    private boolean isInRadius2D(int radius, int x, int z){
        return Math.pow(x, 2) + Math.pow(z, 2) <= Math.pow(radius, 2);
    }
}
