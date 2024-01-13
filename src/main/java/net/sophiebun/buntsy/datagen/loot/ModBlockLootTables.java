package net.sophiebun.buntsy.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.blocks.ModBlocks;

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
        this.add(ModBlocks.GENTLIT_DOOR.get(), block -> createDoorTable(ModBlocks.GENTLIT_SLAB.get()));

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
        this.add(ModBlocks.BRAVOT_DOOR.get(), block -> createDoorTable(ModBlocks.BRAVOT_SLAB.get()));

        this.dropSelf(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get());

        this.dropSelf(ModBlocks.GRINDING_WHEEL.get());
        this.dropSelf(ModBlocks.THREAD_REELER.get());
        this.dropSelf(ModBlocks.FAIRY_TERRARIUM.get());

        //createRedstoneOreDrops();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BlocksRegister.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
