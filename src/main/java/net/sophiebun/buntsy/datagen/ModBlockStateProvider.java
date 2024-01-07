package net.sophiebun.buntsy.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BuntsyMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        stairsBlock((StairBlock) ModBlocks.GENTLIT_STAIRS.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        slabBlock((SlabBlock) ModBlocks.GENTLIT_SLAB.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.GENTLIT_BUTTON.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.GENTLIT_PRESSURE_PLATE.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.GENTLIT_FENCE.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.GENTLIT_FENCE_GATE.get(), blockTexture(ModBlocks.GENTLIT_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.GENTLIT_DOOR.get(), modLoc("block/gentlit_door_bottom"), modLoc("block/gentlit_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.GENTLIT_TRAPDOOR.get(), modLoc("block/gentlit_trapdoor"), true, "cutout");
    }
}
