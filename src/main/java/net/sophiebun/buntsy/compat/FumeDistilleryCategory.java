package net.sophiebun.buntsy.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.recipe.FumeDistilleryRecipe;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FumeDistilleryCategory implements IRecipeCategory<FumeDistilleryRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "fume_distillery");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_fume_distillery_gui.png");

    public static final RecipeType<FumeDistilleryRecipe> FUME_DISTILLERY_RECIPE_TYPE =
            new RecipeType<>(UID, FumeDistilleryRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FumeDistilleryCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FUME_DISTILLERY.get()));
    }

    @Override
    public RecipeType<FumeDistilleryRecipe> getRecipeType() {
        return FUME_DISTILLERY_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.fume_distillery");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, FumeDistilleryRecipe fumeDistilleryRecipe, IFocusGroup iFocusGroup) {

        List<ItemStack> inputs = fumeDistilleryRecipe.getInputs();

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,56, 35).addItemStack(inputs.get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,32, 35).addItemStack(inputs.get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,56, 17).addItemStack(inputs.get(2));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,116, 35).addItemStack(fumeDistilleryRecipe.getResultItem(null));
    }
}
