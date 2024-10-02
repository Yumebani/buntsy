package net.sophiebun.buntsy.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.recipe.*;
import net.sophiebun.buntsy.screen.*;

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
    }
}
