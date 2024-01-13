package net.sophiebun.buntsy.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.tree.bravot.SquishedBlobFoliagePlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.PieFoliagePlacer;

public class ModFoliagePlacers {

    public static final DeferredRegister<FoliagePlacerType<?>> foliagePlacerRegister =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, BuntsyMod.MODID);

    public static final RegistryObject<FoliagePlacerType<PieFoliagePlacer>> PIE_FOLIAGE_PLACER =
            foliagePlacerRegister.register("pie_foliage_placer", () -> new FoliagePlacerType<>(PieFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<SquishedBlobFoliagePlacer>> SQUISHED_BLOB_FOLIAGE_PLACER =
            foliagePlacerRegister.register("squished_blob_foliage_placer", () -> new FoliagePlacerType<>(SquishedBlobFoliagePlacer.CODEC));

    public static void register(IEventBus eventBus){
        foliagePlacerRegister.register(eventBus);
    }
}
