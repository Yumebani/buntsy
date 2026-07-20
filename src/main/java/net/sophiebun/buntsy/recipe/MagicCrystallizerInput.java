package net.sophiebun.buntsy.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record MagicCrystallizerInput(
        List<Ingredient> ingredients
) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return ingredients.get(index).getItems()[0];
    }

    @Override
    public int size() {
        return ingredients.size();
    }
}
