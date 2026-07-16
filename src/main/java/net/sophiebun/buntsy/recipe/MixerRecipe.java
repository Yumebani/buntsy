package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.sophiebun.buntsy.BuntsyMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MixerRecipe implements Recipe<SimpleContainer> {
    private final List<ItemStack> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    public MixerRecipe(List<ItemStack> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    public List<ItemStack> getInputs() {
        return new ArrayList<>(inputItems.stream().toList());
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        List<ItemStack> inputTest = new ArrayList<>(inputItems.stream().toList());
        List<ItemStack> input = new ArrayList<>();
        for (int i = 0; i <= 6; i++){
            input.add(pContainer.getItem(i));
        }

        for (ItemStack item : input){
            if (!item.is(ItemStack.EMPTY.getItem()) && !inputTest.isEmpty()){
                ItemStack test = checkIfContains(inputTest, item);
                if (test != null){
                    inputTest.remove(test);
                }
                else return false;
            }
        }
        return inputTest.isEmpty();
    }

    private ItemStack checkIfContains(List<ItemStack> inputs, ItemStack item){
        for (ItemStack itemIn : inputs){
            if (itemMatch(itemIn, item)) return itemIn;
        }
        return null;
    }

    private boolean itemMatch(ItemStack first, ItemStack second){
        return (first.is(second.getItem())
                && first.getCount() <= second.getCount()) &&
                (!first.hasTag() || first.getTag().equals(second.getTag()));
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

    public static class Type implements RecipeType<MixerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "mixer";
    }

    public static class Serializer implements RecipeSerializer<MixerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(BuntsyMod.MODID, "mixer");

        @Override
        public MixerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            //Inputs
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "inputs");
            List<ItemStack> inputs = new ArrayList<>();
            for(JsonElement entry : ingredients.asList()) {
                inputs.add(itemFromCustomJson(entry.getAsJsonObject()));
            }

            ItemStack output = itemFromCustomJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            return new MixerRecipe(inputs, output, pRecipeId);
        }

        public ItemStack itemFromCustomJson(JsonObject obj){
            ItemStack item = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(obj, "item"));
            JsonArray nbtObj = GsonHelper.getAsJsonArray(obj, "nbt");
            if (!nbtObj.isEmpty()){
                CompoundTag nbt = new CompoundTag();
                for (JsonElement entry : nbtObj.asList()){
                    if (GsonHelper.isNumberValue(entry.getAsJsonObject(), "value")){
                        nbt.putInt(GsonHelper.getAsString(entry.getAsJsonObject(), "field"),
                                GsonHelper.getAsInt(entry.getAsJsonObject(), "value"));
                    }
                    else {
                        nbt.putString(GsonHelper.getAsString(entry.getAsJsonObject(), "field"),
                                GsonHelper.getAsString(entry.getAsJsonObject(), "value"));
                    }
                }
                item.setTag(nbt);
            }
            return item;
        }

        @Override
        public @Nullable MixerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            List<ItemStack> inputs = new ArrayList<>();

            int loopCount = pBuffer.readInt();
            for(int i = 0; i < loopCount; i++) {
                inputs.add(pBuffer.readItem());
            }

            ItemStack output = pBuffer.readItem();

            return new MixerRecipe(inputs, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MixerRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getInputs().size());
            for (ItemStack item : pRecipe.getInputs()) {
                pBuffer.writeItemStack(item, false);
            }

            pBuffer.writeItemStack(pRecipe.output, false);
        }
    }
}
