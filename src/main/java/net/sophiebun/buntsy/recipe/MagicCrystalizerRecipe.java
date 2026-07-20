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

public class MagicCrystalizerRecipe implements Recipe<MagicCrystallizerInput> {
    private final List<Ingredient> inputItems;
    private final ItemStack output;
    public MagicCrystalizerRecipe(List<Ingredient> inputItems, ItemStack output) {
        this.inputItems = inputItems;
        this.output = output;
    }

    public List<Ingredient> getInputs() {
        return inputItems;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public boolean matches(MagicCrystallizerInput magicCrystallizerInput, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        for (int i = 0; i < 7; i++){
            if (!inputItems.get(i).test(magicCrystallizerInput.getItem(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(MagicCrystallizerInput magicCrystallizerInput, HolderLookup.Provider provider) {
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

    public static class Type implements RecipeType<MagicCrystalizerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "magic_crystalizer";
    }

    public static class Serializer implements RecipeSerializer<MagicCrystalizerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "magic_crystalizer");

        @Override
        public MapCodec<MagicCrystalizerRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(
                        Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(MagicCrystalizerRecipe::getInputs),
                        ItemStack.STRICT_CODEC.fieldOf("output").forGetter(MagicCrystalizerRecipe::getOutput)
                ).apply(instance, MagicCrystalizerRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MagicCrystalizerRecipe> streamCodec() {
            return StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), MagicCrystalizerRecipe::getInputs,
                    ItemStack.STREAM_CODEC, MagicCrystalizerRecipe::getOutput,
                    MagicCrystalizerRecipe::new);
        }
    }
}
