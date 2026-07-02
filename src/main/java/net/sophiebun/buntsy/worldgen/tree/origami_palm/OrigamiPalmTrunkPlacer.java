package net.sophiebun.buntsy.worldgen.tree.origami_palm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class OrigamiPalmTrunkPlacer extends TrunkPlacer {

    public static final Codec<OrigamiPalmTrunkPlacer> CODEC = RecordCodecBuilder.create(origamiPalmTrunkPlacerInstance ->
            trunkPlacerParts(origamiPalmTrunkPlacerInstance).apply(origamiPalmTrunkPlacerInstance, OrigamiPalmTrunkPlacer::new));

    public OrigamiPalmTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.ORIGAMI_PALM_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int id, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        int randomHeight = randomSource.nextInt(baseHeight + heightRandA, baseHeight + heightRandB);
        int startAngle = randomSource.nextInt(0, 360);
        double pertrudingAngle = randomSource.nextInt(15, 65);

        int angle = startAngle % 90;

        Direction branchDir;
        switch ((int)Math.floor(startAngle / 90f) * 90){
            case 0 -> branchDir = Direction.SOUTH;
            case 90 -> {branchDir = Direction.WEST; angle = 90 - angle;}
            case 180 -> branchDir = Direction.NORTH;
            default -> {branchDir = Direction.EAST; angle = 90 - angle;}
        }

        double branchXZLinear = Math.tan(Math.toRadians(angle));
        boolean useZ = Math.abs(branchXZLinear) < 1;

        double heightCorrectionRate = 1;
        double heightYLinear;

        int x = 0, y = 0, targetY = 0, z = 0, finalX = 0, finalZ = 0, finalY = 0;
        double distance = 0;
        int currentDistanceXZ;

        while (distance < randomHeight * 10){

            switch (branchDir){
                case EAST -> {finalX = x; finalZ = -z;}
                case NORTH -> {finalX = -x; finalZ = -z;}
                case WEST -> {finalX = -x; finalZ = z;}
                default -> {finalX = x; finalZ = z;}
            }

            finalX = Math.round(finalX / 10f);
            finalY = Math.round(y / 10f);
            finalZ = Math.round(finalZ / 10f);

            if (TreeFeature.validTreePos(levelSimulatedReader, blockPos.offset(finalX, finalY, finalZ))){
                placeLogOriented(finalX, finalY, finalZ, Direction.Axis.Y, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration);
            }

            if (targetY > 0 && targetY > y){
                y++;
            } else if (targetY < 0 && targetY < y) {
                y--;
            } else {

                heightCorrectionRate *= 1.03f;
                pertrudingAngle += heightCorrectionRate - 1;
                pertrudingAngle = pertrudingAngle > 89 ? 89 : pertrudingAngle;
                heightYLinear = Math.tan(Math.toRadians(pertrudingAngle));
                heightYLinear = heightYLinear > 10 ? 10 : heightYLinear;

                if (useZ){
                    x ++;
                    z = (int)Math.round(branchXZLinear * x);
                } else {
                    z ++;
                    x = (int)Math.round((1f / branchXZLinear) * z);
                }
                currentDistanceXZ = getCurrentDistanceXZ(x, z);
                targetY = (int)Math.round(heightYLinear * currentDistanceXZ);

            }

            distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        }

        return List.of(new FoliagePlacer.FoliageAttachment(blockPos.offset(finalX, finalY, finalZ),
                randomSource.nextInt(6, 9), false));
    }

    private void placeLogOriented(int x, int y, int z, Direction.Axis axis, LevelSimulatedReader levelSimulatedReader,
                                  BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration){

        biConsumer.accept(blockPos.offset(x, y, z),
                ((BlockState) Function.identity().apply(treeConfiguration.trunkProvider.getState(randomSource, blockPos).setValue(
                        RotatedPillarBlock.AXIS, axis
                ))));
    }

    private int getCurrentDistanceXZ (int x, int z){
        return (int) Math.hypot(Math.abs(x), Math.abs(z));
    }
}
