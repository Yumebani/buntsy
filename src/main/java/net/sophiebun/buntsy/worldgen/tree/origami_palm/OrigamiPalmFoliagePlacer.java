package net.sophiebun.buntsy.worldgen.tree.origami_palm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModFoliagePlacers;

import java.util.ArrayDeque;
import java.util.Queue;

public class OrigamiPalmFoliagePlacer extends FoliagePlacer {

    public static final Codec<OrigamiPalmFoliagePlacer> CODEC = RecordCodecBuilder.create(origamiPalmFoliagePlacerInstance ->
            foliagePlacerParts(origamiPalmFoliagePlacerInstance).apply(origamiPalmFoliagePlacerInstance, OrigamiPalmFoliagePlacer::new));

    public OrigamiPalmFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
        super(pRadius, pOffset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.ORIGAMI_PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int id, FoliageAttachment foliageAttachment, int i1, int i2, int i3) {

        int angle = randomSource.nextInt(15, 35);
        int distance = foliageAttachment.radiusOffset();

        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 3; x++){
                placePalmLeaf(angle + (45 * y), 65 - (35 * x), distance, levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, foliageAttachment);
            }
        }

    }

    private void placePalmLeaf(int angleY, int angleX, int finalDistance, LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, FoliageAttachment foliageAttachment){

        double pertrudingAngle = angleX;
        int angle = angleY % 90;

        Direction branchDir;
        switch ((int)Math.floor(angleY / 90f) * 90){
            case 0 -> branchDir = Direction.SOUTH;
            case 90 -> {branchDir = Direction.WEST; angle = 90 - angle;}
            case 180 -> branchDir = Direction.NORTH;
            default -> {branchDir = Direction.EAST; angle = 90 - angle;}
        }

        double branchXZLinear = Math.tan(Math.toRadians(angle));
        boolean useZ = Math.abs(branchXZLinear) < 1;

        double heightCorrectionRate = 1;
        double heightYLinear;

        int x = 0, y = 0, targetY = 0, z = 0, finalX = 0, finalZ = 0, finalY = 0, blockCount = 0;
        double distance = 0;
        int currentDistanceXZ;

        while (blockCount < finalDistance && distance < finalDistance * 10){

            switch (branchDir){
                case EAST -> {finalX = x; finalZ = -z;}
                case NORTH -> {finalX = -x; finalZ = -z;}
                case WEST -> {finalX = -x; finalZ = z;}
                default -> {finalX = x; finalZ = z;}
            }

            finalX = Math.round(finalX / 10f);
            finalY = Math.round(y / 10f);
            finalZ = Math.round(finalZ / 10f);

            if (levelSimulatedReader.isStateAtPosition(foliageAttachment.pos().offset(finalX, finalY, finalZ), (BlockBehaviour.BlockStateBase::isAir))){
                tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, foliageAttachment.pos().offset(finalX, finalY, finalZ));
                blockCount++;
            }

            if (targetY > 0 && targetY > y){
                y++;
            } else if (targetY < 0 && targetY < y) {
                y--;
            } else {

                heightCorrectionRate *= 1.05f;
                pertrudingAngle -= heightCorrectionRate - 1;
                pertrudingAngle = pertrudingAngle > 89 ? 89 : pertrudingAngle;
                heightYLinear = Math.tan(Math.toRadians(pertrudingAngle));
                heightYLinear = heightYLinear > 10 ? 10 : heightYLinear;

                if (useZ){
                    x += 2;
                    z = (int)Math.round(branchXZLinear * x);
                } else {
                    z += 2;
                    x = (int)Math.round((1f / branchXZLinear) * z);
                }
                currentDistanceXZ = getCurrentDistanceXZ(x, z);
                targetY = (int)Math.round(heightYLinear * currentDistanceXZ);

            }

            distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource, int i, TreeConfiguration treeConfiguration) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource, int i, int i1, int i2, int i3, boolean b) {
        return false;
    }

    private int getCurrentDistanceXZ (int x, int z){
        return (int) Math.hypot(Math.abs(x), Math.abs(z));
    }
}
