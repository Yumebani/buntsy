package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.world.level.block.*;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class CottonvinePlantBlock extends KelpPlantBlock {
    public CottonvinePlantBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModBlocks.COTTON_VINE.get();
    }
}
