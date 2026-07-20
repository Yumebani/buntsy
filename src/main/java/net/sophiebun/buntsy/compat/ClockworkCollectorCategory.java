package net.sophiebun.buntsy.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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

public class ClockworkCollectorCategory implements IRecipeCategory<ClockworkCollectorRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(BuntsyMod.MODID, "clockwork_fisher");
    public static final ResourceLocation TEXTURE = new ResourceLocation(BuntsyMod.MODID,
            "textures/gui/jei_clockwork_collector_gui.png");

    public static final RecipeType<ClockworkCollectorRecipe> CLOCKWORK_COLLECTOR_RECIPE_TYPE =
            new RecipeType<>(UID, ClockworkCollectorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ClockworkCollectorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CLOCKWORK_FISHER.get()));
    }

    @Override
    public RecipeType<ClockworkCollectorRecipe> getRecipeType() {
        return CLOCKWORK_COLLECTOR_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.buntsy.clockwork_collector");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, ClockworkCollectorRecipe clockworkCollectorRecipe, IFocusGroup iFocusGroup) {

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 40, 25)
                .addItemStack(clockworkCollectorRecipe.getBlock());

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 40, 49)
                .addItemStack(clockworkCollectorRecipe.getExtra())
                .addTooltipCallback((iRecipeSlotView, list) -> {
                    if (clockworkCollectorRecipe.getCondition() != null){
                        for (String string : clockworkCollectorRecipe.getCondition()){
                            list.add(Component.literal(string));
                        }
                    }
                });

        for (int i = 0; i < 15; i++){

            if (!clockworkCollectorRecipe.has(i)){
                break;
            }

            int finalI = i;
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 63 + (18 * (i % 5)), 19 + (18 * (i / 5)))
                    .addIngredients(clockworkCollectorRecipe.getItem(i))
                    .addTooltipCallback((iRecipeSlotView, list) -> {
                        for (Component component : clockworkCollectorRecipe.getText(finalI)){
                            list.add(component);
                        }
                    });

        }
    }
}
