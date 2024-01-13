package net.sophiebun.buntsy.worldgen.tree.bravot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.PositionImpl;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BravotTrunkPlacer extends TrunkPlacer {

    public static final Codec<BravotTrunkPlacer> CODEC = RecordCodecBuilder.create(bravotTrunkPlacerInstance ->
            trunkPlacerParts(bravotTrunkPlacerInstance).apply(bravotTrunkPlacerInstance, BravotTrunkPlacer::new));

    public static final Direction[] VALID_DIRECTIONS = new Direction[]{
            Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH
    };

    public BravotTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.BRAVOT_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int id, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        setDirtAt(levelSimulatedReader, biConsumer, randomSource, blockPos.below(), treeConfiguration);

        int randomHeight = randomSource.nextInt(baseHeight + heightRandA, baseHeight + heightRandB);

        for (int i = 0; i < randomHeight; i++){
            placeLog(levelSimulatedReader, biConsumer, randomSource, blockPos.above(i), treeConfiguration);
        }

        int branchCount = randomSource.nextInt(Math.min(3, (randomHeight / 7)), Math.min(5, (randomHeight / 3)));
        int branchRoot, branchDistance, branchHeight, branchAngle;

        Direction direction;
        List<Direction> directionsToBeUsed = getListFromArray(VALID_DIRECTIONS);

        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<FoliagePlacer.FoliageAttachment>();

        for (int i = 0; i < branchCount; i++){

            branchRoot = randomSource.nextInt(randomHeight / 2, randomHeight - randomHeight / 6);
            branchDistance = randomSource.nextInt((randomHeight - branchRoot / 2) * 4, (randomHeight - branchRoot / 2) * 8);
            branchHeight = branchRoot + (branchDistance / 10);
            branchAngle = randomSource.nextInt(0, 90);

            if (directionsToBeUsed.size() == 0) {directionsToBeUsed = getListFromArray(VALID_DIRECTIONS);}

            direction = directionsToBeUsed.get(randomSource.nextInt(0,directionsToBeUsed.size()));
            directionsToBeUsed.remove(direction);

            attachments.add(makeBranch(randomHeight, branchRoot, branchDistance, branchHeight, branchAngle, direction, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration));
        }

        attachments.add(new FoliagePlacer.FoliageAttachment(blockPos.offset(0, randomHeight, 0),
                (int)(randomHeight * randomSource.nextInt(30, 40) / 10f), false));

        return attachments;
    }

    private <E>List<E> getListFromArray(E[] list){
        return Arrays.stream(list).collect(Collectors.toCollection(ArrayList<E>::new));
    }

    private FoliagePlacer.FoliageAttachment makeBranch(int treeHeight, int branchRoot, int branchDistance, int branchHeight, int branchAngle, Direction branchDir,
                                                       LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        //Linear factor
        float branchXZLinear = getBranchXZLinear(branchDistance, branchAngle);
        float heightDistanceYLinear = getHeightDistanceYLinear(branchDistance / 10, branchRoot, branchHeight);

        int x = 0, y = branchRoot, z = 0;
        boolean placeX = branchXZLinear <= 1;
        boolean lastYIncr = false, placedBlock = false;

        while (getCurrentDistance(x * 10, z * 10) <= branchDistance){

            while (placedBlock && getHeightDistanceYExp(branchDistance / 10, branchRoot, branchHeight, getCurrentDistance(x, z)) + branchRoot > y){

                y++;
                if (!lastYIncr){
                    lastYIncr = true;
                }
                else{
                    placeLogOriented(x, y - 1, z, branchDir, Direction.Axis.Y, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration);
                    placeLogOriented(x, y - 2, z, branchDir, Direction.Axis.Y, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration);
                }
            }

            lastYIncr = false;

            if (branchXZLinear * x > z){
                z++;
                if (!placeX){
                    placeLogOriented(x, y, z, branchDir, Direction.Axis.Z, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration);
                    placedBlock = true;
                }
                else {placedBlock = false;}
            }
            else{
                x++;
                if (placeX){
                    placeLogOriented(x, y, z, branchDir, Direction.Axis.X, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration);
                    placedBlock = true;
                }
                else {placedBlock = false;}
            }
        }

        int finalX, finalZ;

        switch (branchDir){
            case EAST -> {finalX = x; finalZ = -z;}
            case NORTH -> {finalX = -x; finalZ = -z;}
            case WEST -> {finalX = -x; finalZ = z;}
            default -> {finalX = x; finalZ = z;}
        }

        return new FoliagePlacer.FoliageAttachment(blockPos.offset(finalX, y, finalZ),
                (int)(branchHeight * randomSource.nextInt(20, 30) / 10f), false);
    }

    private void placeLogOriented(int x, int y, int z, Direction branchDir, Direction.Axis axis, LevelSimulatedReader levelSimulatedReader,
                                  BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration){

        int finalX, finalZ;

        switch (branchDir){
            case EAST -> {finalX = x; finalZ = -z;}
            case NORTH -> {finalX = -x; finalZ = -z;}
            case WEST -> {finalX = -x; finalZ = z;}
            default -> {finalX = x; finalZ = z;}
        }

        biConsumer.accept(blockPos.offset(finalX, y, finalZ),
                ((BlockState) Function.identity().apply(treeConfiguration.trunkProvider.getState(randomSource, blockPos).setValue(
                        RotatedPillarBlock.AXIS, axis
                ))));
    }

    private float getHeightDistanceYLinear(int branchDistance, int branchRoot, int branchHeight) {
        return (float)(branchHeight - branchRoot) / (float)branchDistance;
    }

    private float getHeightDistanceYExp(int branchDistance, int branchRoot, int branchHeight, float distance) {
        return (float) (Math.exp((distance - branchDistance * 1.25f) / 2) * (branchHeight - branchRoot));
    }

    private float getFinalX(int branchDistance, int branchAngle){
        return (float)Math.cos(Math.toRadians(branchAngle)) * branchDistance;
    }

    private float getFinalZ(int branchDistance, int branchAngle){
        return (float)Math.sin(Math.toRadians(branchAngle)) * branchDistance;
    }

    private float getBranchXZLinear(int branchDistance, int branchAngle) {
        return (Math.max(1f, getFinalZ(branchDistance, branchAngle)) / Math.max(1f, getFinalX(branchDistance, branchAngle)));
    }

    private float getCurrentDistance (float x, float z){
        return (float)Math.hypot(Math.abs(x), Math.abs(z));
    }
}
