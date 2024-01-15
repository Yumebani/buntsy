package net.sophiebun.buntsy.worldgen.tree.grounded_trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GroundedTrunkPlacer extends TrunkPlacer {

    public static final Codec<GroundedTrunkPlacer> CODEC = RecordCodecBuilder.create(groundedTrunkPlacerInstance ->
            trunkPlacerParts(groundedTrunkPlacerInstance).apply(groundedTrunkPlacerInstance, GroundedTrunkPlacer::new));

    private static final Direction.Axis[] validAxis = new Direction.Axis[]{
            Direction.Axis.X,
            Direction.Axis.Z
    };

    public GroundedTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.GROUNDED_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int id, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        int randomLength = randomSource.nextInt(baseHeight + heightRandA, baseHeight + heightRandB);
        Direction.Axis randomAxis = validAxis[randomSource.nextInt(0,2)];
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<FoliagePlacer.FoliageAttachment>();

        for (int i = -randomLength; i <= randomLength; i++){

            biConsumer.accept(blockPos.relative(randomAxis, i),
                    ((BlockState) Function.identity().apply(treeConfiguration.trunkProvider.getState(randomSource, blockPos).setValue(
                            RotatedPillarBlock.AXIS, randomAxis))));

        }

        return attachments;
    }
}
