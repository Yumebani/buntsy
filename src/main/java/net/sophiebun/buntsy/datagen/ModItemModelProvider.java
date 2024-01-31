package net.sophiebun.buntsy.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.custom.minerals.ModGrowableMineral;
import net.sophiebun.buntsy.item.ModItems;

import java.util.LinkedHashMap;
import java.util.List;

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
        simpleItem(ModItems.HOOTNIP);
        simpleItem(ModItems.GROUND_HOOTNIP);
        simpleItem(ModItems.HOOTNIP_CEREAL);
        simpleItem(ModItems.STRAWBERRY_SEEDS);
        simpleItem(ModItems.HOOTNIP_SEEDS);

        //Ores and stuff
        simpleItem(ModItems.AMETHYST_DUST);
        simpleItem(ModItems.PRISTINE_AMETHYST_GRAIN);
        simpleItem(ModItems.IRON_CRYSTAL);
        simpleItem(ModItems.IRON_DUST);
        simpleItem(ModItems.PRISTINE_IRON_SAMPLE);
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

        //Adding plants
        specificCrossBlockItem(ModBlocks.PINK_CHARMIL_GRASS, "_3");
        specificCrossBlockItem(ModBlocks.BLUE_CHARMIL_GRASS, "_3");
        simpleCrossBlockItem(ModBlocks.PINK_BLOOM);
        simpleCrossBlockItem(ModBlocks.BLUE_BLOOM);
        simpleCrossBlockItem(ModBlocks.LOVESHROOM);
        simpleCrossBlockItem(ModBlocks.GLOWSHROOM);

        //Adding mushroom blocks
        blockItemNonBlockDependent(ModBlocks.LOVESHROOM_BLOCK);
        blockItemNonBlockDependent(ModBlocks.GLOWSHROOM_BLOCK);

        //Adding mineral blocks
        for (List<RegistryObject<Block>> minerals : ModGrowableMineral.GROWABLE_MINERAL_STAGES){
            for (RegistryObject<Block> mineral : minerals){
                simpleCrossBlockItem(mineral);
            }
        }

        //Fairy utils
        simpleItem(ModItems.FAIRY_IN_A_BOTTLE);
        simpleItem(ModItems.FAIRY_STAFF);

        //this.getBuilder(ForgeRegistries.BLOCKS.getKey(ModBlocks.THREAD_REELER.get()).getPath())
        //        .parent(new ModelFile.UncheckedModelFile(modLoc("block/thread_reeler_item")));

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

        withExistingParent(ModItems.SILKBUN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FAIRY_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleCrossBlockItem (RegistryObject<Block> block){
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"block/" + block.getId().getPath()));
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
