package net.sophiebun.buntsy.datagen;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.HootnipCrop;
import net.sophiebun.buntsy.blocks.custom.StrawberryCrop;
import net.sophiebun.buntsy.blocks.custom.SyrupExtractorBlock;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;

import java.util.List;
import java.util.Properties;

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

        //Adding soil models
        grassBlock(ModBlocks.PINK_FLUF_CHARMIL_SOIL, ModBlocks.CHARMIL_SOIL.getId().getPath());
        blockWithItem(ModBlocks.CHARMIL_SOIL);
        farmlandBlock(ModBlocks.CHARMIL_FARMLAND, ModBlocks.CHARMIL_SOIL);

        //Crops
        simpleCrossBlock(ModBlocks.WILD_STRAWBERRY);
        strawberryCrop(ModBlocks.STRAWBERRY_CROP);
        wildHootnipBlock(ModBlocks.WILD_HOOTNIP);
        hootnipCrop(ModBlocks.HOOTNIP_CROP);


        //Adding plants
        variedGrass(ModBlocks.PINK_CHARMIL_GRASS); //Item added in item model gen
        variedGrass(ModBlocks.BLUE_CHARMIL_GRASS); //Item added in item model gen
        simpleCrossBlock(ModBlocks.PINK_BLOOM); //Item added in item model gen
        simpleCrossBlock(ModBlocks.BLUE_BLOOM); //Item added in item model gen
        simpleCrossBlock(ModBlocks.LOVESHROOM); //Item added in item model gen
        simpleCrossBlock(ModBlocks.GLOWSHROOM); //Item added in item model gen

        //Adding potted plants
        pottedPlant(ModBlocks.POTTED_PINK_BLOOM, ModBlocks.PINK_BLOOM);
        pottedPlant(ModBlocks.POTTED_BLUE_BLOOM, ModBlocks.BLUE_BLOOM);
        pottedPlant(ModBlocks.POTTED_GENTLIT_SAPLING, ModBlocks.GENTLIT_SAPLING);
        pottedPlant(ModBlocks.POTTED_BRAVOT_SAPLING, ModBlocks.BRAVOT_SAPLING);
        pottedPlant(ModBlocks.POTTED_LOVESHROOM, ModBlocks.LOVESHROOM);
        pottedPlant(ModBlocks.POTTED_GLOWSHROOM, ModBlocks.GLOWSHROOM);

        //MushroomBlocks
        mushroomBlock(ModBlocks.LOVESHROOM_BLOCK); //Item added in item model gen
        mushroomBlock(ModBlocks.GLOWSHROOM_BLOCK); //Item added in item model gen

        //Minerals
        for (List<RegistryObject<Block>> minerals : ModGrowableMineral.GROWABLE_MINERAL_STAGES){
            for (RegistryObject<Block> mineral : minerals){
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

        //Other blocks
        syrupExtractorBlock(ModBlocks.SYRUP_EXTRACTOR);
        simpleBlockItem(ModBlocks.SYRUP_EXTRACTOR.get(), new ModelFile.UncheckedModelFile(modLoc("block/syrup_extractor_stage0")));
    }

    private void hootnipCrop(RegistryObject<Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState ->
                ConfiguredModel.builder().modelFile(models().crop(block.getId().getPath(),
                        new ResourceLocation(BuntsyMod.MODID, "textures/block/" + block.getId().getPath() + "_stage" +
                                blockState.getValue(HootnipCrop.AGE)))).build()
        );
    }

    private void wildHootnipBlock(RegistryObject<Block> block){
        getVariantBuilder(block.get()).partialState()
                .with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                .modelForState().modelFile(getCrossModel(block, "_top")).addModel().partialState()
                .with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                .modelForState().modelFile(getCrossModel(block, "_bottom")).addModel().partialState();
    }

    private void syrupExtractorBlock(RegistryObject<Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState ->
                ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile("block/syrup_extractor_stage" + blockState.getValue(SyrupExtractorBlock.LEVEL)))
                        .rotationY(Math.round(blockState.getValue(HorizontalDirectionalBlock.FACING).getRotation().y))
                        .build());
    }

    private void XZdirectionalBlock(RegistryObject<Block> block, ModelFile modelFile){
        getVariantBuilder(block.get()).partialState()
                .with(ModGrowableMineral.FACING, Direction.EAST)
                .modelForState().modelFile(modelFile).rotationY(90).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.NORTH)
                .modelForState().modelFile(modelFile).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.SOUTH)
                .modelForState().modelFile(modelFile).rotationY(180).addModel().partialState()
                .with(ModGrowableMineral.FACING, Direction.WEST)
                .modelForState().modelFile(modelFile).rotationY(270).addModel().partialState();
    }

    private void strawberryCrop(RegistryObject<Block> block){
        getVariantBuilder(block.get()).forAllStates(blockState ->
           ConfiguredModel.builder().modelFile(models().crop(block.getId().getPath(),
                   new ResourceLocation(BuntsyMod.MODID, "textures/block/" + block.getId().getPath() + "_stage" +
                           blockState.getValue(StrawberryCrop.AGE)))).build()
        );
    }

    private void farmlandBlock(RegistryObject<Block> block, RegistryObject<Block> soil) {
        BlockModelBuilder farmland = farmlandModel(block, soil);
        BlockModelBuilder moistFarmland = moistFarmlandModel(block, soil);

        getVariantBuilder(block.get()).forAllStates(blockState -> {
            if (blockState.getValue(FarmBlock.MOISTURE) == 7){
                return ConfiguredModel.builder().modelFile(moistFarmland).build();
            }
            return ConfiguredModel.builder().modelFile(farmland).build();
        });
    }

    private BlockModelBuilder farmlandModel(RegistryObject<Block> block, RegistryObject<Block> soil){
        return models().withExistingParent(block.getId().getPath(), mcLoc("block/template_farmland"))
                .texture("dirt", new ResourceLocation(BuntsyMod.MODID, "block/" + soil.getId().getPath()))
                .texture("top", new ResourceLocation(BuntsyMod.MODID, "block/" + block.getId().getPath()));
    }
    private BlockModelBuilder moistFarmlandModel(RegistryObject<Block> block, RegistryObject<Block> soil){
        return models().withExistingParent(block.getId().getPath(), mcLoc("block/template_farmland"))
                .texture("dirt", new ResourceLocation(BuntsyMod.MODID, "block/" + soil.getId().getPath()))
                .texture("top", new ResourceLocation(BuntsyMod.MODID, "block/" + block.getId().getPath() + "_moist"));
    }

    private void pottedPlant(RegistryObject<Block> registryObject, RegistryObject<Block> plant){
        simpleBlockWithItem(registryObject.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(registryObject.get()).getPath()
                        , new ResourceLocation("flower_pot_cross"), "plant",
                        blockTexture(plant.get())).renderType("cutout"));
    }

    private void variedGrass(RegistryObject<Block> registryObject){
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

    private void crystalBlock(RegistryObject<Block> registryObject){
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

    private BlockModelBuilder getCrossModel(RegistryObject<Block> registryObject, String var){
        return models().cross(ForgeRegistries.BLOCKS.getKey(registryObject.get()).getPath() + var,
                new ResourceLocation(BuntsyMod.MODID,"block/" + registryObject.getId().getPath() + var)).renderType("cutout");
    }

    private void mushroomBlock(RegistryObject<Block> registryObject){
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

    private BlockModelBuilder mushroomSide(RegistryObject<Block> registryObject, boolean isInside){
        String inside = isInside ? "_inside" : "";
        return models().singleTexture(ForgeRegistries.BLOCKS.getKey(registryObject.get()).getPath() + inside,
                mcLoc("minecraft:block/template_single_face"),
                new ResourceLocation(BuntsyMod.MODID, "block/" + registryObject.getId().getPath() + inside));
    }

    private void grassBlock(RegistryObject<Block> registryObject, String blockBottomPath) {
        simpleBlockWithItem(registryObject.get(),
                models().cubeBottomTop(ForgeRegistries.BLOCKS.getKey(registryObject.get()).getPath(),
                        new ResourceLocation(BuntsyMod.MODID, "block/" + registryObject.getId().getPath() + "_side"),
                        new ResourceLocation(BuntsyMod.MODID, "block/" + blockBottomPath),
                        new ResourceLocation(BuntsyMod.MODID, "block/" + registryObject.getId().getPath() + "_top")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void simpleCrossBlock(RegistryObject<Block> blockRegistry){
        simpleBlock(blockRegistry.get(),getCrossModel(blockRegistry, ""));
    }
}
