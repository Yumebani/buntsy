package net.sophiebun.buntsy.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;

import java.awt.*;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MenusRegister =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BuntsyMod.MODID);

    public static final RegistryObject<MenuType<GrindingWheelMenu>> GRINDING_WHEEL_MENU =
            registerMenuType("grinding_wheel_menu", GrindingWheelMenu::new);
    public static final RegistryObject<MenuType<ThreadReelerMenu>> THREAD_REELER_MENU =
            registerMenuType("thread_reeler_menu", ThreadReelerMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MenusRegister.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MenusRegister.register(eventBus);
    }
}
