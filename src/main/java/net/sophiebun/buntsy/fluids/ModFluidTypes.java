package net.sophiebun.buntsy.fluids;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.sophiebun.buntsy.BuntsyMod;
import org.joml.Vector3f;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL =  ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/water_flow");
    public static final ResourceLocation SOAP_OVERLAY_RL =  ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "misc/in_soap_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, BuntsyMod.MODID);

    public static final DeferredHolder<FluidType, FluidType> HOT_CHOCOLATE_FLUID_TYPE = register("hot_chocolate_fluid",
            FluidType.Properties.create().density(40).viscosity(10).temperature(100));



    private static DeferredHolder<FluidType, FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new WaterBasedFluidType(WATER_STILL_RL, WATER_FLOWING_RL, SOAP_OVERLAY_RL,
                0xFF3b2a25, new Vector3f(120f / 255f, 80f / 255f, 70f / 255f), properties));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
