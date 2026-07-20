package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
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

public class FumeDistilleryRecipe implements Recipe<FumeDistilleryInput> {
    private final List<ItemStack> inputItems;
    private final ItemStack output;
    public FumeDistilleryRecipe(List<ItemStack> inputItems, ItemStack output) {
        this.inputItems = inputItems;
        this.output = output;
    }

    public List<ItemStack> getInputs() {
        return inputItems;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public boolean matches(FumeDistilleryInput recipeInput, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        boolean matches = true;

        for (int i = 0; i < 3; i++){
            ItemStack inputItem = inputItems.get(i);
            ItemStack containerItem = recipeInput.getItem(i);
            matches &= ItemStack.isSameItemSameComponents(inputItem, containerItem);
        }
        return matches;
    }

    @Override
    public ItemStack assemble(FumeDistilleryInput recipeInput, HolderLookup.Provider provider) {
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

    public static class Type implements RecipeType<FumeDistilleryRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fume_distillery";
    }

    public static class Serializer implements RecipeSerializer<FumeDistilleryRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "fume_distillery");

        @Override
        public MapCodec<FumeDistilleryRecipe> codec() {
            return RecordCodecBuilder.mapCodec(fumeDistilleryRecipeInstance -> {
                return fumeDistilleryRecipeInstance.group(
                        ItemStack.STRICT_CODEC.listOf().fieldOf("inputs").forGetter(FumeDistilleryRecipe::getInputs),
                        ItemStack.STRICT_CODEC.fieldOf("output").forGetter(FumeDistilleryRecipe::getOutput)
                ).apply(fumeDistilleryRecipeInstance, FumeDistilleryRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FumeDistilleryRecipe> streamCodec() {
            return StreamCodec.composite(
                        ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), FumeDistilleryRecipe::getInputs,
                        ItemStack.STREAM_CODEC, FumeDistilleryRecipe::getOutput,
                        FumeDistilleryRecipe::new);
        }
    }
}
