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
import net.sophiebun.buntsy.recipe.FairyInfusionRecipe;
import net.sophiebun.buntsy.recipe.FairyOfferingRecipe;

public class FairyInfusionCategory implements IRecipeCategory<FairyInfusionRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "fairy_infusion");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_fairy_infusion_bench_gui.png");

    public static final RecipeType<FairyInfusionRecipe> FAIRY_INFUSION_RECIPE_TYPE =
            new RecipeType<>(UID, FairyInfusionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FairyInfusionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FAIRY_INFUSION_BENCH.get()));
    }

    @Override
    public RecipeType<FairyInfusionRecipe> getRecipeType() {
        return FAIRY_INFUSION_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.fairy_infusion_bench");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, FairyInfusionRecipe fairyInfusionRecipe, IFocusGroup iFocusGroup) {

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,44, 17)
                .addIngredients(fairyInfusionRecipe.getInputs().get(0));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,44, 53)
                .addItemStack(fairyInfusionRecipe.getResultItem(null));

    }
}
