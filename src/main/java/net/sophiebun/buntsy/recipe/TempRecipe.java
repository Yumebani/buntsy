package net.sophiebun.buntsy.recipe;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TEMPORARY
public class TempRecipe {

    private final Ingredient ingredients;

    private final Map<Item, Map<Integer, Float>> results;

    public TempRecipe(Ingredient ingredients, Map<Item, Map<Integer, Float>> results){
        this.ingredients = ingredients;
        this.results = results;
    }

    public Ingredient getIngredients() {
        return ingredients;
    }

    public List<ItemStack> getResults(int rollChance) {
        List<ItemStack> items = new ArrayList<>();
        for (Map.Entry<Item, Map<Integer, Float>> itemEntry : results.entrySet()){
            ItemStack result = new ItemStack(itemEntry.getKey());
            for (Map.Entry<Integer, Float> chanceEntry : itemEntry.getValue().entrySet()){
                if (chanceEntry.getValue() >= rollChance / 100){
                    result.setCount(result.getCount() + chanceEntry.getKey());
                }
            }
            items.add(result);
        }
        return items;
    }

    public boolean isPresent(SimpleContainer inv, int[] slots){
        boolean result = true;
        for (int slot : slots){
            result &= ingredients.test(inv.getItem(slot));
        }
        return result;
    }
}
