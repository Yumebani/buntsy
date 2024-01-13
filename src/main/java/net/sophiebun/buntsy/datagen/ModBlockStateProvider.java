package net.sophiebun.buntsy.datagen;

import net.minecraft.client.Minecraft;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BuntsyMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        logBlock((RotatedPillarBlock) ModBlocks.GENTLIT_LOG.get());
        axisBlock((RotatedPillarBlock) ModBlocks.GENTLIT_WOOD.get(), blockTexture(ModBlocks.GENTLIT_LOG.get()), blockTexture(ModBlocks.GENTLIT_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_GENTLIT_LOG.get(), blockTexture(ModBlocks.STRIPPED_GENTLIT_LOG.get()),
                new ResourceLocation(BuntsyMod.MODID, "block/stripped_gentlit_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_GENTLIT_WOOD.get(), blockTexture(ModBlocks.STRIPPED_GENTLIT_LOG.get()), blockTexture(ModBlocks.GENTLIT_LOG.get()));

        simpleBlock(ModBlocks.GENTLIT_LEAVES.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(ModBlocks.GENTLIT_LEAVES.get()).getPath(),
                new ResourceLocation("minecraft:block/leaves"), "all", blockTexture(ModBlocks.GENTLIT_LEAVES.get())).renderType("cutout"));
        simpleCrossBlock(ModBlocks.GENTLIT_SAPLING);

        blockWithItem(ModBlocks.GENTLIT_PLANKS);
        stairsBlock((StairBlock) ModBlocks.GENTLIT_STAIRS.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        slabBlock((SlabBlock) ModBlocks.GENTLIT_SLAB.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.GENTLIT_BUTTON.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.GENTLIT_PRESSURE_PLATE.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.GENTLIT_FENCE.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.GENTLIT_FENCE_GATE.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.GENTLIT_DOOR.get(), modLoc("block/gentlit_door_bottom"), modLoc("block/gentlit_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.GENTLIT_TRAPDOOR.get(), modLoc("block/gentlit_trapdoor"), true, "cutout");

        logBlock((RotatedPillarBlock) ModBlocks.BRAVOT_LOG.get());
        axisBlock((RotatedPillarBlock) ModBlocks.BRAVOT_WOOD.get(), blockTexture(ModBlocks.BRAVOT_LOG.get()), blockTexture(ModBlocks.BRAVOT_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_BRAVOT_LOG.get(), blockTexture(ModBlocks.STRIPPED_BRAVOT_LOG.get()),
                new ResourceLocation(BuntsyMod.MODID, "block/stripped_bravot_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_BRAVOT_WOOD.get(), blockTexture(ModBlocks.STRIPPED_BRAVOT_LOG.get()), blockTexture(ModBlocks.BRAVOT_LOG.get()));

        simpleBlock(ModBlocks.BRAVOT_LEAVES.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(ModBlocks.BRAVOT_LEAVES.get()).getPath(),
                        new ResourceLocation("minecraft:block/leaves"), "all", blockTexture(ModBlocks.BRAVOT_LEAVES.get())).renderType("cutout"));
        simpleCrossBlock(ModBlocks.BRAVOT_SAPLING);

        blockWithItem(ModBlocks.BRAVOT_PLANKS);
        stairsBlock((StairBlock) ModBlocks.BRAVOT_STAIRS.get(), blockTexture(ModBlocks.BRAVOT_PLANKS.get()));
        slabBlock((SlabBlock) ModBlocks.BRAVOT_SLAB.get(), blockTexture(ModBlocks.BRAVOT_PLANKS.get()), blockTexture(ModBlocks.BRAVOT_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.BRAVOT_BUTTON.get(), blockTexture(ModBlocks.BRAVOT_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.BRAVOT_PRESSURE_PLATE.get(), blockTexture(ModBlocks.BRAVOT_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.BRAVOT_FENCE.get(), blockTexture(ModBlocks.BRAVOT_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.BRAVOT_FENCE_GATE.get(), blockTexture(ModBlocks.BRAVOT_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.BRAVOT_DOOR.get(), modLoc("block/bravot_door_bottom"), modLoc("block/bravot_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.BRAVOT_TRAPDOOR.get(), modLoc("block/bravot_trapdoor"), true, "cutout");

        simpleBlockWithItem(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get(),
                models().cubeBottomTop(ForgeRegistries.BLOCKS.getKey(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get()).getPath(),
                        new ResourceLocation(BuntsyMod.MODID, "block/pink_grass_side"),
                        mcLoc("block/dirt"),
                        new ResourceLocation(BuntsyMod.MODID, "block/pink_grass_top")));

        simpleBlockWithItem(ModBlocks.GRINDING_WHEEL.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/grinding_wheel")));
        simpleBlockWithItem(ModBlocks.THREAD_REELER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/thread_reeler")));
        simpleBlockWithItem(ModBlocks.FAIRY_TERRARIUM.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fairy_terrarium")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void simpleCrossBlock(RegistryObject<Block> blockRegistry){
        simpleBlock(blockRegistry.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistry.get()).getPath(), blockTexture(blockRegistry.get())).renderType("cutout"));
    }
}
