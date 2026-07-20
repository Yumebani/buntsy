package net.sophiebun.buntsy.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, BuntsyMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrindingWheelRecipe>> GRINDING_WHEEL_SERIALIZER =
            SERIALIZERS.register("grinding_wheel", () -> GrindingWheelRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ThreadReelerRecipe>> THREAD_REELER_SERIALIZER =
            SERIALIZERS.register("thread_reeler", () -> ThreadReelerRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FairyOfferingRecipe>> FAIRY_OFFERING_SERIALIZER =
            SERIALIZERS.register("fairy_offering", () -> FairyOfferingRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FairyInfusionRecipe>> FAIRY_INFUSION_SERIALIZER =
            SERIALIZERS.register("fairy_infusion", () -> FairyInfusionRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MagicCrystalizerRecipe>> MAGIC_CRYSTALIZER_SERIALIZER =
            SERIALIZERS.register("magic_crystalizer", () -> MagicCrystalizerRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FumeDistilleryRecipe>> FUME_DISTILLERY_SERIALIZER =
            SERIALIZERS.register("fume_distillery", () -> FumeDistilleryRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<InfusionAltarBasicRecipe>> INFUSION_ALTAR_BASIC_RECIPE =
            SERIALIZERS.register("infusion_altar_basic", () -> InfusionAltarBasicRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<InfusionAltarAdvancedRecipe>> INFUSION_ALTAR_ADVANCED_RECIPE =
            SERIALIZERS.register("infusion_altar_advanced", () -> InfusionAltarAdvancedRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MixerRecipe>> MIXER_RECIPE =
            SERIALIZERS.register("mixer", () -> MixerRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }




}


