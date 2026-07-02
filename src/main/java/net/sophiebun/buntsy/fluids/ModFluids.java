package net.sophiebun.buntsy.fluids;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, BuntsyMod.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_HOT_CHOCOLATE = FLUIDS.register("hot_chocolate_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.HOT_CHOCOLATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HOT_CHOCOLATE = FLUIDS.register("flowing_hot_chocolate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HOT_CHOCOLATE_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties HOT_CHOCOLATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.HOT_CHOCOLATE_FLUID_TYPE, SOURCE_HOT_CHOCOLATE, FLOWING_HOT_CHOCOLATE)
            .slopeFindDistance(2).levelDecreasePerBlock(5).block(ModBlocks.HOT_CHOCOLATE_BLOCK)
            .bucket(ModItems.HOT_CHOCOLATE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
