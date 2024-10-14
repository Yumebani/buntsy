package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class ModCoralTreeFeature extends ModCoralFeature {
    public ModCoralTreeFeature(Codec<NoneFeatureConfiguration> p_65488_) {
        super(p_65488_);
    }

    protected boolean placeFeature(LevelAccessor p_224987_, RandomSource p_224988_, BlockPos p_224989_, BlockState p_224990_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_224989_.mutable();
        int i = p_224988_.nextInt(3) + 1;

        for(int j = 0; j < i; ++j) {
            if (!this.placeCoralBlock(p_224987_, p_224988_, blockpos$mutableblockpos, p_224990_)) {
                return true;
            }

            blockpos$mutableblockpos.move(Direction.UP);
        }

        BlockPos blockpos = blockpos$mutableblockpos.immutable();
        int k = p_224988_.nextInt(3) + 2;
        List<Direction> list = Direction.Plane.HORIZONTAL.shuffledCopy(p_224988_);

        for(Direction direction : list.subList(0, k)) {
            blockpos$mutableblockpos.set(blockpos);
            blockpos$mutableblockpos.move(direction);
            int l = p_224988_.nextInt(5) + 2;
            int i1 = 0;

            for(int j1 = 0; j1 < l && this.placeCoralBlock(p_224987_, p_224988_, blockpos$mutableblockpos, p_224990_); ++j1) {
                ++i1;
                blockpos$mutableblockpos.move(Direction.UP);
                if (j1 == 0 || i1 >= 2 && p_224988_.nextFloat() < 0.25F) {
                    blockpos$mutableblockpos.move(direction);
                    i1 = 0;
                }
            }
        }

        return true;
    }
}