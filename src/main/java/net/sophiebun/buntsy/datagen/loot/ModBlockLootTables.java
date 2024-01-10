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

        this.dropSelf(ModBlocks.GENTLIT_PLANKS.get());
        this.dropSelf(ModBlocks.GENTLIT_STAIRS.get());
        this.dropSelf(ModBlocks.GENTLIT_BUTTON.get());
        this.dropSelf(ModBlocks.GENTLIT_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.GENTLIT_TRAPDOOR.get());
        this.dropSelf(ModBlocks.GENTLIT_FENCE.get());
        this.dropSelf(ModBlocks.GENTLIT_FENCE_GATE.get());

        this.dropSelf(ModBlocks.GRINDING_WHEEL.get());
        this.dropSelf(ModBlocks.THREAD_REELER.get());

        this.add(ModBlocks.GENTLIT_SLAB.get(), block -> createSlabItemTable(ModBlocks.GENTLIT_SLAB.get()));
        this.add(ModBlocks.GENTLIT_DOOR.get(), block -> createDoorTable(ModBlocks.GENTLIT_SLAB.get()));

        this.add(ModBlocks.GENTLIT_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.GENTLIT_LEAVES.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        //createRedstoneOreDrops();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BlocksRegister.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
