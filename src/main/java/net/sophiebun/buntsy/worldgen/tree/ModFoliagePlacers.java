package net.sophiebun.buntsy.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.PieFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.grounded_trunk.SphereFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.malvor.MalvorFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.origami_palm.OrigamiPalmFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.origami_palm.OrigamiPalmTrunkPlacer;

public class ModFoliagePlacers {

    public static final DeferredRegister<FoliagePlacerType<?>> foliagePlacerRegister =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, BuntsyMod.MODID);

    public static final RegistryObject<FoliagePlacerType<PieFoliagePlacer>> PIE_FOLIAGE_PLACER =
            foliagePlacerRegister.register("pie_foliage_placer", () -> new FoliagePlacerType<>(PieFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<BravotFoliagePlacer>> BRAVOT_FOLIAGE_PLACER =
            foliagePlacerRegister.register("bravot_foliage_placer", () -> new FoliagePlacerType<>(BravotFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<SphereFoliagePlacer>> SPHERE_FOLIAGE_PLACER =
            foliagePlacerRegister.register("sphere_foliage_placer", () -> new FoliagePlacerType<>(SphereFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<MalvorFoliagePlacer>> MALVOR_FOLIAGE_PLACER =
            foliagePlacerRegister.register("malvor_foliage_placer", () -> new FoliagePlacerType<>(MalvorFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<OrigamiPalmFoliagePlacer>> ORIGAMI_PALM_FOLIAGE_PLACER =
            foliagePlacerRegister.register("origami_palm_foliage_placer", () -> new FoliagePlacerType<>(OrigamiPalmFoliagePlacer.CODEC));

    public static void register(IEventBus eventBus){
        foliagePlacerRegister.register(eventBus);
    }
}
