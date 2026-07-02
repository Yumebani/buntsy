package net.sophiebun.buntsy.fluids;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import org.joml.Vector3f;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation SOAP_OVERLAY_RL = new ResourceLocation(BuntsyMod.MODID, "misc/in_soap_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, BuntsyMod.MODID);

    public static final RegistryObject<FluidType> HOT_CHOCOLATE_FLUID_TYPE = register("hot_chocolate_fluid",
            FluidType.Properties.create().density(40).viscosity(10).temperature(100));



    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new WaterBasedFluidType(WATER_STILL_RL, WATER_FLOWING_RL, SOAP_OVERLAY_RL,
                0xFF3b2a25, new Vector3f(59f / 255f, 42f / 255f, 37f / 255f), properties));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
