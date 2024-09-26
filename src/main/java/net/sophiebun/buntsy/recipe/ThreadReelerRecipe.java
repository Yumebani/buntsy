package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.sophiebun.buntsy.BuntsyMod;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ThreadReelerRecipe implements Recipe<SimpleContainer> {
    private final List<Ingredient> inputItems;
    private final HashMap<Item, List<Map.Entry<Integer, Float>>> output;
    private final ResourceLocation id;

    public ThreadReelerRecipe(List<Ingredient> inputItems, HashMap<Item, List<Map.Entry<Integer, Float>>> output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        for (Ingredient ing : inputItems){
            if (ing.test(pContainer.getItem(0))){
                return true;
            }
        }
        return false;
    }

    public List<Ingredient> getInputs() {
        return inputItems;
    }

    public HashMap<Item, List<Map.Entry<Integer, Float>>> getOutput() {
        return output;
    }

    public List<ItemStack> getResults(int rollChance) {
        List<ItemStack> items = new ArrayList<>();
        for (Map.Entry<Item, List<Map.Entry<Integer, Float>>> itemEntry : output.entrySet()){
            int finalCount = 0;
            List<Map.Entry<Integer, Float>> entryCounts = itemEntry.getValue();
            for (Map.Entry<Integer, Float> entry : entryCounts){
                float chance = entry.getValue();
                if (chance >= rollChance / 100f){
                    finalCount += entry.getKey();
                }
            }
            if (finalCount > 0){
                items.add(new ItemStack(itemEntry.getKey(), finalCount));
            }
        }
        return items;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ThreadReelerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "thread_reeler";
    }

    public static class Serializer implements RecipeSerializer<ThreadReelerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(BuntsyMod.MODID, "thread_reeler");

        @Override
        public ThreadReelerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            //Inputs
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            for(JsonElement entry : ingredients.asList()) {
                inputs.add(Ingredient.fromJson(entry));
            }

            //Outputs
            JsonArray outputs = GsonHelper.getAsJsonArray(pSerializedRecipe, "output");
            HashMap<Item, List<Map.Entry<Integer, Float>>> results = new HashMap<>();
            for (JsonElement entry : outputs.asList()){
                JsonObject entryObj = entry.getAsJsonObject();
                int count = entryObj.get("count").getAsInt();
                float chance = entryObj.get("chance").getAsFloat();
                String itemId = entryObj.get("item").getAsString();

                List<Map.Entry<Integer, Float>> result;
                Item resultItem = CraftingHelper.getItem(itemId, true);
                if (!results.containsKey(resultItem)){
                    result = new ArrayList<>();
                    results.put(resultItem, result);
                }
                else result = results.get(resultItem);

                result.add(new AbstractMap.SimpleEntry<>(count, chance));
            }

            return new ThreadReelerRecipe(inputs, results, pRecipeId);
        }

        @Override
        public @Nullable ThreadReelerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            HashMap<Item, List<Map.Entry<Integer, Float>>> results = new HashMap<>();
            for (int i = 0; i < pBuffer.readInt(); i++){
                List<Map.Entry<Integer, Float>> result = new ArrayList<>();
                results.put(pBuffer.readItem().getItem(), result);
                for (int j = 0; j < pBuffer.readInt(); j++){
                    result.add(new AbstractMap.SimpleEntry<>(pBuffer.readInt(), pBuffer.readFloat()));
                }
            }

            return new ThreadReelerRecipe(inputs, results, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ThreadReelerRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            HashMap<Item, List<Map.Entry<Integer, Float>>> output = pRecipe.getOutput();

            pBuffer.writeInt(output.size());
            for (Map.Entry<Item, List<Map.Entry<Integer, Float>>> entry : output.entrySet()){
                pBuffer.writeItem(new ItemStack(entry.getKey()));
                pBuffer.writeInt(entry.getValue().size());
                for (Map.Entry<Integer, Float> entry2 : entry.getValue()){
                    pBuffer.writeInt(entry2.getKey());
                    pBuffer.writeFloat(entry2.getValue());
                }
            }
        }
    }
}
