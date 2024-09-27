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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.sophiebun.buntsy.BuntsyMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FairyOfferingRecipe implements Recipe<SimpleContainer> {
    private final List<Ingredient> inputItems;
    private final ItemStack output;
    private final int foodTick;
    private final float chanceModifier;
    private final ResourceLocation id;

    public FairyOfferingRecipe(List<Ingredient> inputItems, ItemStack output, ResourceLocation id, int foodTick, float chanceModifier) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.foodTick = foodTick;
        this.chanceModifier = chanceModifier;
    }
    public FairyOfferingRecipe(List<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.foodTick = 500;
        this.chanceModifier = 1;
    }

    public int getFoodTick(){
        return this.foodTick;
    }

    public float getChanceModifier(){
        return this.chanceModifier;
    }

    public List<Ingredient> getInputs() {
        return inputItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        for (int i = 0; i < 4; i++){
            if (inputItems.get(0).test(pContainer.getItem(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
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

    public static class Type implements RecipeType<FairyOfferingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fairy_offering";
    }

    public static class Serializer implements RecipeSerializer<FairyOfferingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(BuntsyMod.MODID, "fairy_offering");

        @Override
        public FairyOfferingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            //Inputs
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            for(JsonElement entry : ingredients.asList()) {
                inputs.add(Ingredient.fromJson(entry));
            }

            //Outputs
            ItemStack output = new ItemStack(Blocks.AIR);
            if (GsonHelper.getAsBoolean(pSerializedRecipe,"has_output")){
                output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            }
            int foodTick = GsonHelper.getAsInt(pSerializedRecipe, "food_tick");
            float chanceModifier = GsonHelper.getAsFloat(pSerializedRecipe, "chance_modifier");

            return new FairyOfferingRecipe(inputs, output, pRecipeId, foodTick, chanceModifier);
        }

        @Override
        public @Nullable FairyOfferingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            int foodTick = pBuffer.readInt();
            float chanceModifier = pBuffer.readFloat();

            return new FairyOfferingRecipe(inputs, output, pRecipeId, foodTick, chanceModifier);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, FairyOfferingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItem(pRecipe.output);
            pBuffer.writeInt(pRecipe.foodTick);
            pBuffer.writeFloat(pRecipe.chanceModifier);
        }
    }
}
