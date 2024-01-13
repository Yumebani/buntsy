package net.sophiebun.buntsy.worldgen.tree.gentlit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;

public class PieFoliagePlacer extends FoliagePlacer {

    public static final Codec<PieFoliagePlacer> CODEC = RecordCodecBuilder.create(pieFoliagePlacerInstance ->
            foliagePlacerParts(pieFoliagePlacerInstance).apply(pieFoliagePlacerInstance, PieFoliagePlacer::new));

    public PieFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
        super(pRadius, pOffset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.PIE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int id, FoliageAttachment foliageAttachment, int i1, int i2, int i3) {

        int radiusOffset = foliageAttachment.radiusOffset();
        int radiusRounded = (int)Math.ceil(radiusOffset / 10f);

        for (int x = -radiusRounded; x <= radiusRounded; x++){
            for (int y = -radiusRounded; y <= radiusRounded; y++)
            {
                if (isInRadius(radiusOffset, x, y) && randomSource.nextInt(0, 100) > Math.hypot(Math.abs(x), Math.abs(y)) * 10){
                    tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, foliageAttachment.pos().offset(x, 0, y));
                }
            }
        }
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
