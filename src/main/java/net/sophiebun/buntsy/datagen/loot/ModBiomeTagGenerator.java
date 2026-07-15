package net.sophiebun.buntsy.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.tag.ModTags;
import net.sophiebun.buntsy.worldgen.biome.ModBiomes;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagGenerator extends BiomeTagsProvider {

    public ModBiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BuntsyMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Biomes.FAIRY_SPAWN)
                .add(ModBiomes.CUTERLY_BIOME)
                .add(ModBiomes.CANDY_CRAGS_BIOME);

        this.tag(ModTags.Biomes.SILK_BUN_SPAWN)
                .add(ModBiomes.CUTERLY_BIOME);

        this.tag(Tags.Biomes.IS_SNOWY)
                .add(ModBiomes.POWDERY_TUNDRA_BIOME)
                .add(ModBiomes.CHOCOLATE_SPRINGS_BIOME);

        this.tag(Tags.Biomes.IS_DESERT)
                .add(ModBiomes.CANDY_CRAGS_BIOME);

        this.tag(ModTags.Biomes.RUINED_HOUSE_SPAWN)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.FOREST)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.PLAINS)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.CHERRY_GROVE)
                .addTag(Tags.Biomes.IS_LUSH)
                .addTag(Tags.Biomes.IS_CONIFEROUS)
                .addTag(Tags.Biomes.IS_PLAINS);
    }
}
