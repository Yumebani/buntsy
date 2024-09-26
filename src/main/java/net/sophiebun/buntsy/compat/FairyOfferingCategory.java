package net.sophiebun.buntsy.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
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
import net.sophiebun.buntsy.recipe.FairyOfferingRecipe;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FairyOfferingCategory implements IRecipeCategory<FairyOfferingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "fairy_offering");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_fairy_offering_bench_gui.png");

    public static final RecipeType<FairyOfferingRecipe> FAIRY_OFFERING_RECIPE_TYPE =
            new RecipeType<>(UID, FairyOfferingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FairyOfferingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FAIRY_OFFERING_BENCH.get()));
    }

    @Override
    public RecipeType<FairyOfferingRecipe> getRecipeType() {
        return FAIRY_OFFERING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.fairy_offering_bench");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, FairyOfferingRecipe fairyOfferingRecipe, IFocusGroup iFocusGroup) {

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,34, 25)
                .addIngredients(fairyOfferingRecipe.getInputs().get(0))
                .addTooltipCallback((iRecipeSlotView, list) -> list.add(Component.literal(fairyOfferingRecipe.getFoodTick() + " Food ticks")))
                .addTooltipCallback((iRecipeSlotView, list) -> list.add(Component.literal(fairyOfferingRecipe.getChanceModifier() + " Chance multiplier")));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,93, 25)
                .addItemStack(fairyOfferingRecipe.getResultItem(null));

    }
}
