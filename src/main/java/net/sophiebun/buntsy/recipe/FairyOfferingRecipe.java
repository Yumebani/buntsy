package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

public class FairyOfferingRecipe implements Recipe<FairyOfferingInput> {
    private final List<Ingredient> inputItems;
    private final ItemStack output;
    private final int foodTick;
    private final float chanceModifier;
    public FairyOfferingRecipe(List<Ingredient> inputItems, ItemStack output, int foodTick, float chanceModifier) {
        this.inputItems = inputItems;
        this.output = output;
        this.foodTick = foodTick;
        this.chanceModifier = chanceModifier;
    }
    public FairyOfferingRecipe(List<Ingredient> inputItems, ItemStack output) {
        this.inputItems = inputItems;
        this.output = output;
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

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(FairyOfferingInput input, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        for (int i = 0; i < 4; i++){
            if (inputItems.get(0).test(input.getItem(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(FairyOfferingInput singleItemInput, HolderLookup.Provider provider) {
        return  output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
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
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "fairy_offering");

        @Override
        public MapCodec<FairyOfferingRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(
                        Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter(FairyOfferingRecipe::getInputs),
                        ItemStack.OPTIONAL_CODEC.fieldOf("output").forGetter(FairyOfferingRecipe::getOutput),
                        Codec.INT.fieldOf("food_tick").forGetter(FairyOfferingRecipe::getFoodTick),
                        Codec.FLOAT.fieldOf("chance_modifier").forGetter(FairyOfferingRecipe::getChanceModifier)
                ).apply(instance, FairyOfferingRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FairyOfferingRecipe> streamCodec() {
            return StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), FairyOfferingRecipe::getInputs,
                    ItemStack.OPTIONAL_STREAM_CODEC, FairyOfferingRecipe::getOutput,
                    ByteBufCodecs.INT, FairyOfferingRecipe::getFoodTick,
                    ByteBufCodecs.FLOAT, FairyOfferingRecipe::getChanceModifier,
                    FairyOfferingRecipe::new);
        }
    }
}
