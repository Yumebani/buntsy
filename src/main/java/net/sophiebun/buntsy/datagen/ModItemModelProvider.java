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
import net.sophiebun.buntsy.item.ModItems;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BuntsyMod.MODID, existingFileHelper);
    }

    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
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
        simpleItem(ModItems.AMETHYST_DUST);

        simpleItem(ModItems.STRAWBERRY);
        simpleItem(ModItems.BOWL_OF_CARAMEL);
        simpleItem(ModItems.CARAMEL_STRAWBERRIES);
        simpleItem(ModItems.GOLDEN_STRAWBERRY);
        simpleItem(ModItems.BOWL_OF_ROCKCANDY);

        simpleItem(ModItems.FAIRY_IN_A_BOTTLE);

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
        simpleCrossBlockItem(ModBlocks.PINK_CHARMIL_GRASS);
        simpleCrossBlockItem(ModBlocks.BLUE_CHARMIL_GRASS);
        simpleCrossBlockItem(ModBlocks.PINK_BLOOM);
        simpleCrossBlockItem(ModBlocks.BLUE_BLOOM);
        simpleCrossBlockItem(ModBlocks.LOVESHROOM);
        simpleCrossBlockItem(ModBlocks.GLOWSHROOM);

        //Adding mushroom blocks
        blockItemNonBlockDependent(ModBlocks.LOVESHROOM_BLOCK);
        blockItemNonBlockDependent(ModBlocks.GLOWSHROOM_BLOCK);

        handheldItem(ModItems.SILKY_SWORD);
        handheldItem(ModItems.SILKY_PICKAXE);
        handheldItem(ModItems.SILKY_AXE);
        handheldItem(ModItems.SILKY_SHOVEL);
        handheldItem(ModItems.SILKY_HOE);

        trimmedArmorItem(ModItems.SILKY_HELMET);
        trimmedArmorItem(ModItems.SILKY_CHESTPLATE);
        trimmedArmorItem(ModItems.SILKY_LEGGINGS);
        trimmedArmorItem(ModItems.SILKY_BOOTS);
    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = BuntsyMod.MODID; // Change this to your mod id

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private ItemModelBuilder simpleCrossBlockItem (RegistryObject<Block> block){
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BuntsyMod.MODID,"block/" + block.getId().getPath()));
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
