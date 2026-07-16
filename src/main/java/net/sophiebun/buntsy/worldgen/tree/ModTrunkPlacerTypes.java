package net.sophiebun.buntsy.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.GentlitTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.grounded_trunk.GroundedTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.malvor.MalvorTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.origami_palm.OrigamiPalmTrunkPlacer;

public class ModTrunkPlacerTypes {

    public static final DeferredRegister<TrunkPlacerType<?>> trunkPlacerRegister =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, BuntsyMod.MODID);

    public static final RegistryObject<TrunkPlacerType<GentlitTrunkPlacer>> GENTLIT_TRUNK_PLACER =
            trunkPlacerRegister.register("gentlit_trunk_placer", () -> new TrunkPlacerType<>(GentlitTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<BravotTrunkPlacer>> BRAVOT_TRUNK_PLACER =
            trunkPlacerRegister.register("bravot_trunk_placer", () -> new TrunkPlacerType<>(BravotTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<GroundedTrunkPlacer>> GROUNDED_TRUNK_PLACER =
            trunkPlacerRegister.register("grounded_trunk_placer", () -> new TrunkPlacerType<>(GroundedTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<MalvorTrunkPlacer>> MALVOR_TRUNK_PLACER =
            trunkPlacerRegister.register("malvor_trunk_placer", () -> new TrunkPlacerType<>(MalvorTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<OrigamiPalmTrunkPlacer>> ORIGAMI_PALM_TRUNK_PLACER =
            trunkPlacerRegister.register("origami_palm_trunk_placer", () -> new TrunkPlacerType<>(OrigamiPalmTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus){
        trunkPlacerRegister.register(eventBus);
    }
}
