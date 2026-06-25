package net.sophiebun.buntsy.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {

    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, BuntsyMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(ItemTags.HOES).add(ModItems.SILKY_HOE.get());
        this.tag(ItemTags.SWORDS).add(ModItems.SILKY_SWORD.get());
        this.tag(ItemTags.PICKAXES).add(ModItems.SILKY_PICKAXE.get());
        this.tag(ItemTags.SHOVELS).add(ModItems.SILKY_SHOVEL.get());
        this.tag(ItemTags.AXES).add(ModItems.SILKY_AXE.get());

        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.GENTLIT_LOG.get().asItem())
                .add(ModBlocks.GENTLIT_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get().asItem())
                .add(ModBlocks.BRAVOT_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_BRAVOT_LOG.get().asItem())
                .add(ModBlocks.BRAVOT_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_BRAVOT_WOOD.get().asItem())
                .add(ModBlocks.MALVOR_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_MALVOR_LOG.get().asItem())
                .add(ModBlocks.MALVOR_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_MALVOR_WOOD.get().asItem());

        this.tag(ModTags.Items.GENTLIT_LOGS)
                .add(ModBlocks.GENTLIT_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get().asItem())
                .add(ModBlocks.GENTLIT_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get().asItem());

        this.tag(ModTags.Items.BRAVOT_LOGS)
                .add(ModBlocks.BRAVOT_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_BRAVOT_LOG.get().asItem())
                .add(ModBlocks.BRAVOT_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_BRAVOT_WOOD.get().asItem());

        this.tag(ModTags.Items.MALVOR_LOGS)
                .add(ModBlocks.MALVOR_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_MALVOR_LOG.get().asItem())
                .add(ModBlocks.MALVOR_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_MALVOR_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.GENTLIT_PLANKS.get().asItem())
                .add(ModBlocks.BRAVOT_PLANKS.get().asItem())
                .add(ModBlocks.MALVOR_PLANKS.get().asItem());

        this.tag(ModTags.Items.FAIRY_FOOD)
                .add(ModItems.GENTLIT_SYRUP.get().asItem())
                .add(Items.SUGAR)
                .add(Items.HONEY_BOTTLE)
                .add(ModItems.BOWL_OF_CARAMEL.get().asItem())
                .add(ModItems.SUGAR_BOWL.get().asItem())
                .add(ModItems.SYRUPY_MIXTURE_BOWL.get().asItem())
                .add(ModItems.BOWL_OF_ROCKCANDY.get().asItem());

        this.tag(ModTags.Items.BOTTLED_ITEM)
                .add(ModItems.GENTLIT_SYRUP.get().asItem())
                .add(Items.HONEY_BOTTLE);

        this.tag(ModTags.Items.BOWL_ITEM)
                .add(ModItems.BOWL_OF_CARAMEL.get().asItem())
                .add(ModItems.SUGAR_BOWL.get().asItem())
                .add(ModItems.SYRUPY_MIXTURE_BOWL.get().asItem())
                .add(ModItems.BOWL_OF_ROCKCANDY.get().asItem());
    }
}
