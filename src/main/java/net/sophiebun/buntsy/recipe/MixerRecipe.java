package net.sophiebun.buntsy.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.sophiebun.buntsy.BuntsyMod;

import java.util.ArrayList;
import java.util.List;

public class MixerRecipe implements Recipe<MixerInput> {
    private final List<ItemStack> inputItems;
    private final ItemStack output;

    public MixerRecipe(List<ItemStack> inputItems, ItemStack output) {
        this.inputItems = inputItems;
        this.output = output;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public List<ItemStack> getInputs() {
        return new ArrayList<>(inputItems.stream().toList());
    }

    private ItemStack checkIfContains(List<ItemStack> inputs, ItemStack item){
        for (ItemStack itemIn : inputs){
            if (itemMatch(itemIn, item)) return itemIn;
        }
        return null;
    }

    private boolean itemMatch(ItemStack first, ItemStack second){
        return (first.getCount() <= second.getCount()) &&
                ItemStack.isSameItemSameComponents(first, second);
    }

    @Override
    public boolean matches(MixerInput mixerInput, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        List<ItemStack> inputTest = new ArrayList<>(inputItems.stream().toList());
        List<ItemStack> input = new ArrayList<>();
        for (int i = 0; i <= 6; i++){
            input.add(mixerInput.getItem(i));
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

    @Override
    public ItemStack assemble(MixerInput mixerInput, HolderLookup.Provider provider) {
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

    public static class Type implements RecipeType<MixerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "mixer";
    }

    public static class Serializer implements RecipeSerializer<MixerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "mixer");

        @Override
        public MapCodec<MixerRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(
                        ItemStack.STRICT_CODEC.listOf().fieldOf("inputs").forGetter(MixerRecipe::getInputs),
                        ItemStack.STRICT_CODEC.fieldOf("output").forGetter(MixerRecipe::getOutput)
                ).apply(instance, MixerRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MixerRecipe> streamCodec() {
            return StreamCodec.composite(
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), MixerRecipe::getInputs,
                    ItemStack.STREAM_CODEC, MixerRecipe::getOutput,
                    MixerRecipe::new);
        }
    }
}
