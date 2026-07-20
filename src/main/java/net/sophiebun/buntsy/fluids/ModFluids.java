package net.sophiebun.buntsy.fluids;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, BuntsyMod.MODID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE_HOT_CHOCOLATE = FLUIDS.register("hot_chocolate_fluid",
            () -> new BaseFlowingFluid.Source(ModFluids.HOT_CHOCOLATE_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HOT_CHOCOLATE = FLUIDS.register("flowing_hot_chocolate",
            () -> new BaseFlowingFluid.Flowing(ModFluids.HOT_CHOCOLATE_FLUID_PROPERTIES));


    public static final BaseFlowingFluid.Properties HOT_CHOCOLATE_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.HOT_CHOCOLATE_FLUID_TYPE, SOURCE_HOT_CHOCOLATE, FLOWING_HOT_CHOCOLATE)
            .slopeFindDistance(2).levelDecreasePerBlock(3).block(ModBlocks.HOT_CHOCOLATE_BLOCK)
            .bucket(ModItems.HOT_CHOCOLATE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
