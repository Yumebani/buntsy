package net.sophiebun.buntsy.worldgen.tree.malvor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MalvorFoliagePlacer extends FoliagePlacer {

    public static final Codec<MalvorFoliagePlacer> CODEC = RecordCodecBuilder.create(malvorFoliagePlacerInstance ->
            foliagePlacerParts(malvorFoliagePlacerInstance).apply(malvorFoliagePlacerInstance, MalvorFoliagePlacer::new));

    public MalvorFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
        super(pRadius, pOffset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.MALVOR_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int id, FoliageAttachment foliageAttachment, int i1, int i2, int i3) {

        int radiusOffset = foliageAttachment.radiusOffset();

        Queue<Integer> radiuseses = new ArrayDeque<>();
        float divisionAmount = foliageAttachment.doubleTrunk() ? 35f : 45f;
        float maxRadii = foliageAttachment.doubleTrunk() ? 5f : 3f;
        float i = 1;

        while ((radiusOffset / (divisionAmount * i)) > maxRadii){
            radiuseses.add((int)Math.ceil(radiusOffset / (divisionAmount * i)));
            i *= 1.25f * i;
        }

        int j1 = 0, ymodMod = 1;
        float j2 = 1;
        while (!radiuseses.isEmpty()){

            int radius = radiuseses.remove();

            for (int ymod = j1 <= 0 ? 0 : -1; ymod < 2; ymod += 2){
                for (int x = -radius; x <= radius; x++){
                    for (int z = -radius; z <= radius; z++)
                    {
                        if (isInRadius(radius, x, z) && randomSource.nextInt(Math.max(0, radius - Math.round(j2) - 2), Math.max(1, radius - 1)) > Math.hypot(Math.abs(x), Math.abs(z))){
                            tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, foliageAttachment.pos().offset(x, (ymod * j1) + ymodMod, z));
                        }
                    }
                }
            }

            j1++;
            j2 *= 1.25f;
        }
    }

    private boolean isInRadius(int radius, int x, int y){
        return Math.hypot(Math.abs(x), Math.abs(y)) <= radius;
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
