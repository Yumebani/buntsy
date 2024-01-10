package net.sophiebun.buntsy.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

//TEMPORARY
public class TempRecipe {

    private final Ingredient ingredients;
    private final ItemStack result;

    public TempRecipe(Ingredient ingredients, ItemStack result){
        this.ingredients = ingredients;
        this.result = result;
    }

    public Ingredient getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public boolean isPresent(SimpleContainer inv, int[] slots){
        boolean result = true;
        for (int slot : slots){
            result &= ingredients.test(inv.getItem(slot));
        }
        return result;
    }
}
