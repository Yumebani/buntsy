package net.sophiebun.buntsy.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BuntsyMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.COCOON);
        simpleItem(ModItems.SILK);
        simpleItem(ModItems.SILK_SPOOL);
        simpleItem(ModItems.SILK_FABRIC);
        simpleItem(ModItems.MOLTED_MOTH_WINGS);
        simpleItem(ModItems.MOTH_WING_THREAD);
        simpleItem(ModItems.TOUGH_SILK_FABRIC);
        simpleItem(ModItems.SILKY_INGOT);
        simpleItem(ModItems.SILKY_NUGGET);
        simpleItem(ModItems.SILKY_CRYSTAL);
        simpleItem(ModItems.FAIRY_DUST);
        simpleItem(ModItems.GENTLIT_SYRUP);
        simpleItem(ModItems.SUGAR_BOWL);
        simpleItem(ModItems.SYRUPY_MIXTURE_BOWL);

        simpleItem(ModItems.STRAWBERRY);
        simpleItem(ModItems.BOWL_OF_CARAMEL);
        simpleItem(ModItems.CARAMEL_STRAWBERRIES);
        simpleItem(ModItems.GOLDEN_STRAWBERRY);
        simpleItem(ModItems.BOWL_OF_ROCKCANDY);

        simpleItem(ModItems.FAIRY_IN_A_BOTTLE);

        evenSimplerBlockItem(ModBlocks.GENTLIT_STAIRS);
        evenSimplerBlockItem(ModBlocks.GENTLIT_SLAB);
        evenSimplerBlockItem(ModBlocks.GENTLIT_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.GENTLIT_FENCE_GATE);

        fenceItem(ModBlocks.GENTLIT_FENCE, ModBlocks.GENTLIT_PLANKS);
        buttonItem(ModBlocks.GENTLIT_BUTTON, ModBlocks.GENTLIT_PLANKS);
        trapdoorItem(ModBlocks.GENTLIT_TRAPDOOR);
    }

    private ItemModelBuilder simpleItem (RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"item/" + item.getId().getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(BuntsyMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  new ResourceLocation(BuntsyMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(BuntsyMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(BuntsyMod.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }
}
