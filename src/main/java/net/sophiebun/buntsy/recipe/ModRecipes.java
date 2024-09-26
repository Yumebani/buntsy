package net.sophiebun.buntsy.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BuntsyMod.MODID);

    public static final RegistryObject<RecipeSerializer<GrindingWheelRecipe>> GRINDING_WHEEL_SERIALIZER =
            SERIALIZERS.register("grinding_wheel", () -> GrindingWheelRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ThreadReelerRecipe>> THREAD_REELER_SERIALIZER =
            SERIALIZERS.register("thread_reeler", () -> ThreadReelerRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<FairyOfferingRecipe>> FAIRY_OFFERING_SERIALIZER =
            SERIALIZERS.register("fairy_offering", () -> FairyOfferingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<FairyInfusionRecipe>> FAIRY_INFUSION_SERIALIZER =
            SERIALIZERS.register("fairy_infusion", () -> FairyInfusionRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<MagicCrystalizerRecipe>> MAGIC_CRYSTALIZER_SERIALIZER =
            SERIALIZERS.register("magic_crystalizer", () -> MagicCrystalizerRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }




}


