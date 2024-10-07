package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Tuple;
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
    private final HashMap<Item, List<Tuple<Integer, Float>>> output;
    private final ResourceLocation id;

    public ThreadReelerRecipe(List<Ingredient> inputItems, HashMap<Item, List<Tuple<Integer, Float>>> output, ResourceLocation id) {
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

    public HashMap<Item, List<Tuple<Integer, Float>>> getOutput() {
        return output;
    }

    public List<ItemStack> getResults(int rollChance) {
        List<ItemStack> items = new ArrayList<>();
        for (Item key : output.keySet()){
            int finalCount = 0;
            List<Tuple<Integer, Float>> entryCounts = output.get(key);
            for (Tuple<Integer, Float> entry : entryCounts){
                float chance = entry.getB();
                if (chance >= rollChance / 100f){
                    finalCount += entry.getA();
                }
            }
            if (finalCount > 0){
                items.add(new ItemStack(key, finalCount));
            }
        }
        return items;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return new ItemStack(output.keySet().stream().limit(1).toList().get(0));
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(output.keySet().stream().limit(1).toList().get(0));
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
            HashMap<Item, List<Tuple<Integer, Float>>> results = new HashMap<>();
            for (JsonElement entry : outputs.asList()){
                JsonObject entryObj = entry.getAsJsonObject();
                int count = entryObj.get("count").getAsInt();
                float chance = entryObj.get("chance").getAsFloat();
                String itemId = entryObj.get("item").getAsString();

                List<Tuple<Integer, Float>> result;
                Item resultItem = CraftingHelper.getItem(itemId, true);
                if (!results.containsKey(resultItem)){
                    result = new ArrayList<>();
                    results.put(resultItem, result);
                }
                else result = results.get(resultItem);

                result.add(new Tuple<>(count, chance));
            }

            return new ThreadReelerRecipe(inputs, results, pRecipeId);
        }

        @Override
        public @Nullable ThreadReelerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            HashMap<Item, List<Tuple<Integer, Float>>> results = new HashMap<>();
            int loopCount = pBuffer.readInt();
            for (int i = 0; i < loopCount; i++){
                List<Tuple<Integer, Float>> result = new ArrayList<>();
                results.put(pBuffer.readItem().getItem(), result);
                int loopCount2 = pBuffer.readInt();
                for (int j = 0; j < loopCount2; j++){
                    int count = pBuffer.readInt();
                    float chance = pBuffer.readFloat();
                    result.add(new Tuple<>(count, chance));
                }
            }

            return new ThreadReelerRecipe(inputs, results, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ThreadReelerRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getInputs().size());

            for (Ingredient ingredient : pRecipe.getInputs()) {
                ingredient.toNetwork(pBuffer);
            }

            HashMap<Item, List<Tuple<Integer, Float>>> output = pRecipe.getOutput();

            pBuffer.writeInt(output.size());
            for (Item item : output.keySet()){
                pBuffer.writeItem(new ItemStack(item));
                pBuffer.writeInt(output.get(item).size());
                for (Tuple<Integer, Float> tuple : output.get(item)){
                    pBuffer.writeInt(tuple.getA());
                    pBuffer.writeFloat(tuple.getB());
                }
            }
        }
    }
}
