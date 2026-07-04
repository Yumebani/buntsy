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
import net.sophiebun.buntsy.blocks.entity.advancedfairy.MagicCrystalizerBlockEntity;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MenusRegister =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BuntsyMod.MODID);

    public static final RegistryObject<MenuType<FairyOfferingBenchMenu>> FAIRY_OFFERING_BENCH_MENU =
            registerMenuType("fairy_offering_bench_menu", FairyOfferingBenchMenu::new);
    public static final RegistryObject<MenuType<GrindingWheelMenu>> GRINDING_WHEEL_MENU =
            registerMenuType("grinding_wheel_menu", GrindingWheelMenu::new);
    public static final RegistryObject<MenuType<ThreadReelerMenu>> THREAD_REELER_MENU =
            registerMenuType("thread_reeler_menu", ThreadReelerMenu::new);
    public static final RegistryObject<MenuType<FairyCollectionTrayMenu>> FAIRY_COLLECTION_TRAY_MENU =
            registerMenuType("fairy_collection_tray_menu", FairyCollectionTrayMenu::new);
    public static final RegistryObject<MenuType<FairyInfusionBenchMenu>> FAIRY_INFUSION_BENCH_MENU =
            registerMenuType("fairy_infusion_bench_menu", FairyInfusionBenchMenu::new);
    public static final RegistryObject<MenuType<MagicCrystalizerMenu>> MAGIC_CRYSTALIZER_MENU =
            registerMenuType("magic_crystalizer_menu", MagicCrystalizerMenu::new);
    public static final RegistryObject<MenuType<FumeDistilleryMenu>> FUME_DISTILLERY_MENU =
            registerMenuType("fume_distillery_menu", FumeDistilleryMenu::new);
    public static final RegistryObject<MenuType<FumeSpreaderMenu>> FUME_SPREADER_MENU =
            registerMenuType("fume_spreader_menu", FumeSpreaderMenu::new);
    public static final RegistryObject<MenuType<GiantCocoonMenu>> GIANT_COCOON_MENU =
            registerMenuType("giant_cocoon_menu", GiantCocoonMenu::new);
    public static final RegistryObject<MenuType<CocoonBagMenu>> COCOON_BAG_MENU =
            registerMenuType("cocoon_bag_menu", CocoonBagMenu::new);
    public static final RegistryObject<MenuType<MixerMenu>> MIXER_MENU =
            registerMenuType("mixer_menu", MixerMenu::new);

    public static final RegistryObject<MenuType<ClockworkSyrupExtractorMenu>> CLOCKWORK_SYRUP_EXTRACTOR_MENU =
            registerMenuType("clockwork_syrup_extractor_menu", ClockworkSyrupExtractorMenu::new);
    public static final RegistryObject<MenuType<ClockworkGeyserCollectorMenu>> CLOCKWORK_GEYSER_COLLECTOR_MENU =
            registerMenuType("clockwork_geyser_collector_menu", ClockworkGeyserCollectorMenu::new);
    public static final RegistryObject<MenuType<ClockworkPowderedSugarCollectorMenu>> CLOCKWORK_POWDERED_SUGAR_COLLECTOR_MENU =
            registerMenuType("clockwork_powdered_sugar_collector_menu", ClockworkPowderedSugarCollectorMenu::new);
    public static final RegistryObject<MenuType<ClockworkCrafterMenu>> CLOCKWORK_CRAFTER_MENU =
            registerMenuType("clockwork_crafter_menu", ClockworkCrafterMenu::new);
    public static final RegistryObject<MenuType<ClockworkFairyTerminalMenu>> CLOCKWORK_FAIRY_TERMINAL_MENU =
            registerMenuType("clockwork_fairy_terminal_menu", ClockworkFairyTerminalMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MenusRegister.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MenusRegister.register(eventBus);
    }
}
