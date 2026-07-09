package net.sophiebun.buntsy.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ClockworkStructuresFeature extends Feature<NoneFeatureConfiguration> {

    private final SimpleWeightedRandomList<Pair<ResourceLocation, ClockworkStructureConfig>> list = new SimpleWeightedRandomList.Builder<Pair<ResourceLocation, ClockworkStructureConfig>>()
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "thing_1"), new ClockworkStructureConfig(-2, 0, 1f, 0.9f)), 120)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "thing_2"), new ClockworkStructureConfig(-2, 0, 1f, 0.9f)), 80)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "thing_3"), new ClockworkStructureConfig(0, 0, 1f, 0.9f)), 40)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "small_gear"), new ClockworkStructureConfig(-3, -2, 1f, 0.8f)), 80)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "medium_gear"), new ClockworkStructureConfig(-5, -3, 1f, 0.8f)), 50)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "large_gear"), new ClockworkStructureConfig(-7, -4, 1f, 0.8f)), 30)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "small_gear_grounded"), new ClockworkStructureConfig(-1, 0, 1f, 0.8f)), 15)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "medium_gear_grounded"), new ClockworkStructureConfig(-1, 0, 1f, 0.8f)), 10)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "silo"), new ClockworkStructureConfig(-5, -2, 0.9f, 0.6f)), 50)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "windup_platform"), new ClockworkStructureConfig(-3, 0, 1f, 0.6f)), 40)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "small_pipe"), new ClockworkStructureConfig(-1, 0, 1f, 0.75f)), 20)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "medium_pipe"), new ClockworkStructureConfig(-2, 0, 1f, 0.75f)), 10)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "large_pipe"), new ClockworkStructureConfig(-3, 0, 1f, 0.75f)), 5)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "small_ruins"), new ClockworkStructureConfig(-2, -1, 1f, 0.85f)), 60)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "medium_ruins"), new ClockworkStructureConfig(-2, -1, 1f, 0.85f)), 40)
            .add(Pair.of(new ResourceLocation(BuntsyMod.MODID, "storage_container"), new ClockworkStructureConfig(-3, -1, 1f, 0.75f)), 10)
            .build();


    private static record ClockworkStructureConfig(int minHeight, int maxHeight, float minDecay, float maxDecay){}


    public ClockworkStructuresFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        StructureTemplateManager templateManager = level.getServer().getStructureManager();

        Optional<Pair<ResourceLocation, ClockworkStructureConfig>> targetTemplateOpt = list.getRandomValue(random);
        if (targetTemplateOpt.isEmpty()) return false;
        Pair<ResourceLocation, ClockworkStructureConfig> targetTemplate = targetTemplateOpt.get();

        Optional<StructureTemplate> templateOptional = templateManager.get(targetTemplate.getFirst());
        if (templateOptional.isEmpty()) return false;
        StructureTemplate template = templateOptional.get();

        float targetDecay = random.nextInt(Math.round(targetTemplate.getSecond().maxDecay * 100), Math.round(targetTemplate.getSecond().minDecay * 100)) / 100f;
        int targetHeightMod = random.nextInt(targetTemplate.getSecond().minHeight, targetTemplate.getSecond().maxHeight + 1);

        StructurePlaceSettings settings = new StructurePlaceSettings();

        Rotation rotation = Rotation.getRandom(random);
        Mirror mirror = random.nextBoolean() ? Mirror.NONE : Mirror.LEFT_RIGHT;

        settings.setRotation(rotation)
                .setMirror(mirror)
                .setIgnoreEntities(true);

        settings.addProcessor(new ClockworkStructureProcessor(new ResourceLocation(BuntsyMod.MODID, "chests/clockwork_ruins"), targetDecay));
        settings.addProcessor(BlockIgnoreProcessor.AIR);

        BlockPos finalPos = template.getZeroPositionWithTransform(pos.offset(0, targetHeightMod, 0), mirror, rotation);

        return template.placeInWorld(level, finalPos, pos, settings, random, 2);
    }

    private static class ClockworkStructureProcessor extends StructureProcessor {

        private final ResourceLocation lootTable;
        private final float decay;

        public ClockworkStructureProcessor(ResourceLocation lootTable, float decay){
            this.lootTable = lootTable;
            this.decay = decay;
        }

        @Override
        public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader pLevel, BlockPos pOffset, BlockPos pPos, StructureTemplate.StructureBlockInfo pBlockInfo, StructureTemplate.StructureBlockInfo pRelativeBlockInfo, StructurePlaceSettings pSettings) {

            if (pRelativeBlockInfo.state().is(Blocks.YELLOW_WOOL)){

                BlockState toPlace = pSettings.getRandom(pRelativeBlockInfo.pos()).nextInt(0 , 3) == 0 ?
                        ModBlocks.CUT_CLOCKWORK_BRASS.get().defaultBlockState() :
                        ModBlocks.CLOCKWORK_BRASS_BLOCK.get().defaultBlockState();

                return new StructureTemplate.StructureBlockInfo(pRelativeBlockInfo.pos(), toPlace, null);

            } else if (pRelativeBlockInfo.state().is(Blocks.ORANGE_WOOL)){

                if (pSettings.getRandom(pRelativeBlockInfo.pos()).nextFloat() < decay){

                    BlockState toPlace = pSettings.getRandom(pRelativeBlockInfo.pos()).nextInt(0 , 3) == 0 ?
                            ModBlocks.CUT_CLOCKWORK_BRASS.get().defaultBlockState() :
                            ModBlocks.CLOCKWORK_BRASS_BLOCK.get().defaultBlockState();
                    return new StructureTemplate.StructureBlockInfo(pRelativeBlockInfo.pos(), toPlace, null);
                } else {
                    return null;
                }
            } else if (pRelativeBlockInfo.state().is(Blocks.CHEST)){
                CompoundTag tag = new CompoundTag();
                tag.putString("LootTable", this.lootTable.toString());
                tag.putLong("LootTableSeed", pSettings.getRandom(pRelativeBlockInfo.pos()).nextLong());
                return new StructureTemplate.StructureBlockInfo(pRelativeBlockInfo.pos(), pRelativeBlockInfo.state(), tag);
            } else {
                if (pSettings.getRandom(pRelativeBlockInfo.pos()).nextFloat() < decay){
                    return pRelativeBlockInfo;
                } else {
                    return null;
                }
            }
        }

        @Override
        protected StructureProcessorType<?> getType() {
            return null;
        }
    }
}
