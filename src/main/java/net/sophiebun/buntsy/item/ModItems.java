package net.sophiebun.buntsy.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModFoods;

public class ModItems {
    public static final DeferredRegister<Item> ItemsRegister =
            DeferredRegister.create(ForgeRegistries.ITEMS, BuntsyMod.MODID);

    public static final RegistryObject<Item> STRAWBERRY = ItemsRegister.register(
            "strawberry", () -> new Item(new Item.Properties().food(ModFoods.STRAWBERRY)));
    public static final RegistryObject<Item> GOLDEN_STRAWBERRY = ItemsRegister.register(
            "golden_strawberry", () -> new Item(new Item.Properties().food(ModFoods.GOLDEN_STRAWBERRY)));

    public static void register(IEventBus eventBus){
        ItemsRegister.register(eventBus);
    }
}
