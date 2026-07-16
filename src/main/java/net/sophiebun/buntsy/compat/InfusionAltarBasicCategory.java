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
import net.sophiebun.buntsy.recipe.FumeDistilleryRecipe;
import net.sophiebun.buntsy.recipe.InfusionAltarBasicRecipe;

import java.util.List;

public class InfusionAltarBasicCategory implements IRecipeCategory<InfusionAltarBasicRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "infusion_altar_basic");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_infusion_altar_basic.png");

    public static final RecipeType<InfusionAltarBasicRecipe> INFUSION_ALTAR_BASIC_RECIPE_TYPE =
            new RecipeType<>(UID, InfusionAltarBasicRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public InfusionAltarBasicCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.INFUSION_ALTAR_BASIC.get()));
    }

    @Override
    public RecipeType<InfusionAltarBasicRecipe> getRecipeType() {
        return INFUSION_ALTAR_BASIC_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.infusion_altar_basic");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, InfusionAltarBasicRecipe infusionAltarBasicRecipe, IFocusGroup iFocusGroup) {

        List<ItemStack> inputs = infusionAltarBasicRecipe.getInputs();

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,46, 35).addItemStack(inputs.get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,22, 35).addItemStack(inputs.get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,46, 11).addItemStack(inputs.get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,70, 35).addItemStack(inputs.get(3));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,46, 59).addItemStack(inputs.get(4));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,125, 35).addItemStack(infusionAltarBasicRecipe.getResultItem(null));
    }
}
