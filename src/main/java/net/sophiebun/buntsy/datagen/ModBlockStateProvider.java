package net.sophiebun.buntsy.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sophiebun.buntsy.BuntsyMod;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BuntsyMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
