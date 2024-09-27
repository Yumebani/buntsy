package net.sophiebun.buntsy.worldgen.tree.gentlit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.worldgen.tree.ModTrunkPlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GentlitTrunkPlacer extends TrunkPlacer {

    public static final Codec<GentlitTrunkPlacer> CODEC = RecordCodecBuilder.create(gentlitTrunkPlacerInstance ->
            trunkPlacerParts(gentlitTrunkPlacerInstance).apply(gentlitTrunkPlacerInstance, GentlitTrunkPlacer::new));

    public GentlitTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.GENTLIT_TRUNK_PLACER.get();
    }

    protected static void setDirtAt(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig) {
        pBlockSetter.accept(pPos, ModBlocks.CHARMIL_SOIL.get().defaultBlockState());
    }
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int id, BlockPos blockPos, TreeConfiguration treeConfiguration) {

        setDirtAt(levelSimulatedReader, biConsumer, randomSource, blockPos.below(), treeConfiguration);

        int randomHeight = randomSource.nextInt(baseHeight + heightRandA, baseHeight + heightRandB);

        int foliageStartHeight = (int)(randomHeight * 0.7f) / 2;
        int foliageEndHeight = (int)Math.ceil(foliageStartHeight * 4f);
        int foliageLogDistance = foliageStartHeight + foliageEndHeight / 5;
        float foliageMaxGirth = (float)Math.log(foliageLogDistance - foliageStartHeight) * randomHeight;
        float foliageLinear = (0 - ((float)Math.ceil(foliageMaxGirth)) / (foliageEndHeight - foliageLogDistance));

        for (int i = 0; i < randomHeight; i++){
            placeLog(levelSimulatedReader, biConsumer, randomSource, blockPos.above(i), treeConfiguration);
        }

        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<FoliagePlacer.FoliageAttachment>();
        int sizeVar = 0;

        for (int i = foliageStartHeight; i < foliageEndHeight; i++){

            if (i <= foliageLogDistance){
                sizeVar = (int)Math.ceil(Math.log(i - foliageStartHeight) * randomHeight);
            }
            else {
                sizeVar = (int)Math.ceil(foliageLinear * (i - foliageLogDistance) + foliageMaxGirth);
            }

            attachments.add(new FoliagePlacer.FoliageAttachment(blockPos.above(i), sizeVar, false));
        }

        return attachments;
    }
}
