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

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }




}


