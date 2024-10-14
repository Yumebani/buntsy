package net.sophiebun.buntsy.blocks.custom.minerals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.blocks.ModBlocks;

import java.util.List;
import java.util.Map;

public class ModGrowableMineral extends AmethystClusterBlock {

    public static final List<List<RegistryObject<Block>>> GROWABLE_MINERAL_STAGES = List.of(
            List.of(ModBlocks.SMALL_GROWABLE_AMETHYST_CLUSTER,
                    ModBlocks.MEDIUM_GROWABLE_AMETHYST_CLUSTER,
                    ModBlocks.LARGE_GROWABLE_AMETHYST_CLUSTER,
                    ModBlocks.GROWABLE_AMETHYST_CLUSTER),
            List.of(ModBlocks.SMALL_IRON_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_IRON_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_IRON_CRYSTAL_CLUSTER,
                    ModBlocks.IRON_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_COPPER_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_COPPER_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_COPPER_CRYSTAL_CLUSTER,
                    ModBlocks.COPPER_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_GOLD_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_GOLD_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_GOLD_CRYSTAL_CLUSTER,
                    ModBlocks.GOLD_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_REDSTONE_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_REDSTONE_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_REDSTONE_CRYSTAL_CLUSTER,
                    ModBlocks.REDSTONE_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_LAPIS_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_LAPIS_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_LAPIS_CRYSTAL_CLUSTER,
                    ModBlocks.LAPIS_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_DIAMOND_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_DIAMOND_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_DIAMOND_CRYSTAL_CLUSTER,
                    ModBlocks.DIAMOND_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_EMERALD_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_EMERALD_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_EMERALD_CRYSTAL_CLUSTER,
                    ModBlocks.EMERALD_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_DEBRIS_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_DEBRIS_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_DEBRIS_CRYSTAL_CLUSTER,
                    ModBlocks.DEBRIS_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_QUARTZ_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_QUARTZ_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_QUARTZ_CRYSTAL_CLUSTER,
                    ModBlocks.QUARTZ_CRYSTAL_CLUSTER),
            List.of(ModBlocks.SMALL_GLOWSTONE_CRYSTAL_CLUSTER,
                    ModBlocks.MEDIUM_GLOWSTONE_CRYSTAL_CLUSTER,
                    ModBlocks.LARGE_GLOWSTONE_CRYSTAL_CLUSTER,
                    ModBlocks.GLOWSTONE_CRYSTAL_CLUSTER)
            );

    private byte id;
    private int stage;

    public ModGrowableMineral(byte id, int stage, int pSize, int pOffset, Properties pProperties) {
        super(pSize, pOffset, pProperties);
        this.id = id;
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public List<RegistryObject<Block>> getStages() {
        return GROWABLE_MINERAL_STAGES.get(id);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {

        if (this.stage != 3 && pLevel.random.nextInt(0,3) == 0){
            pLevel.setBlockAndUpdate(pPos, getStages().get(stage + 1).get().defaultBlockState()
                    .setValue(AmethystClusterBlock.FACING, pState.getValue(AmethystClusterBlock.FACING))
                    .setValue(AmethystClusterBlock.WATERLOGGED, pState.getFluidState().getType() == Fluids.WATER));
        }

        super.randomTick(pState, pLevel, pPos, pRandom);
    }
}
