package net.sophiebun.buntsy.blocks.custom.plants;

import net.minecraft.world.level.block.*;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class CottonvineBlock extends KelpBlock {
    public CottonvineBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected Block getBodyBlock() {
        return ModBlocks.COTTON_VINE_PLANT.get();
    }
}
