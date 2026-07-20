package net.sophiebun.buntsy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.custom.*;
import net.sophiebun.buntsy.blocks.custom.entityblocks.*;
import net.sophiebun.buntsy.blocks.custom.farmland.CharmilFarmland;
import net.sophiebun.buntsy.blocks.custom.farmland.OdiateFarmland;
import net.sophiebun.buntsy.blocks.custom.hanging_block.HangingObjectBlock;
import net.sophiebun.buntsy.blocks.custom.hanging_block.HangingStringBlock;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.blocks.custom.plants.*;
import net.sophiebun.buntsy.blocks.custom.wood.*;
import net.sophiebun.buntsy.fluids.ModFluids;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.worldgen.ModConfiguredFeatures;
import net.sophiebun.buntsy.worldgen.ModPlacedFeatures;
import net.sophiebun.buntsy.worldgen.tree.ModTreeGrowers;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BlocksRegister =
            DeferredRegister.create(Registries.BLOCK, BuntsyMod.MODID);

    public static final DeferredHolder<Block, Block> MALVOR_LEAVES = registerBlock("malvor_leaves",
            () -> new ModThickLeaves(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().strength(0.2F).randomTicks().isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false)));
    public static final DeferredHolder<Block, Block> MALVOR_SAPLING = registerBlock("malvor_sapling",
            () -> new SaplingBlock(ModTreeGrowers.MALVOR_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredHolder<Block, Block> POTTED_MALVOR_SAPLING = BlocksRegister.register("potted_malvor_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.MALVOR_SAPLING,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final DeferredHolder<Block, Block> MALVOR_LOG = registerBlock("malvor_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, Block> STRIPPED_MALVOR_LOG = registerBlock("stripped_malvor_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, Block> MALVOR_WOOD = registerBlock("malvor_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, Block> STRIPPED_MALVOR_WOOD = registerBlock("stripped_malvor_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredHolder<Block, Block> MALVOR_PLANKS = registerBlock("malvor_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> MALVOR_STAIRS = registerBlock("malvor_stairs",
            () -> new ModWoodStairs(ModBlocks.MALVOR_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> MALVOR_SLAB = registerBlock("malvor_slab",
            () -> new ModWoodSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> MALVOR_BUTTON = registerBlock("malvor_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> MALVOR_PRESSURE_PLATE = registerBlock("malvor_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> MALVOR_FENCE = registerBlock("malvor_fence",
            () -> new ModWoodFence(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> MALVOR_FENCE_GATE = registerBlock("malvor_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).noOcclusion(), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> MALVOR_DOOR = registerBlock("malvor_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> MALVOR_TRAPDOOR = registerBlock("malvor_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));


    public static final DeferredHolder<Block, Block> GENTLIT_LEAVES = registerBlock("gentlit_leaves",
            () -> new ModLeaves(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().strength(0.2F).randomTicks().isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false)));
    public static final DeferredHolder<Block, Block> GENTLIT_SAPLING = registerBlock("gentlit_sapling",
            () -> new SaplingBlock(ModTreeGrowers.GENTLIT_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredHolder<Block, Block> POTTED_GENTLIT_SAPLING = BlocksRegister.register("potted_gentlit_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.GENTLIT_SAPLING,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final DeferredHolder<Block, Block> GENTLIT_LOG = registerBlock("gentlit_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, Block> STRIPPED_GENTLIT_LOG = registerBlock("stripped_gentlit_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, Block> GENTLIT_WOOD = registerBlock("gentlit_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, Block> STRIPPED_GENTLIT_WOOD = registerBlock("stripped_gentlit_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredHolder<Block, Block> GENTLIT_PLANKS = registerBlock("gentlit_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> GENTLIT_STAIRS = registerBlock("gentlit_stairs",
            () -> new ModWoodStairs(ModBlocks.GENTLIT_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> GENTLIT_SLAB = registerBlock("gentlit_slab",
            () -> new ModWoodSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> GENTLIT_BUTTON = registerBlock("gentlit_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> GENTLIT_PRESSURE_PLATE = registerBlock("gentlit_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> GENTLIT_FENCE = registerBlock("gentlit_fence",
            () -> new ModWoodFence(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> GENTLIT_FENCE_GATE = registerBlock("gentlit_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).noOcclusion(), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> GENTLIT_DOOR = registerBlock("gentlit_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> GENTLIT_TRAPDOOR = registerBlock("gentlit_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final DeferredHolder<Block, Block> BRAVOT_LEAVES = registerBlock("bravot_leaves",
            () -> new ModLeaves(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().strength(0.2F).randomTicks().isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false)));
    public static final DeferredHolder<Block, Block> BRAVOT_SAPLING = registerBlock("bravot_sapling",
            () -> new SaplingBlock(ModTreeGrowers.BRAVOT_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredHolder<Block, Block> POTTED_BRAVOT_SAPLING = BlocksRegister.register("potted_bravot_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.BRAVOT_SAPLING,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final DeferredHolder<Block, Block> BRAVOT_LOG = registerBlock("bravot_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, Block> STRIPPED_BRAVOT_LOG = registerBlock("stripped_bravot_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, Block> BRAVOT_WOOD = registerBlock("bravot_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, Block> STRIPPED_BRAVOT_WOOD = registerBlock("stripped_bravot_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredHolder<Block, Block> BRAVOT_PLANKS = registerBlock("bravot_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> BRAVOT_STAIRS = registerBlock("bravot_stairs",
            () -> new ModWoodStairs(ModBlocks.BRAVOT_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> BRAVOT_SLAB = registerBlock("bravot_slab",
            () -> new ModWoodSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> BRAVOT_BUTTON = registerBlock("bravot_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> BRAVOT_PRESSURE_PLATE = registerBlock("bravot_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> BRAVOT_FENCE = registerBlock("bravot_fence",
            () -> new ModWoodFence(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> BRAVOT_FENCE_GATE = registerBlock("bravot_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).noOcclusion(), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> BRAVOT_DOOR = registerBlock("bravot_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> BRAVOT_TRAPDOOR = registerBlock("bravot_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_LEAVES = registerBlock("origami_palm_leaves",
            () -> new ModLeaves(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().strength(0.2F).randomTicks().isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_SAPLING = registerBlock("origami_palm_sapling",
            () -> new SandySaplingBlock(ModTreeGrowers.ORIGAMI_PALM_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredHolder<Block, Block> POTTED_ORIGAMI_PALM_SAPLING = BlocksRegister.register("potted_origami_palm_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.ORIGAMI_PALM_SAPLING,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_LOG = registerBlock("origami_palm_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, Block> STRIPPED_ORIGAMI_PALM_LOG = registerBlock("stripped_origami_palm_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_WOOD = registerBlock("origami_palm_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, Block> STRIPPED_ORIGAMI_PALM_WOOD = registerBlock("stripped_origami_palm_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_PLANKS = registerBlock("origami_palm_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_STAIRS = registerBlock("origami_palm_stairs",
            () -> new ModWoodStairs(ModBlocks.ORIGAMI_PALM_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_SLAB = registerBlock("origami_palm_slab",
            () -> new ModWoodSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_BUTTON = registerBlock("origami_palm_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_PRESSURE_PLATE = registerBlock("origami_palm_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_FENCE = registerBlock("origami_palm_fence",
            () -> new ModWoodFence(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_FENCE_GATE = registerBlock("origami_palm_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).noOcclusion(), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_DOOR = registerBlock("origami_palm_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> ORIGAMI_PALM_TRAPDOOR = registerBlock("origami_palm_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final DeferredHolder<Block, Block> CRYSTALLIZED_LOG = registerBlock("crystallized_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_ICE).noOcclusion()));
    public static final DeferredHolder<Block, Block> CRYSTALLIZED_LEAVES = registerBlock("crystallized_leaves",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_ICE).noOcclusion()));

    //Biome ground blocks
    public static final DeferredHolder<Block, Block> CHARMIL_FARMLAND = registerBlock("charmil_farmland",
            () -> new CharmilFarmland(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND).sound(SoundType.MOSS)));
    public static final DeferredHolder<Block, Block> CHARMIL_SOIL = registerBlock("charmil_soil",
            () -> new TillableModSoil(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.MOSS), ModBlocks.CHARMIL_FARMLAND.get()));
    public static final DeferredHolder<Block, Block> PINK_FLUF_CHARMIL_SOIL = registerBlock("pink_fluf_charmil_soil",
            () -> new TillableModGrass(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).sound(SoundType.MOSS),
                    ModBlocks.CHARMIL_SOIL.get(), ModBlocks.CHARMIL_FARMLAND.get(),
                    ModPlacedFeatures.CHARMIL_BONEMEAL_PLACED_KEY));

    public static final DeferredHolder<Block, Block> ODIATE_FARMLAND = registerBlock("odiate_farmland",
            () -> new OdiateFarmland(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND).sound(SoundType.MOSS)));
    public static final DeferredHolder<Block, Block> ODIATE_SOIL = registerBlock("odiate_soil",
            () -> new TillableModSoil(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.MOSS), ModBlocks.ODIATE_FARMLAND.get()));
    public static final DeferredHolder<Block, Block> GRAY_MOSS_ODIATE_SOIL = registerBlock("gray_moss_odiate_soil",
            () -> new TillableModGrass(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).sound(SoundType.MOSS),
                    ModBlocks.ODIATE_SOIL.get(), ModBlocks.ODIATE_FARMLAND.get(),
                    ModPlacedFeatures.ODIATE_BONEMEAL_PLACED_KEY));
    public static final DeferredHolder<Block, Block> ODIATE_MUD = registerBlock("odiate_mud",
            () -> new MudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD).sound(SoundType.MUD)));
    public static final DeferredHolder<Block, Block> PACKED_ODIATE_MUD = registerBlock("packed_odiate_mud",
            () -> new MudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_MUD).sound(SoundType.PACKED_MUD)));
    public static final DeferredHolder<Block, Block> ODIATE_MUD_BRICKS = registerBlock("odiate_mud_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> ODIATE_MUD_BRICKS_STAIRS = registerBlock("odiate_mud_bricks_stairs",
            () -> new StairBlock(ModBlocks.ODIATE_MUD_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> ODIATE_MUD_BRICKS_SLAB = registerBlock("odiate_mud_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> ODIATE_MUD_BRICKS_WALL = registerBlock("odiate_mud_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final DeferredHolder<Block, Block> FROZEN_POWDER_BLOCK = registerBlock("frozen_powder_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK).sound(SoundType.SNOW)));
    public static final DeferredHolder<Block, Block> FROZEN_POWDER_LAYER = registerBlock("frozen_powder_layer",
            () -> new SnowLayerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK).sound(SoundType.SNOW)){
                @Override
                public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {}
            });
    public static final DeferredHolder<Block, Block> FROZEN_CORAL_SAND = registerBlock("frozen_coral_sand",
            () -> new ColoredFallingBlock(new ColorRGBA(0xA3C8DA), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE = registerBlock("frozen_limestone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_STAIRS = registerBlock("frozen_limestone_stairs",
            () -> new StairBlock(ModBlocks.FROZEN_LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_SLAB = registerBlock("frozen_limestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_WALL = registerBlock("frozen_limestone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_BRICKS = registerBlock("frozen_limestone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_BRICKS_STAIRS = registerBlock("frozen_limestone_bricks_stairs",
            () -> new StairBlock(ModBlocks.FROZEN_LIMESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_BRICKS_SLAB = registerBlock("frozen_limestone_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> FROZEN_LIMESTONE_BRICKS_WALL = registerBlock("frozen_limestone_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final DeferredHolder<Block, Block> SWICE = registerBlock("swice",
            () -> new HalfTransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).noOcclusion()));

    public static final DeferredHolder<Block, Block> SUNNY_CORAL_SAND = registerBlock("sunny_coral_sand",
            () -> new ColoredFallingBlock(new ColorRGBA(0xA3C8DA), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE = registerBlock("sunny_limestone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_STAIRS = registerBlock("sunny_limestone_stairs",
            () -> new StairBlock(ModBlocks.SUNNY_LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_SLAB = registerBlock("sunny_limestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_WALL = registerBlock("sunny_limestone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_BRICKS = registerBlock("sunny_limestone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_BRICKS_STAIRS = registerBlock("sunny_limestone_bricks_stairs",
            () -> new StairBlock(ModBlocks.SUNNY_LIMESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_BRICKS_SLAB = registerBlock("sunny_limestone_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> SUNNY_LIMESTONE_BRICKS_WALL = registerBlock("sunny_limestone_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE = registerBlock("petrified_chocolate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_STAIRS = registerBlock("petrified_chocolate_stairs",
            () -> new StairBlock(ModBlocks.PETRIFIED_CHOCOLATE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_SLAB = registerBlock("petrified_chocolate_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_WALL = registerBlock("petrified_chocolate_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_BRICKS = registerBlock("petrified_chocolate_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_BRICKS_STAIRS = registerBlock("petrified_chocolate_bricks_stairs",
            () -> new StairBlock(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_BRICKS_SLAB = registerBlock("petrified_chocolate_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> PETRIFIED_CHOCOLATE_BRICKS_WALL = registerBlock("petrified_chocolate_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final DeferredHolder<Block, Block> COBBLED_PETRIFIED_CHOCOLATE = registerBlock("cobbled_petrified_chocolate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)));
    public static final DeferredHolder<Block, Block> COBBLED_PETRIFIED_CHOCOLATE_STAIRS = registerBlock("cobbled_petrified_chocolate_stairs",
            () -> new StairBlock(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> COBBLED_PETRIFIED_CHOCOLATE_SLAB = registerBlock("cobbled_petrified_chocolate_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> COBBLED_PETRIFIED_CHOCOLATE_WALL = registerBlock("cobbled_petrified_chocolate_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final DeferredHolder<Block, Block> CHOCOLATE_BLOCK = registerBlock("chocolate_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredHolder<Block, Block> CHOCOLATE_GEYSER = registerBlock("chocolate_geyser",
            () -> new ChocolateGeyserBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));

    public static final DeferredHolder<Block, Block> DECAYED_CLOCKWORK_BRASS_BLOCK = registerBlock("decayed_clockwork_brass_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
    public static final DeferredHolder<Block, Block> DECAYED_CUT_CLOCKWORK_BRASS = registerBlock("decayed_cut_clockwork_brass",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER)));

    public static final DeferredHolder<Block, Block> CLOCKWORK_BRASS_BLOCK = registerBlock("clockwork_brass_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
    public static final DeferredHolder<Block, Block> CUT_CLOCKWORK_BRASS = registerBlock("cut_clockwork_brass",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER)));
    public static final DeferredHolder<Block, Block> CUT_CLOCKWORK_BRASS_STAIRS = registerBlock("cut_clockwork_brass_stairs",
            () -> new StairBlock(ModBlocks.CUT_CLOCKWORK_BRASS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> CUT_CLOCKWORK_BRASS_SLAB = registerBlock("cut_clockwork_brass_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));

    public static final DeferredHolder<Block, Block> SEA_SHELLS = registerBlock("sea_shells",
            () -> new SeaShellsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).sound(SoundType.STONE).noCollission().noOcclusion().instabreak()));

    public static final DeferredHolder<Block, Block> SWEET_CORAL_SAND = registerBlock("sweet_coral_sand",
            () -> new ColoredFallingBlock(new ColorRGBA(0xdaa3b0), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)));

    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE = registerBlock("sweet_limestone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_STAIRS = registerBlock("sweet_limestone_stairs",
            () -> new StairBlock(ModBlocks.SWEET_LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_SLAB = registerBlock("sweet_limestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_WALL = registerBlock("sweet_limestone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_BRICKS = registerBlock("sweet_limestone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_BRICKS_STAIRS = registerBlock("sweet_limestone_bricks_stairs",
            () -> new StairBlock(ModBlocks.SWEET_LIMESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_BRICKS_SLAB = registerBlock("sweet_limestone_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredHolder<Block, Block> SWEET_LIMESTONE_BRICKS_WALL = registerBlock("sweet_limestone_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final DeferredHolder<Block, Block> SWEET_CANDY_ROCK = registerBlock("sweet_candy_rock",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_ROCK_STAIRS = registerBlock("sweet_candy_rock_stairs",
            () -> new StairBlock(ModBlocks.SWEET_CANDY_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_ROCK_SLAB = registerBlock("sweet_candy_rock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_ROCK_WALL = registerBlock("sweet_candy_rock_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_BRICKS = registerBlock("sweet_candy_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_BRICKS_STAIRS = registerBlock("sweet_candy_bricks_stairs",
            () -> new StairBlock(ModBlocks.SWEET_CANDY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_BRICKS_SLAB = registerBlock("sweet_candy_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SWEET_CANDY_BRICKS_WALL = registerBlock("sweet_candy_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));

    public static final DeferredHolder<Block, Block> BITTER_CANDY_ROCK = registerBlock("bitter_candy_rock",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_ROCK_STAIRS = registerBlock("bitter_candy_rock_stairs",
            () -> new StairBlock(ModBlocks.BITTER_CANDY_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_ROCK_SLAB = registerBlock("bitter_candy_rock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_ROCK_WALL = registerBlock("bitter_candy_rock_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_BRICKS = registerBlock("bitter_candy_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_BRICKS_STAIRS = registerBlock("bitter_candy_bricks_stairs",
            () -> new StairBlock(ModBlocks.BITTER_CANDY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_BRICKS_SLAB = registerBlock("bitter_candy_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> BITTER_CANDY_BRICKS_WALL = registerBlock("bitter_candy_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));

    public static final DeferredHolder<Block, Block> SOUR_CANDY_ROCK = registerBlock("sour_candy_rock",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_ROCK_STAIRS = registerBlock("sour_candy_rock_stairs",
            () -> new StairBlock(ModBlocks.SOUR_CANDY_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_ROCK_SLAB = registerBlock("sour_candy_rock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_ROCK_WALL = registerBlock("sour_candy_rock_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_BRICKS = registerBlock("sour_candy_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_BRICKS_STAIRS = registerBlock("sour_candy_bricks_stairs",
            () -> new StairBlock(ModBlocks.SOUR_CANDY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_BRICKS_SLAB = registerBlock("sour_candy_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));
    public static final DeferredHolder<Block, Block> SOUR_CANDY_BRICKS_WALL = registerBlock("sour_candy_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)));

    //Crops
    public static final DeferredHolder<Block, Block> WILD_STRAWBERRY = registerBlock("wild_strawberry",
            () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> STRAWBERRY_CROP = registerBlock("strawberry_crop",
            () -> new StrawberryCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> WILD_HOOTNIP = registerBlock("wild_hootnip",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> HOOTNIP_CROP = registerBlock("hootnip_crop",
            () -> new HootnipCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> SUGARDEW_CROP = registerBlock("sugardew_crop",
            () -> new SugardewCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> WINTER_ROOT_CROP = registerBlock("winter_root_crop",
            () -> new WinterRootsCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion().noCollission()));

    //Biome decor
    public static final DeferredHolder<Block, Block> HANGING_STRING = registerBlock("hanging_string",
            () -> new HangingStringBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> HANGING_CLOCKWORK = registerBlock("hanging_clockwork",
            () -> new HangingObjectBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> HANGING_LUMINUM = registerBlock("hanging_luminum",
            () -> new HangingObjectBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE).noOcclusion().noCollission()){

                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 8;
                }
            });

    //Biome plants
    //Ocean
    public static final DeferredHolder<Block, Block> CHARMING_LOTUS = registerBlock("charming_lotus",
            () -> new LotusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD).noOcclusion()));
    public static final DeferredHolder<Block, Block> BRAVE_LOTUS = registerBlock("brave_lotus",
            () -> new LotusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD).noOcclusion()));
    public static final DeferredHolder<Block, Block> MALIUM_LOTUS = registerBlock("malium_lotus",
            () -> new LotusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD).noOcclusion()));

    public static final DeferredHolder<Block, Block> SWEETGRASS = registerBlock("sweetgrass",
            () -> new SweetgrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SEAGRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> TALL_SWEETGRASS = registerBlock("tall_sweetgrass",
            () -> new TallSweetgrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_SEAGRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> COTTON_VINE = registerBlock("cotton_vine",
            () -> new CottonvineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.KELP).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> COTTON_VINE_PLANT = registerBlock("cotton_vine_plant",
            () -> new CottonvinePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.KELP_PLANT).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> SWEET_PICKLE = registerBlock("sweet_pickle",
            () -> new SweetPickleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SEA_PICKLE).noOcclusion().noCollission()));
    //Corals
    public static final DeferredHolder<Block, Block> DEAD_SWEET_CORAL_BLOCK = registerBlock("dead_sweet_coral_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_FIRE_CORAL_BLOCK)));
    public static final DeferredHolder<Block, Block> DEAD_SWEET_CORAL = registerBlock("dead_sweet_coral",
            () -> new BaseCoralPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().instabreak()));
    public static final DeferredHolder<Block, Block> DEAD_SWEET_CORAL_FAN = registerBlock("dead_sweet_coral_fan",
            () -> new BaseCoralFanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().instabreak()));
    public static final DeferredHolder<Block, Block> DEAD_SWEET_CORAL_WALL_FAN = registerBlock("dead_sweet_coral_wall_fan",
            () -> new BaseCoralWallFanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().instabreak().dropsLike(DEAD_SWEET_CORAL_FAN.get())));
    public static final DeferredHolder<Block, Block> SWEET_CORAL_BLOCK = registerBlock("sweet_coral_block",
            () -> new CoralBlock(DEAD_SWEET_CORAL_BLOCK.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE_CORAL_BLOCK)));
    public static final DeferredHolder<Block, Block> SWEET_CORAL = registerBlock("sweet_coral",
            () -> new CoralPlantBlock(DEAD_SWEET_CORAL.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE_CORAL).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> SWEET_CORAL_FAN = registerBlock("sweet_coral_fan",
            () -> new CoralFanBlock(DEAD_SWEET_CORAL_FAN.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE_CORAL_FAN).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> SWEET_CORAL_WALL_FAN = registerBlock("sweet_coral_wall_fan",
            () -> new CoralWallFanBlock(DEAD_SWEET_CORAL_WALL_FAN.get(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS).dropsLike(SWEET_CORAL_FAN.get()).pushReaction(PushReaction.DESTROY)));

    public static final DeferredHolder<Block, Block> DEAD_BITTER_CORAL_BLOCK = registerBlock("dead_bitter_coral_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_TUBE_CORAL_BLOCK)));
    public static final DeferredHolder<Block, Block> DEAD_BITTER_CORAL = registerBlock("dead_bitter_coral",
            () -> new BaseCoralPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().instabreak()));
    public static final DeferredHolder<Block, Block> DEAD_BITTER_CORAL_FAN = registerBlock("dead_bitter_coral_fan",
            () -> new BaseCoralFanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().instabreak()));
    public static final DeferredHolder<Block, Block> DEAD_BITTER_CORAL_WALL_FAN = registerBlock("dead_bitter_coral_wall_fan",
            () -> new BaseCoralWallFanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().instabreak().dropsLike(DEAD_BITTER_CORAL_FAN.get())));
    public static final DeferredHolder<Block, Block> BITTER_CORAL_BLOCK = registerBlock("bitter_coral_block",
            () -> new CoralBlock(DEAD_BITTER_CORAL_BLOCK.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.TUBE_CORAL_BLOCK)));
    public static final DeferredHolder<Block, Block> BITTER_CORAL = registerBlock("bitter_coral",
            () -> new CoralPlantBlock(DEAD_BITTER_CORAL.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.TUBE_CORAL).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> BITTER_CORAL_FAN = registerBlock("bitter_coral_fan",
            () -> new CoralFanBlock(DEAD_BITTER_CORAL_FAN.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.TUBE_CORAL_FAN).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> BITTER_CORAL_WALL_FAN = registerBlock("bitter_coral_wall_fan",
            () -> new CoralWallFanBlock(DEAD_BITTER_CORAL_WALL_FAN.get(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS).dropsLike(BITTER_CORAL_FAN.get()).pushReaction(PushReaction.DESTROY)));

    public static final DeferredHolder<Block, Block> SWEEDS = registerBlock("sweeds",
            () -> new Sweeds(BlockBehaviour.Properties.ofFullCopy(Blocks.SUGAR_CANE).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> CROCKTUS = registerBlock("crocktus",
            () -> new CrocktusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CACTUS).noOcclusion().noCollission()));

    public static final DeferredHolder<Block, Block> PINK_CHARMIL_GRASS = registerBlock("pink_charmil_grass",
            () -> new ModTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> BLUE_CHARMIL_GRASS = registerBlock("blue_charmil_grass",
            () -> new ModTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> PALEGRASS = registerBlock("palegrass",
            () -> new ModTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> LUMINUM = registerBlock("luminum",
            () -> new RotatedTallFlowerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SUNFLOWER).noOcclusion().noCollission()){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 8;
                }
            });
    public static final DeferredHolder<Block, Block> FROZEN_GRASS = registerBlock("frozen_grass",
            () -> new FrozenTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).noOcclusion().noCollission()));

    public static final DeferredHolder<Block, Block> PINK_BLOOM = registerBlock("pink_bloom",
            () -> new FlowerBlock(MobEffects.MOVEMENT_SPEED, 20,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> POTTED_PINK_BLOOM = BlocksRegister.register("potted_pink_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.PINK_BLOOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));
    public static final DeferredHolder<Block, Block> BLUE_BLOOM = registerBlock("blue_bloom",
            () -> new FlowerBlock(MobEffects.DAMAGE_BOOST, 20,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> POTTED_BLUE_BLOOM = BlocksRegister.register("potted_blue_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.PINK_BLOOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));
    public static final DeferredHolder<Block, Block> ABYSSAL_BLOOM = registerBlock("abyssal_bloom",
            () -> new FlowerBlock(MobEffects.WITHER, 10,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> POTTED_ABYSSAL_BLOOM = BlocksRegister.register("potted_abyssal_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.ABYSSAL_BLOOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));
    public static final DeferredHolder<Block, Block> FROZEN_BLOOM = registerBlock("frozen_bloom",
            () -> new FrozenFlowerBlock(MobEffects.SATURATION, 1,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> POTTED_FROZEN_BLOOM = BlocksRegister.register("potted_frozen_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.FROZEN_BLOOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final DeferredHolder<Block, Block> ORIGAMI_FERN = registerBlock("origami_fern",
            () -> new SandyFlowerBlock(MobEffects.JUMP, 20,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> POTTED_ORIGAMI_FERN = BlocksRegister.register("potted_origami_fern",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.ORIGAMI_FERN,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final DeferredHolder<Block, Block> LOVESHROOM = registerBlock("loveshroom",
            () -> new MushroomBlock(ModConfiguredFeatures.GIANT_LOVESHROOM_KEY,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM).noOcclusion().noCollission()));
    public static final DeferredHolder<Block, Block> POTTED_LOVESHROOM = BlocksRegister.register("potted_loveshroom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.LOVESHROOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()));
    public static final DeferredHolder<Block, Block> LOVESHROOM_BLOCK = registerBlock("loveshroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM_BLOCK)));

    public static final DeferredHolder<Block, Block> GLOWSHROOM = registerBlock("glowshroom",
            () -> new MushroomBlock(ModConfiguredFeatures.GIANT_GLOWSHROOM_KEY,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM).noOcclusion().noCollission()){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }
            });
    public static final DeferredHolder<Block, Block> POTTED_GLOWSHROOM = BlocksRegister.register("potted_glowshroom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.GLOWSHROOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()){

                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }
            });
    public static final DeferredHolder<Block, Block> GLOWSHROOM_BLOCK = registerBlock("glowshroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM_BLOCK)){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 8;
                }
            });

    public static final DeferredHolder<Block, Block> PALESHROOM = registerBlock("paleshroom",
            () -> new MushroomBlock(ModConfiguredFeatures.GIANT_GLOWSHROOM_KEY,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM).noOcclusion().noCollission()){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }
            });
    public static final DeferredHolder<Block, Block> POTTED_PALESHROOM = BlocksRegister.register("potted_paleshroom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.PALESHROOM,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_PINK_TULIP).noOcclusion()){

                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }
            });
    public static final DeferredHolder<Block, Block> PALESHROOM_BLOCK = registerBlock("paleshroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM_BLOCK)){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 8;
                }
            });

    //Fairy minerals
    public static final DeferredHolder<Block, Block> GROWABLE_AMETHYST_CLUSTER = registerBlock("growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_GROWABLE_AMETHYST_CLUSTER = registerBlock("large_growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_GROWABLE_AMETHYST_CLUSTER = registerBlock("medium_growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_GROWABLE_AMETHYST_CLUSTER = registerBlock("small_growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> IRON_CRYSTAL_CLUSTER = registerBlock("iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_IRON_CRYSTAL_CLUSTER = registerBlock("large_iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_IRON_CRYSTAL_CLUSTER = registerBlock("medium_iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_IRON_CRYSTAL_CLUSTER = registerBlock("small_iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> COPPER_CRYSTAL_CLUSTER = registerBlock("copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_COPPER_CRYSTAL_CLUSTER = registerBlock("large_copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_COPPER_CRYSTAL_CLUSTER = registerBlock("medium_copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_COPPER_CRYSTAL_CLUSTER = registerBlock("small_copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> GOLD_CRYSTAL_CLUSTER = registerBlock("gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_GOLD_CRYSTAL_CLUSTER = registerBlock("large_gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_GOLD_CRYSTAL_CLUSTER = registerBlock("medium_gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_GOLD_CRYSTAL_CLUSTER = registerBlock("small_gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> REDSTONE_CRYSTAL_CLUSTER = registerBlock("redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_REDSTONE_CRYSTAL_CLUSTER = registerBlock("large_redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_REDSTONE_CRYSTAL_CLUSTER = registerBlock("medium_redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_REDSTONE_CRYSTAL_CLUSTER = registerBlock("small_redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> LAPIS_CRYSTAL_CLUSTER = registerBlock("lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_LAPIS_CRYSTAL_CLUSTER = registerBlock("large_lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_LAPIS_CRYSTAL_CLUSTER = registerBlock("medium_lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_LAPIS_CRYSTAL_CLUSTER = registerBlock("small_lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> DIAMOND_CRYSTAL_CLUSTER = registerBlock("diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_DIAMOND_CRYSTAL_CLUSTER = registerBlock("large_diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_DIAMOND_CRYSTAL_CLUSTER = registerBlock("medium_diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_DIAMOND_CRYSTAL_CLUSTER = registerBlock("small_diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> EMERALD_CRYSTAL_CLUSTER = registerBlock("emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_EMERALD_CRYSTAL_CLUSTER = registerBlock("large_emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_EMERALD_CRYSTAL_CLUSTER = registerBlock("medium_emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_EMERALD_CRYSTAL_CLUSTER = registerBlock("small_emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> DEBRIS_CRYSTAL_CLUSTER = registerBlock("debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_DEBRIS_CRYSTAL_CLUSTER = registerBlock("large_debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_DEBRIS_CRYSTAL_CLUSTER = registerBlock("medium_debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_DEBRIS_CRYSTAL_CLUSTER = registerBlock("small_debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> QUARTZ_CRYSTAL_CLUSTER = registerBlock("quartz_crystal_cluster",
            () -> new ModGrowableMineral((byte) 9, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_QUARTZ_CRYSTAL_CLUSTER = registerBlock("large_quartz_crystal_cluster",
            () -> new ModGrowableMineral((byte) 9, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_QUARTZ_CRYSTAL_CLUSTER = registerBlock("medium_quartz_crystal_cluster",
            () -> new ModGrowableMineral((byte) 9, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_QUARTZ_CRYSTAL_CLUSTER = registerBlock("small_quartz_crystal_cluster",
            () -> new ModGrowableMineral((byte) 9, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final DeferredHolder<Block, Block> GLOWSTONE_CRYSTAL_CLUSTER = registerBlock("glowstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 10, 3,7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> LARGE_GLOWSTONE_CRYSTAL_CLUSTER = registerBlock("large_glowstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 10, 2,5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> MEDIUM_GLOWSTONE_CRYSTAL_CLUSTER = registerBlock("medium_glowstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 10, 1,4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final DeferredHolder<Block, Block> SMALL_GLOWSTONE_CRYSTAL_CLUSTER = registerBlock("small_glowstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 10, 0,3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    //Block entities
    public static final DeferredHolder<Block, Block> FAIRY_OFFERING_BENCH = registerBlock("fairy_offering_bench",
            () -> new FairyOfferingBenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> GRINDING_WHEEL = registerBlock("grinding_wheel",
            () -> new GrindingWheelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()));
    public static final DeferredHolder<Block, Block> THREAD_REELER = registerBlock("thread_reeler",
            () -> new ThreadReelerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> FAIRY_COLLECTION_TRAY = registerBlock("fairy_collection_tray",
            () -> new FairyCollectionTrayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> FAIRY_INFUSION_BENCH = registerBlock("fairy_infusion_bench",
            () -> new FairyInfusionBenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> MAGIC_CRYSTALIZER = registerBlock("magic_crystalizer",
            () -> new MagicCrystalizerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> FUME_DISTILLERY = registerBlock("fume_distillery",
            () -> new FumeDistilleryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> FUME_SPREADER = registerBlock("fume_spreader",
            () -> new FumeSpreaderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> INFUSION_PEDESTAL = registerBlock("infusion_pedestal",
            () -> new InfusionPedestal(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final DeferredHolder<Block, Block> FAIRY_POWER_RELAY = registerBlock("fairy_power_relay",
            () -> new FairyPowerRelayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> INFUSION_ALTAR_BASIC = registerBlock("infusion_altar_basic",
            () -> new InfusionAltarBasic(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> INFUSION_ALTAR_ADVANCED = registerBlock("infusion_altar_advanced",
            () -> new InfusionAltarAdvanced(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> GIANT_COCOON = registerBlock("giant_cocoon",
            () -> new GiantCocoonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final DeferredHolder<Block, Block> MIXER_BLOCK = registerBlock("mixer_block",
            () -> new MixerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final DeferredHolder<Block, Block> CLOCKWORK_SYRUP_EXTRACTOR = registerBlock("clockwork_syrup_extractor",
            () -> new ClockworkSyrupExtractorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> CLOCKWORK_GEYSER_COLLECTOR = registerBlock("clockwork_geyser_collector",
            () -> new ClockworkGeyserCollectorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> CLOCKWORK_POWDERED_SUGAR_COLLECTOR = registerBlock("clockwork_powdered_sugar_collector",
            () -> new ClockworkPowderedSugarCollectorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> CLOCKWORK_FISHER = registerBlock("clockwork_fisher",
            () -> new ClockworkFisherBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> CLOCKWORK_WINDER = registerBlock("clockwork_winder",
            () -> new ClockworkWinderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> CLOCKWORK_CRAFTER = registerBlock("clockwork_crafter",
            () -> new ClockworkCrafterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredHolder<Block, Block> CLOCKWORK_FAIRY_TERMINAL = registerBlock("clockwork_fairy_terminal",
            () -> new ClockworkFairyTerminalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final DeferredHolder<Block, Block> CLOCKWORK_MAIDEN_TERMINAL = registerBlock("clockwork_maiden_terminal",
            () -> new ClockworkMaidenTerminalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    //Beacon
    public static final DeferredHolder<Block, Block> PRISMATIC_BEACON = registerBlock("prismatic_beacon",
            () -> new PrismaticBeaconBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> PRISMATIC_BEACON_BASE = registerBlock("prismatic_beacon_base",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_HEALTH_BOOST_MODIFIER = registerBlock("beacon_health_boost_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_SPEED_MODIFIER = registerBlock("beacon_speed_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_HASTE_MODIFIER = registerBlock("beacon_haste_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_STRENGTH_MODIFIER = registerBlock("beacon_strength_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_JUMP_BOOST_MODIFIER = registerBlock("beacon_jump_boost_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_REGENERATION_MODIFIER = registerBlock("beacon_regeneration_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_RESISTANCE_MODIFIER = registerBlock("beacon_resistance_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_FIRE_RESISTANCE_MODIFIER = registerBlock("beacon_fire_resistance_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredHolder<Block, Block> BEACON_WATER_BREATHING_MODIFIER = registerBlock("beacon_water_breathing_modifier",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredHolder<Block, LiquidBlock> HOT_CHOCOLATE_BLOCK = registerBlock("hot_chocolate_block",
            () -> new ChocolateFluidBlock(ModFluids.SOURCE_HOT_CHOCOLATE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable().noOcclusion().noCollission().randomTicks()));


    public static final DeferredHolder<Block, Block> SYRUP_EXTRACTOR = registerBlock("syrup_extractor",
            () -> new SyrupExtractorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block){
        DeferredHolder<Block, T> toRet = BlocksRegister.register(name, block);
        registerBlockItem(name, toRet);
        return toRet;
    }

    private static <T extends Block> DeferredHolder<Item, Item> registerBlockItem(String name, DeferredHolder<Block, T> block) {
        return ModItems.ItemsRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BlocksRegister.register(eventBus);
    }
}
