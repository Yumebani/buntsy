package net.sophiebun.buntsy.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootDataResolver;
import net.minecraft.world.level.storage.loot.LootTable;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.entityblocks.InfusionAltarAdvanced;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.recipe.*;
import net.sophiebun.buntsy.screen.*;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIBuntsyPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BuntsyMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrindingWheelCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ThreadReelerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FairyOfferingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FairyInfusionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MagicCrystalizerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FumeDistilleryCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new InfusionAltarBasicCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new InfusionAltarAdvancedCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MixerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ClockworkCollectorCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GRINDING_WHEEL.get()), GrindingWheelCategory.GRINDING_WHEEL_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.THREAD_REELER.get()), ThreadReelerCategory.THREAD_REELER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FAIRY_OFFERING_BENCH.get()), FairyOfferingCategory.FAIRY_OFFERING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FAIRY_INFUSION_BENCH.get()), FairyInfusionCategory.FAIRY_INFUSION_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MAGIC_CRYSTALIZER.get()), MagicCrystalizerCategory.MAGIC_CRYSTALIZER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FUME_DISTILLERY.get()), FumeDistilleryCategory.FUME_DISTILLERY_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_ALTAR_BASIC.get()), InfusionAltarBasicCategory.INFUSION_ALTAR_BASIC_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_ALTAR_ADVANCED.get()), InfusionAltarBasicCategory.INFUSION_ALTAR_BASIC_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_PEDESTAL.get()), InfusionAltarBasicCategory.INFUSION_ALTAR_BASIC_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_PEDESTAL.get()), InfusionAltarBasicCategory.INFUSION_ALTAR_BASIC_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MIXER_BLOCK.get()), MixerCategory.MIXER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLOCKWORK_FISHER.get()), ClockworkCollectorCategory.CLOCKWORK_COLLECTOR_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLOCKWORK_GEYSER_COLLECTOR.get()), ClockworkCollectorCategory.CLOCKWORK_COLLECTOR_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLOCKWORK_POWDERED_SUGAR_COLLECTOR.get()), ClockworkCollectorCategory.CLOCKWORK_COLLECTOR_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLOCKWORK_SYRUP_EXTRACTOR.get()), ClockworkCollectorCategory.CLOCKWORK_COLLECTOR_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<GrindingWheelRecipe> grindingWheelRecipes = recipeManager.getAllRecipesFor(GrindingWheelRecipe.Type.INSTANCE);
        registration.addRecipes(GrindingWheelCategory.GRINDING_WHEEL_RECIPE_TYPE, grindingWheelRecipes);

        List<ThreadReelerRecipe> threadReelerRecipes = recipeManager.getAllRecipesFor(ThreadReelerRecipe.Type.INSTANCE);
        registration.addRecipes(ThreadReelerCategory.THREAD_REELER_RECIPE_TYPE, threadReelerRecipes);

        List<FairyOfferingRecipe> fairyOfferingRecipes = recipeManager.getAllRecipesFor(FairyOfferingRecipe.Type.INSTANCE);
        registration.addRecipes(FairyOfferingCategory.FAIRY_OFFERING_RECIPE_TYPE, fairyOfferingRecipes);

        List<FairyInfusionRecipe> fairyInfusionRecipes = recipeManager.getAllRecipesFor(FairyInfusionRecipe.Type.INSTANCE);
        registration.addRecipes(FairyInfusionCategory.FAIRY_INFUSION_RECIPE_TYPE, fairyInfusionRecipes);

        List<MagicCrystalizerRecipe> magicCrystalizerRecipes = recipeManager.getAllRecipesFor(MagicCrystalizerRecipe.Type.INSTANCE);
        registration.addRecipes(MagicCrystalizerCategory.MAGIC_CRYSTALIZER_RECIPE_TYPE, magicCrystalizerRecipes);

        List<FumeDistilleryRecipe> fumeDistilleryRecipes = recipeManager.getAllRecipesFor(FumeDistilleryRecipe.Type.INSTANCE);
        registration.addRecipes(FumeDistilleryCategory.FUME_DISTILLERY_RECIPE_TYPE, fumeDistilleryRecipes);

        List<InfusionAltarBasicRecipe> infusionAltarBasicCategories = recipeManager.getAllRecipesFor(InfusionAltarBasicRecipe.Type.INSTANCE);
        registration.addRecipes(InfusionAltarBasicCategory.INFUSION_ALTAR_BASIC_RECIPE_TYPE, infusionAltarBasicCategories);

        List<InfusionAltarAdvancedRecipe> infusionAltarAdvancedRecipes = recipeManager.getAllRecipesFor(InfusionAltarAdvancedRecipe.Type.INSTANCE);
        registration.addRecipes(InfusionAltarAdvancedCategory.INFUSION_ALTAR_ADVANCED_RECIPE_TYPE, infusionAltarAdvancedRecipes);

        List<MixerRecipe> mixerRecipes = recipeManager.getAllRecipesFor(MixerRecipe.Type.INSTANCE);
        registration.addRecipes(MixerCategory.MIXER_RECIPE_TYPE, mixerRecipes);

        registration.addRecipes(ClockworkCollectorCategory.CLOCKWORK_COLLECTOR_RECIPE_TYPE, getCollectorRecipes());
    }

    private List<ClockworkCollectorRecipe> getCollectorRecipes(){
        List<ClockworkCollectorRecipe> list = new ArrayList<>();

        list.add(getFisherRecipes());

        list.add(new ClockworkCollectorRecipe(new ItemStack(ModBlocks.CLOCKWORK_GEYSER_COLLECTOR.get()), new ItemStack(ModBlocks.CHOCOLATE_GEYSER.get()),
                List.of("Needs to be above a Geyser.", "Geyser needs to be active.", "Every 10 to 15 seconds."))
                .addEntry(Ingredient.of(ModItems.CHOCOLATE_FLAKES.get()), 1, 4));

        list.add(new ClockworkCollectorRecipe(new ItemStack(ModBlocks.CLOCKWORK_POWDERED_SUGAR_COLLECTOR.get()), new ItemStack(ModBlocks.FROZEN_POWDER_BLOCK.get()),
                List.of("Needs to be in a tundra or chocolate springs.", "Every 60 to 90 seconds.", "Snowing increases speed."))
                .addEntry(Ingredient.of(ModItems.COLD_POWDERED_SUGAR.get()), 1, 3, 0.75f)
                .addEntry(Ingredient.of(ModItems.SWICE_SHARDS.get()), 1, 3, 0.25f));

        list.add(new ClockworkCollectorRecipe(new ItemStack(ModBlocks.CLOCKWORK_SYRUP_EXTRACTOR.get()), new ItemStack(ModItems.GENTLIT_SYRUP.get()),
                List.of("Needs to be on a Gentlit tree.", "Needs bottles to fill up.", "Every 90 seconds."))
                .addEntry(Ingredient.of(ModItems.GENTLIT_SYRUP.get())));

        return list;
    }

    private ClockworkCollectorRecipe getFisherRecipes(){
        return new ClockworkCollectorRecipe(new ItemStack(ModBlocks.CLOCKWORK_FISHER.get()), new ItemStack(Items.FISHING_ROD),
                List.of("Needs to be submerged.", "Checks a 3x3x4 area.", "Needs at least 18 water blocks."))
                .addEntry(Ingredient.of(Items.COD), 0.51f)
                .addEntry(Ingredient.of(Items.SALMON), 0.213f)
                .addEntry(Ingredient.of(Items.PUFFERFISH), 0.11f)
                .addEntry(Ingredient.of(Items.TROPICAL_FISH), 0.017f)
                .addEntry(Ingredient.of(Items.BOW, Items.ENCHANTED_BOOK, Items.FISHING_ROD, Items.NAME_TAG, Items.NAUTILUS_SHELL, Items.SADDLE), 0.071f, "Treasure")
                .addEntry(Ingredient.of(Items.LILY_PAD, Items.BONE, Items.BOWL, Items.LEATHER, Items.LEATHER_BOOTS, Items.ROTTEN_FLESH, Items.GLASS_BOTTLE,
                        Items.TRIPWIRE_HOOK, Items.STICK, Items.STRING, Items.FISHING_ROD, Items.INK_SAC), 0.01f, "Trash");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrindingWheelScreen.class, 66, 33, 24, 24,
                GrindingWheelCategory.GRINDING_WHEEL_RECIPE_TYPE);

        registration.addRecipeClickArea(ThreadReelerScreen.class, 66, 33, 24, 24,
                ThreadReelerCategory.THREAD_REELER_RECIPE_TYPE);

        registration.addRecipeClickArea(FairyOfferingBenchScreen.class, 70, 24, 20, 35,
                FairyOfferingCategory.FAIRY_OFFERING_RECIPE_TYPE);

        registration.addRecipeClickArea(FairyInfusionBenchScreen.class, 52, 35, 71, 15,
                FairyInfusionCategory.FAIRY_INFUSION_RECIPE_TYPE);

        registration.addRecipeClickArea(MagicCrystalizerScreen.class, 98, 34, 24, 24,
                MagicCrystalizerCategory.MAGIC_CRYSTALIZER_RECIPE_TYPE);

        registration.addRecipeClickArea(FumeDistilleryScreen.class, 79, 34, 24, 24,
                FumeDistilleryCategory.FUME_DISTILLERY_RECIPE_TYPE);

        registration.addRecipeClickArea(MixerScreen.class, 87, 23, 24, 36,
                MixerCategory.MIXER_RECIPE_TYPE);
    }
}
