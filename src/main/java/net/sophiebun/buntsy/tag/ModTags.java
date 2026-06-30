package net.sophiebun.buntsy.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sophiebun.buntsy.BuntsyMod;

public class ModTags {

    public static class Blocks {


        public static final TagKey<Block> BUNTSY_CORAL_BLOCKS = tag("buntsy_coral_blocks");
        public static final TagKey<Block> BUNTSY_WALL_CORALS = tag("buntsy_wall_corals");
        public static final TagKey<Block> BUNTSY_CORAL_PLANTS = tag("buntsy_coral_plants");
        public static final TagKey<Block> BUNTSY_CORALS = tag("buntsy_corals");

        public static final TagKey<Block> ODIATE_SOIL = tag("odiate_soil");
        public static final TagKey<Block> FREEZWEET_BLOCKS = tag("freezweet_blocks");

        public static final TagKey<Block> GENTLIT_LOGS = tag("gentlit_logs");
        public static final TagKey<Block> BRAVOT_LOGS = tag("bravot_logs");
        public static final TagKey<Block> MALVOR_LOGS = tag("malvor_logs");
        public static final TagKey<Block> NEEDS_SIlKY_TOOL = tag("needs_silky_tool");

        public static final TagKey<Block> FUME_TICKABLE = tag("fume_tickable");
        public static final TagKey<Block> FAIRY_HARVESTABLE = tag("fairy_harvestable");
        public static final TagKey<Block> FAIRY_INTERACTABLE_BLOCK_ENTITY = tag("fairy_interactable_block_entity");

        public static final TagKey<Block> CUTERLY_SPAWNER = tag("cuterly_spawner");
        public static final TagKey<Block> PRISMATIC_BEACON_EFFECT_BLOCK = tag("prismatic_beacon_effect_block");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(BuntsyMod.MODID, name));
        }
    }

    public static class Items {


        public static final TagKey<Item> GENTLIT_LOGS = tag("gentlit_logs");
        public static final TagKey<Item> BRAVOT_LOGS = tag("bravot_logs");
        public static final TagKey<Item> MALVOR_LOGS = tag("malvor_logs");

        public static final TagKey<Item> FAIRY_FOOD = tag("fairy_food");
        public static final TagKey<Item> BOTTLED_ITEM = tag("bottled_item");
        public static final TagKey<Item> BOWL_ITEM = tag("bowl_item");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(BuntsyMod.MODID, name));
        }
    }
    
    public static class Biomes {


        public static final TagKey<Biome> SILK_BUN_SPAWN = tag("silk_bun_spawn");
        public static final TagKey<Biome> FAIRY_SPAWN = tag("fairy_spawn");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(BuntsyMod.MODID, name));
        }
    }
}
