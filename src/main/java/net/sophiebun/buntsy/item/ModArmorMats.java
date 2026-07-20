package net.sophiebun.buntsy.item;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMats {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, BuntsyMod.MODID);

    public static final Holder<ArmorMaterial> CLOTH = ARMOR_MATERIALS.register("cloth", () -> new ArmorMaterial(

            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.CHESTPLATE, 1);
                map.put(ArmorItem.Type.LEGGINGS, 1);
                map.put(ArmorItem.Type.BOOTS, 1);
            }),
            5,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ModItems.SILK_FABRIC.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "cloth"))),
            0.0F,
            0.0F
    ));

    public static final Holder<ArmorMaterial> HOOTCAT = ARMOR_MATERIALS.register("hootcat", () -> new ArmorMaterial(

            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.LEGGINGS, 3);
                map.put(ArmorItem.Type.BOOTS, 2);
            }),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ModItems.HOOTCAT_PLUME.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "hootcat"))),
            0.0F,
            0.0F
    ));

    public static final Holder<ArmorMaterial> SILKY = ARMOR_MATERIALS.register("silky", () -> new ArmorMaterial(

            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.BOOTS, 3);
            }),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ModItems.HOOTCAT_PLUME.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "hootcat"))),
            1F,
            0.0F
    ));

    public static void register(IEventBus eventBus){
        ARMOR_MATERIALS.register(eventBus);
    }
}
