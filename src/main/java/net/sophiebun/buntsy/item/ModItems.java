package net.sophiebun.buntsy.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModFoods;
import net.sophiebun.buntsy.item.custom.FairyBottle;

public class ModItems {
    public static final DeferredRegister<Item> ItemsRegister =
            DeferredRegister.create(ForgeRegistries.ITEMS, BuntsyMod.MODID);

    public static final RegistryObject<Item> COCOON = ItemsRegister.register(
            "cocoon", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILK = ItemsRegister.register(
            "silk", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILK_SPOOL = ItemsRegister.register(
            "silk_spool", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILK_FABRIC = ItemsRegister.register(
            "silk_fabric", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOLTED_MOTH_WINGS = ItemsRegister.register(
            "molted_moth_wings", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOTH_WING_THREAD = ItemsRegister.register(
            "moth_wing_thread", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOUGH_SILK_FABRIC = ItemsRegister.register(
            "tough_silk_fabric", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILKY_INGOT = ItemsRegister.register(
            "silky_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILKY_NUGGET = ItemsRegister.register(
            "silky_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILKY_CRYSTAL = ItemsRegister.register(
            "silky_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FAIRY_DUST = ItemsRegister.register(
            "fairy_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GENTLIT_SYRUP = ItemsRegister.register(
            "gentlit_syrup", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SUGAR_BOWL = ItemsRegister.register(
            "sugar_bowl", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SYRUPY_MIXTURE_BOWL = ItemsRegister.register(
            "syrupy_mixture_bowl", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> BOWL_OF_CARAMEL = ItemsRegister.register(
            "bowl_of_caramel", () -> new Item(new Item.Properties().food(ModFoods.BOWL_OF_CARAMEL).stacksTo(16)));
    public static final RegistryObject<Item> BOWL_OF_ROCKCANDY = ItemsRegister.register(
            "bowl_of_rockcandy", () -> new Item(new Item.Properties().food(ModFoods.BOWL_OF_ROCKCANDY).stacksTo(16)));
    public static final RegistryObject<Item> CARAMEL_STRAWBERRIES = ItemsRegister.register(
            "caramel_strawberries", () -> new Item(new Item.Properties().food(ModFoods.CARAMEL_STRAWBERRIES).stacksTo(16)));
    public static final RegistryObject<Item> STRAWBERRY = ItemsRegister.register(
            "strawberry", () -> new Item(new Item.Properties().food(ModFoods.STRAWBERRY)));
    public static final RegistryObject<Item> GOLDEN_STRAWBERRY = ItemsRegister.register(
            "golden_strawberry", () -> new Item(new Item.Properties().food(ModFoods.GOLDEN_STRAWBERRY)));

    public static final RegistryObject<Item> FAIRY_IN_A_BOTTLE = ItemsRegister.register(
            "fairy_in_a_bottle", () -> new FairyBottle(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus eventBus){
        ItemsRegister.register(eventBus);
    }
}
