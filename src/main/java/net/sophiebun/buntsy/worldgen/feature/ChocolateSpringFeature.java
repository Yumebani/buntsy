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
import net.sophiebun.buntsy.tag.ModTags;

public class ChocolateSpringFeature extends Feature<NoneFeatureConfiguration> {

    public ChocolateSpringFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();

        for (int i = 0; i < 3; i++){

            int startX = randomsource.nextInt(-2, 3);
            int startZ = randomsource.nextInt(-2, 3);

            int size = randomsource.nextInt(4, 7);
            boolean alongationX = randomsource.nextBoolean();
            float alongation = randomsource.nextInt(1, 4) / 2f;

            int sizeAlongX = Math.round(size * (alongationX ? alongation : 1));
            int sizeAlongZ = Math.round(size * (alongationX ? 1 : alongation));
            int maxSize = Math.round(size * alongation);

            for (int x = -sizeAlongX; x <= sizeAlongX; x++){
                for (int z = -sizeAlongZ; z <= sizeAlongZ; z++){
                    for (int y = 0; y >= -10; y--) {

                        int xAlong = Math.round(x + (x * (alongationX ? 0 : alongation - 1)));
                        int zAlong = Math.round(z + (z * (alongationX ? alongation - 1 : 0)));

                        BlockState check = worldgenlevel.getBlockState(blockpos.offset(x + startX, y, z + startZ));
                        FluidState checkFluid = check.getFluidState();

                        if ((isInRadius(maxSize, xAlong, Math.round(y / 1.25f), zAlong))
                                && (check.is(ModTags.Blocks.FREEZWEET_BLOCKS) || check.isAir() || checkFluid.isEmpty())) {
                            worldgenlevel.setBlock(blockpos.offset(x + startX, y, z + startZ), ModBlocks.PETRIFIED_CHOCOLATE.get().defaultBlockState(), 2);
                        }

                        if ((isInRadius(maxSize - 2, xAlong, y * 2, zAlong))
                                && (check.is(ModBlocks.PETRIFIED_CHOCOLATE.get()) || check.isAir() || checkFluid.isEmpty())) {
                            worldgenlevel.setBlock(blockpos.offset(x + startX, y, z + startZ), ModBlocks.HOT_CHOCOLATE_BLOCK.get().defaultBlockState(), 2);
                        }
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
