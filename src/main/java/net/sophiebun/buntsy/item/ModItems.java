package net.sophiebun.buntsy.item;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModFoods;
import net.sophiebun.buntsy.item.custom.BowlFoodFairyFoodItem;
import net.sophiebun.buntsy.item.custom.FairyBottle;
import net.sophiebun.buntsy.item.custom.FairyFoodItem;

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
    public static final RegistryObject<Item> AMETHYST_DUST = ItemsRegister.register(
            "amethyst_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GENTLIT_SYRUP = ItemsRegister.register(
            "gentlit_syrup", () -> new FairyFoodItem(new Item.Properties().stacksTo(16), 800, 1f));
    public static final RegistryObject<Item> SUGAR_BOWL = ItemsRegister.register(
            "sugar_bowl", () -> new FairyFoodItem(new Item.Properties().stacksTo(16), 1000, 1f));
    public static final RegistryObject<Item> SYRUPY_MIXTURE_BOWL = ItemsRegister.register(
            "syrupy_mixture_bowl", () -> new FairyFoodItem(new Item.Properties().stacksTo(16), 2000, 1.5f));
    public static final RegistryObject<Item> HOOTNIP = ItemsRegister.register(
            "hootnip", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GROUND_HOOTNIP = ItemsRegister.register(
            "ground_hootnip", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HOOTNIP_CEREAL = ItemsRegister.register(
            "hootnip_cereal", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> SILKY_SWORD = ItemsRegister.register(
            "silky_sword", () -> new SwordItem(ModToolTiers.SILKY, 7, 2f, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_PICKAXE = ItemsRegister.register(
            "silky_pickaxe", () -> new PickaxeItem(ModToolTiers.SILKY, 5, 1.4f, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_AXE = ItemsRegister.register(
            "silky_axe", () -> new AxeItem(ModToolTiers.SILKY, 7, 1.2f, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_SHOVEL = ItemsRegister.register(
            "silky_shovel", () -> new ShovelItem(ModToolTiers.SILKY, 5.5f, 1.2f, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_HOE = ItemsRegister.register(
            "silky_hoe", () -> new HoeItem(ModToolTiers.SILKY, 1, 5f, new Item.Properties()));

    public static final RegistryObject<Item> SILKY_HELMET = ItemsRegister.register(
            "silky_helmet", () -> new ArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_CHESTPLATE = ItemsRegister.register(
            "silky_chestplate", () -> new ArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_LEGGINGS = ItemsRegister.register(
            "silky_leggings", () -> new ArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_BOOTS = ItemsRegister.register(
            "silky_boots", () -> new ArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> BOWL_OF_CARAMEL = ItemsRegister.register(
            "bowl_of_caramel", () -> new BowlFoodFairyFoodItem(new Item.Properties().food(ModFoods.BOWL_OF_CARAMEL).stacksTo(16),
                    2000, 1.5f));
    public static final RegistryObject<Item> BOWL_OF_ROCKCANDY = ItemsRegister.register(
            "bowl_of_rockcandy", () -> new BowlFoodFairyFoodItem(new Item.Properties().food(ModFoods.BOWL_OF_ROCKCANDY)
                    .stacksTo(16),4000, 2f));
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
