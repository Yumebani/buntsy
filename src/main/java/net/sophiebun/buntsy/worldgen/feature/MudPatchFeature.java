package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;

public class MudPatchFeature extends Feature<NoneFeatureConfiguration> {

    public MudPatchFeature(Codec<NoneFeatureConfiguration > pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();

        int size = randomsource.nextInt(4, 7);

        for (int x = -size; x <= size; x++){
            for (int z = -size; z <= size; z++){
                for (int y = -size; y <= size; y++) {
                    if (isInRadius(size, x, y, z) && worldgenlevel.getBlockState(blockpos.offset(x, y, z)).is(ModTags.Blocks.ODIATE_SOIL)) {
                        worldgenlevel.setBlock(blockpos.offset(x, y, z), ModBlocks.ODIATE_MUD.get().defaultBlockState(), 2);
                    }
                }
            }
        }

        return true;
    }

    private boolean isInRadius(int radius, int x, int y, int z){
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) <= Math.pow(radius, 2);
    }
}
