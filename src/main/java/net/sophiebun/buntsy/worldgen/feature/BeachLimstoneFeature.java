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
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;

public class BeachLimstoneFeature extends Feature<BlockStateConfiguration> {

    public BeachLimstoneFeature(Codec<BlockStateConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {

        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        BlockState state = context.config().state;

        int size = randomsource.nextInt(25, 35);
        float alongation = randomsource.nextInt(5, 15) / 10f;
        float skewX = (randomsource.nextInt(0, 6) / 10f) * (randomsource.nextBoolean() ? 1 : -1);
        float skewZ = (randomsource.nextInt(0, 6) / 10f) * (randomsource.nextBoolean() ? 1 : -1);

        int sizeAlong = Math.round(size * alongation);

        for (int x = -size; x <= size; x++){
            for (int z = -size; z <= size; z++){
                for (int y = -sizeAlong; y <= sizeAlong; y++) {

                    int xAlong = x + Math.round(y * skewX);
                    int zAlong = z + Math.round(y * skewZ);

                    BlockState check = worldgenlevel.getBlockState(blockpos.offset(Math.round(xAlong / 10f), Math.round(y / 10f), Math.round(zAlong / 10f)));
                    FluidState checkFluid = check.getFluidState();

                    if (isInRadius(size, x, Math.round(y / alongation), z) && (check.is(Tags.Blocks.SAND) || check.isAir() || !checkFluid.isEmpty())) {
                        worldgenlevel.setBlock(blockpos.offset(Math.round(xAlong / 10f), Math.round(y / 10f), Math.round(zAlong / 10f)), state, 2);
                    }
                }
            }
        }

        return true;
    }

    private boolean isInRadius2D(int radius, int x, int z){
        return Math.pow(x, 2) + Math.pow(z, 2) <= Math.pow(radius, 2);
    }

    private boolean isInRadius(int radius, int x, int y, int z){
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) <= Math.pow(radius, 2);
    }
}
