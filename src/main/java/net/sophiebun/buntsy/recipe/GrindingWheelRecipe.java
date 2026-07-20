package net.sophiebun.buntsy.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.codec.ChanceItemEntry;

import java.util.ArrayList;
import java.util.List;

public class GrindingWheelRecipe implements Recipe<SingleItemInput> {
    private final List<Ingredient> inputItems;
    private final List<ChanceItemEntry> output;


    public GrindingWheelRecipe(List<Ingredient> inputItems, List<ChanceItemEntry> output) {
        this.inputItems = inputItems;
        this.output = output;
    }

    public List<Ingredient> getInputs() {
        return inputItems;
    }

    public List<ChanceItemEntry> getOutputs(){
        return output;
    }

    public List<ItemStack> getResults(int rollChance) {
        List<ItemStack> items = new ArrayList<>();
        for (ChanceItemEntry entry : output){
            if (entry.left() >= rollChance / 100f){
                for (ItemStack itemStack : items){
                    if (ItemStack.isSameItemSameComponents(itemStack, entry.right())){
                        itemStack.grow(entry.right().getCount());
                        break;
                    }
                }
                items.add(entry.right().copy());
            }
        }
        return items;
    }

    @Override
    public boolean matches(SingleItemInput singleItemInput, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        for (Ingredient ing : inputItems){
            if (ing.test(singleItemInput.getItem(0))){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(SingleItemInput singleItemInput, HolderLookup.Provider provider) {
        return output.get(0).right().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.get(0).right().copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<GrindingWheelRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "grinding_wheel";
    }

    public static class Serializer implements RecipeSerializer<GrindingWheelRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "grinding_wheel");

        @Override
        public MapCodec<GrindingWheelRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(
                        Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter(GrindingWheelRecipe::getInputs),
                        ChanceItemEntry.CODEC.codec().listOf().fieldOf("output").forGetter(GrindingWheelRecipe::getOutputs)
                ).apply(instance, GrindingWheelRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GrindingWheelRecipe> streamCodec() {
            return StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), GrindingWheelRecipe::getInputs,
                    ChanceItemEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), GrindingWheelRecipe::getOutputs,
                    GrindingWheelRecipe::new);
        }
    }
}
