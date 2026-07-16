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
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;

import java.util.*;

public class GrindingWheelCategory implements IRecipeCategory<GrindingWheelRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "grinding_wheel");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_grinding_wheel_gui.png");

    public static final RecipeType<GrindingWheelRecipe> GRINDING_WHEEL_RECIPE_TYPE =
            new RecipeType<>(UID, GrindingWheelRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public GrindingWheelCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.GRINDING_WHEEL.get()));
    }

    @Override
    public RecipeType<GrindingWheelRecipe> getRecipeType() {
        return GRINDING_WHEEL_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.grinding_wheel");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, GrindingWheelRecipe grindingWheelRecipe, IFocusGroup iFocusGroup) {

        List<ItemStack> inputs = new ArrayList<>();
        for (Ingredient ingredient : grindingWheelRecipe.getInputs()){
            inputs.addAll(Arrays.asList(ingredient.getItems()));
        }
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,40, 25).addItemStacks(inputs);

        int i = 0;
        HashMap<Item, List<Tuple<Integer, Float>>> outputs = grindingWheelRecipe.getOutput();
        for (Map.Entry<Item, List<Tuple<Integer, Float>>> itemEntry : outputs.entrySet()){
            List<Tuple<Integer, Float>> entryCounts = itemEntry.getValue();
            for (Tuple<Integer, Float> entry : entryCounts){
                iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,99 + ((i % 3)* 18), 26 + ((i / 3) * 18))
                        .addItemStack(new ItemStack(itemEntry.getKey(), entry.getA()))
                        .addTooltipCallback((iRecipeSlotView, list) -> list.add(Component.literal((entry.getB() * 100) + "% Chance")));
                i++;
            }
        }
    }
}
