package net.sophiebun.buntsy.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;

import javax.swing.*;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RecipeSerializer =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BuntsyMod.MODID);

    public  static  final RegistryObject<RecipeSerializer<GrindingWheelRecipe>> GRINDING_WHEEL_SERIALIZER =
            RecipeSerializer.register("grinding_wheel", () -> GrindingWheelRecipe.Serializer.INSTANCE);
    public static void register(IEventBus eventBus){
        RecipeSerializer.register(eventBus);
    }
}
