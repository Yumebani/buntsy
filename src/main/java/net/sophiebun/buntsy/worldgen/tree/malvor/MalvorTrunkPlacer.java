package net.sophiebun.buntsy.worldgen.tree.malvor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.phys.Vec3;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MalvorTrunkPlacer extends TrunkPlacer {

    public static final Codec<MalvorTrunkPlacer> CODEC = RecordCodecBuilder.create(malvorTrunkPlacerInstance ->
            trunkPlacerParts(malvorTrunkPlacerInstance).apply(malvorTrunkPlacerInstance, MalvorTrunkPlacer::new));

    private static final int TRUNK_WIDTH = 10;

    public MalvorTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.MALVOR_TRUNK_PLACER.get();
    }

    protected static void setDirtAt(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig) {
        pBlockSetter.accept(pPos, Blocks.DIRT.defaultBlockState());
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int id, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        setDirtAt(levelSimulatedReader, biConsumer, randomSource, blockPos.below(), treeConfiguration);

        int randomHeight = randomSource.nextInt(baseHeight + heightRandA, baseHeight + heightRandB);
        int trunkRad = ((int) Math.ceil((TRUNK_WIDTH - 5) / 10f));

        for (int x = -trunkRad; x <= trunkRad; x++){
            for (int z = -trunkRad; z <= trunkRad; z++){
                for (int i = 0; i < randomHeight; i++){
                    if (isInRadius(TRUNK_WIDTH, x, z)){
                        placeLog(levelSimulatedReader, biConsumer, randomSource, blockPos.offset(x, i, z), treeConfiguration);
                    }
                }
            }
        }

        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<FoliagePlacer.FoliageAttachment>();

        int largeBranchCount = randomSource.nextInt(3, 4);
        int branchRoot, branchDistance, branchHeight, branchAngle;

        Deque<Integer> angles = new ArrayDeque<>();
        angles.push(randomSource.nextInt(0, 360));
        int avgAngle = 360 / largeBranchCount;
        int leftOver = 0;
        for (int i = 1; i < largeBranchCount; i++){
            int lastAngle = angles.peekLast();
            int genAngle = randomSource.nextInt(Math.round(avgAngle * 0.85f), Math.round(avgAngle * 0.15f) + avgAngle);
            int finalAngle = lastAngle + genAngle + leftOver;
            angles.add(finalAngle >= 360 ? finalAngle - 360 : finalAngle);
            leftOver = avgAngle - genAngle;
        }

        for (int i = 0; i < largeBranchCount; i++){

            branchRoot = randomSource.nextInt(Math.round(randomHeight * 0.75f), Math.round(randomHeight * 0.95f));
            branchDistance = randomSource.nextInt(Math.round(baseHeight * 0.35f), Math.round(baseHeight * 0.45f));
            branchHeight = branchRoot + randomSource.nextInt(Math.round(baseHeight * 0.25f), Math.round(baseHeight * 0.35f));
            branchAngle = angles.removeFirst();

            attachments.add(makeBranch(true, branchRoot, branchDistance, branchHeight, branchAngle, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration));
        }

        int smallBranchCount = randomSource.nextInt(3, 5);

        angles = new ArrayDeque<>();
        angles.push(randomSource.nextInt(0, 360));
        avgAngle = 360 / smallBranchCount;
        leftOver = 0;
        for (int i = 1; i < smallBranchCount; i++){
            int lastAngle = angles.peekLast();
            int genAngle = randomSource.nextInt(Math.round(avgAngle * 0.85f), Math.round(avgAngle * 0.15f) + avgAngle);
            int finalAngle = lastAngle + genAngle + leftOver;
            angles.add(finalAngle >= 360 ? finalAngle - 360 : finalAngle);
            leftOver = avgAngle - genAngle;
        }

        for (int i = 0; i < smallBranchCount; i++){

            branchRoot = randomSource.nextInt(Math.round(randomHeight * 0.2f), Math.round(randomHeight * 0.8f));
            branchDistance = randomSource.nextInt(Math.round(baseHeight * 0.3f), Math.round(baseHeight * 0.4f));
            branchHeight = branchRoot + randomSource.nextInt(Math.round(baseHeight * 0.2f), Math.round(baseHeight * 0.3f));
            branchAngle = angles.removeFirst();

            attachments.add(makeBranch(false, branchRoot, branchDistance, branchHeight, branchAngle, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration));
        }

        return attachments;
    }

    private FoliagePlacer.FoliageAttachment makeBranch(boolean bigBranch, int branchRoot, int branchDistance, int branchHeight, int branchAngle,
                                                       LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        int angle = branchAngle % 90;

        Direction branchDir;
        switch ((int)Math.floor(branchAngle / 90f) * 90){
            case 0 -> branchDir = Direction.SOUTH;
            case 90 -> {branchDir = Direction.WEST; angle = 90 - angle;}
            case 180 -> branchDir = Direction.NORTH;
            default -> {branchDir = Direction.EAST; angle = 90 - angle;}
        }

        Direction.Axis branchAxis;
        switch ((int)Math.floor((branchAngle + 45) / 90f) * 90){
            case 0, 180 -> branchAxis = Direction.Axis.X;
            default -> branchAxis = Direction.Axis.Z;
        }

        double branchXZLinear = Math.tan(Math.toRadians(angle));
        boolean useZ = Math.abs(branchXZLinear) < 1;
        double heightYLinear = (double)(branchHeight - branchRoot) / branchDistance;

        int branchTotalLength = (int) Math.hypot(branchDistance * 10, (branchHeight - branchRoot) * 10);

        int x = 0, yBase = branchRoot * 10, y = 0, z = 0, finalX = 0, finalZ = 0, finalY = 0;
        int currentDistanceXZ;

        while (getCurrentDistance(x, y, z) <= branchTotalLength){

            switch (branchDir){
                case EAST -> {finalX = x; finalZ = -z;}
                case NORTH -> {finalX = -x; finalZ = -z;}
                case WEST -> {finalX = -x; finalZ = z;}
                default -> {finalX = x; finalZ = z;}
            }

            finalX = Math.round(finalX / 10f);
            finalY = Math.round((y + yBase) / 10f);
            finalZ = Math.round(finalZ / 10f);

            if (TreeFeature.validTreePos(levelSimulatedReader, blockPos.offset(finalX, finalY, finalZ))){
                placeLogOriented(finalX, finalY, finalZ, branchAxis, levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration);
            }

            if (useZ){
                x ++;
                z = (int)Math.round(branchXZLinear * x);
            } else {
                z ++;
                x = (int)Math.round((1f / branchXZLinear) * z);
            }
            currentDistanceXZ = getCurrentDistanceXZ(x, z);
            y = (int)Math.round(heightYLinear * currentDistanceXZ);
        }

        return new FoliagePlacer.FoliageAttachment(blockPos.offset(finalX, finalY, finalZ),
                (branchTotalLength * 4), bigBranch);
    }

    private boolean isInRadius(int radius, int x, int y){
        return Math.hypot(Math.abs(x), Math.abs(y)) * 10 <= radius;
    }

    private void placeLogOriented(int x, int y, int z, Direction.Axis axis, LevelSimulatedReader levelSimulatedReader,
                                  BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration){

        biConsumer.accept(blockPos.offset(x, y, z),
                ((BlockState) Function.identity().apply(treeConfiguration.trunkProvider.getState(randomSource, blockPos).setValue(
                        RotatedPillarBlock.AXIS, axis
                ))));
    }

    private int getCurrentDistance (int x, int y, int z){
        return (int) Math.hypot(y, Math.hypot(Math.abs(x), Math.abs(z)));
    }

    private int getCurrentDistanceXZ (int x, int z){
        return (int) Math.hypot(Math.abs(x), Math.abs(z));
    }
}
