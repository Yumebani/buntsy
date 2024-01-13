package net.sophiebun.buntsy.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.worldgen.tree.bravot.BravotTrunkPlacer;
import net.sophiebun.buntsy.worldgen.tree.gentlit.GentlitTrunkPlacer;

public class ModTrunkPlacerTypes {

    public static final DeferredRegister<TrunkPlacerType<?>> trunkPlacerRegister =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, BuntsyMod.MODID);

    public static final RegistryObject<TrunkPlacerType<GentlitTrunkPlacer>> GENTLIT_TRUNK_PLACER =
            trunkPlacerRegister.register("gentlit_trunk_placer", () -> new TrunkPlacerType<>(GentlitTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<BravotTrunkPlacer>> BRAVOT_TRUNK_PLACER =
            trunkPlacerRegister.register("bravot_trunk_placer", () -> new TrunkPlacerType<>(BravotTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus){
        trunkPlacerRegister.register(eventBus);
    }
}
