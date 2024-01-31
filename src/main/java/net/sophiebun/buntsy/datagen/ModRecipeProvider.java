package net.sophiebun.buntsy.datagen;

import com.mojang.datafixers.functions.PointFreeRule;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        //Wood recipes
        planksRecipe(ModTags.Items.GENTLIT_LOGS, ModBlocks.GENTLIT_PLANKS.get(), consumer);
        stairsRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_STAIRS.get(), consumer);
        trapdoorRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_TRAPDOOR.get(), consumer);
        slabRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_SLAB.get(), consumer);
        doorRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_DOOR.get(), consumer);
        fenceRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_FENCE.get(), consumer);
        fencegateRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_FENCE_GATE.get(), consumer);
        pressurePlateRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_PRESSURE_PLATE.get(), consumer);
        buttonRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_BUTTON.get(), consumer);

        planksRecipe(ModTags.Items.BRAVOT_LOGS, ModBlocks.BRAVOT_PLANKS.get(), consumer);
        stairsRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_STAIRS.get(), consumer);
        trapdoorRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_TRAPDOOR.get(), consumer);
        slabRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_SLAB.get(), consumer);
        doorRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_DOOR.get(), consumer);
        fenceRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_FENCE.get(), consumer);
        fencegateRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_FENCE_GATE.get(), consumer);
        pressurePlateRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_PRESSURE_PLATE.get(), consumer);
        buttonRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_BUTTON.get(), consumer);

        //Silk fabric
        compact2By2(ModItems.SILK_SPOOL.get(), ModItems.SILK_FABRIC.get(), 2, consumer);

        //Wool
        compact2By2(ModItems.SILK_FABRIC.get(), Items.WHITE_WOOL, 2, consumer);

        //Mineral shards
        compact2By2(ModItems.DIAMOND_SHARD.get(), Items.DIAMOND, 1, consumer);
        compact2By2(ModItems.EMERALD_SHARD.get(), Items.EMERALD, 1, consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.NETHERITE_SCRAP, 1)
                .requires(ModItems.DEBRIS_SHARD.get(), 3)
                .unlockedBy(getHasName(ModItems.DEBRIS_SHARD.get()), has(ModItems.DEBRIS_SHARD.get()))
                .save(consumer);

        //Smelting dust
        smeltingRecipe(ModItems.IRON_DUST.get(), Items.IRON_INGOT, 0.35f, consumer);
        smeltingRecipe(ModItems.COPPER_DUST.get(), Items.COPPER_INGOT, 0.35f, consumer);
        smeltingRecipe(ModItems.GOLD_DUST.get(), Items.GOLD_INGOT, 0.35f, consumer);
        smeltingRecipe(ModItems.NETHERITE_DUST.get(), Items.NETHERITE_SCRAP, 0.35f, consumer);

        //Silky solids
        upgradeSmithing(ModItems.FAIRY_DUST.get(), Items.IRON_INGOT, ModItems.TOUGH_SILK_FABRIC.get(), ModItems.SILKY_INGOT.get(), consumer);
        upgradeSmithing(ModItems.FAIRY_DUST.get(), Items.DIAMOND, ModItems.TOUGH_SILK_FABRIC.get(), ModItems.SILKY_CRYSTAL.get(), consumer);
        compact3By3(ModItems.SILKY_NUGGET.get(), ModItems.SILKY_INGOT.get(), 1, consumer);
        uncompact(ModItems.SILKY_INGOT.get(), ModItems.SILKY_NUGGET.get(), 9, consumer);

        //Foods
        cookingFoodRecipe(ModItems.SUGAR_BOWL.get(), ModItems.BOWL_OF_CARAMEL.get(), 0.35f, consumer);
        cookingFoodRecipe(ModItems.SYRUPY_MIXTURE_BOWL.get(), ModItems.BOWL_OF_ROCKCANDY.get(), 0.35f, consumer);

        //Silk spool
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILK_SPOOL.get(), 1)
                .define('A', ModItems.SILK.get())
                .define('B', Items.STICK)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .unlockedBy(getHasName(ModItems.SILK.get()), has(ModItems.SILK.get()))
                .save(consumer);


        //Tough silk fabric
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOUGH_SILK_FABRIC.get(), 1)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('B', ModItems.MOTH_WING_THREAD.get())
                .pattern("ABA")
                .pattern("BAB")
                .pattern("ABA")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .unlockedBy(getHasName(ModItems.MOTH_WING_THREAD.get()), has(ModItems.MOTH_WING_THREAD.get()))
                .save(consumer);

        //Sugar bowl
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SUGAR_BOWL.get(), 1)
                .requires(Items.SUGAR, 4)
                .requires(Items.BOWL, 1)
                .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
                .save(consumer);

        //Caramel Strawberries
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CARAMEL_STRAWBERRIES.get(), 1)
                .requires(ModItems.STRAWBERRY.get(), 3)
                .requires(ModItems.BOWL_OF_CARAMEL.get(), 1)
                .unlockedBy(getHasName(ModItems.STRAWBERRY.get()), has(ModItems.STRAWBERRY.get()))
                .unlockedBy(getHasName(ModItems.BOWL_OF_CARAMEL.get()), has(ModItems.BOWL_OF_CARAMEL.get()))
                .save(consumer);

        //Syrupy bowl
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SYRUPY_MIXTURE_BOWL.get(), 1)
                .requires(Items.SUGAR, 4)
                .requires(Items.HONEY_BOTTLE, 1)
                .requires(ModItems.GENTLIT_SYRUP.get(), 1)
                .requires(Items.BOWL, 1)
                .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
                .unlockedBy(getHasName(Items.HONEY_BOTTLE), has(Items.HONEY_BOTTLE))
                .unlockedBy(getHasName(ModItems.GENTLIT_SYRUP.get()), has(ModItems.GENTLIT_SYRUP.get()))
                .save(consumer);
    }

    private void cookingFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        smeltingFoodRecipe(material, result, experience, consumer);
        smokingFoodRecipe(material, result, experience, consumer);
        campfireFoodRecipe(material, result, experience, consumer);
    }

    private void smeltingFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.FOOD, result, experience, 200, RecipeSerializer.SMELTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smelting");
    }

    private void smokingFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.FOOD, result, experience, 100, RecipeSerializer.SMOKING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smoking");
    }

    private void campfireFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.FOOD, result, experience, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_campfire_cooking");
    }

    private void smeltingRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.MISC, result, experience, 200, RecipeSerializer.SMELTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smelting");
    }

    private void compact2By2(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void compact3By3(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
                .define('#', material)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void uncompact(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count)
                .requires(material, 1)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void upgradeSmithing(ItemLike template, ItemLike upgradable, ItemLike material, Item result, Consumer<FinishedRecipe> consumer) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(new ItemLike[]{template}),
                        Ingredient.of(new ItemLike[]{upgradable}),
                        Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.MISC, result)
                .unlocks("has_" + getItemName(material), has(material))
                .unlocks("has_" + getItemName(template), has(template))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_smithing");
    }

    private void planksRecipe(TagKey<Item> tags, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 4)
                .requires(Ingredient.of(tags), 1)
                .unlockedBy(tags.toString(), has(tags))
                .save(consumer);
    }

    private void trapdoorRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 2)
                .define('#', material)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void slabRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 6)
                .define('#', material)
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void stairsRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 4)
                .define('#', material)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void doorRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void fenceRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .define('S', Items.STICK)
                .pattern("#S#")
                .pattern("#S#")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void fencegateRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .define('S', Items.STICK)
                .pattern("S#S")
                .pattern("S#S")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void pressurePlateRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void buttonRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 1)
                .requires(Ingredient.of(material), 1)
                .unlockedBy(material.toString(), has(material))
                .save(consumer);
    }
}
