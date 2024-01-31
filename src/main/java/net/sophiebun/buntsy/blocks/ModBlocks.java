package net.sophiebun.buntsy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.custom.*;
import net.sophiebun.buntsy.blocks.custom.entityblocks.*;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.worldgen.ModConfiguredFeatures;
import net.sophiebun.buntsy.worldgen.tree.BravotTreeGrower;
import net.sophiebun.buntsy.worldgen.tree.GentlitTreeGrower;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BlocksRegister =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BuntsyMod.MODID);

    public static final RegistryObject<Block> GENTLIT_LEAVES = registerBlock("gentlit_leaves",
            () -> new ModLeaves(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> GENTLIT_SAPLING = registerBlock("gentlit_sapling",
            () -> new SaplingBlock(new GentlitTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> POTTED_GENTLIT_SAPLING = BlocksRegister.register("potted_gentlit_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.GENTLIT_SAPLING,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final RegistryObject<Block> GENTLIT_LOG = registerBlock("gentlit_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_GENTLIT_LOG = registerBlock("stripped_gentlit_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> GENTLIT_WOOD = registerBlock("gentlit_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_GENTLIT_WOOD = registerBlock("stripped_gentlit_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> GENTLIT_PLANKS = registerBlock("gentlit_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> GENTLIT_STAIRS = registerBlock("gentlit_stairs",
            () -> new ModWoodStairs(() -> ModBlocks.GENTLIT_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> GENTLIT_SLAB = registerBlock("gentlit_slab",
            () -> new ModWoodSlab(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> GENTLIT_BUTTON = registerBlock("gentlit_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 10, true));
    public static final RegistryObject<Block> GENTLIT_PRESSURE_PLATE = registerBlock("gentlit_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE),
                    BlockSetType.OAK));
    public static final RegistryObject<Block> GENTLIT_FENCE = registerBlock("gentlit_fence",
            () -> new ModWoodFence(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> GENTLIT_FENCE_GATE = registerBlock("gentlit_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> GENTLIT_DOOR = registerBlock("gentlit_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noCollission(), BlockSetType.OAK));
    public static final RegistryObject<Block> GENTLIT_TRAPDOOR = registerBlock("gentlit_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noCollission(), BlockSetType.OAK));

    public static final RegistryObject<Block> BRAVOT_LEAVES = registerBlock("bravot_leaves",
            () -> new ModLeaves(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> BRAVOT_SAPLING = registerBlock("bravot_sapling",
            () -> new SaplingBlock(new BravotTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> POTTED_BRAVOT_SAPLING = BlocksRegister.register("potted_bravot_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.BRAVOT_SAPLING,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final RegistryObject<Block> BRAVOT_LOG = registerBlock("bravot_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_BRAVOT_LOG = registerBlock("stripped_bravot_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> BRAVOT_WOOD = registerBlock("bravot_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_BRAVOT_WOOD = registerBlock("stripped_bravot_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> BRAVOT_PLANKS = registerBlock("bravot_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BRAVOT_STAIRS = registerBlock("bravot_stairs",
            () -> new ModWoodStairs(() -> ModBlocks.BRAVOT_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> BRAVOT_SLAB = registerBlock("bravot_slab",
            () -> new ModWoodSlab(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> BRAVOT_BUTTON = registerBlock("bravot_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 10, true));
    public static final RegistryObject<Block> BRAVOT_PRESSURE_PLATE = registerBlock("bravot_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE),
                    BlockSetType.OAK));
    public static final RegistryObject<Block> BRAVOT_FENCE = registerBlock("bravot_fence",
            () -> new ModWoodFence(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BRAVOT_FENCE_GATE = registerBlock("bravot_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> BRAVOT_DOOR = registerBlock("bravot_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noCollission(), BlockSetType.OAK));
    public static final RegistryObject<Block> BRAVOT_TRAPDOOR = registerBlock("bravot_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noCollission(), BlockSetType.OAK));

    //Biome ground blocks
    public static final RegistryObject<Block> PINK_FLUF_CHARMIL_SOIL = registerBlock("pink_fluf_charmil_soil", //MAKE FLUF SPIRAL DOWNWWARDS
            () -> new TillableCharmilSoil(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).sound(SoundType.MOSS)));
    public static final RegistryObject<Block> CHARMIL_SOIL = registerBlock("charmil_soil",
            () -> new TillableCharmilSoil(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.MOSS)));
    public static final RegistryObject<Block> CHARMIL_FARMLAND = registerBlock("charmil_farmland",
            () -> new CharmilFarmland(BlockBehaviour.Properties.copy(Blocks.FARMLAND).sound(SoundType.MOSS)));

    //Crops
    public static final RegistryObject<Block> WILD_STRAWBERRY = registerBlock("wild_strawberry",
            () -> new TallGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().noCollission()));
    public static final RegistryObject<Block> STRAWBERRY_CROP = registerBlock("strawberry_crop",
            () -> new StrawberryCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));
    public static final RegistryObject<Block> WILD_HOOTNIP = registerBlock("wild_hootnip",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().noCollission()));

    //Biome plants
    public static final RegistryObject<Block> PINK_CHARMIL_GRASS = registerBlock("pink_charmil_grass",
            () -> new TallGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().noCollission()));
    public static final RegistryObject<Block> BLUE_CHARMIL_GRASS = registerBlock("blue_charmil_grass",
            () -> new TallGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noOcclusion().noCollission()));


    public static final RegistryObject<Block> PINK_BLOOM = registerBlock("pink_bloom",
            () -> new FlowerBlock(() -> MobEffects.DIG_SPEED, 20,
                    BlockBehaviour.Properties.copy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final RegistryObject<Block> POTTED_PINK_BLOOM = BlocksRegister.register("potted_pink_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.PINK_BLOOM,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_PINK_TULIP).noOcclusion()));
    public static final RegistryObject<Block> BLUE_BLOOM = registerBlock("blue_bloom",
            () -> new FlowerBlock(() -> MobEffects.DIG_SPEED, 20,
                    BlockBehaviour.Properties.copy(Blocks.PINK_TULIP).noOcclusion().noCollission()));
    public static final RegistryObject<Block> POTTED_BLUE_BLOOM = BlocksRegister.register("potted_blue_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.PINK_BLOOM,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_PINK_TULIP).noOcclusion()));

    public static final RegistryObject<Block> LOVESHROOM = registerBlock("loveshroom",
            () -> new MushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).noOcclusion().noCollission(),
                    ModConfiguredFeatures.GIANT_LOVESHROOM_KEY));
    public static final RegistryObject<Block> POTTED_LOVESHROOM = BlocksRegister.register("potted_loveshroom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.LOVESHROOM,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_PINK_TULIP).noOcclusion()));
    public static final RegistryObject<Block> LOVESHROOM_BLOCK = registerBlock("loveshroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK)));

    public static final RegistryObject<Block> GLOWSHROOM = registerBlock("glowshroom",
            () -> new MushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).noOcclusion().noCollission(),
                    ModConfiguredFeatures.GIANT_GLOWSHROOM_KEY){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> POTTED_GLOWSHROOM = BlocksRegister.register("potted_glowshroom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.GLOWSHROOM,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_PINK_TULIP).noOcclusion()){

                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> GLOWSHROOM_BLOCK = registerBlock("glowshroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK)){
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 8;
                }
            });

    //Fairy minerals
    public static final RegistryObject<Block> GROWABLE_AMETHYST_CLUSTER = registerBlock("growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_GROWABLE_AMETHYST_CLUSTER = registerBlock("large_growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_GROWABLE_AMETHYST_CLUSTER = registerBlock("medium_growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_GROWABLE_AMETHYST_CLUSTER = registerBlock("small_growable_amethyst_cluster",
            () -> new ModGrowableMineral((byte) 0, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> IRON_CRYSTAL_CLUSTER = registerBlock("iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_IRON_CRYSTAL_CLUSTER = registerBlock("large_iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_IRON_CRYSTAL_CLUSTER = registerBlock("medium_iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_IRON_CRYSTAL_CLUSTER = registerBlock("small_iron_crystal_cluster",
            () -> new ModGrowableMineral((byte) 1, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> COPPER_CRYSTAL_CLUSTER = registerBlock("copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_COPPER_CRYSTAL_CLUSTER = registerBlock("large_copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_COPPER_CRYSTAL_CLUSTER = registerBlock("medium_copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_COPPER_CRYSTAL_CLUSTER = registerBlock("small_copper_crystal_cluster",
            () -> new ModGrowableMineral((byte) 2, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> GOLD_CRYSTAL_CLUSTER = registerBlock("gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_GOLD_CRYSTAL_CLUSTER = registerBlock("large_gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_GOLD_CRYSTAL_CLUSTER = registerBlock("medium_gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_GOLD_CRYSTAL_CLUSTER = registerBlock("small_gold_crystal_cluster",
            () -> new ModGrowableMineral((byte) 3, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> REDSTONE_CRYSTAL_CLUSTER = registerBlock("redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_REDSTONE_CRYSTAL_CLUSTER = registerBlock("large_redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_REDSTONE_CRYSTAL_CLUSTER = registerBlock("medium_redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_REDSTONE_CRYSTAL_CLUSTER = registerBlock("small_redstone_crystal_cluster",
            () -> new ModGrowableMineral((byte) 4, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> LAPIS_CRYSTAL_CLUSTER = registerBlock("lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_LAPIS_CRYSTAL_CLUSTER = registerBlock("large_lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_LAPIS_CRYSTAL_CLUSTER = registerBlock("medium_lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_LAPIS_CRYSTAL_CLUSTER = registerBlock("small_lapis_crystal_cluster",
            () -> new ModGrowableMineral((byte) 5, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> DIAMOND_CRYSTAL_CLUSTER = registerBlock("diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_DIAMOND_CRYSTAL_CLUSTER = registerBlock("large_diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_DIAMOND_CRYSTAL_CLUSTER = registerBlock("medium_diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_DIAMOND_CRYSTAL_CLUSTER = registerBlock("small_diamond_crystal_cluster",
            () -> new ModGrowableMineral((byte) 6, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> EMERALD_CRYSTAL_CLUSTER = registerBlock("emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_EMERALD_CRYSTAL_CLUSTER = registerBlock("large_emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_EMERALD_CRYSTAL_CLUSTER = registerBlock("medium_emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_EMERALD_CRYSTAL_CLUSTER = registerBlock("small_emerald_crystal_cluster",
            () -> new ModGrowableMineral((byte) 7, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    public static final RegistryObject<Block> DEBRIS_CRYSTAL_CLUSTER = registerBlock("debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 3,7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> LARGE_DEBRIS_CRYSTAL_CLUSTER = registerBlock("large_debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 2,5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> MEDIUM_DEBRIS_CRYSTAL_CLUSTER = registerBlock("medium_debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 1,4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
    public static final RegistryObject<Block> SMALL_DEBRIS_CRYSTAL_CLUSTER = registerBlock("small_debris_crystal_cluster",
            () -> new ModGrowableMineral((byte) 8, 0,3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion()));

    //Block entities
    public static final RegistryObject<Block> FAIRY_OFFERING_BENCH = registerBlock("fairy_offering_bench",
            () -> new FairyOfferingBenchBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistryObject<Block> GRINDING_WHEEL = registerBlock("grinding_wheel",
            () -> new GrindingWheelBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));
    public static final RegistryObject<Block> THREAD_REELER = registerBlock("thread_reeler",
            () -> new ThreadReelerBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistryObject<Block> FAIRY_COLLECTION_TRAY = registerBlock("fairy_collection_tray",
            () -> new FairyCollectionTrayBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistryObject<Block> FAIRY_INFUSION_BENCH = registerBlock("fairy_infusion_bench",
            () -> new FairyInfusionBenchBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistryObject<Block> MAGIC_CRYSTALIZER = registerBlock("magic_crystalizer",
            () -> new MagicCrystalizerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toRet = BlocksRegister.register(name, block);
        registerBlockItem(name, toRet);
        return toRet;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ItemsRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BlocksRegister.register(eventBus);
    }
}
