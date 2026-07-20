package net.sophiebun.buntsy.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.plants.HootnipCrop;
import net.sophiebun.buntsy.blocks.custom.plants.RotatedTallFlowerBlock;
import net.sophiebun.buntsy.blocks.custom.plants.StrawberryCrop;
import net.sophiebun.buntsy.blocks.custom.SyrupExtractorBlock;
import net.sophiebun.buntsy.blocks.custom.hanging_block.HangingStringBlock;
import net.sophiebun.buntsy.blocks.custom.hanging_block.HangingStringEnding;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.blocks.custom.plants.Sweeds;

import java.util.List;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BuntsyMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        logBlock((RotatedPillarBlock) ModBlocks.GENTLIT_LOG.get());
        axisBlock((RotatedPillarBlock) ModBlocks.GENTLIT_WOOD.get(), blockTexture(ModBlocks.GENTLIT_LOG.get()), blockTexture(ModBlocks.GENTLIT_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_GENTLIT_LOG.get(), blockTexture(ModBlocks.STRIPPED_GENTLIT_LOG.get()),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/stripped_gentlit_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_GENTLIT_WOOD.get(), blockTexture(ModBlocks.STRIPPED_GENTLIT_LOG.get()), blockTexture(ModBlocks.GENTLIT_LOG.get()));

        simpleBlock(ModBlocks.GENTLIT_LEAVES.get(),
                models().singleTexture(ModBlocks.GENTLIT_LEAVES.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/leaves"), "all", blockTexture(ModBlocks.GENTLIT_LEAVES.get())).renderType("cutout"));
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
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/stripped_bravot_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_BRAVOT_WOOD.get(), blockTexture(ModBlocks.STRIPPED_BRAVOT_LOG.get()), blockTexture(ModBlocks.BRAVOT_LOG.get()));

        simpleBlock(ModBlocks.BRAVOT_LEAVES.get(),
                models().singleTexture(ModBlocks.BRAVOT_LEAVES.getId().getPath(),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/leaves"), "all", blockTexture(ModBlocks.BRAVOT_LEAVES.get())).renderType("cutout"));
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

        logBlock((RotatedPillarBlock) ModBlocks.MALVOR_LOG.get());
        axisBlock((RotatedPillarBlock) ModBlocks.MALVOR_WOOD.get(), blockTexture(ModBlocks.MALVOR_LOG.get()), blockTexture(ModBlocks.MALVOR_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_MALVOR_LOG.get(), blockTexture(ModBlocks.STRIPPED_MALVOR_LOG.get()),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/stripped_malvor_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_MALVOR_WOOD.get(), blockTexture(ModBlocks.STRIPPED_MALVOR_LOG.get()), blockTexture(ModBlocks.MALVOR_LOG.get()));

        simpleBlock(ModBlocks.MALVOR_LEAVES.get(),
                models().singleTexture(ModBlocks.MALVOR_LEAVES.getId().getPath(),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/leaves"), "all", blockTexture(ModBlocks.MALVOR_LEAVES.get())).renderType("cutout"));
        simpleCrossBlock(ModBlocks.MALVOR_SAPLING);

        blockWithItem(ModBlocks.MALVOR_PLANKS);
        stairsBlock((StairBlock) ModBlocks.MALVOR_STAIRS.get(), blockTexture(ModBlocks.MALVOR_PLANKS.get()));
        slabBlock((SlabBlock) ModBlocks.MALVOR_SLAB.get(), blockTexture(ModBlocks.MALVOR_PLANKS.get()), blockTexture(ModBlocks.MALVOR_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.MALVOR_BUTTON.get(), blockTexture(ModBlocks.MALVOR_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.MALVOR_PRESSURE_PLATE.get(), blockTexture(ModBlocks.MALVOR_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.MALVOR_FENCE.get(), blockTexture(ModBlocks.MALVOR_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.MALVOR_FENCE_GATE.get(), blockTexture(ModBlocks.MALVOR_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.MALVOR_DOOR.get(), modLoc("block/malvor_door_bottom"), modLoc("block/malvor_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.MALVOR_TRAPDOOR.get(), modLoc("block/malvor_trapdoor"), true, "cutout");

        logBlock((RotatedPillarBlock) ModBlocks.ORIGAMI_PALM_LOG.get());
        axisBlock((RotatedPillarBlock) ModBlocks.ORIGAMI_PALM_WOOD.get(), blockTexture(ModBlocks.ORIGAMI_PALM_LOG.get()), blockTexture(ModBlocks.ORIGAMI_PALM_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_ORIGAMI_PALM_LOG.get(), blockTexture(ModBlocks.STRIPPED_ORIGAMI_PALM_LOG.get()),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/stripped_origami_palm_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_ORIGAMI_PALM_WOOD.get(), blockTexture(ModBlocks.STRIPPED_ORIGAMI_PALM_LOG.get()), blockTexture(ModBlocks.ORIGAMI_PALM_LOG.get()));

        simpleBlock(ModBlocks.ORIGAMI_PALM_LEAVES.get(),
                models().singleTexture(ModBlocks.ORIGAMI_PALM_LEAVES.getId().getPath(),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/leaves"), "all", blockTexture(ModBlocks.ORIGAMI_PALM_LEAVES.get())).renderType("cutout"));
        simpleCrossBlock(ModBlocks.ORIGAMI_PALM_SAPLING);

        blockWithItem(ModBlocks.ORIGAMI_PALM_PLANKS);
        stairsBlock((StairBlock) ModBlocks.ORIGAMI_PALM_STAIRS.get(), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()));
        slabBlock((SlabBlock) ModBlocks.ORIGAMI_PALM_SLAB.get(), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.ORIGAMI_PALM_BUTTON.get(), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.ORIGAMI_PALM_PRESSURE_PLATE.get(), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.ORIGAMI_PALM_FENCE.get(), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.ORIGAMI_PALM_FENCE_GATE.get(), blockTexture(ModBlocks.ORIGAMI_PALM_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.ORIGAMI_PALM_DOOR.get(), modLoc("block/origami_palm_door_bottom"), modLoc("block/origami_palm_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.ORIGAMI_PALM_TRAPDOOR.get(), modLoc("block/origami_palm_trapdoor"), true, "cutout");

        logBlock((RotatedPillarBlock) ModBlocks.CRYSTALLIZED_LOG.get());
        simpleBlock(ModBlocks.CRYSTALLIZED_LEAVES.get(),
                models().singleTexture(ModBlocks.CRYSTALLIZED_LEAVES.getId().getPath(),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/leaves"), "all", blockTexture(ModBlocks.CRYSTALLIZED_LEAVES.get())).renderType("cutout"));


        //Adding soil models
        grassBlock(ModBlocks.PINK_FLUF_CHARMIL_SOIL, ModBlocks.CHARMIL_SOIL.getId().getPath());
        blockWithItem(ModBlocks.CHARMIL_SOIL);
        farmlandBlock(ModBlocks.CHARMIL_FARMLAND, ModBlocks.CHARMIL_SOIL);

        grassBlock(ModBlocks.GRAY_MOSS_ODIATE_SOIL, ModBlocks.ODIATE_SOIL.getId().getPath());
        blockWithItem(ModBlocks.ODIATE_SOIL);
        blockWithItem(ModBlocks.ODIATE_MUD);
        blockWithItem(ModBlocks.PACKED_ODIATE_MUD);
        blockWithItem(ModBlocks.ODIATE_MUD_BRICKS);
        stairsBlock((StairBlock) ModBlocks.ODIATE_MUD_BRICKS_STAIRS.get(), blockTexture(ModBlocks.ODIATE_MUD_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.ODIATE_MUD_BRICKS_SLAB.get(), blockTexture(ModBlocks.ODIATE_MUD_BRICKS.get()), blockTexture(ModBlocks.ODIATE_MUD_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.ODIATE_MUD_BRICKS_WALL.get(), blockTexture(ModBlocks.ODIATE_MUD_BRICKS.get()));
        farmlandBlock(ModBlocks.ODIATE_FARMLAND, ModBlocks.ODIATE_SOIL);

        blockWithItem(ModBlocks.SWEET_CORAL_SAND);
        blockWithItem(ModBlocks.FROZEN_CORAL_SAND);
        blockWithItem(ModBlocks.SUNNY_CORAL_SAND);

        blockWithItem(ModBlocks.SWEET_CANDY_ROCK);
        stairsBlock((StairBlock) ModBlocks.SWEET_CANDY_ROCK_STAIRS.get(), blockTexture(ModBlocks.SWEET_CANDY_ROCK.get()));
        slabBlock((SlabBlock) ModBlocks.SWEET_CANDY_ROCK_SLAB.get(), blockTexture(ModBlocks.SWEET_CANDY_ROCK.get()), blockTexture(ModBlocks.SWEET_CANDY_ROCK.get()));
        wallBlock((WallBlock) ModBlocks.SWEET_CANDY_ROCK_WALL.get(), blockTexture(ModBlocks.SWEET_CANDY_ROCK.get()));
        blockWithItem(ModBlocks.SWEET_CANDY_BRICKS);
        stairsBlock((StairBlock) ModBlocks.SWEET_CANDY_BRICKS_STAIRS.get(), blockTexture(ModBlocks.SWEET_CANDY_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.SWEET_CANDY_BRICKS_SLAB.get(), blockTexture(ModBlocks.SWEET_CANDY_BRICKS.get()), blockTexture(ModBlocks.SWEET_CANDY_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.SWEET_CANDY_BRICKS_WALL.get(), blockTexture(ModBlocks.SWEET_CANDY_BRICKS.get()));

        blockWithItem(ModBlocks.BITTER_CANDY_ROCK);
        stairsBlock((StairBlock) ModBlocks.BITTER_CANDY_ROCK_STAIRS.get(), blockTexture(ModBlocks.BITTER_CANDY_ROCK.get()));
        slabBlock((SlabBlock) ModBlocks.BITTER_CANDY_ROCK_SLAB.get(), blockTexture(ModBlocks.BITTER_CANDY_ROCK.get()), blockTexture(ModBlocks.BITTER_CANDY_ROCK.get()));
        wallBlock((WallBlock) ModBlocks.BITTER_CANDY_ROCK_WALL.get(), blockTexture(ModBlocks.BITTER_CANDY_ROCK.get()));
        blockWithItem(ModBlocks.BITTER_CANDY_BRICKS);
        stairsBlock((StairBlock) ModBlocks.BITTER_CANDY_BRICKS_STAIRS.get(), blockTexture(ModBlocks.BITTER_CANDY_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.BITTER_CANDY_BRICKS_SLAB.get(), blockTexture(ModBlocks.BITTER_CANDY_BRICKS.get()), blockTexture(ModBlocks.BITTER_CANDY_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.BITTER_CANDY_BRICKS_WALL.get(), blockTexture(ModBlocks.BITTER_CANDY_BRICKS.get()));

        blockWithItem(ModBlocks.SOUR_CANDY_ROCK);
        stairsBlock((StairBlock) ModBlocks.SOUR_CANDY_ROCK_STAIRS.get(), blockTexture(ModBlocks.SOUR_CANDY_ROCK.get()));
        slabBlock((SlabBlock) ModBlocks.SOUR_CANDY_ROCK_SLAB.get(), blockTexture(ModBlocks.SOUR_CANDY_ROCK.get()), blockTexture(ModBlocks.SOUR_CANDY_ROCK.get()));
        wallBlock((WallBlock) ModBlocks.SOUR_CANDY_ROCK_WALL.get(), blockTexture(ModBlocks.SOUR_CANDY_ROCK.get()));
        blockWithItem(ModBlocks.SOUR_CANDY_BRICKS);
        stairsBlock((StairBlock) ModBlocks.SOUR_CANDY_BRICKS_STAIRS.get(), blockTexture(ModBlocks.SOUR_CANDY_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.SOUR_CANDY_BRICKS_SLAB.get(), blockTexture(ModBlocks.SOUR_CANDY_BRICKS.get()), blockTexture(ModBlocks.SOUR_CANDY_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.SOUR_CANDY_BRICKS_WALL.get(), blockTexture(ModBlocks.SOUR_CANDY_BRICKS.get()));

        blockWithItem(ModBlocks.SWEET_LIMESTONE);
        stairsBlock((StairBlock) ModBlocks.SWEET_LIMESTONE_STAIRS.get(), blockTexture(ModBlocks.SWEET_LIMESTONE.get()));
        slabBlock((SlabBlock) ModBlocks.SWEET_LIMESTONE_SLAB.get(), blockTexture(ModBlocks.SWEET_LIMESTONE.get()), blockTexture(ModBlocks.SWEET_LIMESTONE.get()));
        wallBlock((WallBlock) ModBlocks.SWEET_LIMESTONE_WALL.get(), blockTexture(ModBlocks.SWEET_LIMESTONE.get()));
        blockWithItem(ModBlocks.SWEET_LIMESTONE_BRICKS);
        stairsBlock((StairBlock) ModBlocks.SWEET_LIMESTONE_BRICKS_STAIRS.get(), blockTexture(ModBlocks.SWEET_LIMESTONE_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.SWEET_LIMESTONE_BRICKS_SLAB.get(), blockTexture(ModBlocks.SWEET_LIMESTONE_BRICKS.get()), blockTexture(ModBlocks.SWEET_LIMESTONE_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.SWEET_LIMESTONE_BRICKS_WALL.get(), blockTexture(ModBlocks.SWEET_LIMESTONE_BRICKS.get()));

        blockWithItem(ModBlocks.SUNNY_LIMESTONE);
        stairsBlock((StairBlock) ModBlocks.SUNNY_LIMESTONE_STAIRS.get(), blockTexture(ModBlocks.SUNNY_LIMESTONE.get()));
        slabBlock((SlabBlock) ModBlocks.SUNNY_LIMESTONE_SLAB.get(), blockTexture(ModBlocks.SUNNY_LIMESTONE.get()), blockTexture(ModBlocks.SUNNY_LIMESTONE.get()));
        wallBlock((WallBlock) ModBlocks.SUNNY_LIMESTONE_WALL.get(), blockTexture(ModBlocks.SUNNY_LIMESTONE.get()));
        blockWithItem(ModBlocks.SUNNY_LIMESTONE_BRICKS);
        stairsBlock((StairBlock) ModBlocks.SUNNY_LIMESTONE_BRICKS_STAIRS.get(), blockTexture(ModBlocks.SUNNY_LIMESTONE_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.SUNNY_LIMESTONE_BRICKS_SLAB.get(), blockTexture(ModBlocks.SUNNY_LIMESTONE_BRICKS.get()), blockTexture(ModBlocks.SUNNY_LIMESTONE_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.SUNNY_LIMESTONE_BRICKS_WALL.get(), blockTexture(ModBlocks.SUNNY_LIMESTONE_BRICKS.get()));

        blockWithItem(ModBlocks.FROZEN_LIMESTONE);
        stairsBlock((StairBlock) ModBlocks.FROZEN_LIMESTONE_STAIRS.get(), blockTexture(ModBlocks.FROZEN_LIMESTONE.get()));
        slabBlock((SlabBlock) ModBlocks.FROZEN_LIMESTONE_SLAB.get(), blockTexture(ModBlocks.FROZEN_LIMESTONE.get()), blockTexture(ModBlocks.FROZEN_LIMESTONE.get()));
        wallBlock((WallBlock) ModBlocks.FROZEN_LIMESTONE_WALL.get(), blockTexture(ModBlocks.FROZEN_LIMESTONE.get()));
        blockWithItem(ModBlocks.FROZEN_LIMESTONE_BRICKS);
        stairsBlock((StairBlock) ModBlocks.FROZEN_LIMESTONE_BRICKS_STAIRS.get(), blockTexture(ModBlocks.FROZEN_LIMESTONE_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.FROZEN_LIMESTONE_BRICKS_SLAB.get(), blockTexture(ModBlocks.FROZEN_LIMESTONE_BRICKS.get()), blockTexture(ModBlocks.FROZEN_LIMESTONE_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.FROZEN_LIMESTONE_BRICKS_WALL.get(), blockTexture(ModBlocks.FROZEN_LIMESTONE_BRICKS.get()));

        blockWithItem(ModBlocks.PETRIFIED_CHOCOLATE);
        stairsBlock((StairBlock) ModBlocks.PETRIFIED_CHOCOLATE_STAIRS.get(), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE.get()));
        slabBlock((SlabBlock) ModBlocks.PETRIFIED_CHOCOLATE_SLAB.get(), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE.get()), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE.get()));
        wallBlock((WallBlock) ModBlocks.PETRIFIED_CHOCOLATE_WALL.get(), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE.get()));
        blockWithItem(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS);
        stairsBlock((StairBlock) ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_STAIRS.get(), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get()));
        slabBlock((SlabBlock) ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_SLAB.get(), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get()), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get()));
        wallBlock((WallBlock) ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_WALL.get(), blockTexture(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get()));
        blockWithItem(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE);
        stairsBlock((StairBlock) ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_STAIRS.get(), blockTexture(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get()));
        slabBlock((SlabBlock) ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_SLAB.get(), blockTexture(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get()), blockTexture(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get()));
        wallBlock((WallBlock) ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_WALL.get(), blockTexture(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get()));

        blockWithItem(ModBlocks.CHOCOLATE_BLOCK);
        simpleBlockWithItem(ModBlocks.CHOCOLATE_GEYSER.get(), models().cubeTop(
                ModBlocks.CHOCOLATE_GEYSER.getId().getPath(),
                modLoc("block/" + ModBlocks.PETRIFIED_CHOCOLATE.getId().getPath()),
                modLoc("block/" + ModBlocks.CHOCOLATE_GEYSER.getId().getPath())));

        blockWithItem(ModBlocks.DECAYED_CLOCKWORK_BRASS_BLOCK);
        blockWithItem(ModBlocks.DECAYED_CUT_CLOCKWORK_BRASS);
        blockWithItem(ModBlocks.CLOCKWORK_BRASS_BLOCK);
        blockWithItem(ModBlocks.CUT_CLOCKWORK_BRASS);
        stairsBlock((StairBlock) ModBlocks.CUT_CLOCKWORK_BRASS_STAIRS.get(), blockTexture(ModBlocks.CUT_CLOCKWORK_BRASS.get()));
        slabBlock((SlabBlock) ModBlocks.CUT_CLOCKWORK_BRASS_SLAB.get(), blockTexture(ModBlocks.CUT_CLOCKWORK_BRASS.get()), blockTexture(ModBlocks.CUT_CLOCKWORK_BRASS.get()));

        blockWithItem(ModBlocks.FROZEN_POWDER_BLOCK);
        translucentBlockWithItem(ModBlocks.SWICE);

        seaShells(ModBlocks.SEA_SHELLS);

        //Hanging blocks
        hangingString(ModBlocks.HANGING_STRING);
        variedCross(ModBlocks.HANGING_LUMINUM);
        variedCross(ModBlocks.HANGING_CLOCKWORK);

        //Crops
        simpleCrossBlock(ModBlocks.WILD_STRAWBERRY);
        strawberryCrop(ModBlocks.STRAWBERRY_CROP);
        wildHootnipBlock(ModBlocks.WILD_HOOTNIP);
        hootnipCrop(ModBlocks.HOOTNIP_CROP);
        crossCropBlock(ModBlocks.SUGARDEW_CROP);
        cropBlock(ModBlocks.WINTER_ROOT_CROP);


        //Adding plants
        sweedsCrossBlock(ModBlocks.SWEEDS);
        variedCross(ModBlocks.PINK_CHARMIL_GRASS);
        variedCross(ModBlocks.BLUE_CHARMIL_GRASS);
        variedCross(ModBlocks.PALEGRASS);
        variedCross(ModBlocks.FROZEN_GRASS);
        simpleCrossBlock(ModBlocks.PINK_BLOOM);
        simpleCrossBlock(ModBlocks.BLUE_BLOOM);
        simpleCrossBlock(ModBlocks.ABYSSAL_BLOOM);
        simpleCrossBlock(ModBlocks.FROZEN_BLOOM);
        simpleCrossBlock(ModBlocks.ORIGAMI_FERN);
        simpleCrossBlock(ModBlocks.LOVESHROOM);
        simpleCrossBlock(ModBlocks.GLOWSHROOM);
        simpleCrossBlock(ModBlocks.PALESHROOM);
        luminum(ModBlocks.LUMINUM);

        //Adding potted plants
        pottedPlant(ModBlocks.POTTED_PINK_BLOOM, ModBlocks.PINK_BLOOM);
        pottedPlant(ModBlocks.POTTED_BLUE_BLOOM, ModBlocks.BLUE_BLOOM);
        pottedPlant(ModBlocks.POTTED_ABYSSAL_BLOOM, ModBlocks.ABYSSAL_BLOOM);
        pottedPlant(ModBlocks.POTTED_FROZEN_BLOOM, ModBlocks.FROZEN_BLOOM);
        pottedPlant(ModBlocks.POTTED_ORIGAMI_FERN, ModBlocks.ORIGAMI_FERN);
        pottedPlant(ModBlocks.POTTED_GENTLIT_SAPLING, ModBlocks.GENTLIT_SAPLING);
        pottedPlant(ModBlocks.POTTED_BRAVOT_SAPLING, ModBlocks.BRAVOT_SAPLING);
        pottedPlant(ModBlocks.POTTED_MALVOR_SAPLING, ModBlocks.MALVOR_SAPLING);
        pottedPlant(ModBlocks.POTTED_ORIGAMI_PALM_SAPLING, ModBlocks.ORIGAMI_PALM_SAPLING);
        pottedPlant(ModBlocks.POTTED_LOVESHROOM, ModBlocks.LOVESHROOM);
        pottedPlant(ModBlocks.POTTED_GLOWSHROOM, ModBlocks.GLOWSHROOM);
        pottedPlant(ModBlocks.POTTED_PALESHROOM, ModBlocks.PALESHROOM);

        //MushroomBlocks
        mushroomBlock(ModBlocks.LOVESHROOM_BLOCK);
        mushroomBlock(ModBlocks.GLOWSHROOM_BLOCK);
        mushroomBlock(ModBlocks.PALESHROOM_BLOCK);

        //Minerals
        for (List<DeferredHolder<Block, Block>> minerals : ModGrowableMineral.GROWABLE_MINERAL_STAGES){
            for (DeferredHolder<Block, Block> mineral : minerals){
                crystalBlock(mineral);
            }
        }

        //Block entities
        simpleBlockWithItem(ModBlocks.FAIRY_OFFERING_BENCH.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fairy_offering_bench")));
        simpleBlockWithItem(ModBlocks.GRINDING_WHEEL.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/grinding_wheel_item")));
        simpleBlockWithItem(ModBlocks.THREAD_REELER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/thread_reeler_item")));
        simpleBlockWithItem(ModBlocks.FAIRY_COLLECTION_TRAY.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fairy_collection_tray")));
        simpleBlockWithItem(ModBlocks.FAIRY_INFUSION_BENCH.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fairy_infusion_bench")));
        XZdirectionalBlock(ModBlocks.MAGIC_CRYSTALIZER,
                new ModelFile.UncheckedModelFile(modLoc("block/magic_crystalizer")));
        simpleBlockItem(ModBlocks.MAGIC_CRYSTALIZER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/magic_crystalizer")));
        XZdirectionalBlock(ModBlocks.FUME_DISTILLERY,
                new ModelFile.UncheckedModelFile(modLoc("block/fume_distiller")));
        simpleBlockItem(ModBlocks.FUME_DISTILLERY.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fume_distiller")));
        simpleBlockWithItem(ModBlocks.FUME_SPREADER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fume_spreader")));
        simpleBlockWithItem(ModBlocks.INFUSION_PEDESTAL.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/infusion_pedestal")));
        simpleBlockWithItem(ModBlocks.FAIRY_POWER_RELAY.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fairy_power_relay")));
        simpleBlockWithItem(ModBlocks.INFUSION_ALTAR_BASIC.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/infusion_altar_basic")));
        simpleBlockWithItem(ModBlocks.INFUSION_ALTAR_ADVANCED.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/infusion_altar_advanced")));
        simpleBlockWithItem(ModBlocks.MIXER_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/mixer")));
        simpleBlockWithItem(ModBlocks.GIANT_COCOON.get(),
                models().cubeColumn(ModBlocks.GIANT_COCOON.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/giant_cocoon_side"),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/giant_cocoon_top")));

        simpleBlockWithItem(ModBlocks.CLOCKWORK_CRAFTER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_crafter")));
        simpleBlockWithItem(ModBlocks.CLOCKWORK_WINDER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_winder")));
        clockworkSyrupExtractorBlock(ModBlocks.CLOCKWORK_SYRUP_EXTRACTOR);
        simpleBlockItem(ModBlocks.CLOCKWORK_SYRUP_EXTRACTOR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_syrup_extractor")));
        simpleBlockWithItem(ModBlocks.CLOCKWORK_GEYSER_COLLECTOR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_geyser_collector")));
        simpleBlockWithItem(ModBlocks.CLOCKWORK_POWDERED_SUGAR_COLLECTOR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_powdered_sugar_collector")));
        simpleBlockWithItem(ModBlocks.CLOCKWORK_FISHER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_fisher")));
        simpleBlockWithItem(ModBlocks.CLOCKWORK_FAIRY_TERMINAL.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_fairy_terminal")));

        simpleBlockWithItem(ModBlocks.CLOCKWORK_MAIDEN_TERMINAL.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/clockwork_maiden_terminal")));




        //Beacon
        simpleBlockWithItem(ModBlocks.PRISMATIC_BEACON.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/prismatic_beacon")));
        simpleBlockWithItem(ModBlocks.PRISMATIC_BEACON_BASE.get(),
                models().cubeBottomTop(ModBlocks.PRISMATIC_BEACON_BASE.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/prismatic_beacon_base_side"),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/prismatic_beacon_base_bottom"),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/prismatic_beacon_base_top")));
        blockWithItem(ModBlocks.BEACON_HASTE_MODIFIER);
        blockWithItem(ModBlocks.BEACON_FIRE_RESISTANCE_MODIFIER);
        blockWithItem(ModBlocks.BEACON_HEALTH_BOOST_MODIFIER);
        blockWithItem(ModBlocks.BEACON_JUMP_BOOST_MODIFIER);
        blockWithItem(ModBlocks.BEACON_SPEED_MODIFIER);
        blockWithItem(ModBlocks.BEACON_RESISTANCE_MODIFIER);
        blockWithItem(ModBlocks.BEACON_REGENERATION_MODIFIER);
        blockWithItem(ModBlocks.BEACON_STRENGTH_MODIFIER);
        blockWithItem(ModBlocks.BEACON_WATER_BREATHING_MODIFIER);

        //Other blocks
        syrupExtractorBlock(ModBlocks.SYRUP_EXTRACTOR);
        simpleBlockItem(ModBlocks.SYRUP_EXTRACTOR.get(), new ModelFile.UncheckedModelFile(modLoc("block/syrup_extractor_stage0")));


    }

    private void hootnipCrop(DeferredHolder<Block, Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState -> {
                String modelName = block.getId().getPath() + "_stage" + blockState.getValue(HootnipCrop.AGE);
                return ConfiguredModel.builder().modelFile(models().crop(modelName,
                        ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + modelName)).renderType("cutout")).build();}
        );
    }


    private void wildHootnipBlock(DeferredHolder<Block, Block> block){
        getVariantBuilder(block.get()).partialState()
                .with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                .modelForState().modelFile(getCrossModel(block, "_top")).addModel().partialState()
                .with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                .modelForState().modelFile(getCrossModel(block, "_bottom")).addModel().partialState();
    }

    private void syrupExtractorBlock(DeferredHolder<Block, Block> block){

        getVariantBuilder(block.get()).forAllStates(blockState -> {
            ModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/syrup_extractor_stage" + blockState.getValue(SyrupExtractorBlock.LEVEL)));
            switch (blockState.getValue(HorizontalDirectionalBlock.FACING)){
                case EAST:
                    return ConfiguredModel.builder().modelFile(model).rotationY(270).build();
                case SOUTH:
                    return ConfiguredModel.builder().modelFile(model).rotationY(0).build();
                case WEST:
                    return ConfiguredModel.builder().modelFile(model).rotationY(90).build();
                default:
                    return ConfiguredModel.builder().modelFile(model).rotationY(180).build();
            }
        });
    }

    private void clockworkSyrupExtractorBlock(DeferredHolder<Block, Block> block){

        getVariantBuilder(block.get()).forAllStates(blockState -> {
            ModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/clockwork_syrup_extractor"));
            switch (blockState.getValue(HorizontalDirectionalBlock.FACING)){
                case EAST:
                    return ConfiguredModel.builder().modelFile(model).rotationY(90).build();
                case SOUTH:
                    return ConfiguredModel.builder().modelFile(model).rotationY(180).build();
                case WEST:
                    return ConfiguredModel.builder().modelFile(model).rotationY(270).build();
                default:
                    return ConfiguredModel.builder().modelFile(model).rotationY(0).build();
            }
        });
    }

    private void XZdirectionalBlock(DeferredHolder<Block, Block> block, ModelFile modelFile){
        getVariantBuilder(block.get()).partialState()
                .with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(modelFile).rotationY(90).addModel().partialState()
                .with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(modelFile).addModel().partialState()
                .with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(modelFile).rotationY(180).addModel().partialState()
                .with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(modelFile).rotationY(270).addModel().partialState();
    }

    private void strawberryCrop(DeferredHolder<Block, Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState -> {
            String model_name = block.getId().getPath() + "_stage" + blockState.getValue(StrawberryCrop.AGE);
           return ConfiguredModel.builder().modelFile(models().crop(model_name,
                   ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + model_name)).renderType("cutout")).build();}
        );
    }

    private void cropBlock(DeferredHolder<Block, Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState -> {
            String model_name = block.getId().getPath() + "_stage" + blockState.getValue(CropBlock.AGE);
           return ConfiguredModel.builder().modelFile(models().crop(model_name,
                   ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + model_name)).renderType("cutout")).build();}
        );
    }

    private void crossCropBlock(DeferredHolder<Block, Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState -> {
            String model_name = block.getId().getPath() + "_stage" + blockState.getValue(CropBlock.AGE);
           return ConfiguredModel.builder().modelFile(models().cross(model_name,
                   ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + model_name)).renderType("cutout")).build();}
        );
    }

    private void farmlandBlock(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> soil) {
        BlockModelBuilder farmland = farmlandModel(block, soil);
        BlockModelBuilder moistFarmland = moistFarmlandModel(block, soil);

        getVariantBuilder(block.get()).forAllStates(blockState -> {
            if (blockState.getValue(FarmBlock.MOISTURE) == 7){
                return ConfiguredModel.builder().modelFile(moistFarmland).build();
            }
            return ConfiguredModel.builder().modelFile(farmland).build();
        });
    }

    private BlockModelBuilder farmlandModel(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> soil){
        return models().withExistingParent(block.getId().getPath(), mcLoc("block/template_farmland"))
                .texture("dirt", ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + soil.getId().getPath()))
                .texture("top", ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + block.getId().getPath()));
    }
    private BlockModelBuilder moistFarmlandModel(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> soil){
        return models().withExistingParent(block.getId().getPath(), mcLoc("block/template_farmland"))
                .texture("dirt", ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + soil.getId().getPath()))
                .texture("top", ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + block.getId().getPath() + "_moist"));
    }

    private void pottedPlant(DeferredHolder<Block, Block> registryObject, DeferredHolder<Block, Block> plant){
        simpleBlockWithItem(registryObject.get(),
                models().singleTexture(registryObject.getId().getPath()
                        , ResourceLocation.fromNamespaceAndPath("minecraft", "flower_pot_cross"), "plant",
                        blockTexture(plant.get())).renderType("cutout"));
    }

    private void variedCross(DeferredHolder<Block, Block> registryObject){
        BlockModelBuilder var1 = getCrossModel(registryObject, "_1");
        BlockModelBuilder var2 = getCrossModel(registryObject, "_2");
        BlockModelBuilder var3 = getCrossModel(registryObject, "_3");

        getVariantBuilder(registryObject.get()).forAllStates(blockState ->
            ConfiguredModel.builder()
                    .modelFile(var1).weight(2).nextModel()
                    .modelFile(var2).weight(2).nextModel()
                    .modelFile(var3).weight(1)
                    .build());
    }

    private void luminum(DeferredHolder<Block, Block> registryObject){
        BlockModelBuilder bottom = getTwoCrossModel(registryObject, "_bottom");
        BlockModelBuilder top1 = getTwoCrossModel(registryObject, "_top_1");
        BlockModelBuilder top2 = getTwoCrossModel(registryObject, "_top_2");
        BlockModelBuilder top3 = getTwoCrossModel(registryObject, "_top_3");

        getVariantBuilder(registryObject.get()).forAllStates(blockState -> {

            int rotation;
            switch (blockState.getValue(RotatedTallFlowerBlock.FACING)){
                case NORTH -> rotation = 0;
                case EAST -> rotation = 90;
                case SOUTH -> rotation = 180;
                default -> rotation = 270;
            }

            if (blockState.getValue(RotatedTallFlowerBlock.HALF) == DoubleBlockHalf.LOWER){
                return ConfiguredModel.builder()
                        .modelFile(bottom).rotationY(rotation).build();
            }
            else {
                return ConfiguredModel.builder()
                        .modelFile(top1).rotationY(rotation).weight(2).nextModel()
                        .modelFile(top2).rotationY(rotation).weight(1).nextModel()
                        .modelFile(top3).rotationY(rotation).weight(1).build();
            }
        });
    }

    private void seaShells(DeferredHolder<Block, Block> registryObject){
        ResourceLocation parent = modLoc("block/ground_image");
        BlockModelBuilder var1 = getSingleTextureModel(registryObject, parent, "_1");
        BlockModelBuilder var2 = getSingleTextureModel(registryObject, parent, "_2");
        BlockModelBuilder var3 = getSingleTextureModel(registryObject, parent, "_3");

        getVariantBuilder(registryObject.get()).forAllStates(blockState ->
                ConfiguredModel.builder()
                        .modelFile(var1).weight(3).nextModel()
                        .modelFile(var1).weight(3).rotationY(90).nextModel()
                        .modelFile(var1).weight(3).rotationY(180).nextModel()
                        .modelFile(var1).weight(3).rotationY(270).nextModel()
                        .modelFile(var2).weight(2).nextModel()
                        .modelFile(var2).weight(2).rotationY(90).nextModel()
                        .modelFile(var2).weight(2).rotationY(180).nextModel()
                        .modelFile(var2).weight(2).rotationY(270).nextModel()
                        .modelFile(var3).weight(1).nextModel()
                        .modelFile(var3).weight(1).rotationY(90).nextModel()
                        .modelFile(var3).weight(1).rotationY(180).nextModel()
                        .modelFile(var3).weight(1).rotationY(270)
                        .build());

    }

    private BlockModelBuilder getSingleTextureModel(DeferredHolder<Block, Block> registryObject, ResourceLocation parent, String var){
        return  models().singleTexture(
                registryObject.getId().getPath() + var,
                parent, "image",
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID,"block/" + registryObject.getId().getPath() + var)).renderType("cutout");

    }

    private void hangingString(DeferredHolder<Block, Block> registryObject){
        BlockModelBuilder top = getCrossModel(registryObject, "_top");
        BlockModelBuilder topEnding = getCrossModel(registryObject, "_top_ending");
        BlockModelBuilder topGrab = getCrossModel(registryObject, "_top_grab");
        BlockModelBuilder middle = getCrossModel(registryObject, "_middle");
        BlockModelBuilder middleGrabbing = getCrossModel(registryObject, "_middle_ending");
        BlockModelBuilder grab = getCrossModel(registryObject, "_grab");

        getVariantBuilder(registryObject.get()).partialState()
                .with(HangingStringBlock.TYPE, HangingStringEnding.TOP)
                .modelForState().modelFile(top).addModel().partialState()
                .with(HangingStringBlock.TYPE, HangingStringEnding.TOP_ENDING)
                .modelForState().modelFile(topEnding).addModel().partialState()
                .with(HangingStringBlock.TYPE, HangingStringEnding.TOP_GRAB)
                .modelForState().modelFile(topGrab).addModel().partialState()
                .with(HangingStringBlock.TYPE, HangingStringEnding.MIDDLE)
                .modelForState().modelFile(middle).addModel().partialState()
                .with(HangingStringBlock.TYPE, HangingStringEnding.MIDDLE_ENDING)
                .modelForState().modelFile(middleGrabbing).addModel().partialState()
                .with(HangingStringBlock.TYPE, HangingStringEnding.GRAB)
                .modelForState().modelFile(grab).addModel().partialState();
    }

    private void crystalBlock(DeferredHolder<Block, Block> registryObject){
        BlockModelBuilder base = getCrossModel(registryObject, "");

        getVariantBuilder(registryObject.get()).partialState()
                .with(ModGrowableMineral.FACING, Direction.DOWN)
                .modelForState().modelFile(base).rotationX(180).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.EAST)
                .modelForState().modelFile(base).rotationX(90).rotationY(90).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.NORTH)
                .modelForState().modelFile(base).rotationX(90).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.SOUTH)
                .modelForState().modelFile(base).rotationX(90).rotationY(180).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.UP)
                .modelForState().modelFile(base).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.WEST)
                .modelForState().modelFile(base).rotationX(90).rotationY(270).addModel().partialState();
    }

    private BlockModelBuilder getTwoCrossModel(DeferredHolder<Block, Block> registryObject, String var){
        return models().withExistingParent(registryObject.getId().getPath() + var,
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "two_cross"))
                .texture("xtex", ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID,"block/" + registryObject.getId().getPath() + "_x" + var))
                .texture("ytex", ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID,"block/" + registryObject.getId().getPath() + "_y" + var))
                .renderType("cutout");
    }

    private BlockModelBuilder getCrossModel(DeferredHolder<Block, Block> registryObject, String var){
        return models().cross(registryObject.getId().getPath() + var,
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID,"block/" + registryObject.getId().getPath() + var)).renderType("cutout");
    }

    private void mushroomBlock(DeferredHolder<Block, Block> registryObject){
        BlockModelBuilder mushroomInside = mushroomSide(registryObject, true);
        BlockModelBuilder mushroomOutside = mushroomSide(registryObject, false);

        getMultipartBuilder(registryObject.get())
                .part().modelFile(mushroomOutside).addModel().condition(HugeMushroomBlock.NORTH, true).end()
                .part().modelFile(mushroomOutside).uvLock(true).rotationY(90).addModel().condition(HugeMushroomBlock.EAST, true).end()
                .part().modelFile(mushroomOutside).uvLock(true).rotationY(180).addModel().condition(HugeMushroomBlock.SOUTH, true).end()
                .part().modelFile(mushroomOutside).uvLock(true).rotationY(270).addModel().condition(HugeMushroomBlock.WEST, true).end()
                .part().modelFile(mushroomOutside).uvLock(true).rotationX(270).addModel().condition(HugeMushroomBlock.UP, true).end()
                .part().modelFile(mushroomOutside).uvLock(true).rotationX(90).addModel().condition(HugeMushroomBlock.DOWN, true).end()
                .part().modelFile(mushroomInside).addModel().condition(HugeMushroomBlock.NORTH,false).end()
                .part().modelFile(mushroomInside).uvLock(false).rotationY(90).addModel().condition(HugeMushroomBlock.EAST,false).end()
                .part().modelFile(mushroomInside).uvLock(false).rotationY(180).addModel().condition(HugeMushroomBlock.SOUTH,false).end()
                .part().modelFile(mushroomInside).uvLock(false).rotationY(270).addModel().condition(HugeMushroomBlock.WEST,false).end()
                .part().modelFile(mushroomInside).uvLock(false).rotationX(270).addModel().condition(HugeMushroomBlock.UP,false).end()
                .part().modelFile(mushroomInside).uvLock(false).rotationX(90).addModel().condition(HugeMushroomBlock.DOWN,false).end();

    }

    private BlockModelBuilder mushroomSide(DeferredHolder<Block, Block> registryObject, boolean isInside){
        String inside = isInside ? "_inside" : "";
        return models().singleTexture(registryObject.getId().getPath() + inside,
                mcLoc("minecraft:block/template_single_face"),
                ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + registryObject.getId().getPath() + inside));
    }

    private void grassBlock(DeferredHolder<Block, Block> registryObject, String blockBottomPath) {
        simpleBlockWithItem(registryObject.get(),
                models().cubeBottomTop(registryObject.getId().getPath(),
                        ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + registryObject.getId().getPath() + "_side"),
                        ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + blockBottomPath),
                        ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "block/" + registryObject.getId().getPath() + "_top")));
    }

    private void blockWithItem(DeferredHolder<Block, Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void translucentBlockWithItem(DeferredHolder<Block, Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(ModBlocks.SWICE.getId().getPath(),
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/cube_all"), "all", blockTexture(ModBlocks.SWICE.get())).renderType("translucent"));
    }

    private void simpleCrossBlock(DeferredHolder<Block, Block> blockRegistry){
        simpleBlock(blockRegistry.get(),getCrossModel(blockRegistry, ""));
    }

    private void sweedsCrossBlock(DeferredHolder<Block, Block> blockRegistry){
        BlockModelBuilder base = getCrossModel(blockRegistry, "");
        BlockModelBuilder top = getCrossModel(blockRegistry, "_top");

        getVariantBuilder(blockRegistry.get()).forAllStates(blockState -> {
            return blockState.getValue(Sweeds.HEIGHT) == 4 ? ConfiguredModel.builder().modelFile(top).build() :
                    ConfiguredModel.builder().modelFile(base).build();
        });
    }
}
