package net.sophiebun.buntsy.worldgen.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> featureRegister =
            DeferredRegister.create(Registries.FEATURE, BuntsyMod.MODID);

    public static final RegistryObject<Feature<HugeMushroomFeatureConfiguration>> GIANT_LOVESHROOM_FEATURE =
            featureRegister.register("giant_loveshroom_feature", () -> new HugeLoveshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<HugeMushroomFeatureConfiguration>> GIANT_GLOWSHROOM_FEATURE =
            featureRegister.register("giant_glowshroom_feature", () -> new HugeGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));


    public static void register(IEventBus eventBus){
        featureRegister.register(eventBus);
    }
}
