package net.sophiebun.buntsy.recipe;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.util.Tuple;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TEMPORARY
public class TempRecipe {

    private final Ingredient ingredients;

    private final  Map<Item, ImmutableMultimap<Integer, Float>> results;

    public TempRecipe(Ingredient ingredients, Map<Item, ImmutableMultimap<Integer, Float>> results){
        this.ingredients = ingredients;
        this.results = results;
    }

    public Ingredient getIngredients() {
        return ingredients;
    }

    public List<ItemStack> getResults(int rollChance) {
        List<ItemStack> items = new ArrayList<>();
        for (Map.Entry<Item, ImmutableMultimap<Integer, Float>> itemEntry : results.entrySet()) {
            int finalCount = 0;
            ImmutableMultimap<Integer, Float> entryCounts = itemEntry.getValue();
            for (Integer count : entryCounts.keys()) {
                for (Float chance : entryCounts.get(count))
                    if (chance >= rollChance / 100f) {
                        finalCount += count;
                    }
            }
            if (finalCount > 0) {
                items.add(new ItemStack(itemEntry.getKey(), finalCount));
            }
        }
        return items;
    }

    public boolean isPresent(SimpleContainer inv, int[] slots){
        boolean result = true;
        ItemStack[] ingredientsList = ingredients.getItems();
        for (int slot = slots[0]; slot <= (slots.length == 1 ? slots[0] : slots[1]); slot++){
            result &= (ingredientsList[slot].getItem() == inv.getItem(slot).getItem());
            result &= (ingredientsList[slot].getCount() == inv.getItem(slot).getCount());
        }
        return result;
    }
}
