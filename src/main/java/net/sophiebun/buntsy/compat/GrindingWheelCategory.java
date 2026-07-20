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
import net.minecraft.world.item.crafting.Ingredient;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.codec.ChanceItemEntry;
import net.sophiebun.buntsy.recipe.GrindingWheelRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrindingWheelCategory implements IRecipeCategory<GrindingWheelRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "grinding_wheel");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID,
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
        List<ChanceItemEntry> outputs = grindingWheelRecipe.getOutputs();
        for (ChanceItemEntry itemEntry : outputs){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,99 + ((i % 3)* 18), 26 + ((i / 3) * 18))
                    .addItemStack(itemEntry.right())
                    .addRichTooltipCallback((iRecipeSlotView, list) -> list.add(Component.literal((itemEntry.left() * 100) + "% Chance")));
            i++;
        }
    }
}
