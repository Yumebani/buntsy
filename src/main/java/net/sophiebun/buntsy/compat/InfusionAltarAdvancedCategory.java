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
import net.minecraft.world.item.ItemStack;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.recipe.InfusionAltarAdvancedRecipe;
import net.sophiebun.buntsy.recipe.InfusionAltarBasicRecipe;

import java.util.List;

public class InfusionAltarAdvancedCategory implements IRecipeCategory<InfusionAltarAdvancedRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "infusion_altar_advanced");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_infusion_altar_advanced.png");

    public static final RecipeType<InfusionAltarAdvancedRecipe> INFUSION_ALTAR_ADVANCED_RECIPE_TYPE =
            new RecipeType<>(UID, InfusionAltarAdvancedRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public InfusionAltarAdvancedCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.INFUSION_ALTAR_ADVANCED.get()));
    }

    @Override
    public RecipeType<InfusionAltarAdvancedRecipe> getRecipeType() {
        return INFUSION_ALTAR_ADVANCED_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.infusion_altar_advanced");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, InfusionAltarAdvancedRecipe infusionAltarAdvancedRecipe, IFocusGroup iFocusGroup) {

        List<ItemStack> inputs = infusionAltarAdvancedRecipe.getInputs();

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,46, 35).addItemStack(inputs.get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,22, 35).addItemStack(inputs.get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,22, 11).addItemStack(inputs.get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,46, 11).addItemStack(inputs.get(3));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,70, 11).addItemStack(inputs.get(4));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,70, 35).addItemStack(inputs.get(5));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,70, 59).addItemStack(inputs.get(6));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,46, 59).addItemStack(inputs.get(7));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,22, 59).addItemStack(inputs.get(8));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,125, 35).addItemStack(infusionAltarAdvancedRecipe.getResultItem(null));
    }
}
