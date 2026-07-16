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
import net.minecraftforge.items.SlotItemHandler;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.inventory.OutputSlot;
import net.sophiebun.buntsy.recipe.FumeDistilleryRecipe;
import net.sophiebun.buntsy.recipe.MixerRecipe;

import java.util.List;

public class MixerCategory implements IRecipeCategory<MixerRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "mixer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_mixer_gui.png");

    public static final RecipeType<MixerRecipe> MIXER_RECIPE_TYPE =
            new RecipeType<>(UID, MixerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public MixerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MIXER_BLOCK.get()));
    }

    @Override
    public RecipeType<MixerRecipe> getRecipeType() {
        return MIXER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buntsy.mixer");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, MixerRecipe mixerRecipe, IFocusGroup iFocusGroup) {

        List<ItemStack> inputs = mixerRecipe.getInputs();

        for (int i = 0; i < 6; i++){
            if (!(inputs.size() < i + 1)){
                iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,29 + (i % 3) * 18, 20 + (i / 3) * 18).addItemStack(inputs.get(i));
            }
        }

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT,122, 30).addItemStack(mixerRecipe.getResultItem(null));
    }
}
