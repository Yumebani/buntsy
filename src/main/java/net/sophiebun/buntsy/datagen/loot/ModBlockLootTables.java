package net.sophiebun.buntsy.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.HootnipCrop;
import net.sophiebun.buntsy.blocks.custom.StrawberryCrop;
import net.sophiebun.buntsy.item.ModItems;

import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.GENTLIT_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_GENTLIT_LOG.get());
        this.dropSelf(ModBlocks.GENTLIT_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_GENTLIT_WOOD.get());

        this.add(ModBlocks.GENTLIT_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.GENTLIT_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.GENTLIT_SAPLING.get());

        this.dropSelf(ModBlocks.GENTLIT_PLANKS.get());
        this.dropSelf(ModBlocks.GENTLIT_STAIRS.get());
        this.dropSelf(ModBlocks.GENTLIT_BUTTON.get());
        this.dropSelf(ModBlocks.GENTLIT_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.GENTLIT_TRAPDOOR.get());
        this.dropSelf(ModBlocks.GENTLIT_FENCE.get());
        this.dropSelf(ModBlocks.GENTLIT_FENCE_GATE.get());
        this.add(ModBlocks.GENTLIT_SLAB.get(), block -> createSlabItemTable(ModBlocks.GENTLIT_SLAB.get()));
        this.add(ModBlocks.GENTLIT_DOOR.get(), block -> createDoorTable(ModBlocks.GENTLIT_DOOR.get()));

        this.dropSelf(ModBlocks.BRAVOT_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_BRAVOT_LOG.get());
        this.dropSelf(ModBlocks.BRAVOT_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_BRAVOT_WOOD.get());

        this.add(ModBlocks.BRAVOT_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.BRAVOT_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.BRAVOT_SAPLING.get());

        this.dropSelf(ModBlocks.BRAVOT_PLANKS.get());
        this.dropSelf(ModBlocks.BRAVOT_STAIRS.get());
        this.dropSelf(ModBlocks.BRAVOT_BUTTON.get());
        this.dropSelf(ModBlocks.BRAVOT_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.BRAVOT_TRAPDOOR.get());
        this.dropSelf(ModBlocks.BRAVOT_FENCE.get());
        this.dropSelf(ModBlocks.BRAVOT_FENCE_GATE.get());
        this.add(ModBlocks.BRAVOT_SLAB.get(), block -> createSlabItemTable(ModBlocks.BRAVOT_SLAB.get()));
        this.add(ModBlocks.BRAVOT_DOOR.get(), block -> createDoorTable(ModBlocks.BRAVOT_DOOR.get()));

        this.dropSelf(ModBlocks.MALVOR_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_MALVOR_LOG.get());
        this.dropSelf(ModBlocks.MALVOR_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_MALVOR_WOOD.get());

        this.add(ModBlocks.MALVOR_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.MALVOR_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.MALVOR_SAPLING.get());

        this.dropSelf(ModBlocks.MALVOR_PLANKS.get());
        this.dropSelf(ModBlocks.MALVOR_STAIRS.get());
        this.dropSelf(ModBlocks.MALVOR_BUTTON.get());
        this.dropSelf(ModBlocks.MALVOR_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.MALVOR_TRAPDOOR.get());
        this.dropSelf(ModBlocks.MALVOR_FENCE.get());
        this.dropSelf(ModBlocks.MALVOR_FENCE_GATE.get());
        this.add(ModBlocks.MALVOR_SLAB.get(), block -> createSlabItemTable(ModBlocks.MALVOR_SLAB.get()));
        this.add(ModBlocks.MALVOR_DOOR.get(), block -> createDoorTable(ModBlocks.MALVOR_DOOR.get()));

        //Adding soil
        this.add(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get(), block -> createSingleItemTableWithSilkTouch(block, ModBlocks.CHARMIL_SOIL.get()));
        this.dropSelf(ModBlocks.CHARMIL_SOIL.get());
        this.dropSelf(ModBlocks.SWEET_CORAL_SAND.get());
        this.dropSelf(ModBlocks.SWEET_CANDY_ROCK.get());
        this.dropSelf(ModBlocks.BITTER_CANDY_ROCK.get());
        this.dropSelf(ModBlocks.SOUR_CANDY_ROCK.get());
        this.add(ModBlocks.CHARMIL_FARMLAND.get(), block -> createSingleItemTable(ModBlocks.CHARMIL_SOIL.get()));

        //Adding crops
        this.add(ModBlocks.WILD_STRAWBERRY.get(), block -> createSingleItemTableWithSilkTouch(block, ModItems.STRAWBERRY.get()));
        LootItemCondition.Builder strawberryLootBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.STRAWBERRY_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StrawberryCrop.AGE, 5));
        this.add(ModBlocks.STRAWBERRY_CROP.get(), this.applyExplosionDecay(ModBlocks.STRAWBERRY_CROP.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add((LootItem.lootTableItem(ModItems.STRAWBERRY.get()))
                                .when(strawberryLootBuilder).otherwise(LootItem.lootTableItem(ModItems.STRAWBERRY_SEEDS.get())))
                        .when(strawberryLootBuilder)
                        .add(LootItem.lootTableItem(ModItems.STRAWBERRY.get()))
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));

        this.add(ModBlocks.WILD_HOOTNIP.get(), block -> createSingleItemTableWithSilkTouch(block, ModItems.HOOTNIP.get()));
        LootItemCondition.Builder hootnipCropCriteria = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.HOOTNIP_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HootnipCrop.AGE, 6));
        this.add(ModBlocks.HOOTNIP_CROP.get(), this.applyExplosionDecay(ModBlocks.HOOTNIP_CROP.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add((LootItem.lootTableItem(ModItems.HOOTNIP.get()))
                                .when(hootnipCropCriteria).otherwise(LootItem.lootTableItem(ModItems.HOOTNIP_SEEDS.get())))
                        .when(hootnipCropCriteria)
                        .add(LootItem.lootTableItem(ModItems.HOOTNIP.get()))
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));

        //Adding plants
        this.add(ModBlocks.SWEETGRASS.get(), block -> createShearsOnlyDrop(block));
        this.add(ModBlocks.TALL_SWEETGRASS.get(), block -> createDoublePlantShearsDrop(ModBlocks.SWEETGRASS.get()));
        this.dropSelf(ModBlocks.COTTON_VINE.get());
        this.dropOther(ModBlocks.COTTON_VINE_PLANT.get(), ModBlocks.COTTON_VINE.get());
        this.add(ModBlocks.SWEET_PICKLE.get(), (p_248918_) -> {
            return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(ModBlocks.SWEET_PICKLE.get(), LootItem.lootTableItem(p_248918_).apply(List.of(2, 3, 4), (p_251952_) -> {
                return SetItemCountFunction.setCount(ConstantValue.exactly((float)p_251952_.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_248918_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeaPickleBlock.PICKLES, p_251952_)));
            }))));
        });


        this.otherWhenSilkTouch(ModBlocks.DEAD_SWEET_CORAL_WALL_FAN.get(), ModBlocks.DEAD_SWEET_CORAL_FAN.get());
        this.otherWhenSilkTouch(ModBlocks.DEAD_BITTER_CORAL_WALL_FAN.get(), ModBlocks.DEAD_BITTER_CORAL_FAN.get());
        this.otherWhenSilkTouch(ModBlocks.SWEET_CORAL_WALL_FAN.get(), ModBlocks.SWEET_CORAL_FAN.get());
        this.otherWhenSilkTouch(ModBlocks.BITTER_CORAL_WALL_FAN.get(), ModBlocks.BITTER_CORAL_FAN.get());
        this.dropWhenSilkTouch(ModBlocks.DEAD_SWEET_CORAL.get());
        this.dropWhenSilkTouch(ModBlocks.DEAD_BITTER_CORAL.get());
        this.dropWhenSilkTouch(ModBlocks.SWEET_CORAL.get());
        this.dropWhenSilkTouch(ModBlocks.BITTER_CORAL.get());
        this.dropWhenSilkTouch(ModBlocks.DEAD_SWEET_CORAL_FAN.get());
        this.dropWhenSilkTouch(ModBlocks.DEAD_BITTER_CORAL_FAN.get());
        this.dropWhenSilkTouch(ModBlocks.SWEET_CORAL_FAN.get());
        this.dropWhenSilkTouch(ModBlocks.BITTER_CORAL_FAN.get());
        this.dropSelf(ModBlocks.DEAD_SWEET_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.DEAD_BITTER_CORAL_BLOCK.get());
        this.add(ModBlocks.SWEET_CORAL_BLOCK.get(), (p_248608_) -> {
            return this.createSingleItemTableWithSilkTouch(p_248608_, ModBlocks.DEAD_SWEET_CORAL_BLOCK.get());
        });

        this.add(ModBlocks.BITTER_CORAL_BLOCK.get(), (p_248608_) -> {
            return this.createSingleItemTableWithSilkTouch(p_248608_, ModBlocks.DEAD_BITTER_CORAL_BLOCK.get());
        });

        this.add(ModBlocks.PINK_CHARMIL_GRASS.get(), block -> createShearsOnlyDrop(block));
        this.add(ModBlocks.BLUE_CHARMIL_GRASS.get(), block -> createShearsOnlyDrop(block));
        this.dropSelf(ModBlocks.PINK_BLOOM.get());
        this.dropSelf(ModBlocks.BLUE_BLOOM.get());
        this.dropSelf(ModBlocks.LOVESHROOM.get());
        this.dropSelf(ModBlocks.GLOWSHROOM.get());

        //Adding potted plants
        this.add(ModBlocks.POTTED_PINK_BLOOM.get(), createPotFlowerItemTable(ModBlocks.PINK_BLOOM.get()));
        this.add(ModBlocks.POTTED_BLUE_BLOOM.get(), createPotFlowerItemTable(ModBlocks.BLUE_BLOOM.get()));
        this.add(ModBlocks.POTTED_LOVESHROOM.get(), createPotFlowerItemTable(ModBlocks.LOVESHROOM.get()));
        this.add(ModBlocks.POTTED_GLOWSHROOM.get(), createPotFlowerItemTable(ModBlocks.GLOWSHROOM.get()));
        this.add(ModBlocks.POTTED_GENTLIT_SAPLING.get(), createPotFlowerItemTable(ModBlocks.GENTLIT_SAPLING.get()));
        this.add(ModBlocks.POTTED_BRAVOT_SAPLING.get(), createPotFlowerItemTable(ModBlocks.BRAVOT_SAPLING.get()));
        this.add(ModBlocks.POTTED_MALVOR_SAPLING.get(), createPotFlowerItemTable(ModBlocks.MALVOR_SAPLING.get()));

        //Adding mushroom blocks
        this.add(ModBlocks.LOVESHROOM_BLOCK.get(), block -> createMushroomBlockDrop(block, ModBlocks.LOVESHROOM.get()));
        this.add(ModBlocks.GLOWSHROOM_BLOCK.get(), block -> createMushroomBlockDrop(block, ModBlocks.GLOWSHROOM.get()));

        //Adding minerals
        this.add(ModBlocks.GROWABLE_AMETHYST_CLUSTER.get(), block -> createOreDrop(block, Items.AMETHYST_SHARD));
        this.add(ModBlocks.LARGE_GROWABLE_AMETHYST_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_GROWABLE_AMETHYST_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_GROWABLE_AMETHYST_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.IRON_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.IRON_CRYSTAL.get()));
        this.add(ModBlocks.LARGE_IRON_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_IRON_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_IRON_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.COPPER_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.COPPER_CRYSTAL.get()));
        this.add(ModBlocks.LARGE_COPPER_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_COPPER_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_COPPER_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.GOLD_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.GOLD_CRYSTAL.get()));
        this.add(ModBlocks.LARGE_GOLD_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_GOLD_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_GOLD_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.REDSTONE_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.REDSTONE_CRYSTAL.get()));
        this.add(ModBlocks.LARGE_REDSTONE_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_REDSTONE_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_REDSTONE_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.LAPIS_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.LAPIS_CRYSTAL.get()));
        this.add(ModBlocks.LARGE_LAPIS_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_LAPIS_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_LAPIS_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.DIAMOND_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.DIAMOND_SHARD.get()));
        this.add(ModBlocks.LARGE_DIAMOND_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_DIAMOND_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_DIAMOND_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.EMERALD_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.EMERALD_SHARD.get()));
        this.add(ModBlocks.LARGE_EMERALD_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_EMERALD_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_EMERALD_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.DEBRIS_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.DEBRIS_SHARD.get()));
        this.add(ModBlocks.LARGE_DEBRIS_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_DEBRIS_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_DEBRIS_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.QUARTZ_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, Items.QUARTZ));
        this.add(ModBlocks.LARGE_QUARTZ_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_QUARTZ_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_QUARTZ_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.GLOWSTONE_CRYSTAL_CLUSTER.get(), block -> createOreDrop(block, ModItems.GLOWSTONE_CRYSTAL.get()));
        this.add(ModBlocks.LARGE_GLOWSTONE_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.MEDIUM_GLOWSTONE_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));
        this.add(ModBlocks.SMALL_GLOWSTONE_CRYSTAL_CLUSTER.get(), block -> createSilkTouchOnlyTable(block));

        //Adding tile entities
        this.dropSelf(ModBlocks.FAIRY_OFFERING_BENCH.get());
        this.dropSelf(ModBlocks.GRINDING_WHEEL.get());
        this.dropSelf(ModBlocks.THREAD_REELER.get());
        this.dropSelf(ModBlocks.FAIRY_COLLECTION_TRAY.get());
        this.dropSelf(ModBlocks.FAIRY_INFUSION_BENCH.get());
        this.dropSelf(ModBlocks.MAGIC_CRYSTALIZER.get());
        this.dropSelf(ModBlocks.FUME_DISTILLERY.get());
        this.dropSelf(ModBlocks.FUME_SPREADER.get());
        this.dropSelf(ModBlocks.INFUSION_PEDESTAL.get());
        this.dropSelf(ModBlocks.FAIRY_POWER_RELAY.get());
        this.dropSelf(ModBlocks.INFUSION_ALTAR_BASIC.get());
        this.dropSelf(ModBlocks.INFUSION_ALTAR_ADVANCED.get());
        this.dropSelf(ModBlocks.GIANT_COCOON.get());
        this.dropSelf(ModBlocks.MIXER_BLOCK.get());

        this.dropSelf(ModBlocks.PRISMATIC_BEACON.get());
        this.dropSelf(ModBlocks.PRISMATIC_BEACON_BASE.get());
        this.dropSelf(ModBlocks.BEACON_HEALTH_BOOST_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_SPEED_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_HASTE_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_STRENGTH_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_JUMP_BOOST_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_REGENERATION_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_RESISTANCE_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_FIRE_RESISTANCE_MODIFIER.get());
        this.dropSelf(ModBlocks.BEACON_WATER_BREATHING_MODIFIER.get());


        //Others
        this.dropSelf(ModBlocks.SYRUP_EXTRACTOR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BlocksRegister.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
