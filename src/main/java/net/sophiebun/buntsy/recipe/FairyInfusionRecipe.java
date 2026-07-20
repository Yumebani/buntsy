package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

public class FairyInfusionRecipe implements Recipe<FairyInfusionInput> {
    private final List<Ingredient> inputItems;
    private final ItemStack output;
    public FairyInfusionRecipe(List<Ingredient> inputItems, ItemStack output) {
        this.inputItems = inputItems;
        this.output = output;
    }

    public ItemStack getOutput() {
        return output;
    }

    public List<Ingredient> getInputs() {
        return inputItems;
    }

    @Override
    public boolean matches(FairyInfusionInput fairyInfusionInput, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        for (int i = 0; i < 5; i++){
            if (inputItems.get(0).test(fairyInfusionInput.getItem(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(FairyInfusionInput fairyInfusionInput, HolderLookup.Provider provider) {
        return output.copy();
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

    public static class Type implements RecipeType<FairyInfusionRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fairy_infusion";
    }

    public static class Serializer implements RecipeSerializer<FairyInfusionRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "fairy_infusion");

        @Override
        public MapCodec<FairyInfusionRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(
                        Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter(FairyInfusionRecipe::getInputs),
                        ItemStack.STRICT_CODEC.fieldOf("output").forGetter(FairyInfusionRecipe::getOutput)
                ).apply(instance, FairyInfusionRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FairyInfusionRecipe> streamCodec() {
            return StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), FairyInfusionRecipe::getInputs,
                    ItemStack.STREAM_CODEC, FairyInfusionRecipe::getOutput,
                    FairyInfusionRecipe::new);
        }
    }
}
