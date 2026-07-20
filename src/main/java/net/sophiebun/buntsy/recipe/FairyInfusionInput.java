package net.sophiebun.buntsy.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record FairyInfusionInput(
        List<ItemStack> inputs
) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return inputs.get(index);
    }

    @Override
    public int size() {
        return inputs.size();
    }
}
