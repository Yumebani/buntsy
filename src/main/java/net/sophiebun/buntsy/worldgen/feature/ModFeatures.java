package net.sophiebun.buntsy.worldgen.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
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
    public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> SWEETGRASS_FEATURE =
            featureRegister.register("sweetgrass_feature", () -> new SweetgrassFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<CountConfiguration>> SWEETPICKLE_FEATURE =
            featureRegister.register("sweet_pickle_feature", () -> new SweetPickleFeature(CountConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> COTTON_VINE_FEATURE =
            featureRegister.register("cotton_vine_feature", () -> new CottonVineFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOD_CORAL_TREE =
            featureRegister.register("mod_coral_tree", () -> new ModCoralTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOD_CORAL_MUSHROOM =
            featureRegister.register("mod_coral_mushroom", () -> new ModCoralMushroomFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOD_CORAL_CLAW =
            featureRegister.register("mod_coral_claw", () -> new ModCoralClawFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CORAL_SAND_NEAR_WATER =
            featureRegister.register("coral_sand_near_water", () -> new CoralSandNearWaterFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CANDY_CRAG_PILE =
            featureRegister.register("candy_crag_pile", () -> new CandyCragPileFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CANDY_BOULDER =
            featureRegister.register("candy_boulder", () -> new CandyBoulderFeature(NoneFeatureConfiguration.CODEC));


    public static void register(IEventBus eventBus){
        featureRegister.register(eventBus);
    }
}
