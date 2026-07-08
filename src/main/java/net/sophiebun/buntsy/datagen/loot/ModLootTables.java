package net.sophiebun.buntsy.datagen.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.ModItems;

import java.util.function.BiConsumer;

public class ModLootTables implements LootTableSubProvider {

    public static final ResourceLocation FAIRY_TALE_CHEST = new ResourceLocation(BuntsyMod.MODID, "chests/fairy_tale_book_chest");

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        writer.accept(FAIRY_TALE_CHEST, LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .name("base_loot")
                                .setRolls(UniformGenerator.between(5.0F, 10.0F))
                                .add(LootItem.lootTableItem(Items.COBWEB).setWeight(5))
                                .add(LootItem.lootTableItem(Items.STRING).setWeight(10))
                                .add(LootItem.lootTableItem(ModItems.FAIRY_DUST.get()).setWeight(5))
                ).withPool(
                        LootPool.lootPool()
                                .name("fairy_tale_book")
                                .setRolls(UniformGenerator.between(1.0f, 1.0f))
                                .add(LootItem.lootTableItem(ModItems.FAIRY_TALE_BOOK.get()))
                )
        );
    }
}
