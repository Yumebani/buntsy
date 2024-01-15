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

public class HugeLoveshroomFeature extends AbstractHugeMushroomFeature {

    public HugeLoveshroomFeature(Codec<HugeMushroomFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    protected int getTreeRadiusForHeight(int i, int i1, int i2, int i3) {
        return 0;
    }

    @Override
    protected int getTreeHeight(RandomSource pRandom) {
        return UniformInt.of(3,5).sample(pRandom);
    }

    protected int getCapHeight(RandomSource pRandom) {
        return UniformInt.of(3,5).sample(pRandom);
    }

    @Override
    protected void makeCap(LevelAccessor levelAccessor, RandomSource randomSource, BlockPos blockPos, int maxHeight, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration) {

        float linearYRadius = UniformFloat.of(0.25f, 0.75f).sample(randomSource);
        int height = getCapHeight(randomSource);

        for (int y = 1; y < height; y++) {

            float radius = (y * 10 * linearYRadius) + 5;
            int radiusRounded = Math.round(radius);

            for (int x = -radiusRounded; x <= radiusRounded; x++) {
                for (int z = -radiusRounded; z <= radiusRounded; z++) {

                    mutableBlockPos.setWithOffset(blockPos, x, maxHeight + y - 1, z);
                    if (isInRadius(radius, x, z) && !levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
                        this.setBlock(levelAccessor, mutableBlockPos,
                                ((BlockState) Function.identity().apply(hugeMushroomFeatureConfiguration.capProvider.getState(randomSource, blockPos)
                                        .setValue(HugeMushroomBlock.EAST, false)
                                        .setValue(HugeMushroomBlock.SOUTH, false)
                                        .setValue(HugeMushroomBlock.WEST, false)
                                        .setValue(HugeMushroomBlock.NORTH, false)
                                        .setValue(HugeMushroomBlock.DOWN, false))));
                    }
                }
            }
        }
    }

    private boolean isInRadius(float radius, int x, int z){
        return Math.hypot(Math.abs(z), Math.abs(x)) * 10 <= radius;
    }
}
