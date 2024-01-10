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
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.SILKY_HELMET.get(),
                        ModItems.SILKY_CHESTPLATE.get(),
                        ModItems.SILKY_LEGGINGS.get(),
                        ModItems.SILKY_BOOTS.get());

        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.GENTLIT_LOG.get().asItem())
                .add(ModBlocks.GENTLIT_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_GENTLIT_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_GENTLIT_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.GENTLIT_PLANKS.get().asItem());

        this.tag(ModTags.Items.FAIRY_FOOD)
                .add(ModItems.GENTLIT_SYRUP.get().asItem())
                .add(Items.SUGAR)
                .add(Items.HONEY_BOTTLE);

        this.tag(ModTags.Items.BOTTLED_ITEM)
                .add(ModItems.GENTLIT_SYRUP.get().asItem())
                .add(Items.HONEY_BOTTLE);
    }
}
