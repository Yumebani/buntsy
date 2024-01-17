package net.sophiebun.buntsy.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.sophiebun.buntsy.BuntsyMod;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> GENTLIT_LOGS = tag("gentlit_logs");
        public static final TagKey<Block> NEEDS_SIlKY_TOOL = tag("needs_silky_tool");

        public static final TagKey<Block> FAIRY_MINERAL = tag("fairy_mineral");
        public static final TagKey<Block> FAIRY_HARVESTABLES = tag("fully_grown_mineral");

        public static final TagKey<Block> SILKBUN_SPAWNABLE_ON = tag("silkbun_spawnable_on");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(BuntsyMod.MODID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> GENTLIT_LOGS = tag("gentlit_logs");
        public static final TagKey<Item> FAIRY_FOOD = tag("fairy_food");
        public static final TagKey<Item> BOTTLED_ITEM = tag("bottled_item");
        public static final TagKey<Item> BOWL_ITEM = tag("bowl_item");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(BuntsyMod.MODID, name));
        }
    }
}
