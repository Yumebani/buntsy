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
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;
import net.sophiebun.buntsy.screen.GrindingWheelScreen;

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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<GrindingWheelRecipe> grindingWheelRecipes = recipeManager.getAllRecipesFor(GrindingWheelRecipe.Type.INSTANCE);
        registration.addRecipes(GrindingWheelCategory.GRINDING_WHEEL_RECIPE_TYPE, grindingWheelRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrindingWheelScreen.class, 66, 33, 24, 24,
                GrindingWheelCategory.GRINDING_WHEEL_RECIPE_TYPE);
    }
}
