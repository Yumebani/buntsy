package net.sophiebun.buntsy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.sophiebun.buntsy.BuntsyMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InfusionAltarBasicRecipe implements Recipe<InfusionAltarInput> {
    private final List<ItemStack> inputItems;
    private final ItemStack output;
    private final int maxProgress;
    public InfusionAltarBasicRecipe(List<ItemStack> inputItems, ItemStack output, int maxProgress) {
        this.inputItems = inputItems;
        this.output = output;
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public List<ItemStack> getInputs() {
        return inputItems;
    }

    private ItemStack checkIfContains(List<ItemStack> inputs, ItemStack item){
        for (ItemStack itemIn : inputs){
            if (itemMatch(itemIn, item)) return itemIn;
        }
        return null;
    }

    private boolean itemMatch(ItemStack first, ItemStack second){
        return (first.getCount() <= second.getCount()) && ItemStack.isSameItemSameComponents(first, second);
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(InfusionAltarInput infusionAltarInput, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        if (!itemMatch(inputItems.get(0), infusionAltarInput.getItem(0))) return false;

        List<ItemStack> inputTest = new ArrayList<>(inputItems.stream().toList());
        List<ItemStack> input = new ArrayList<>();
        for (int i = 0; i <= 4; i++){
            input.add(infusionAltarInput.getItem(i));
        }

        for (ItemStack item : input){
            ItemStack test = checkIfContains(inputTest, item);
            if (test != null){
                inputTest.remove(test);
            }
            else return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(InfusionAltarInput infusionAltarInput, HolderLookup.Provider provider) {
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

    public static class Type implements RecipeType<InfusionAltarBasicRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "infusion_altar_basic";
    }

    public static class Serializer implements RecipeSerializer<InfusionAltarBasicRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "infusion_altar_basic");

        @Override
        public MapCodec<InfusionAltarBasicRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(
                        ItemStack.STRICT_CODEC.listOf().fieldOf("inputs").forGetter(InfusionAltarBasicRecipe::getInputs),
                        ItemStack.STRICT_CODEC.fieldOf("output").forGetter(InfusionAltarBasicRecipe::getOutput),
                        Codec.INT.fieldOf("max_progress").forGetter(InfusionAltarBasicRecipe::getMaxProgress)
                ).apply(instance, InfusionAltarBasicRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfusionAltarBasicRecipe> streamCodec() {
            return StreamCodec.composite(
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), InfusionAltarBasicRecipe::getInputs,
                    ItemStack.STREAM_CODEC, InfusionAltarBasicRecipe::getOutput,
                    ByteBufCodecs.INT, InfusionAltarBasicRecipe::getMaxProgress,
                    InfusionAltarBasicRecipe::new
            );
        }
    }
}
