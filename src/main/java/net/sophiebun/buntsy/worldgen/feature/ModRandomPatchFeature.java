package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.sophiebun.buntsy.blocks.custom.ModLeaves;

public class ModRandomPatchFeature extends Feature<RandomPatchConfiguration> {

    public ModRandomPatchFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> config) {
        RandomPatchConfiguration randompatchconfiguration = config.config();
        RandomSource randomsource = config.random();
        BlockPos blockpos = config.origin();
        WorldGenLevel worldgenlevel = config.level();

        int y = 0;
        BlockState state = worldgenlevel.getBlockState(blockpos.offset(0, y, 0). below());
        while (state.isAir() || (state.getBlock() instanceof ModLeaves)){
            y--;
            state = worldgenlevel.getBlockState(blockpos.offset(0, y, 0));
        }

        int i = 0;
        int j = randompatchconfiguration.xzSpread() + 1;
        int k = randompatchconfiguration.ySpread() + 1;

        for(int l = 0; l < randompatchconfiguration.tries(); ++l) {
            if (randompatchconfiguration.feature().value().place(worldgenlevel, config.chunkGenerator(), randomsource,
                    blockpos.offset(randomsource.nextInt(j) - randomsource.nextInt(j), y + randomsource.nextInt(k) - randomsource.nextInt(k), randomsource.nextInt(j) - randomsource.nextInt(j)))) {
                ++i;
            }
        }

        return i > 0;
    }
}
