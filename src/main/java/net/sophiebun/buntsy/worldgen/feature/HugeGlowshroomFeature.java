package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

import java.util.function.Function;

public class HugeGlowshroomFeature extends AbstractHugeMushroomFeature {

    public HugeGlowshroomFeature(Codec<HugeMushroomFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    protected int getTreeRadiusForHeight(int i, int i1, int i2, int i3) {
        return 0;
    }

    @Override
    protected int getTreeHeight(RandomSource pRandom) {
        return UniformInt.of(7,10).sample(pRandom);
    }

    protected int getCapHeight(RandomSource pRandom) {
        return UniformInt.of(5,7).sample(pRandom);
    }

    @Override
    protected void makeCap(LevelAccessor levelAccessor, RandomSource randomSource, BlockPos blockPos, int maxHeight, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration) {

        float radiusModifier = UniformFloat.of(1.3f, 1.6f).sample(randomSource);
        int height = getCapHeight(randomSource);

        for (int y = 0; y < height; y++){

            float radius = (float) (Math.log(y + 0.6f)* radiusModifier) * 10;
            float innerRadius = (float) (Math.log(y - 0.75f) * radiusModifier - 0.75f) * 10;
            int radiusRounded = Math.round(radius);

            for (int x = -radiusRounded; x <= radiusRounded; x++){
                for (int z = -radiusRounded; z <= radiusRounded; z++){

                    mutableBlockPos.setWithOffset(blockPos, x, maxHeight - y, z);
                    if (isInRadius(radius, x, z) && !isInRadius(innerRadius, x, z)){
                        this.setBlock(levelAccessor, mutableBlockPos,
                                ((BlockState) Function.identity().apply(hugeMushroomFeatureConfiguration.capProvider.getState(randomSource, blockPos)
                                        .setValue(HugeMushroomBlock.EAST, !isSideCloser(x, z, x + 1, z))
                                        .setValue(HugeMushroomBlock.SOUTH, !isSideCloser(x, z, x, z + 1))
                                        .setValue(HugeMushroomBlock.WEST, !isSideCloser(x, z, x - 1, z))
                                        .setValue(HugeMushroomBlock.NORTH, !isSideCloser(x, z, x, z - 1))
                                        .setValue(HugeMushroomBlock.DOWN, false))));
                    }
                }
            }
        }
    }

    private boolean isSideCloser(int x1, int z1, int x2, int z2){
        return Math.hypot(Math.abs(x2), Math.abs(z2)) <= Math.hypot(Math.abs(x1), Math.abs(z1));
    }

    private boolean isInRadius(float radius, int x, int z){
        return Math.hypot(Math.abs(z), Math.abs(x)) * 10 <= radius;
    }
}
