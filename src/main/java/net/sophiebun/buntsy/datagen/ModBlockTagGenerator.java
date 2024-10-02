package net.sophiebun.buntsy.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BuntsyMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.GENTLIT_LOG.get())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get())
                .add(ModBlocks.GENTLIT_WOOD.get())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get())
                .add(ModBlocks.GENTLIT_PLANKS.get())
                .add(ModBlocks.GENTLIT_TRAPDOOR.get())
                .add(ModBlocks.GENTLIT_BUTTON.get())
                .add(ModBlocks.GENTLIT_STAIRS.get())
                .add(ModBlocks.GENTLIT_SLAB.get())
                .add(ModBlocks.GENTLIT_DOOR.get())
                .add(ModBlocks.BRAVOT_PLANKS.get())
                .add(ModBlocks.BRAVOT_TRAPDOOR.get())
                .add(ModBlocks.BRAVOT_BUTTON.get())
                .add(ModBlocks.BRAVOT_STAIRS.get())
                .add(ModBlocks.BRAVOT_SLAB.get())
                .add(ModBlocks.BRAVOT_DOOR.get())
                .add(ModBlocks.LOVESHROOM_BLOCK.get())
                .add(ModBlocks.GLOWSHROOM_BLOCK.get())
                .add(ModBlocks.FAIRY_OFFERING_BENCH.get())
                .add(ModBlocks.FAIRY_COLLECTION_TRAY.get())
                .add(ModBlocks.FAIRY_INFUSION_BENCH.get())
                .add(ModBlocks.THREAD_REELER.get());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.CHARMIL_SOIL.get())
                .add(ModBlocks.CHARMIL_FARMLAND.get())
                .add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MAGIC_CRYSTALIZER.get())
                .add(ModBlocks.FUME_DISTILLERY.get())
                .add(ModBlocks.FUME_SPREADER.get())
                .add(ModBlocks.INFUSION_PEDESTAL.get())
                .add(ModBlocks.FAIRY_POWER_RELAY.get())
                .add(ModBlocks.INFUSION_ALTAR_BASIC.get())
                .add(ModBlocks.GRINDING_WHEEL.get());

        this.tag(ModTags.Blocks.GENTLIT_LOGS)
                .add(ModBlocks.GENTLIT_LOG.get())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get())
                .add(ModBlocks.GENTLIT_WOOD.get())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get());

        this.tag(ModTags.Blocks.BRAVOT_LOGS)
                .add(ModBlocks.BRAVOT_LOG.get())
                .add(ModBlocks.STRIPPED_BRAVOT_LOG.get())
                .add(ModBlocks.BRAVOT_WOOD.get())
                .add(ModBlocks.STRIPPED_BRAVOT_WOOD.get());

        this.tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.GENTLIT_FENCE.get())
                .add(ModBlocks.BRAVOT_FENCE.get());

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.GENTLIT_FENCE_GATE.get())
                .add(ModBlocks.BRAVOT_FENCE_GATE.get());

        this.tag(BlockTags.LEAVES)
                .add(ModBlocks.GENTLIT_LEAVES.get())
                .add(ModBlocks.BRAVOT_LEAVES.get());

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.GENTLIT_LOG.get())
                .add(ModBlocks.GENTLIT_WOOD.get())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get())
                .add(ModBlocks.BRAVOT_LOG.get())
                .add(ModBlocks.BRAVOT_WOOD.get())
                .add(ModBlocks.STRIPPED_BRAVOT_LOG.get())
                .add(ModBlocks.STRIPPED_BRAVOT_WOOD.get());

        this.tag(BlockTags.LOGS)
                .add(ModBlocks.GENTLIT_LOG.get())
                .add(ModBlocks.GENTLIT_WOOD.get())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get())
                .add(ModBlocks.BRAVOT_LOG.get())
                .add(ModBlocks.BRAVOT_WOOD.get())
                .add(ModBlocks.STRIPPED_BRAVOT_LOG.get())
                .add(ModBlocks.STRIPPED_BRAVOT_WOOD.get());

        this.tag(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get())
                .add(ModBlocks.CHARMIL_SOIL.get());

        this.tag(BlockTags.DIRT)
                .add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get())
                .add(ModBlocks.CHARMIL_SOIL.get());

        this.tag(BlockTags.ANIMALS_SPAWNABLE_ON)
                .add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get())
                .add(ModBlocks.CHARMIL_SOIL.get());

        this.tag(ModTags.Blocks.CUTERLY_SPAWNER)
                .add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get())
                .add(ModBlocks.CHARMIL_SOIL.get());

        this.tag(BlockTags.VALID_SPAWN)
                .add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get())
                .add(ModBlocks.CHARMIL_SOIL.get());

        this.tag(ModTags.Blocks.FAIRY_INTERACTABLE_BLOCK_ENTITY)
                .add(ModBlocks.FAIRY_OFFERING_BENCH.get())
                .add(ModBlocks.GRINDING_WHEEL.get())
                .add(ModBlocks.THREAD_REELER.get())
                .add(ModBlocks.FAIRY_COLLECTION_TRAY.get())
                .add(ModBlocks.FAIRY_INFUSION_BENCH.get())
                .add(ModBlocks.FUME_DISTILLERY.get())
                .add(ModBlocks.FAIRY_POWER_RELAY.get())
                .add(ModBlocks.MAGIC_CRYSTALIZER.get());

        this.tag(ModTags.Blocks.FAIRY_HARVESTABLE)
                .addTag(BlockTags.FLOWERS)
                .add(ModBlocks.GROWABLE_AMETHYST_CLUSTER.get())
                .add(ModBlocks.IRON_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.COPPER_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.GOLD_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.REDSTONE_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.LAPIS_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.DIAMOND_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.EMERALD_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.QUARTZ_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.GLOWSTONE_CRYSTAL_CLUSTER.get())
                .add(ModBlocks.DEBRIS_CRYSTAL_CLUSTER.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.GENTLIT_PLANKS.get());

        this.tag(ModTags.Blocks.FUME_TICKABLE)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(Blocks.SUGAR_CANE);
    }
}
