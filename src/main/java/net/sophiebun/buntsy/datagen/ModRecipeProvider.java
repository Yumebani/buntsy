package net.sophiebun.buntsy.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
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

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        planksRecipe(ModBlocks.GENTLIT_LOG.get(), ModBlocks.GENTLIT_PLANKS.get(), consumer);
        //trapdoorRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_TRAPDOOR.get(), consumer);
        compact2By2(ModItems.SILK_SPOOL.get(), ModItems.SILK_FABRIC.get(), 2, consumer);

        //Silky solids
        upgradeSmithing(ModItems.FAIRY_DUST.get(), Items.IRON_INGOT, ModItems.TOUGH_SILK_FABRIC.get(), ModItems.SILKY_INGOT.get(), consumer);
        upgradeSmithing(ModItems.FAIRY_DUST.get(), Items.DIAMOND, ModItems.TOUGH_SILK_FABRIC.get(), ModItems.SILKY_CRYSTAL.get(), consumer);
        compact3By3(ModItems.SILKY_NUGGET.get(), ModItems.SILKY_INGOT.get(), 1, consumer);
        uncompact(ModItems.SILKY_INGOT.get(), ModItems.SILKY_NUGGET.get(), 9, consumer);

        //Foods
        cookingFoodRecipe(ModItems.SUGAR_BOWL.get(), ModItems.BOWL_OF_CARAMEL.get(), 0.35f, consumer);
        cookingFoodRecipe(ModItems.SYRUPY_MIXTURE_BOWL.get(), ModItems.BOWL_OF_ROCKCANDY.get(), 0.35f, consumer);

        //Silk spool
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILK_SPOOL.get(),1)
                .define('A', ModItems.SILK.get())
                .define('B', Items.STICK)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .unlockedBy(getHasName(ModItems.SILK.get()), has(ModItems.SILK.get()))
                .save(consumer);

        //Tough silk fabric
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOUGH_SILK_FABRIC.get(),1)
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

    private void cookingFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer){
        smeltingFoodRecipe(material, result, experience, consumer);
        smokingFoodRecipe(material, result, experience, consumer);
        campfireFoodRecipe(material, result, experience, consumer);
    }

    private void smeltingFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer){
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.FOOD, result, experience, 200, RecipeSerializer.SMELTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smelting");
    }

    private void smokingFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer){
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.FOOD, result, experience, 100, RecipeSerializer.SMOKING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smoking");
    }

    private void campfireFoodRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer){
        SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{material}),
                        RecipeCategory.FOOD, result, experience, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_campfire_cooking");
    }

    private void compact2By2(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void compact3By3(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
                .define('#', material)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void uncompact(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer){
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count)
                .requires(material, 1)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void upgradeSmithing(ItemLike template, ItemLike upgradable, ItemLike material, Item result, Consumer<FinishedRecipe> consumer){
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(new ItemLike[]{template}),
                Ingredient.of(new ItemLike[]{upgradable}),
                Ingredient.of(new ItemLike[]{material}),
                RecipeCategory.MISC, result)
                .unlocks("has_" + getItemName(material), has(material))
                .unlocks("has_" + getItemName(template), has(template))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_smithing");
    }

    private void planksRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer){
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 8)
                .requires(material, 1)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void trapdoorRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 2)
                .define('#', material)
                .pattern("   ")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }
}
