package net.sophiebun.buntsy.worldgen.tree.bravot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;

public class SquishedBlobFoliagePlacer extends FoliagePlacer {

    public static final Codec<SquishedBlobFoliagePlacer> CODEC = RecordCodecBuilder.create(squishedBlobFoliagePlacerInstance ->
            foliagePlacerParts(squishedBlobFoliagePlacerInstance).apply(squishedBlobFoliagePlacerInstance, SquishedBlobFoliagePlacer::new));

    public SquishedBlobFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
        super(pRadius, pOffset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.SQUISHED_BLOB_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int id, FoliageAttachment foliageAttachment, int i1, int i2, int i3) {

        int radiusOffset = foliageAttachment.radiusOffset();
        int radiusRounded = (int)Math.ceil(radiusOffset / 10f);
        int radiusY;

        for (int y = 0; y <= 3; y++){
            radiusY = radiusHeighDecrease(y, radiusOffset);
            for (int x = -radiusRounded; x <= radiusRounded; x++){
                for (int z = -radiusRounded; z <= radiusRounded; z++)
                {
                    if (isInRadius(radiusY, x, z)){
                        tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, foliageAttachment.pos().offset(x, y, z));
                    }
                }
            }
        }
    }

    private int radiusHeighDecrease(int height, int radius){
        return radius - (int)Math.exp(height * 2);
    }

    private boolean isInRadius(int radius, int x, int y){
        return Math.hypot(Math.abs(x), Math.abs(y)) * 10 <= radius;
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
