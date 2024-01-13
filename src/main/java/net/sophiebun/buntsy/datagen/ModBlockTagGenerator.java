package net.sophiebun.buntsy.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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
                .add(ModBlocks.GENTLIT_PLANKS.get());

        this.tag(ModTags.Blocks.GENTLIT_LOGS)
                .add(ModBlocks.GENTLIT_LOG.get())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get())
                .add(ModBlocks.GENTLIT_WOOD.get())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get());

        this.tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.GENTLIT_FENCE.get());

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.GENTLIT_FENCE_GATE.get());

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
                .add(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get());

        this.tag(BlockTags.DIRT)
                .add(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.GENTLIT_PLANKS.get());
    }
}
