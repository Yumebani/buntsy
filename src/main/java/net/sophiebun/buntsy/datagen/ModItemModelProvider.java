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
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.item.ModItems;

import java.util.List;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BuntsyMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(ModItems.FAIRY_TALE_BOOK);

        simpleItem(ModItems.SILK_SPOOL);
        simpleItem(ModItems.EMPTY_CATALYST);
        simpleItem(ModItems.FAIRY_POWER_RECEPTOR);
        simpleItem(ModItems.FAIRY_POWER_EMITTER);
        simpleItem(ModItems.HOOTCAT_FEATHER);
        simpleItem(ModItems.HOOTCAT_PLUME);
        simpleItem(ModItems.PHELINIX_FEATHER);
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
        simpleItem(ModItems.CLOCKWORK_SCRAP);
        simpleItem(ModItems.CLOCKWORK_SCRAP_CLUMP);
        simpleItem(ModItems.CLOCKWORK_BRASS);
        simpleItem(ModItems.CLOCKWORK_GEAR);
        simpleItem(ModItems.CLOCKWORK_PROCESSOR);
        simpleItem(ModItems.CLOCKWORK_MODIFICATION);
        simpleItem(ModItems.SIMPLE_CLOCKWORK_UNIT);
        simpleItem(ModItems.INTRICATE_CLOCKWORK_UNIT);
        simpleItem(ModItems.COMPLEX_CLOCKWORK_UNIT);
        simpleItem(ModItems.FAIRY_DUST);
        simpleItem(ModItems.GENTLIT_SYRUP);
        simpleItem(ModItems.SUGAR_BOWL);
        simpleItem(ModItems.SYRUPY_MIXTURE_BOWL);
        simpleItem(ModItems.HOOTNIP);
        simpleItem(ModItems.BLAZING_HOOTNIP);
        simpleItem(ModItems.GROUND_HOOTNIP);
        simpleItem(ModItems.HOOTNIP_CEREAL);
        simpleItem(ModItems.SWICE_SHARDS);
        simpleItem(ModItems.COLD_POWDERED_SUGAR);
        simpleItem(ModItems.CHOCOLATE_FLAKES);
        simpleItem(ModItems.STRAWBERRY_SEEDS);
        simpleItem(ModItems.HOOTNIP_SEEDS);
        simpleItem(ModItems.SPEED_BLEND);
        simpleItem(ModItems.EFFICIENCY_BLEND);
        simpleItem(ModItems.GROWTH_BLEND);
        simpleItem(ModItems.SLOTH_BLEND);
        simpleItem(ModItems.ROTTEN_BLEND);

        //Ores and stuff
        simpleItem(ModItems.AMETHYST_DUST);
        simpleItem(ModItems.PRISTINE_AMETHYST_GRAIN);
        simpleItem(ModItems.IRON_CRYSTAL);
        simpleItem(ModItems.IRON_DUST);
        simpleItem(ModItems.PRISTINE_IRON_SAMPLE);
        simpleItem(ModItems.PRISTINE_QUARTZ_SAMPLE);
        simpleItem(ModItems.PRISTINE_GLOWSTONE_SAMPLE);
        simpleItem(ModItems.GLOWSTONE_CRYSTAL);
        simpleItem(ModItems.COPPER_CRYSTAL);
        simpleItem(ModItems.COPPER_DUST);
        simpleItem(ModItems.PRISTINE_COPPER_SAMPLE);
        simpleItem(ModItems.GOLD_CRYSTAL);
        simpleItem(ModItems.GOLD_DUST);
        simpleItem(ModItems.PRISTINE_GOLD_SAMPLE);
        simpleItem(ModItems.DEBRIS_SHARD);
        simpleItem(ModItems.PRISTINE_DEBRIS_SAMPLE);
        simpleItem(ModItems.NETHERITE_DUST);
        simpleItem(ModItems.REDSTONE_CRYSTAL);
        simpleItem(ModItems.PRISTINE_REDSTONE_SAMPLE);
        simpleItem(ModItems.LAPIS_CRYSTAL);
        simpleItem(ModItems.PRISTINE_LAPIS_SAMPLE);
        simpleItem(ModItems.DIAMOND_SHARD);
        simpleItem(ModItems.PRISTINE_DIAMOND_SAMPLE);
        simpleItem(ModItems.EMERALD_SHARD);
        simpleItem(ModItems.PRISTINE_EMERALD_SAMPLE);

        //Food
        simpleItem(ModItems.STRAWBERRY);
        simpleItem(ModItems.BOWL_OF_CARAMEL);
        simpleItem(ModItems.CARAMEL_STRAWBERRIES);
        simpleItem(ModItems.GOLDEN_STRAWBERRY);
        simpleItem(ModItems.BOWL_OF_ROCKCANDY);

        evenSimplerBlockItem(ModBlocks.GENTLIT_LOG);
        evenSimplerBlockItem(ModBlocks.STRIPPED_GENTLIT_LOG);
        evenSimplerBlockItem(ModBlocks.GENTLIT_WOOD);
        evenSimplerBlockItem(ModBlocks.STRIPPED_GENTLIT_WOOD);

        evenSimplerBlockItem(ModBlocks.GENTLIT_LEAVES);
        simpleCrossBlockItem(ModBlocks.GENTLIT_SAPLING);

        evenSimplerBlockItem(ModBlocks.GENTLIT_PLANKS);
        fenceItem(ModBlocks.GENTLIT_FENCE, ModBlocks.GENTLIT_PLANKS);
        buttonItem(ModBlocks.GENTLIT_BUTTON, ModBlocks.GENTLIT_PLANKS);
        trapdoorItem(ModBlocks.GENTLIT_TRAPDOOR);
        simpleBlockItem(ModBlocks.GENTLIT_DOOR);
        evenSimplerBlockItem(ModBlocks.GENTLIT_STAIRS);
        evenSimplerBlockItem(ModBlocks.GENTLIT_SLAB);
        evenSimplerBlockItem(ModBlocks.GENTLIT_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.GENTLIT_FENCE_GATE);

        evenSimplerBlockItem(ModBlocks.BRAVOT_LOG);
        evenSimplerBlockItem(ModBlocks.STRIPPED_BRAVOT_LOG);
        evenSimplerBlockItem(ModBlocks.BRAVOT_WOOD);
        evenSimplerBlockItem(ModBlocks.STRIPPED_BRAVOT_WOOD);

        evenSimplerBlockItem(ModBlocks.BRAVOT_LEAVES);
        simpleCrossBlockItem(ModBlocks.BRAVOT_SAPLING);

        evenSimplerBlockItem(ModBlocks.BRAVOT_PLANKS);
        fenceItem(ModBlocks.BRAVOT_FENCE, ModBlocks.BRAVOT_PLANKS);
        buttonItem(ModBlocks.BRAVOT_BUTTON, ModBlocks.BRAVOT_PLANKS);
        trapdoorItem(ModBlocks.BRAVOT_TRAPDOOR);
        simpleBlockItem(ModBlocks.BRAVOT_DOOR);
        evenSimplerBlockItem(ModBlocks.BRAVOT_STAIRS);
        evenSimplerBlockItem(ModBlocks.BRAVOT_SLAB);
        evenSimplerBlockItem(ModBlocks.BRAVOT_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.BRAVOT_FENCE_GATE);

        evenSimplerBlockItem(ModBlocks.MALVOR_LOG);
        evenSimplerBlockItem(ModBlocks.STRIPPED_MALVOR_LOG);
        evenSimplerBlockItem(ModBlocks.MALVOR_WOOD);
        evenSimplerBlockItem(ModBlocks.STRIPPED_MALVOR_WOOD);

        evenSimplerBlockItem(ModBlocks.MALVOR_LEAVES);
        simpleCrossBlockItem(ModBlocks.MALVOR_SAPLING);

        evenSimplerBlockItem(ModBlocks.MALVOR_PLANKS);
        fenceItem(ModBlocks.MALVOR_FENCE, ModBlocks.MALVOR_PLANKS);
        buttonItem(ModBlocks.MALVOR_BUTTON, ModBlocks.MALVOR_PLANKS);
        trapdoorItem(ModBlocks.MALVOR_TRAPDOOR);
        simpleBlockItem(ModBlocks.MALVOR_DOOR);
        evenSimplerBlockItem(ModBlocks.MALVOR_STAIRS);
        evenSimplerBlockItem(ModBlocks.MALVOR_SLAB);
        evenSimplerBlockItem(ModBlocks.MALVOR_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.MALVOR_FENCE_GATE);

        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_LOG);
        evenSimplerBlockItem(ModBlocks.STRIPPED_ORIGAMI_PALM_LOG);
        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_WOOD);
        evenSimplerBlockItem(ModBlocks.STRIPPED_ORIGAMI_PALM_WOOD);

        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_LEAVES);
        simpleCrossBlockItem(ModBlocks.ORIGAMI_PALM_SAPLING);

        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_PLANKS);
        fenceItem(ModBlocks.ORIGAMI_PALM_FENCE, ModBlocks.ORIGAMI_PALM_PLANKS);
        buttonItem(ModBlocks.ORIGAMI_PALM_BUTTON, ModBlocks.ORIGAMI_PALM_PLANKS);
        trapdoorItem(ModBlocks.ORIGAMI_PALM_TRAPDOOR);
        simpleBlockItem(ModBlocks.ORIGAMI_PALM_DOOR);
        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_STAIRS);
        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_SLAB);
        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.ORIGAMI_PALM_FENCE_GATE);

        evenSimplerBlockItem(ModBlocks.CRYSTALLIZED_LOG);
        evenSimplerBlockItem(ModBlocks.CRYSTALLIZED_LEAVES);

        //Crops
        simpleCropBlockItem(ModBlocks.WILD_STRAWBERRY, "wild_strawberry");
        simpleCropBlockItem(ModBlocks.STRAWBERRY_CROP, "strawberry_crop_stage5");
        simpleCropBlockItem(ModBlocks.WILD_HOOTNIP, "wild_hootnip_top");
        simpleCropBlockItem(ModBlocks.HOOTNIP_CROP, "hootnip_crop_stage6");

        //Adding plants
        specificCrossBlockItem(ModBlocks.PINK_CHARMIL_GRASS, "_3");
        specificCrossBlockItem(ModBlocks.BLUE_CHARMIL_GRASS, "_3");
        specificCrossBlockItem(ModBlocks.PALEGRASS, "_3");
        specificCrossBlockItem(ModBlocks.SEA_SHELLS, "_1");
        specificCrossBlockItem(ModBlocks.FROZEN_GRASS, "_3");
        specificCrossBlockItem(ModBlocks.LUMINUM, "_y_top_1");
        simpleCrossBlockItem(ModBlocks.PINK_BLOOM);
        simpleCrossBlockItem(ModBlocks.BLUE_BLOOM);
        simpleCrossBlockItem(ModBlocks.ABYSSAL_BLOOM);
        simpleCrossBlockItem(ModBlocks.FROZEN_BLOOM);
        simpleCrossBlockItem(ModBlocks.ORIGAMI_FERN);
        simpleCrossBlockItem(ModBlocks.LOVESHROOM);
        simpleCrossBlockItem(ModBlocks.GLOWSHROOM);
        simpleCrossBlockItem(ModBlocks.PALESHROOM);

        //Adding mushroom blocks
        blockItemNonBlockDependent(ModBlocks.LOVESHROOM_BLOCK);
        blockItemNonBlockDependent(ModBlocks.GLOWSHROOM_BLOCK);
        blockItemNonBlockDependent(ModBlocks.PALESHROOM_BLOCK);

        //Adding mineral blocks
        for (List<RegistryObject<Block>> minerals : ModGrowableMineral.GROWABLE_MINERAL_STAGES){
            for (RegistryObject<Block> mineral : minerals){
                simpleCrossBlockItem(mineral);
            }
        }

        //Fairy utils
        simpleItem(ModItems.FAIRY_IN_A_BOTTLE);
        simpleItem(ModItems.FAIRY_STAFF);
        simpleItem(ModItems.BINDING_STAFF);

        simpleItem(ModItems.CLOCKWORK_CARD_PUNCHER);

        simpleItem(ModItems.HOT_CHOCOLATE_BUCKET);

        simpleItem(ModItems.HOT_CHOCOLATE_BUCKET);

        //Cocoon and stuff
        simpleItem(ModItems.URO);
        simpleItem(ModItems.COCOON_BAG);
        simpleItem(ModItems.STRANGE_COCOON);

        //Adding tools and armor
        handheldItem(ModItems.SILKY_SWORD);
        handheldItem(ModItems.SILKY_PICKAXE);
        handheldItem(ModItems.SILKY_AXE);
        handheldItem(ModItems.SILKY_SHOVEL);
        handheldItem(ModItems.SILKY_HOE);

        simpleItem(ModItems.SILKY_HELMET);
        simpleItem(ModItems.SILKY_CHESTPLATE);
        simpleItem(ModItems.SILKY_LEGGINGS);
        simpleItem(ModItems.SILKY_BOOTS);

        simpleItem(ModItems.HOOTCAT_HELMET);
        simpleItem(ModItems.HOOTCAT_CHESTPLATE);
        simpleItem(ModItems.HOOTCAT_LEGGINGS);
        simpleItem(ModItems.HOOTCAT_BOOTS);

        simpleItem(ModItems.BUNNY_EARS);
        simpleItem(ModItems.CAT_EARS);
        simpleItem(ModItems.HEAD_BOW);
        simpleItem(ModItems.GAS_MASK);

        withExistingParent(ModItems.SILKBUN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FAIRY_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.HOOTCAT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.MARIONETTE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        //Fume bottle
        fumeBottleItem(ModItems.FUME_BOTTLE);
        catalystItem(ModItems.CATALYST);

        //Essences
        simpleItem(ModItems.ESSENCE);
        simpleItem(ModItems.PRISM);
    }

    private ItemModelBuilder simpleCrossBlockItem (RegistryObject<Block> block){
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"block/" + block.getId().getPath()));
    }

    private ItemModelBuilder simpleCropBlockItem (RegistryObject<Block> block, String path){
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"block/" + path));
    }

    private ItemModelBuilder specificCrossBlockItem (RegistryObject<Block> block, String extra){
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"block/" + block.getId().getPath() + extra));
    }


    private ItemModelBuilder simpleItem (RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder fumeBottleItem (RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"item/fume_bottle_fume")).texture("layer1",
                new ResourceLocation(BuntsyMod.MODID,"item/fume_bottle"));
    }

    private ItemModelBuilder catalystItem (RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"item/catalyst_shine")).texture("layer1",
                new ResourceLocation(BuntsyMod.MODID,"item/catalyst"));
    }

    private ItemModelBuilder simpleBlockItem (RegistryObject<Block> block){
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"item/" + block.getId().getPath()));
    }

    private ItemModelBuilder handheldItem (RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
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

    public void blockItemNonBlockDependent(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                mcLoc("block/cube_all")).texture("all",
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }
}
