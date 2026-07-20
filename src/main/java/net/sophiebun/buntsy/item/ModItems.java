package net.sophiebun.buntsy.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.ModFoods;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.fluids.ModFluids;
import net.sophiebun.buntsy.item.custom.*;
import org.jetbrains.annotations.Nullable;

public class ModItems {
    public static final DeferredRegister<Item> ItemsRegister =
            DeferredRegister.create(Registries.ITEM, BuntsyMod.MODID);

    public static final DeferredHolder<Item, Item> FAIRY_TALE_BOOK = ItemsRegister.register(
            "fairy_tale_book", () -> new FairyTaleBook(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> CLOCKWORK_SCRAP = ItemsRegister.register(
            "clockwork_scrap", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLOCKWORK_SCRAP_CLUMP = ItemsRegister.register(
            "clockwork_scrap_clump", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLOCKWORK_BRASS = ItemsRegister.register(
            "clockwork_brass", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLOCKWORK_GEAR = ItemsRegister.register(
            "clockwork_gear", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLOCKWORK_PROCESSOR = ItemsRegister.register(
            "clockwork_processor", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLOCKWORK_MODIFICATION = ItemsRegister.register(
            "clockwork_modification", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIMPLE_CLOCKWORK_UNIT = ItemsRegister.register(
            "simple_clockwork_unit", () -> new ClockworkUpgradeItem(ClockworkTier.SIMPLE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> INTRICATE_CLOCKWORK_UNIT = ItemsRegister.register(
            "intricate_clockwork_unit", () -> new ClockworkUpgradeItem(ClockworkTier.INTRICATE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> COMPLEX_CLOCKWORK_UNIT = ItemsRegister.register(
            "complex_clockwork_unit", () -> new ClockworkUpgradeItem(ClockworkTier.COMPLEX, new Item.Properties()));

    public static final DeferredHolder<Item, Item> HOOTCAT_FEATHER = ItemsRegister.register(
            "hootcat_feather", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOOTCAT_PLUME = ItemsRegister.register(
            "hootcat_plume", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PHELINIX_FEATHER = ItemsRegister.register(
            "phelinix_feather", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COCOON = ItemsRegister.register(
            "cocoon", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILK = ItemsRegister.register(
            "silk", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILK_SPOOL = ItemsRegister.register(
            "silk_spool", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILK_FABRIC = ItemsRegister.register(
            "silk_fabric", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOLTED_MOTH_WINGS = ItemsRegister.register(
            "molted_moth_wings", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOTH_WING_THREAD = ItemsRegister.register(
            "moth_wing_thread", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> TOUGH_SILK_FABRIC = ItemsRegister.register(
            "tough_silk_fabric", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILKY_INGOT = ItemsRegister.register(
            "silky_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILKY_NUGGET = ItemsRegister.register(
            "silky_nugget", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILKY_CRYSTAL = ItemsRegister.register(
            "silky_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FAIRY_DUST = ItemsRegister.register(
            "fairy_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GENTLIT_SYRUP = ItemsRegister.register(
            "gentlit_syrup", () -> new BottleItem(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final DeferredHolder<Item, Item> SUGAR_BOWL = ItemsRegister.register(
            "sugar_bowl", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> SYRUPY_MIXTURE_BOWL = ItemsRegister.register(
            "syrupy_mixture_bowl", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> HOOTNIP = ItemsRegister.register(
            "hootnip", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BLAZING_HOOTNIP = ItemsRegister.register(
            "blazing_hootnip", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GROUND_HOOTNIP = ItemsRegister.register(
            "ground_hootnip", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOOTNIP_CEREAL = ItemsRegister.register(
            "hootnip_cereal", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> SWICE_SHARDS = ItemsRegister.register(
            "swice_shards", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COLD_POWDERED_SUGAR = ItemsRegister.register(
            "cold_powdered_sugar", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CHOCOLATE_FLAKES = ItemsRegister.register(
            "chocolate_flakes", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FUME_BOTTLE = ItemsRegister.register(
            "fume_bottle", () -> new FumeBottle(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CATALYST = ItemsRegister.register(
            "catalyst", () -> new Catalyst(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMPTY_CATALYST = ItemsRegister.register(
            "empty_catalyst", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> URO = ItemsRegister.register(
            "uro", () -> new Item(new Item.Properties().stacksTo(2)));
    public static final DeferredHolder<Item, Item> COCOON_BAG = ItemsRegister.register(
            "cocoon_bag", () -> new CocoonBag(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> STRANGE_COCOON = ItemsRegister.register(
            "strange_cocoon", () -> new CocoonBag(new Item.Properties().stacksTo(1)));


    public static final DeferredHolder<Item, Item> FAIRY_POWER_RECEPTOR = ItemsRegister.register(
            "fairy_power_receptor", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FAIRY_POWER_EMITTER = ItemsRegister.register(
            "fairy_power_emitter", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> AMETHYST_DUST = ItemsRegister.register(
            "amethyst_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_AMETHYST_GRAIN = ItemsRegister.register(
            "pristine_amethyst_grain", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> IRON_CRYSTAL = ItemsRegister.register(
            "iron_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> IRON_DUST = ItemsRegister.register(
            "iron_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_IRON_SAMPLE = ItemsRegister.register(
            "pristine_iron_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GLOWSTONE_CRYSTAL = ItemsRegister.register(
            "glowstone_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_GLOWSTONE_SAMPLE = ItemsRegister.register(
            "pristine_glowstone_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_QUARTZ_SAMPLE = ItemsRegister.register(
            "pristine_quartz_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COPPER_CRYSTAL = ItemsRegister.register(
            "copper_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COPPER_DUST = ItemsRegister.register(
            "copper_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_COPPER_SAMPLE = ItemsRegister.register(
            "pristine_copper_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLD_CRYSTAL = ItemsRegister.register(
            "gold_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLD_DUST = ItemsRegister.register(
            "gold_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_GOLD_SAMPLE = ItemsRegister.register(
            "pristine_gold_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DEBRIS_SHARD = ItemsRegister.register(
            "debris_shard", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHERITE_DUST = ItemsRegister.register(
            "netherite_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_DEBRIS_SAMPLE = ItemsRegister.register(
            "pristine_debris_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REDSTONE_CRYSTAL = ItemsRegister.register(
            "redstone_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_REDSTONE_SAMPLE = ItemsRegister.register(
            "pristine_redstone_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LAPIS_CRYSTAL = ItemsRegister.register(
            "lapis_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_LAPIS_SAMPLE = ItemsRegister.register(
            "pristine_lapis_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DIAMOND_SHARD = ItemsRegister.register(
            "diamond_shard", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_DIAMOND_SAMPLE = ItemsRegister.register(
            "pristine_diamond_sample", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMERALD_SHARD = ItemsRegister.register(
            "emerald_shard", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISTINE_EMERALD_SAMPLE = ItemsRegister.register(
            "pristine_emerald_sample", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> STRAWBERRY_SEEDS = ItemsRegister.register(
            "strawberry_seeds", () -> new ItemNameBlockItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOOTNIP_SEEDS = ItemsRegister.register(
            "hootnip_seeds", () -> new ItemNameBlockItem(ModBlocks.HOOTNIP_CROP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> SUGARDEW_SEEDS = ItemsRegister.register(
            "sugardew_seeds", () -> new ItemNameBlockItem(ModBlocks.SUGARDEW_CROP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> SUGARDEW = ItemsRegister.register(
            "sugardew", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WINTER_ROOT = ItemsRegister.register(
            "winter_root", () -> new ItemNameBlockItem(ModBlocks.WINTER_ROOT_CROP.get(), new Item.Properties().food(ModFoods.WINTER_ROOT)));

    public static final DeferredHolder<Item, Item> SUGARDEW_BALL = ItemsRegister.register(
            "sugardew_ball", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DEW_CRYSTALS = ItemsRegister.register(
            "dew_crystals", () -> new Item(new Item.Properties()){
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 1600;
                }
            });
    public static final DeferredHolder<Item, Item> ROOT_FLOUR = ItemsRegister.register(
            "root_flour", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SPEED_BLEND = ItemsRegister.register(
            "speed_blend", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EFFICIENCY_BLEND = ItemsRegister.register(
            "efficiency_blend", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GROWTH_BLEND = ItemsRegister.register(
            "growth_blend", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SLOTH_BLEND = ItemsRegister.register(
            "sloth_blend", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ROTTEN_BLEND = ItemsRegister.register(
            "rotten_blend", () -> new Item(new Item.Properties()));


    public static final DeferredHolder<Item, Item> ESSENCE = ItemsRegister.register(
            "essence", () -> new Essence(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PRISM = ItemsRegister.register(
            "prism", () -> new Prism(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SILKY_SWORD = ItemsRegister.register(
            "silky_sword", () -> new SwordItem(ModToolTiers.SILKY, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.SILKY, 7, -2.0F))));
    public static final DeferredHolder<Item, Item> SILKY_PICKAXE = ItemsRegister.register(
            "silky_pickaxe", () -> new PickaxeItem(ModToolTiers.SILKY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.SILKY, 5, -2.4F))));
    public static final DeferredHolder<Item, Item> SILKY_AXE = ItemsRegister.register(
            "silky_axe", () -> new AxeItem(ModToolTiers.SILKY, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.SILKY, 7, -2.8F))));
    public static final DeferredHolder<Item, Item> SILKY_SHOVEL = ItemsRegister.register(
            "silky_shovel", () -> new ShovelItem(ModToolTiers.SILKY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.SILKY, 5.5f, -2.6F))));
    public static final DeferredHolder<Item, Item> SILKY_HOE = ItemsRegister.register(
            "silky_hoe", () -> new HoeItem(ModToolTiers.SILKY, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.SILKY, 1, -2.6F))));

    public static final DeferredHolder<Item, Item> SILKY_HELMET = ItemsRegister.register(
            "silky_helmet", () -> new SilkyArmorItem(ModArmorMats.SILKY, ArmorItem.Type.HELMET, new Item.Properties().durability(319)));
    public static final DeferredHolder<Item, Item> SILKY_CHESTPLATE = ItemsRegister.register(
            "silky_chestplate", () -> new SilkyArmorItem(ModArmorMats.SILKY, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(464)));
    public static final DeferredHolder<Item, Item> SILKY_LEGGINGS = ItemsRegister.register(
            "silky_leggings", () -> new SilkyArmorItem(ModArmorMats.SILKY, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(464)));
    public static final DeferredHolder<Item, Item> SILKY_BOOTS = ItemsRegister.register(
            "silky_boots", () -> new SilkyArmorItem(ModArmorMats.SILKY, ArmorItem.Type.BOOTS, new Item.Properties().durability(368)));

    public static final DeferredHolder<Item, Item> HOOTCAT_HELMET = ItemsRegister.register(
            "hootcat_helmet", () -> new HootcatArmorItem(ModArmorMats.HOOTCAT, ArmorItem.Type.HELMET, new Item.Properties().durability(319)));
    public static final DeferredHolder<Item, Item> HOOTCAT_CHESTPLATE = ItemsRegister.register(
            "hootcat_chestplate", () -> new HootcatArmorItem(ModArmorMats.HOOTCAT, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(464)));
    public static final DeferredHolder<Item, Item> HOOTCAT_LEGGINGS = ItemsRegister.register(
            "hootcat_leggings", () -> new HootcatArmorItem(ModArmorMats.HOOTCAT, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(464)));
    public static final DeferredHolder<Item, Item> HOOTCAT_BOOTS = ItemsRegister.register(
            "hootcat_boots", () -> new HootcatArmorItem(ModArmorMats.HOOTCAT, ArmorItem.Type.BOOTS, new Item.Properties().durability(368)));

    public static final DeferredHolder<Item, Item> BUNNY_EARS = ItemsRegister.register(
            "bunny_ears", () -> new BunnyEarsItem(ModArmorMats.CLOTH, ArmorItem.Type.HELMET, new Item.Properties().durability(440)));
    public static final DeferredHolder<Item, Item> CAT_EARS = ItemsRegister.register(
            "cat_ears", () -> new CatEarsItem(ModArmorMats.CLOTH, ArmorItem.Type.HELMET, new Item.Properties().durability(440)));
    public static final DeferredHolder<Item, Item> HEAD_BOW = ItemsRegister.register(
            "head_bow", () -> new HeadBowItem(ModArmorMats.CLOTH, ArmorItem.Type.HELMET, new Item.Properties().durability(440)));
    public static final DeferredHolder<Item, Item> GAS_MASK = ItemsRegister.register(
            "gas_mask", () -> new GasMaskItem(ModArmorMats.CLOTH, ArmorItem.Type.HELMET, new Item.Properties().durability(440)));

    public static final DeferredHolder<Item, Item> BOWL_OF_CARAMEL = ItemsRegister.register(
            "bowl_of_caramel", () -> new Item(new Item.Properties().food(ModFoods.BOWL_OF_CARAMEL).stacksTo(16).craftRemainder(Items.BOWL)));
    public static final DeferredHolder<Item, Item> BOWL_OF_ROCKCANDY = ItemsRegister.register(
            "bowl_of_rockcandy", () -> new Item(new Item.Properties().food(ModFoods.BOWL_OF_ROCKCANDY).stacksTo(16)));
    public static final DeferredHolder<Item, Item> CARAMEL_STRAWBERRIES = ItemsRegister.register(
            "caramel_strawberries", () -> new Item(new Item.Properties().food(ModFoods.CARAMEL_STRAWBERRIES)));
    public static final DeferredHolder<Item, Item> CHOCOLATE_STRAWBERRIES = ItemsRegister.register(
            "chocolate_strawberries", () -> new Item(new Item.Properties().food(ModFoods.CHOCOLATE_STRAWBERRIES)));
    public static final DeferredHolder<Item, Item> STRAWBERRY = ItemsRegister.register(
            "strawberry", () -> new Item(new Item.Properties().food(ModFoods.STRAWBERRY)));
    public static final DeferredHolder<Item, Item> GOLDEN_STRAWBERRY = ItemsRegister.register(
            "golden_strawberry", () -> new Item(new Item.Properties().food(ModFoods.GOLDEN_STRAWBERRY)));
    public static final DeferredHolder<Item, Item> CHOCOLATE = ItemsRegister.register(
            "chocolate", () -> new Item(new Item.Properties().food(ModFoods.CHOCOLATE)));
    public static final DeferredHolder<Item, Item> VANILLA_ICECREAM = ItemsRegister.register(
            "vanilla_icecream", () -> new Item(new Item.Properties().food(ModFoods.ICECREAM)));
    public static final DeferredHolder<Item, Item> CHOCOLATE_ICECREAM = ItemsRegister.register(
            "chocolate_icecream", () -> new Item(new Item.Properties().food(ModFoods.CHOCOLATE_ICECREAM)));
    public static final DeferredHolder<Item, Item> CARAMEL_ICECREAM = ItemsRegister.register(
            "caramel_icecream", () -> new Item(new Item.Properties().food(ModFoods.CARAMEL_ICECREAM)));
    public static final DeferredHolder<Item, Item> TRIPLE_SHOT_ICECREAM = ItemsRegister.register(
            "triple_shot_icecream", () -> new Item(new Item.Properties().food(ModFoods.TRIPLE_SHOT_ICECREAM)));
    public static final DeferredHolder<Item, Item> ROOT_WAFFLE = ItemsRegister.register(
            "root_waffle", () -> new Item(new Item.Properties().food(ModFoods.ROOT_WAFFLE)));

    public static final DeferredHolder<Item, Item> FAIRY_IN_A_BOTTLE = ItemsRegister.register(
            "fairy_in_a_bottle", () -> new FairyBottle(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> FAIRY_STAFF = ItemsRegister.register(
            "fairy_staff", () -> new FairyStaff(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> BINDING_STAFF = ItemsRegister.register(
            "binding_staff", () -> new BindingStaff(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> CLOCKWORK_CARD_PUNCHER = ItemsRegister.register(
            "clockwork_card_puncher", () -> new ClockworkCardPuncher(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> HOT_CHOCOLATE_BUCKET = ItemsRegister.register(
            "hot_chocolate_bucket", () -> new BucketItem(ModFluids.SOURCE_HOT_CHOCOLATE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredHolder<Item, Item> EMPTY_CLOCKWORK_FAIRY_TERMINAL = ItemsRegister.register(
            "empty_clockwork_fairy_terminal", () -> new EmptyClockworkFairyTerminal(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SILKBUN_SPAWN_EGG = ItemsRegister.register(
            "silkbun_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.SILKBUN_ENTITY, 0xfdf4f7, 0x673f4e , new Item.Properties()));
    public static final DeferredHolder<Item, Item> FAIRY_SPAWN_EGG = ItemsRegister.register(
            "fairy_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.FAIRY_ENTITY, 0xfae54e, 0xfff4ac , new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOOTCAT_SPAWN_EGG = ItemsRegister.register(
            "hootcat_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.HOOTCAT_ENTITY, 0x4d3f3f, 0x9e7f4b , new Item.Properties()));

    public static final DeferredHolder<Item, Item> MARIONETTE_SPAWN_EGG = ItemsRegister.register(
            "marionette_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.MARIONETTE, 0x45393b, 0xb8433b , new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLOCKWORK_MAIDEN = ItemsRegister.register(
            "clockwork_maiden", () -> new DeferredSpawnEggItem(ModEntities.CLOCKWORK_MAIDEN_ENTITY, 0xFFFFFF, 0xFFFFFF , new Item.Properties()));

    public static void register(IEventBus eventBus){
        ItemsRegister.register(eventBus);
    }
}
