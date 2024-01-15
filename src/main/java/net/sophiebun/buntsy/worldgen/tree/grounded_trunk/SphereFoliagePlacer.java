package net.sophiebun.buntsy.worldgen.tree.grounded_trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;

public class SphereFoliagePlacer extends FoliagePlacer {

    public static final Codec<SphereFoliagePlacer> CODEC = RecordCodecBuilder.create(sphereFoliagePlacerInstance ->
            foliagePlacerParts(sphereFoliagePlacerInstance).apply(sphereFoliagePlacerInstance, SphereFoliagePlacer::new));

    public SphereFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
        super(pRadius, pOffset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.SPHERE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int id, FoliageAttachment foliageAttachment, int i1, int i2, int i3) {

        int radiusOffset = foliageAttachment.radiusOffset();
        int radiusRounded = (int)Math.ceil(radiusOffset / 10f);

        for (int x = -radiusRounded; x <= radiusRounded; x++){
            for (int y = -radiusRounded; y <= radiusRounded; y++)
            {
                for (int z = -radiusRounded; z <= radiusRounded; z++)
                {
                    if (isInRadius(radiusOffset, x, y, z)){
                        tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, foliageAttachment.pos().offset(x, y, z));
                    }
                }
            }
        }
    }

    private boolean isInRadius(int radius, int x, int y, int z){
        return Math.hypot(Math.abs(z), Math.hypot(Math.abs(x), Math.abs(y))) * 10 <= radius;
    }

    @Override
    public int foliageHeight(RandomSource randomSource, int i, TreeConfiguration treeConfiguration) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource, int i, int i1, int i2, int i3, boolean b) {
        return false;
    }
}
