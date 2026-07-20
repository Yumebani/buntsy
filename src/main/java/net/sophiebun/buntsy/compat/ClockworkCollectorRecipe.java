package net.sophiebun.buntsy.compat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.checkerframework.checker.units.qual.A;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ClockworkCollectorRecipe {

    private final List<Entry> entries = new ArrayList<>();

    private final ItemStack block;
    private final ItemStack extra;
    private final List<String> condition;

    public ClockworkCollectorRecipe(ItemStack block, ItemStack extra, @Nullable List<String> condition){
        this.block = block;
        this.extra = extra;
        this.condition = condition;
    }

    public ItemStack getBlock() {
        return block;
    }

    public ItemStack getExtra() {
        return extra;
    }

    public @Nullable List<String> getCondition() {
        return condition;
    }

    public ClockworkCollectorRecipe addEntry(Ingredient ingredient){
        entries.add(new Entry(ingredient, null, null, null, null));
        return this;
    }

    public ClockworkCollectorRecipe addEntry(Ingredient ingredient, float probability){
        entries.add(new Entry(ingredient, probability, null, null, null));
        return this;
    }

    public ClockworkCollectorRecipe addEntry(Ingredient ingredient, float probability, String text){
        entries.add(new Entry(ingredient, probability, null, null, text));
        return this;
    }

    public ClockworkCollectorRecipe addEntry(Ingredient ingredient, String text){
        entries.add(new Entry(ingredient, null, null, null, text));
        return this;
    }

    public ClockworkCollectorRecipe addEntry(Ingredient ingredient, int min, int max){
        entries.add(new Entry(ingredient, null, min, max, null));
        return this;
    }

    public ClockworkCollectorRecipe addEntry(Ingredient ingredient, int min, int max, float probability){
        entries.add(new Entry(ingredient, probability, min, max, null));
        return this;
    }

    public Ingredient getItem(int i) {
        return entries.get(i).ingredient;
    }

    public List<Component> getText(int i){
        Entry entry = entries.get(i);
        List<Component> components = new ArrayList<>();

        if (entry.text != null){
            components.add(Component.literal(entry.text));
        }

        if (entry.min != null){
            components.add(Component.literal(entry.min + " - " + entry.max));
        }

        if (entry.chance != null){
            components.add(Component.literal(entry.chance * 100 + "% Chance"));
        }
        return components;
    }

    public boolean has(int i) {
        return entries.size() > i;
    }
    private record Entry(Ingredient ingredient, Float chance, Integer min, Integer max, String text){}
}
