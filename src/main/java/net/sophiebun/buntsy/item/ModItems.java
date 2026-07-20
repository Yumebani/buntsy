package net.sophiebun.buntsy.item;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.ModFoods;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.fluids.ModFluids;
import net.sophiebun.buntsy.item.custom.*;
import org.jetbrains.annotations.Nullable;

public class ModItems {
    public static final DeferredRegister<Item> ItemsRegister =
            DeferredRegister.create(ForgeRegistries.ITEMS, BuntsyMod.MODID);

    public static final RegistryObject<Item> FAIRY_TALE_BOOK = ItemsRegister.register(
            "fairy_tale_book", () -> new FairyTaleBook(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CLOCKWORK_SCRAP = ItemsRegister.register(
            "clockwork_scrap", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CLOCKWORK_SCRAP_CLUMP = ItemsRegister.register(
            "clockwork_scrap_clump", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CLOCKWORK_BRASS = ItemsRegister.register(
            "clockwork_brass", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CLOCKWORK_GEAR = ItemsRegister.register(
            "clockwork_gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CLOCKWORK_PROCESSOR = ItemsRegister.register(
            "clockwork_processor", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CLOCKWORK_MODIFICATION = ItemsRegister.register(
            "clockwork_modification", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIMPLE_CLOCKWORK_UNIT = ItemsRegister.register(
            "simple_clockwork_unit", () -> new ClockworkUpgradeItem(ClockworkTier.SIMPLE, new Item.Properties()));
    public static final RegistryObject<Item> INTRICATE_CLOCKWORK_UNIT = ItemsRegister.register(
            "intricate_clockwork_unit", () -> new ClockworkUpgradeItem(ClockworkTier.INTRICATE, new Item.Properties()));
    public static final RegistryObject<Item> COMPLEX_CLOCKWORK_UNIT = ItemsRegister.register(
            "complex_clockwork_unit", () -> new ClockworkUpgradeItem(ClockworkTier.COMPLEX, new Item.Properties()));

    public static final RegistryObject<Item> HOOTCAT_FEATHER = ItemsRegister.register(
            "hootcat_feather", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HOOTCAT_PLUME = ItemsRegister.register(
            "hootcat_plume", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHELINIX_FEATHER = ItemsRegister.register(
            "phelinix_feather", () -> new Item(new Item.Properties()));
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
            "gentlit_syrup", () -> new BottleItem(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> SUGAR_BOWL = ItemsRegister.register(
            "sugar_bowl", () -> new BowlFoodItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SYRUPY_MIXTURE_BOWL = ItemsRegister.register(
            "syrupy_mixture_bowl", () -> new BowlFoodItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> HOOTNIP = ItemsRegister.register(
            "hootnip", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLAZING_HOOTNIP = ItemsRegister.register(
            "blazing_hootnip", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GROUND_HOOTNIP = ItemsRegister.register(
            "ground_hootnip", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HOOTNIP_CEREAL = ItemsRegister.register(
            "hootnip_cereal", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SWICE_SHARDS = ItemsRegister.register(
            "swice_shards", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COLD_POWDERED_SUGAR = ItemsRegister.register(
            "cold_powdered_sugar", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOCOLATE_FLAKES = ItemsRegister.register(
            "chocolate_flakes", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FUME_BOTTLE = ItemsRegister.register(
            "fume_bottle", () -> new FumeBottle(new Item.Properties()));
    public static final RegistryObject<Item> CATALYST = ItemsRegister.register(
            "catalyst", () -> new Catalyst(new Item.Properties()));
    public static final RegistryObject<Item> EMPTY_CATALYST = ItemsRegister.register(
            "empty_catalyst", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> URO = ItemsRegister.register(
            "uro", () -> new Item(new Item.Properties().stacksTo(2)));
    public static final RegistryObject<Item> COCOON_BAG = ItemsRegister.register(
            "cocoon_bag", () -> new CocoonBag(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STRANGE_COCOON = ItemsRegister.register(
            "strange_cocoon", () -> new CocoonBag(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> FAIRY_POWER_RECEPTOR = ItemsRegister.register(
            "fairy_power_receptor", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FAIRY_POWER_EMITTER = ItemsRegister.register(
            "fairy_power_emitter", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_DUST = ItemsRegister.register(
            "amethyst_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_AMETHYST_GRAIN = ItemsRegister.register(
            "pristine_amethyst_grain", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_CRYSTAL = ItemsRegister.register(
            "iron_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_DUST = ItemsRegister.register(
            "iron_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_IRON_SAMPLE = ItemsRegister.register(
            "pristine_iron_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLOWSTONE_CRYSTAL = ItemsRegister.register(
            "glowstone_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_GLOWSTONE_SAMPLE = ItemsRegister.register(
            "pristine_glowstone_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_QUARTZ_SAMPLE = ItemsRegister.register(
            "pristine_quartz_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_CRYSTAL = ItemsRegister.register(
            "copper_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_DUST = ItemsRegister.register(
            "copper_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_COPPER_SAMPLE = ItemsRegister.register(
            "pristine_copper_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CRYSTAL = ItemsRegister.register(
            "gold_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_DUST = ItemsRegister.register(
            "gold_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_GOLD_SAMPLE = ItemsRegister.register(
            "pristine_gold_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEBRIS_SHARD = ItemsRegister.register(
            "debris_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_DUST = ItemsRegister.register(
            "netherite_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_DEBRIS_SAMPLE = ItemsRegister.register(
            "pristine_debris_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REDSTONE_CRYSTAL = ItemsRegister.register(
            "redstone_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_REDSTONE_SAMPLE = ItemsRegister.register(
            "pristine_redstone_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LAPIS_CRYSTAL = ItemsRegister.register(
            "lapis_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_LAPIS_SAMPLE = ItemsRegister.register(
            "pristine_lapis_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_SHARD = ItemsRegister.register(
            "diamond_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_DIAMOND_SAMPLE = ItemsRegister.register(
            "pristine_diamond_sample", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_SHARD = ItemsRegister.register(
            "emerald_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISTINE_EMERALD_SAMPLE = ItemsRegister.register(
            "pristine_emerald_sample", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STRAWBERRY_SEEDS = ItemsRegister.register(
            "strawberry_seeds", () -> new ItemNameBlockItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> HOOTNIP_SEEDS = ItemsRegister.register(
            "hootnip_seeds", () -> new ItemNameBlockItem(ModBlocks.HOOTNIP_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUGARDEW_SEEDS = ItemsRegister.register(
            "sugardew_seeds", () -> new ItemNameBlockItem(ModBlocks.SUGARDEW_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUGARDEW = ItemsRegister.register(
            "sugardew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WINTER_ROOT = ItemsRegister.register(
            "winter_root", () -> new ItemNameBlockItem(ModBlocks.WINTER_ROOT_CROP.get(), new Item.Properties().food(ModFoods.WINTER_ROOT)));

    public static final RegistryObject<Item> SUGARDEW_BALL = ItemsRegister.register(
            "sugardew_ball", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEW_CRYSTALS = ItemsRegister.register(
            "dew_crystals", () -> new Item(new Item.Properties()){
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 1600;
                }
            });
    public static final RegistryObject<Item> ROOT_FLOUR = ItemsRegister.register(
            "root_flour", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SPEED_BLEND = ItemsRegister.register(
            "speed_blend", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EFFICIENCY_BLEND = ItemsRegister.register(
            "efficiency_blend", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GROWTH_BLEND = ItemsRegister.register(
            "growth_blend", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SLOTH_BLEND = ItemsRegister.register(
            "sloth_blend", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_BLEND = ItemsRegister.register(
            "rotten_blend", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> ESSENCE = ItemsRegister.register(
            "essence", () -> new Essence(new Item.Properties()));
    public static final RegistryObject<Item> PRISM = ItemsRegister.register(
            "prism", () -> new Prism(new Item.Properties()));

    public static final RegistryObject<Item> SILKY_SWORD = ItemsRegister.register(
            "silky_sword", () -> new SwordItem(ModToolTiers.SILKY, 7, -2.0F, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_PICKAXE = ItemsRegister.register(
            "silky_pickaxe", () -> new PickaxeItem(ModToolTiers.SILKY, 5, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_AXE = ItemsRegister.register(
            "silky_axe", () -> new AxeItem(ModToolTiers.SILKY, 7, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_SHOVEL = ItemsRegister.register(
            "silky_shovel", () -> new ShovelItem(ModToolTiers.SILKY, 5.5f, -2.6F, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_HOE = ItemsRegister.register(
            "silky_hoe", () -> new HoeItem(ModToolTiers.SILKY, 1, -2.6F, new Item.Properties()));

    public static final RegistryObject<Item> SILKY_HELMET = ItemsRegister.register(
            "silky_helmet", () -> new SilkyArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_CHESTPLATE = ItemsRegister.register(
            "silky_chestplate", () -> new SilkyArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_LEGGINGS = ItemsRegister.register(
            "silky_leggings", () -> new SilkyArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SILKY_BOOTS = ItemsRegister.register(
            "silky_boots", () -> new SilkyArmorItem(ModArmorMaterials.SILKY, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> HOOTCAT_HELMET = ItemsRegister.register(
            "hootcat_helmet", () -> new HootcatArmorItem(HootcatArmorMaterial.HOOTCAT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> HOOTCAT_CHESTPLATE = ItemsRegister.register(
            "hootcat_chestplate", () -> new HootcatArmorItem(HootcatArmorMaterial.HOOTCAT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> HOOTCAT_LEGGINGS = ItemsRegister.register(
            "hootcat_leggings", () -> new HootcatArmorItem(HootcatArmorMaterial.HOOTCAT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> HOOTCAT_BOOTS = ItemsRegister.register(
            "hootcat_boots", () -> new HootcatArmorItem(HootcatArmorMaterial.HOOTCAT, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> BUNNY_EARS = ItemsRegister.register(
            "bunny_ears", () -> new BunnyEarsItem(ClothArmorMaterial.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> CAT_EARS = ItemsRegister.register(
            "cat_ears", () -> new CatEarsItem(ClothArmorMaterial.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> HEAD_BOW = ItemsRegister.register(
            "head_bow", () -> new HeadBowItem(ClothArmorMaterial.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> GAS_MASK = ItemsRegister.register(
            "gas_mask", () -> new GasMaskItem(ClothArmorMaterial.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> BOWL_OF_CARAMEL = ItemsRegister.register(
            "bowl_of_caramel", () -> new BowlFoodItem(new Item.Properties().food(ModFoods.BOWL_OF_CARAMEL).stacksTo(16).craftRemainder(Items.BOWL)));
    public static final RegistryObject<Item> BOWL_OF_ROCKCANDY = ItemsRegister.register(
            "bowl_of_rockcandy", () -> new BowlFoodItem(new Item.Properties().food(ModFoods.BOWL_OF_ROCKCANDY).stacksTo(16)));
    public static final RegistryObject<Item> CARAMEL_STRAWBERRIES = ItemsRegister.register(
            "caramel_strawberries", () -> new Item(new Item.Properties().food(ModFoods.CARAMEL_STRAWBERRIES)));
    public static final RegistryObject<Item> CHOCOLATE_STRAWBERRIES = ItemsRegister.register(
            "chocolate_strawberries", () -> new Item(new Item.Properties().food(ModFoods.CHOCOLATE_STRAWBERRIES)));
    public static final RegistryObject<Item> STRAWBERRY = ItemsRegister.register(
            "strawberry", () -> new Item(new Item.Properties().food(ModFoods.STRAWBERRY)));
    public static final RegistryObject<Item> GOLDEN_STRAWBERRY = ItemsRegister.register(
            "golden_strawberry", () -> new Item(new Item.Properties().food(ModFoods.GOLDEN_STRAWBERRY)));
    public static final RegistryObject<Item> CHOCOLATE = ItemsRegister.register(
            "chocolate", () -> new Item(new Item.Properties().food(ModFoods.CHOCOLATE)));
    public static final RegistryObject<Item> VANILLA_ICECREAM = ItemsRegister.register(
            "vanilla_icecream", () -> new Item(new Item.Properties().food(ModFoods.ICECREAM)));
    public static final RegistryObject<Item> CHOCOLATE_ICECREAM = ItemsRegister.register(
            "chocolate_icecream", () -> new Item(new Item.Properties().food(ModFoods.CHOCOLATE_ICECREAM)));
    public static final RegistryObject<Item> CARAMEL_ICECREAM = ItemsRegister.register(
            "caramel_icecream", () -> new Item(new Item.Properties().food(ModFoods.CARAMEL_ICECREAM)));
    public static final RegistryObject<Item> TRIPLE_SHOT_ICECREAM = ItemsRegister.register(
            "triple_shot_icecream", () -> new Item(new Item.Properties().food(ModFoods.TRIPLE_SHOT_ICECREAM)));
    public static final RegistryObject<Item> ROOT_WAFFLE = ItemsRegister.register(
            "root_waffle", () -> new Item(new Item.Properties().food(ModFoods.ROOT_WAFFLE)));

    public static final RegistryObject<Item> FAIRY_IN_A_BOTTLE = ItemsRegister.register(
            "fairy_in_a_bottle", () -> new FairyBottle(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FAIRY_STAFF = ItemsRegister.register(
            "fairy_staff", () -> new FairyStaff(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BINDING_STAFF = ItemsRegister.register(
            "binding_staff", () -> new BindingStaff(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CLOCKWORK_CARD_PUNCHER = ItemsRegister.register(
            "clockwork_card_puncher", () -> new ClockworkCardPuncher(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HOT_CHOCOLATE_BUCKET = ItemsRegister.register(
            "hot_chocolate_bucket", () -> new BucketItem(ModFluids.SOURCE_HOT_CHOCOLATE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> EMPTY_CLOCKWORK_FAIRY_TERMINAL = ItemsRegister.register(
            "empty_clockwork_fairy_terminal", () -> new EmptyClockworkFairyTerminal(new Item.Properties()));

    public static final RegistryObject<Item> SILKBUN_SPAWN_EGG = ItemsRegister.register(
            "silkbun_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SILKBUN_ENTITY, 0xfdf4f7, 0x673f4e , new Item.Properties()));
    public static final RegistryObject<Item> FAIRY_SPAWN_EGG = ItemsRegister.register(
            "fairy_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FAIRY_ENTITY, 0xfae54e, 0xfff4ac , new Item.Properties()));
    public static final RegistryObject<Item> HOOTCAT_SPAWN_EGG = ItemsRegister.register(
            "hootcat_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.HOOTCAT_ENTITY, 0x4d3f3f, 0x9e7f4b , new Item.Properties()));

    public static final RegistryObject<Item> MARIONETTE_SPAWN_EGG = ItemsRegister.register(
            "marionette_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.MARIONETTE, 0x45393b, 0xb8433b , new Item.Properties()));
    public static final RegistryObject<Item> CLOCKWORK_MAIDEN = ItemsRegister.register(
            "clockwork_maiden", () -> new ForgeSpawnEggItem(ModEntities.CLOCKWORK_MAIDEN_ENTITY, 0xFFFFFF, 0xFFFFFF , new Item.Properties()));

    public static void register(IEventBus eventBus){
        ItemsRegister.register(eventBus);
    }
}
