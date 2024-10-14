package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sophiebun.buntsy.blocks.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class CandyCragPileFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockState SWEET = ModBlocks.SWEET_CANDY_ROCK.get().defaultBlockState();
    private static final BlockState SOUR = ModBlocks.SOUR_CANDY_ROCK.get().defaultBlockState();
    private static final BlockState BITTER = ModBlocks.BITTER_CANDY_ROCK.get().defaultBlockState();

    private static final BlockState[] BLOCK_RANGE = {SWEET, SWEET, SWEET, SOUR, BITTER};
    public CandyCragPileFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        RandomSource randomsource = pContext.random();
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        return this.placeFeature(worldgenlevel, randomsource, blockpos);
    }

    protected boolean placeFeature(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos){

        if (pLevel.getBlockState(pPos).isAir()){

            /*
            int count = pRandom.nextInt(1, 3);

            List<BlockPos> placements = new ArrayList<>();

            int tries = 20;
            while (placements.size() <= count && tries > 0){
                int newX = pRandom.nextInt(4, 8);
                int newZ = pRandom.nextInt(4, 8);
                boolean valid = true;
                for (BlockPos placement : placements){
                    valid &= !isInRadius(6, placement.getX() - newX, placement.getZ() - newZ);
                }
                if (valid) placements.add(pPos.offset(newX, 0, newZ));
                tries--;
            }
            */
            int initDistance = pRandom.nextInt(3, 10);
            int divDistance = (int)Math.max(1, Math.pow(initDistance, 2) / 10);
            int height = pRandom.nextInt(20 / divDistance, 40 / divDistance);
            generatePillar(pLevel, pRandom, pPos.offset(0, 0,0), height, initDistance);
            /*
            List<BlockPos> peaks = new ArrayList<>();

            for (BlockPos newPos : placements){
                peaks.add(generatePillar(pLevel, pRandom, newPos, height, initDistance));
            }

            while (placements.size() > 1){
                BlockPos origin = peaks.get(0);
                BlockPos end = peaks.get(1);

                float calculatedDistance = initDistance * 0.05f * (height - 10);

                generateBridge(pLevel, pRandom, origin, end, calculatedDistance, initDistance);

                placements.remove(0);
            }
            */

            return true;
        }
        else return false;
    }

    private void generateBridge(LevelAccessor pLevel, RandomSource pRandom, BlockPos origin, BlockPos end, float calculatedDistance, int initDistance) {

        double oEdistance = origin.distSqr(end);

        RandomSource heightRandom = pRandom.fork();

        for (int i = 0; i < oEdistance; i++){
            float f = (origin.getX() - end.getX() + 0f) / (origin.getZ() - end.getZ() + 0f);
            int xAnchor = (int) (f * i);
            int zAnchor = (int) ((1 / f) * i);
            BlockPos newPos = origin.offset(xAnchor, (int)((origin.getY() - end.getY() + 0f) * i), zAnchor);
            for (int x = -initDistance; x <= initDistance; x++){
                for (int z = -initDistance; z <= initDistance; z++){
                    for (int y = -initDistance; y <= 0; y++){
                        if (isInSphere(calculatedDistance, x, y, z)){

                            heightRandom.setSeed(newPos.getY());
                            BlockState blockToPlace = BLOCK_RANGE[heightRandom.nextInt(0, 5)];

                            if (y == 0){
                                pLevel.setBlock(newPos.offset(x, y, z), ModBlocks.SWEET_CORAL_SAND.get().defaultBlockState(), 3);
                            }
                            else {
                                pLevel.setBlock(newPos.offset(x, y, z), blockToPlace, 3);
                            }
                        }
                    }
                }
            }
        }
    }


    private void createShelf(LevelAccessor pLevel, RandomSource pRandom, BlockPos pos, int initDistance){

        RandomSource heightRandom = pRandom.fork();

        for (int y = -initDistance; y <= 0; y++){
            for (int x = -initDistance; x <= initDistance; x++){
                for (int z = -initDistance; z <= initDistance; z++){
                    if (isInSphere(initDistance, x, y, z) && pLevel.getBlockState(pos.offset(x, y, z)).isAir()){

                        heightRandom.setSeed(pos.getY() + y);
                        BlockState blockToPlace = BLOCK_RANGE[heightRandom.nextInt(0, 5)];

                        if (y == 0){
                            pLevel.setBlock(pos.offset(x, y, z), ModBlocks.SWEET_CORAL_SAND.get().defaultBlockState(), 3);
                        }
                        else {
                            pLevel.setBlock(pos.offset(x, y, z), blockToPlace, 3);
                        }
                    }
                }
            }
        }
    }

    private BlockPos generatePillar(LevelAccessor pLevel, RandomSource pRandom, BlockPos newPos, int height, int initDistance){
        float distance = initDistance;

        RandomSource heightRandom = pRandom.fork();
        List<BlockPos> shelves = new ArrayList<>();

        for (int y = 0; y <= height; y++){

            if (pRandom.nextInt(0, 3) == 1){
                newPos = newPos.offset(pRandom.nextInt(-1, 2), 0, pRandom.nextInt(-1, 2));
            }

            if (pRandom.nextBoolean()){
                distance -= 0.05f;
            }

            boolean makeShelve = initDistance <= 5 && pRandom.nextInt(0,6) == 5;

            for (int x = -initDistance; x <= initDistance; x++){
                for (int z = -initDistance; z <= initDistance; z++){
                    if (isInRadius(distance, x, z)){

                        int newY = y;
                        while (pLevel.getBlockState(newPos.offset(x, newY - 1, z)).isAir()){
                            newY--;
                        }

                        if (makeShelve && pRandom.nextInt(0, 100) == 25){
                            shelves.add(newPos.offset(x, newY, z));
                        }

                        heightRandom.setSeed(newPos.getY() + newY);
                        BlockState blockToPlace = BLOCK_RANGE[heightRandom.nextInt(0, 5)];

                        if (y == height){
                            pLevel.setBlock(newPos.offset(x, newY, z), ModBlocks.SWEET_CORAL_SAND.get().defaultBlockState(), 3);
                        }
                        else {
                            pLevel.setBlock(newPos.offset(x, newY, z), blockToPlace, 3);
                        }
                    }
                }
            }
        }

        for (BlockPos shelve : shelves){
            createShelf(pLevel, pRandom, shelve, initDistance);
        }

        return newPos;
    }

    private boolean isInRadius(float radius, int x, int z){
        return Math.hypot(Math.abs(z), Math.abs(x)) <= radius;
    }
    private boolean isInSphere(float radius, int x, int y, int z){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) <= radius;
    }
}
