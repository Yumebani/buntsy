package net.sophiebun.buntsy.screen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.screen.clockwork.*;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MenusRegister =
            DeferredRegister.create(Registries.MENU, BuntsyMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<FairyOfferingBenchMenu>> FAIRY_OFFERING_BENCH_MENU =
            registerMenuType("fairy_offering_bench_menu", FairyOfferingBenchMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<GrindingWheelMenu>> GRINDING_WHEEL_MENU =
            registerMenuType("grinding_wheel_menu", GrindingWheelMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ThreadReelerMenu>> THREAD_REELER_MENU =
            registerMenuType("thread_reeler_menu", ThreadReelerMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<FairyCollectionTrayMenu>> FAIRY_COLLECTION_TRAY_MENU =
            registerMenuType("fairy_collection_tray_menu", FairyCollectionTrayMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<FairyInfusionBenchMenu>> FAIRY_INFUSION_BENCH_MENU =
            registerMenuType("fairy_infusion_bench_menu", FairyInfusionBenchMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<MagicCrystalizerMenu>> MAGIC_CRYSTALIZER_MENU =
            registerMenuType("magic_crystalizer_menu", MagicCrystalizerMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<FumeDistilleryMenu>> FUME_DISTILLERY_MENU =
            registerMenuType("fume_distillery_menu", FumeDistilleryMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<FumeSpreaderMenu>> FUME_SPREADER_MENU =
            registerMenuType("fume_spreader_menu", FumeSpreaderMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<GiantCocoonMenu>> GIANT_COCOON_MENU =
            registerMenuType("giant_cocoon_menu", GiantCocoonMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CocoonBagMenu>> COCOON_BAG_MENU =
            registerMenuType("cocoon_bag_menu", CocoonBagMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<MixerMenu>> MIXER_MENU =
            registerMenuType("mixer_menu", MixerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkSyrupExtractorMenu>> CLOCKWORK_SYRUP_EXTRACTOR_MENU =
            registerMenuType("clockwork_syrup_extractor_menu", ClockworkSyrupExtractorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkGeyserCollectorMenu>> CLOCKWORK_GEYSER_COLLECTOR_MENU =
            registerMenuType("clockwork_geyser_collector_menu", ClockworkGeyserCollectorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkPowderedSugarCollectorMenu>> CLOCKWORK_POWDERED_SUGAR_COLLECTOR_MENU =
            registerMenuType("clockwork_powdered_sugar_collector_menu", ClockworkPowderedSugarCollectorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkFisherMenu>> CLOCKWORK_FISHER_MENU =
            registerMenuType("clockwork_fisher_menu", ClockworkFisherMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkWinderMenu>> CLOCKWORK_WINDER_MENU =
            registerMenuType("clockwork_winder_menu", ClockworkWinderMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkCrafterMenu>> CLOCKWORK_CRAFTER_MENU =
            registerMenuType("clockwork_crafter_menu", ClockworkCrafterMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ClockworkFairyTerminalMenu>> CLOCKWORK_FAIRY_TERMINAL_MENU =
            registerMenuType("clockwork_fairy_terminal_menu", ClockworkFairyTerminalMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<CMTParticipantMenu>> CMT_PARTICIPANT_MENU =
            registerMenuType("cmt_participant_menu", CMTParticipantMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MenusRegister.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus){
        MenusRegister.register(eventBus);
    }
}
