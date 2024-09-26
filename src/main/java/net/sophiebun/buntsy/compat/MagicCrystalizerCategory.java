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
import net.minecraftforge.items.SlotItemHandler;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.inventory.OutputSlot;
import net.sophiebun.buntsy.recipe.FairyInfusionRecipe;
import net.sophiebun.buntsy.recipe.MagicCrystalizerRecipe;

public class MagicCrystalizerCategory implements IRecipeCategory<MagicCrystalizerRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "magic_crystalizer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_magic_crystalizer_gui.png");

    public static final RecipeType<MagicCrystalizerRecipe> MAGIC_CRYSTALIZER_RECIPE_TYPE =
            new RecipeType<>(UID, MagicCrystalizerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public MagicCrystalizerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MAGIC_CRYSTALIZER.get()));
    }

    @Override
    public RecipeType<MagicCrystalizerRecipe> getRecipeType() {
        return MAGIC_CRYSTALIZER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.magic_crystalizer");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, MagicCrystalizerRecipe magicCrystalizerRecipe, IFocusGroup iFocusGroup) {

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 50, 26)
                .addIngredients(magicCrystalizerRecipe.getInputs().get(0));

        for (int i = 0; i < 4; i++){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 23 + (54 * (i % 2)), 17 + (18 * (i / 2)))
                    .addIngredients(magicCrystalizerRecipe.getInputs().get(i + 1));
        }

        for (int i = 0; i < 3; i++){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 32 + (18 * i), 53)
                    .addIngredients(magicCrystalizerRecipe.getInputs().get(i + 5));
        }

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,132, 35)
                .addItemStack(magicCrystalizerRecipe.getResultItem(null));

    }
}
