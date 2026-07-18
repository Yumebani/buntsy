package net.sophiebun.buntsy.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.ModItems;
import net.sophiebun.buntsy.tag.ModTags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        //Book
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FAIRY_TALE_BOOK.get(), 1)
                .requires(ModItems.FAIRY_DUST.get(), 1)
                .requires(Items.BOOK, 1)
                .unlockedBy(getHasName(ModItems.FAIRY_DUST.get()), has(ModItems.FAIRY_DUST.get()))
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .save(consumer, "buntsy:fairy_tale_book_crafting");

        //Wood recipes
        planksRecipe(ModTags.Items.GENTLIT_LOGS, ModBlocks.GENTLIT_PLANKS.get(), consumer);
        stairsRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_STAIRS.get(), consumer);
        trapdoorRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_TRAPDOOR.get(), consumer);
        slabRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_SLAB.get(), consumer);
        doorRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_DOOR.get(), consumer);
        fenceRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_FENCE.get(), consumer);
        fencegateRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_FENCE_GATE.get(), consumer);
        pressurePlateRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_PRESSURE_PLATE.get(), consumer);
        buttonRecipe(ModBlocks.GENTLIT_PLANKS.get(), ModBlocks.GENTLIT_BUTTON.get(), consumer);

        planksRecipe(ModTags.Items.BRAVOT_LOGS, ModBlocks.BRAVOT_PLANKS.get(), consumer);
        stairsRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_STAIRS.get(), consumer);
        trapdoorRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_TRAPDOOR.get(), consumer);
        slabRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_SLAB.get(), consumer);
        doorRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_DOOR.get(), consumer);
        fenceRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_FENCE.get(), consumer);
        fencegateRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_FENCE_GATE.get(), consumer);
        pressurePlateRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_PRESSURE_PLATE.get(), consumer);
        buttonRecipe(ModBlocks.BRAVOT_PLANKS.get(), ModBlocks.BRAVOT_BUTTON.get(), consumer);

        planksRecipe(ModTags.Items.MALVOR_LOGS, ModBlocks.MALVOR_PLANKS.get(), consumer);
        stairsRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_STAIRS.get(), consumer);
        trapdoorRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_TRAPDOOR.get(), consumer);
        slabRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_SLAB.get(), consumer);
        doorRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_DOOR.get(), consumer);
        fenceRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_FENCE.get(), consumer);
        fencegateRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_FENCE_GATE.get(), consumer);
        pressurePlateRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_PRESSURE_PLATE.get(), consumer);
        buttonRecipe(ModBlocks.MALVOR_PLANKS.get(), ModBlocks.MALVOR_BUTTON.get(), consumer);

        planksRecipe(ModTags.Items.ORIGAMI_PALM_LOGS, ModBlocks.ORIGAMI_PALM_PLANKS.get(), consumer);
        stairsRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_STAIRS.get(), consumer);
        trapdoorRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_TRAPDOOR.get(), consumer);
        slabRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_SLAB.get(), consumer);
        doorRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_DOOR.get(), consumer);
        fenceRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_FENCE.get(), consumer);
        fencegateRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_FENCE_GATE.get(), consumer);
        pressurePlateRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_PRESSURE_PLATE.get(), consumer);
        buttonRecipe(ModBlocks.ORIGAMI_PALM_PLANKS.get(), ModBlocks.ORIGAMI_PALM_BUTTON.get(), consumer);

        //Silk fabric
        compact2By2(ModItems.SILK_SPOOL.get(), ModItems.SILK_FABRIC.get(), 2, consumer);

        //Wool
        compact2By2(ModItems.SILK_FABRIC.get(), Items.WHITE_WOOL, 2, consumer);



        //Sand
        smeltingRecipe(ModBlocks.SWEET_CORAL_SAND.get(), Blocks.GLASS, 0.5f, consumer);
        brickBlock(ModBlocks.SWEET_CORAL_SAND.get(), ModBlocks.SWEET_LIMESTONE.get(), consumer);
        stairsRecipe(ModBlocks.SWEET_LIMESTONE.get(), ModBlocks.SWEET_LIMESTONE_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SWEET_LIMESTONE.get(), ModBlocks.SWEET_LIMESTONE_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SWEET_LIMESTONE.get(), ModBlocks.SWEET_LIMESTONE_WALL.get(), consumer);
        brickBlock(ModBlocks.SWEET_LIMESTONE.get(), ModBlocks.SWEET_LIMESTONE_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.SWEET_LIMESTONE_BRICKS.get(), ModBlocks.SWEET_LIMESTONE_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SWEET_LIMESTONE_BRICKS.get(), ModBlocks.SWEET_LIMESTONE_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SWEET_LIMESTONE_BRICKS.get(), ModBlocks.SWEET_LIMESTONE_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_STAIRS.get(), ModBlocks.SWEET_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_SLAB.get(), ModBlocks.SWEET_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_WALL.get(), ModBlocks.SWEET_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_BRICKS.get(), ModBlocks.SWEET_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_BRICKS_STAIRS.get(), ModBlocks.SWEET_LIMESTONE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_BRICKS_SLAB.get(), ModBlocks.SWEET_LIMESTONE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_LIMESTONE_BRICKS_WALL.get(), ModBlocks.SWEET_LIMESTONE_BRICKS.get());


        smeltingRecipe(ModBlocks.FROZEN_CORAL_SAND.get(), Blocks.GLASS, 0.5f, consumer);
        brickBlock(ModBlocks.FROZEN_CORAL_SAND.get(), ModBlocks.FROZEN_LIMESTONE.get(), consumer);
        stairsRecipe(ModBlocks.FROZEN_LIMESTONE.get(), ModBlocks.FROZEN_LIMESTONE_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.FROZEN_LIMESTONE.get(), ModBlocks.FROZEN_LIMESTONE_SLAB.get(), consumer);
        wallRecipe(ModBlocks.FROZEN_LIMESTONE.get(), ModBlocks.FROZEN_LIMESTONE_WALL.get(), consumer);
        brickBlock(ModBlocks.FROZEN_LIMESTONE.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.FROZEN_LIMESTONE_BRICKS.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.FROZEN_LIMESTONE_BRICKS.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.FROZEN_LIMESTONE_BRICKS.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_STAIRS.get(), ModBlocks.FROZEN_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_SLAB.get(), ModBlocks.FROZEN_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_WALL.get(), ModBlocks.FROZEN_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_BRICKS.get(), ModBlocks.FROZEN_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_BRICKS_STAIRS.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_BRICKS_SLAB.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FROZEN_LIMESTONE_BRICKS_WALL.get(), ModBlocks.FROZEN_LIMESTONE_BRICKS.get());


        smeltingRecipe(ModBlocks.SUNNY_CORAL_SAND.get(), Blocks.GLASS, 0.5f, consumer);
        brickBlock(ModBlocks.SUNNY_CORAL_SAND.get(), ModBlocks.SUNNY_LIMESTONE.get(), consumer);
        stairsRecipe(ModBlocks.SUNNY_LIMESTONE.get(), ModBlocks.SUNNY_LIMESTONE_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SUNNY_LIMESTONE.get(), ModBlocks.SUNNY_LIMESTONE_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SUNNY_LIMESTONE.get(), ModBlocks.SUNNY_LIMESTONE_WALL.get(), consumer);
        brickBlock(ModBlocks.SUNNY_LIMESTONE.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.SUNNY_LIMESTONE_BRICKS.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SUNNY_LIMESTONE_BRICKS.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SUNNY_LIMESTONE_BRICKS.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_STAIRS.get(), ModBlocks.SUNNY_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_SLAB.get(), ModBlocks.SUNNY_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_WALL.get(), ModBlocks.SUNNY_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_BRICKS.get(), ModBlocks.SUNNY_LIMESTONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_BRICKS_STAIRS.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_BRICKS_SLAB.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SUNNY_LIMESTONE_BRICKS_WALL.get(), ModBlocks.SUNNY_LIMESTONE_BRICKS.get());

        stairsRecipe(ModBlocks.SOUR_CANDY_ROCK.get(), ModBlocks.SOUR_CANDY_ROCK_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SOUR_CANDY_ROCK.get(), ModBlocks.SOUR_CANDY_ROCK_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SOUR_CANDY_ROCK.get(), ModBlocks.SOUR_CANDY_ROCK_WALL.get(), consumer);
        brickBlock(ModBlocks.SOUR_CANDY_ROCK.get(), ModBlocks.SOUR_CANDY_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.SOUR_CANDY_BRICKS.get(), ModBlocks.SOUR_CANDY_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SOUR_CANDY_BRICKS.get(), ModBlocks.SOUR_CANDY_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SOUR_CANDY_BRICKS.get(), ModBlocks.SOUR_CANDY_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_ROCK_STAIRS.get(), ModBlocks.SOUR_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_ROCK_SLAB.get(), ModBlocks.SOUR_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_ROCK_WALL.get(), ModBlocks.SOUR_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_BRICKS.get(), ModBlocks.SOUR_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_BRICKS_STAIRS.get(), ModBlocks.SOUR_CANDY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_BRICKS_SLAB.get(), ModBlocks.SOUR_CANDY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUR_CANDY_BRICKS_WALL.get(), ModBlocks.SOUR_CANDY_BRICKS.get());

        stairsRecipe(ModBlocks.SWEET_CANDY_ROCK.get(), ModBlocks.SWEET_CANDY_ROCK_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SWEET_CANDY_ROCK.get(), ModBlocks.SWEET_CANDY_ROCK_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SWEET_CANDY_ROCK.get(), ModBlocks.SWEET_CANDY_ROCK_WALL.get(), consumer);
        brickBlock(ModBlocks.SWEET_CANDY_ROCK.get(), ModBlocks.SWEET_CANDY_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.SWEET_CANDY_BRICKS.get(), ModBlocks.SWEET_CANDY_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.SWEET_CANDY_BRICKS.get(), ModBlocks.SWEET_CANDY_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.SWEET_CANDY_BRICKS.get(), ModBlocks.SWEET_CANDY_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_ROCK_STAIRS.get(), ModBlocks.SWEET_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_ROCK_SLAB.get(), ModBlocks.SWEET_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_ROCK_WALL.get(), ModBlocks.SWEET_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_BRICKS.get(), ModBlocks.SWEET_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_BRICKS_STAIRS.get(), ModBlocks.SWEET_CANDY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_BRICKS_SLAB.get(), ModBlocks.SWEET_CANDY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SWEET_CANDY_BRICKS_WALL.get(), ModBlocks.SWEET_CANDY_BRICKS.get());

        stairsRecipe(ModBlocks.BITTER_CANDY_ROCK.get(), ModBlocks.BITTER_CANDY_ROCK_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.BITTER_CANDY_ROCK.get(), ModBlocks.BITTER_CANDY_ROCK_SLAB.get(), consumer);
        wallRecipe(ModBlocks.BITTER_CANDY_ROCK.get(), ModBlocks.BITTER_CANDY_ROCK_WALL.get(), consumer);
        brickBlock(ModBlocks.BITTER_CANDY_ROCK.get(), ModBlocks.BITTER_CANDY_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.BITTER_CANDY_BRICKS.get(), ModBlocks.BITTER_CANDY_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.BITTER_CANDY_BRICKS.get(), ModBlocks.BITTER_CANDY_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.BITTER_CANDY_BRICKS.get(), ModBlocks.BITTER_CANDY_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_ROCK_STAIRS.get(), ModBlocks.BITTER_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_ROCK_SLAB.get(), ModBlocks.BITTER_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_ROCK_WALL.get(), ModBlocks.BITTER_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_BRICKS.get(), ModBlocks.BITTER_CANDY_ROCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_BRICKS_STAIRS.get(), ModBlocks.BITTER_CANDY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_BRICKS_SLAB.get(), ModBlocks.BITTER_CANDY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BITTER_CANDY_BRICKS_WALL.get(), ModBlocks.BITTER_CANDY_BRICKS.get());

        stairsRecipe(ModBlocks.PETRIFIED_CHOCOLATE.get(), ModBlocks.PETRIFIED_CHOCOLATE_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.PETRIFIED_CHOCOLATE.get(), ModBlocks.PETRIFIED_CHOCOLATE_SLAB.get(), consumer);
        wallRecipe(ModBlocks.PETRIFIED_CHOCOLATE.get(), ModBlocks.PETRIFIED_CHOCOLATE_WALL.get(), consumer);
        smeltingRecipe(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get(), ModBlocks.PETRIFIED_CHOCOLATE.get(), 0.5f, consumer);
        stairsRecipe(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get(), ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get(), ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_SLAB.get(), consumer);
        wallRecipe(ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get(), ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_WALL.get(), consumer);
        brickBlock(ModBlocks.PETRIFIED_CHOCOLATE.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_STAIRS.get(), ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_SLAB.get(), ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.COBBLED_PETRIFIED_CHOCOLATE_WALL.get(), ModBlocks.COBBLED_PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_WALL.get(), ModBlocks.PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_SLAB.get(), ModBlocks.PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_STAIRS.get(), ModBlocks.PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get(), ModBlocks.PETRIFIED_CHOCOLATE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_STAIRS.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_SLAB.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_CHOCOLATE_BRICKS_WALL.get(), ModBlocks.PETRIFIED_CHOCOLATE_BRICKS.get());

        brickBlock(ModBlocks.PACKED_ODIATE_MUD.get(), ModBlocks.ODIATE_MUD_BRICKS.get(), consumer);
        stairsRecipe(ModBlocks.ODIATE_MUD_BRICKS.get(), ModBlocks.ODIATE_MUD_BRICKS_STAIRS.get(), consumer);
        slabRecipe(ModBlocks.ODIATE_MUD_BRICKS.get(), ModBlocks.ODIATE_MUD_BRICKS_SLAB.get(), consumer);
        wallRecipe(ModBlocks.ODIATE_MUD_BRICKS.get(), ModBlocks.ODIATE_MUD_BRICKS_WALL.get(), consumer);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ODIATE_MUD_BRICKS_STAIRS.get(), ModBlocks.ODIATE_MUD_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ODIATE_MUD_BRICKS_SLAB.get(), ModBlocks.ODIATE_MUD_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ODIATE_MUD_BRICKS_WALL.get(), ModBlocks.ODIATE_MUD_BRICKS.get());


        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CLOCKWORK_BRASS_BLOCK.get(), ModItems.CLOCKWORK_BRASS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_CLOCKWORK_BRASS.get(), ModBlocks.CLOCKWORK_BRASS_BLOCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_CLOCKWORK_BRASS_STAIRS.get(), ModBlocks.CLOCKWORK_BRASS_BLOCK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_CLOCKWORK_BRASS_SLAB.get(), ModBlocks.CLOCKWORK_BRASS_BLOCK.get());

        oneToOneConversionRecipe(consumer, Items.SUGAR, ModBlocks.SWEEDS.get(), null);
        oneToOneConversionRecipe(consumer, Items.SUGAR, ModBlocks.CROCKTUS.get(), null);

        smeltingRecipe(ModBlocks.CROCKTUS.get(), Items.CYAN_DYE, 0.5f, consumer);

        //Icy
        compact2By2(ModItems.COLD_POWDERED_SUGAR.get(), ModBlocks.FROZEN_POWDER_BLOCK.get(), 1, consumer);
        compact2By2(ModItems.SWICE_SHARDS.get(), ModBlocks.SWICE.get(), 1, consumer);

        //Clockwork
        compact2By2(ModItems.CLOCKWORK_SCRAP.get(), ModItems.CLOCKWORK_SCRAP_CLUMP.get(), 1, consumer);
        smeltingRecipe(ModItems.CLOCKWORK_SCRAP_CLUMP.get(), ModItems.CLOCKWORK_BRASS.get(), 0.5f, consumer);

        //Mineral shards
        compact2By2(ModItems.DIAMOND_SHARD.get(), Items.DIAMOND, 1, consumer);
        compact2By2(ModItems.EMERALD_SHARD.get(), Items.EMERALD, 1, consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.NETHERITE_SCRAP, 1)
                .requires(ModItems.DEBRIS_SHARD.get(), 3)
                .unlockedBy(getHasName(ModItems.DEBRIS_SHARD.get()), has(ModItems.DEBRIS_SHARD.get()))
                .save(consumer, "buntsy:debris_shard_compacting");

        //Smelting dust
        oreSmeltingRecipe(ModItems.IRON_DUST.get(), Items.IRON_INGOT, consumer);
        oreSmeltingRecipe(ModItems.COPPER_DUST.get(), Items.COPPER_INGOT, consumer);
        oreSmeltingRecipe(ModItems.GOLD_DUST.get(), Items.GOLD_INGOT, consumer);
        oreSmeltingRecipe(ModItems.NETHERITE_DUST.get(), Items.NETHERITE_SCRAP, consumer);

        //Silky solids
        upgradeSmithing(ModItems.FAIRY_DUST.get(), Items.NETHERITE_SCRAP, ModItems.TOUGH_SILK_FABRIC.get(), ModItems.SILKY_INGOT.get(), consumer);
        upgradeSmithing(ModItems.FAIRY_DUST.get(), Items.DIAMOND, ModItems.TOUGH_SILK_FABRIC.get(), ModItems.SILKY_CRYSTAL.get(), consumer);
        compact3By3(ModItems.SILKY_NUGGET.get(), ModItems.SILKY_INGOT.get(), consumer);
        uncompact(ModItems.SILKY_INGOT.get(), ModItems.SILKY_NUGGET.get(), consumer);

        //Foods
        cookingFoodRecipe(ModItems.SUGAR_BOWL.get(), ModItems.BOWL_OF_CARAMEL.get(), consumer);
        cookingFoodRecipe(ModItems.SYRUPY_MIXTURE_BOWL.get(), ModItems.BOWL_OF_ROCKCANDY.get(), consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SUGAR, 2)
                .requires(ModItems.SUGARDEW.get(), 1)
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .save(consumer, "buntsy:sugar_from_sugardew");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ROOT_WAFFLE.get(), 2)
                .requires(ModItems.ROOT_FLOUR.get(), 4)
                .requires(ModItems.SUGARDEW.get(), 1)
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .unlockedBy(getHasName(ModItems.ROOT_FLOUR.get()), has(ModItems.ROOT_FLOUR.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SUGARDEW_BALL.get(), 1)
                .requires(ModItems.SUGARDEW.get(), 1)
                .requires(Items.SUGAR, 3)
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .save(consumer);
        smeltingRecipe(ModItems.SUGARDEW_BALL.get(), ModItems.DEW_CRYSTALS.get(), 0.5f, consumer);


        //Clockwork processor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PACKED_ODIATE_MUD.get(), 1)
                .define('A', ModBlocks.ODIATE_MUD.get())
                .define('C', Items.WHEAT)
                .pattern("AAC")
                .pattern("AAC")
                .unlockedBy(getHasName(ModBlocks.ODIATE_MUD.get()), has(ModBlocks.ODIATE_MUD.get()))
                .save(consumer);

        //Clockwork gear
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CLOCKWORK_GEAR.get(), 2)
                .define('A', ModItems.CLOCKWORK_BRASS.get())
                .pattern(" A ")
                .pattern("A A")
                .pattern(" A ")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_BRASS.get()), has(ModItems.CLOCKWORK_BRASS.get()))
                .save(consumer);

        //Clockwork processor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CLOCKWORK_PROCESSOR.get(), 1)
                .define('A', ModItems.CLOCKWORK_GEAR.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('C', Items.DIAMOND)
                .define('D', Items.REDSTONE)
                .pattern("ABD")
                .pattern("BCB")
                .pattern("DBA")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_BRASS.get()), has(ModItems.CLOCKWORK_BRASS.get()))
                .save(consumer);

        //Clockwork modification
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CLOCKWORK_MODIFICATION.get(), 1)
                .define('A', ModItems.CLOCKWORK_GEAR.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('D', Items.REDSTONE)
                .pattern("BDB")
                .pattern("BAB")
                .pattern("BDB")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_BRASS.get()), has(ModItems.CLOCKWORK_BRASS.get()))
                .save(consumer);

        //Clockwork units
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SIMPLE_CLOCKWORK_UNIT.get(), 1)
                .define('A', ModItems.CLOCKWORK_GEAR.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('C', ModItems.CLOCKWORK_PROCESSOR.get())
                .define('D', ModItems.IRON_CRYSTAL.get())
                .define('E', Items.IRON_INGOT)
                .pattern("ABD")
                .pattern("BCB")
                .pattern("EBA")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Clockwork units
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INTRICATE_CLOCKWORK_UNIT.get(), 1)
                .define('A', ModItems.SIMPLE_CLOCKWORK_UNIT.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('D', ModItems.DIAMOND_SHARD.get())
                .define('E', Items.DIAMOND)
                .pattern("BEB")
                .pattern("ADA")
                .pattern("BEB")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Clockwork units
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COMPLEX_CLOCKWORK_UNIT.get(), 1)
                .define('A', ModItems.INTRICATE_CLOCKWORK_UNIT.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('D', ModItems.DEBRIS_SHARD.get())
                .define('E', Items.NETHERITE_SCRAP)
                .pattern("BEB")
                .pattern("ADA")
                .pattern("BEB")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Maiden
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CLOCKWORK_MAIDEN.get(), 1)
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('D', ModItems.CLOCKWORK_PROCESSOR.get())
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("EDE")
                .pattern("BDB")
                .pattern("BEB")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Fairy terminal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EMPTY_CLOCKWORK_FAIRY_TERMINAL.get(), 1)
                .define('A', ModItems.SIMPLE_CLOCKWORK_UNIT.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('C', ModBlocks.FAIRY_OFFERING_BENCH.get())
                .define('D', ModItems.CLOCKWORK_PROCESSOR.get())
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("BDB")
                .pattern("EAE")
                .pattern("BCB")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Clockwork crafter
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCKWORK_CRAFTER.get(), 1)
                .define('A', Items.CRAFTING_TABLE)
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("BE")
                .pattern("BA")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Chocolate block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHOCOLATE_BLOCK.get(), 1)
                .define('A', ModItems.CHOCOLATE.get())
                .pattern("AA")
                .pattern("AA")
                .unlockedBy(getHasName(ModItems.CHOCOLATE.get()), has(ModItems.CHOCOLATE.get()))
                .save(consumer);

        //Clockwork geyser
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCKWORK_GEYSER_COLLECTOR.get(), 1)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("BEB")
                .pattern("AAA")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_BRASS.get()), has(ModItems.CLOCKWORK_BRASS.get()))
                .save(consumer);

        //Clockwork powder
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCKWORK_POWDERED_SUGAR_COLLECTOR.get(), 1)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("AAA")
                .pattern("BEB")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_BRASS.get()), has(ModItems.CLOCKWORK_BRASS.get()))
                .save(consumer);

        //Clockwork fisher
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCKWORK_FISHER.get(), 1)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('C', Items.FISHING_ROD)
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("ECE")
                .pattern("ABA")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_BRASS.get()), has(ModItems.CLOCKWORK_BRASS.get()))
                .save(consumer);

        //Clockwork syrup extractor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCKWORK_SYRUP_EXTRACTOR.get(), 1)
                .define('A', ModBlocks.SYRUP_EXTRACTOR.get())
                .define('B', ModItems.CLOCKWORK_BRASS.get())
                .define('E', ModItems.CLOCKWORK_GEAR.get())
                .pattern("EBE")
                .pattern("AEA")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Clockwork maiden terminal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCKWORK_MAIDEN_TERMINAL.get(), 1)
                .define('A', ModItems.CLOCKWORK_PROCESSOR.get())
                .define('C', ModItems.CLOCKWORK_BRASS.get())
                .define('D', ModItems.CLOCKWORK_GEAR.get())
                .define('E', Items.DIAMOND)
                .pattern("CEC")
                .pattern("CAC")
                .pattern("DAD")
                .unlockedBy(getHasName(ModItems.CLOCKWORK_PROCESSOR.get()), has(ModItems.CLOCKWORK_PROCESSOR.get()))
                .save(consumer);

        //Silk spool
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILK_SPOOL.get(), 1)
                .define('A', ModItems.SILK.get())
                .define('B', Items.STICK)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .unlockedBy(getHasName(ModItems.SILK.get()), has(ModItems.SILK.get()))
                .save(consumer);


        //Tough silk fabric
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOUGH_SILK_FABRIC.get(), 3)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('B', ModItems.MOTH_WING_THREAD.get())
                .pattern("ABA")
                .pattern("BAB")
                .pattern("ABA")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .unlockedBy(getHasName(ModItems.MOTH_WING_THREAD.get()), has(ModItems.MOTH_WING_THREAD.get()))
                .save(consumer);

        //Giant Cocoon
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GIANT_COCOON.get(), 1)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('B', ModItems.TOUGH_SILK_FABRIC.get())
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .unlockedBy(getHasName(ModItems.MOTH_WING_THREAD.get()), has(ModItems.MOTH_WING_THREAD.get()))
                .save(consumer);

        //Cocoon bag
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COCOON_BAG.get(), 1)
                .define('A', ModItems.SILK_FABRIC.get())
                .define('S', ModItems.SILK.get())
                .define('B', ModItems.TOUGH_SILK_FABRIC.get())
                .pattern(" S ")
                .pattern("BAB")
                .pattern(" B ")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .unlockedBy(getHasName(ModItems.MOTH_WING_THREAD.get()), has(ModItems.MOTH_WING_THREAD.get()))
                .save(consumer);

        //Fairy power receptor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FAIRY_POWER_RECEPTOR.get(), 1)
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.AMETHYST_SHARD)
                .pattern(" C ")
                .pattern("GCG")
                .pattern(" C ")
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(consumer);

        //Fairy power emitter
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FAIRY_POWER_EMITTER.get(), 1)
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.AMETHYST_SHARD)
                .pattern(" G ")
                .pattern("CCC")
                .pattern(" G ")
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(consumer);

        //Empty catalyst
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EMPTY_CATALYST.get(), 1)
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.AMETHYST_SHARD)
                .pattern(" G ")
                .pattern("GCG")
                .pattern(" G ")
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(consumer);

        //Sugar bowl
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SUGAR_BOWL.get(), 1)
                .requires(Items.SUGAR, 4)
                .requires(Items.BOWL, 1)
                .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
                .save(consumer);

        //Caramel Strawberries
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CARAMEL_STRAWBERRIES.get(), 1)
                .requires(ModItems.STRAWBERRY.get(), 3)
                .requires(ModItems.BOWL_OF_CARAMEL.get(), 1)
                .unlockedBy(getHasName(ModItems.STRAWBERRY.get()), has(ModItems.STRAWBERRY.get()))
                .unlockedBy(getHasName(ModItems.BOWL_OF_CARAMEL.get()), has(ModItems.BOWL_OF_CARAMEL.get()))
                .save(consumer);

        //Chocolate Strawberries
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CHOCOLATE_STRAWBERRIES.get(), 1)
                .requires(ModItems.STRAWBERRY.get(), 3)
                .requires(ModItems.CHOCOLATE.get(), 1)
                .unlockedBy(getHasName(ModItems.STRAWBERRY.get()), has(ModItems.STRAWBERRY.get()))
                .unlockedBy(getHasName(ModItems.CHOCOLATE.get()), has(ModItems.CHOCOLATE.get()))
                .save(consumer);

        //Icecream
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VANILLA_ICECREAM.get(), 2)
                .requires(ModItems.COLD_POWDERED_SUGAR.get(), 3)
                .requires(ModItems.SWICE_SHARDS.get(), 2)
                .requires(ModItems.SUGARDEW.get(), 1)
                .requires(Ingredient.of(ModItems.GENTLIT_SYRUP.get(), Items.HONEY_BOTTLE, Items.MILK_BUCKET), 1)
                .unlockedBy(getHasName(ModItems.COLD_POWDERED_SUGAR.get()), has(ModItems.COLD_POWDERED_SUGAR.get()))
                .unlockedBy(getHasName(ModItems.SWICE_SHARDS.get()), has(ModItems.SWICE_SHARDS.get()))
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .save(consumer);

        //Chocolate icecream
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CHOCOLATE_ICECREAM.get(), 2)
                .requires(ModItems.COLD_POWDERED_SUGAR.get(), 3)
                .requires(ModItems.SWICE_SHARDS.get(), 2)
                .requires(ModItems.CHOCOLATE.get(), 1)
                .requires(ModItems.SUGARDEW.get(), 1)
                .requires(Ingredient.of(ModItems.GENTLIT_SYRUP.get(), Items.HONEY_BOTTLE, Items.MILK_BUCKET), 1)
                .unlockedBy(getHasName(ModItems.COLD_POWDERED_SUGAR.get()), has(ModItems.COLD_POWDERED_SUGAR.get()))
                .unlockedBy(getHasName(ModItems.SWICE_SHARDS.get()), has(ModItems.SWICE_SHARDS.get()))
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .save(consumer);

        //Caramel icecream
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CARAMEL_ICECREAM.get(), 2)
                .requires(ModItems.COLD_POWDERED_SUGAR.get(), 3)
                .requires(ModItems.SWICE_SHARDS.get(), 2)
                .requires(ModItems.BOWL_OF_CARAMEL.get(), 1)
                .requires(ModItems.SUGARDEW.get(), 1)
                .requires(Ingredient.of(ModItems.GENTLIT_SYRUP.get(), Items.HONEY_BOTTLE, Items.MILK_BUCKET), 1)
                .unlockedBy(getHasName(ModItems.COLD_POWDERED_SUGAR.get()), has(ModItems.COLD_POWDERED_SUGAR.get()))
                .unlockedBy(getHasName(ModItems.SWICE_SHARDS.get()), has(ModItems.SWICE_SHARDS.get()))
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .save(consumer);

        //Triple shot icecream
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TRIPLE_SHOT_ICECREAM.get(), 2)
                .requires(ModItems.VANILLA_ICECREAM.get(), 1)
                .requires(ModItems.CHOCOLATE_ICECREAM.get(), 1)
                .requires(ModItems.CARAMEL_ICECREAM.get(), 1)
                .requires(ModItems.ROOT_WAFFLE.get(), 1)
                .unlockedBy(getHasName(ModItems.VANILLA_ICECREAM.get()), has(ModItems.VANILLA_ICECREAM.get()))
                .unlockedBy(getHasName(ModItems.CHOCOLATE_ICECREAM.get()), has(ModItems.CHOCOLATE_ICECREAM.get()))
                .unlockedBy(getHasName(ModItems.CARAMEL_ICECREAM.get()), has(ModItems.CARAMEL_ICECREAM.get()))
                .unlockedBy(getHasName(ModItems.ROOT_WAFFLE.get()), has(ModItems.ROOT_WAFFLE.get()))
                .save(consumer);

        //Chocolate
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CHOCOLATE.get(), 1)
                .requires(ModItems.CHOCOLATE_FLAKES.get(), 4)
                .unlockedBy(getHasName(ModItems.CHOCOLATE_FLAKES.get()), has(ModItems.CHOCOLATE_FLAKES.get()))
                .save(consumer, "chocolate_from_chocolate_flakes");

        //Chocolate
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CHOCOLATE.get(), 4)
                .requires(ModBlocks.CHOCOLATE_BLOCK.get(), 1)
                .unlockedBy(getHasName(ModItems.CHOCOLATE.get()), has(ModItems.CHOCOLATE.get()))
                .save(consumer, "chocolate_from_chocolate_block");

        //Syrupy bowl
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SYRUPY_MIXTURE_BOWL.get(), 1)
                .requires(Items.SUGAR, 4)
                .requires(Items.HONEY_BOTTLE, 1)
                .requires(ModItems.GENTLIT_SYRUP.get(), 1)
                .requires(Items.BOWL, 1)
                .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
                .unlockedBy(getHasName(Items.HONEY_BOTTLE), has(Items.HONEY_BOTTLE))
                .unlockedBy(getHasName(ModItems.GENTLIT_SYRUP.get()), has(ModItems.GENTLIT_SYRUP.get()))
                .save(consumer);

        //Bread
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BREAD, 2)
                .define('F', ModItems.ROOT_FLOUR.get())
                .pattern("FFF")
                .unlockedBy(getHasName(ModItems.ROOT_FLOUR.get()), has(ModItems.ROOT_FLOUR.get()))
                .save(consumer, "buntsy:syrupy_mixture_bowl_syrup");

        //Syrupy bowl
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SYRUPY_MIXTURE_BOWL.get(), 1)
                .requires(Items.SUGAR, 4)
                .requires(ModItems.SUGARDEW.get(), 2)
                .requires(ModItems.GENTLIT_SYRUP.get(), 1)
                .requires(Items.BOWL, 1)
                .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
                .unlockedBy(getHasName(ModItems.SUGARDEW.get()), has(ModItems.SUGARDEW.get()))
                .unlockedBy(getHasName(ModItems.GENTLIT_SYRUP.get()), has(ModItems.GENTLIT_SYRUP.get()))
                .save(consumer, "buntsy:syrupy_mixture_bowl_dew");

        //Blazing hootnip
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLAZING_HOOTNIP.get(), 1)
                .requires(ModItems.HOOTNIP.get(), 1)
                .requires(Items.BLAZE_POWDER, 1)
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(consumer);

        //Plant seeds
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HOOTNIP_SEEDS.get(), 1)
                .requires(ModItems.HOOTNIP.get(), 1)
                .unlockedBy(getHasName(ModItems.HOOTNIP.get()), has(ModItems.HOOTNIP.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STRAWBERRY_SEEDS.get(), 1)
                .requires(ModItems.STRAWBERRY.get(), 1)
                .unlockedBy(getHasName(ModItems.STRAWBERRY.get()), has(ModItems.STRAWBERRY.get()))
                .save(consumer);

        //Machine Recipes
        //Fairy offering bench
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FAIRY_OFFERING_BENCH.get(), 1)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .define('S', ModBlocks.GENTLIT_SLAB.get())
                .pattern("PSP")
                .pattern("PSP")
                .pattern("L L")
                .unlockedBy(getHasName(ModBlocks.GENTLIT_LOG.get()), has(ModBlocks.GENTLIT_LOG.get()))
                .save(consumer);

        //Infusion Pedestal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INFUSION_PEDESTAL.get(), 1)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('S', ModBlocks.GENTLIT_SLAB.get())
                .pattern("LSL")
                .pattern("LLL")
                .pattern("L L")
                .unlockedBy(getHasName(ModBlocks.GENTLIT_LOG.get()), has(ModBlocks.GENTLIT_LOG.get()))
                .save(consumer);

        //Fairy collection tray
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FAIRY_COLLECTION_TRAY.get(), 1)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .define('S', ModBlocks.GENTLIT_SLAB.get())
                .pattern("PSP")
                .pattern("PSP")
                .pattern("LSL")
                .unlockedBy(getHasName(ModBlocks.GENTLIT_LOG.get()), has(ModBlocks.GENTLIT_LOG.get()))
                .save(consumer);

        //Fairy infusion bench
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FAIRY_INFUSION_BENCH.get(), 1)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .define('T', ModBlocks.GENTLIT_TRAPDOOR.get())
                .pattern("PTP")
                .pattern("PTP")
                .pattern("L L")
                .unlockedBy(getHasName(ModBlocks.GENTLIT_LOG.get()), has(ModBlocks.GENTLIT_LOG.get()))
                .save(consumer);

        //Grinding Wheel
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GRINDING_WHEEL.get(), 1)
                .define('B', Blocks.SMOOTH_STONE)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('I', Items.IRON_INGOT)
                .define('F', ModItems.FAIRY_POWER_RECEPTOR.get())
                .pattern("SSS")
                .pattern("BFB")
                .pattern("BIB")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Mixer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MIXER_BLOCK.get(), 1)
                .define('C', Blocks.CAULDRON)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('I', Items.IRON_INGOT)
                .define('F', ModItems.FAIRY_POWER_RECEPTOR.get())
                .pattern("SSS")
                .pattern("ICI")
                .pattern("IFI")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Reeling Wheel
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THREAD_REELER.get(), 1)
                .define('W', Items.WATER_BUCKET)
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('F', ModItems.FAIRY_POWER_RECEPTOR.get())
                .pattern("LPL")
                .pattern("LFL")
                .pattern("LWL")
                .unlockedBy(getHasName(ModItems.FAIRY_DUST.get()), has(ModItems.FAIRY_DUST.get()))
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Magic Crystalizer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MAGIC_CRYSTALIZER.get(), 1)
                .define('I', Items.IRON_INGOT)
                .define('D', ModItems.PRISTINE_DIAMOND_SAMPLE.get())
                .define('F', ModItems.FAIRY_DUST.get())
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .define('S', Blocks.SMOOTH_STONE)
                .define('R', ModItems.FAIRY_POWER_RECEPTOR.get())
                .pattern("IRI")
                .pattern("PDP")
                .pattern("SFS")
                .unlockedBy(getHasName(ModItems.FAIRY_DUST.get()), has(ModItems.FAIRY_DUST.get()))
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Fume Distillery
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FUME_DISTILLERY.get(), 1)
                .define('C', Items.COPPER_INGOT)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('B', Blocks.COPPER_BLOCK)
                .define('R', ModItems.FAIRY_POWER_RECEPTOR.get())
                .pattern("LCC")
                .pattern("LBB")
                .pattern("LRR")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Fume Spreader
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FUME_SPREADER.get(), 1)
                .define('C', Items.COPPER_INGOT)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('B', Blocks.COPPER_BLOCK)
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .pattern("CCC")
                .pattern("PBP")
                .pattern("L L")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(consumer);

        //Basic altar
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INFUSION_ALTAR_BASIC.get(), 1)
                .define('I', Items.GOLD_INGOT)
                .define('L', ModBlocks.GENTLIT_LOG.get())
                .define('R', ModItems.FAIRY_POWER_RECEPTOR.get())
                .define('F', ModItems.FAIRY_DUST.get())
                .define('B', Blocks.GOLD_BLOCK)
                .pattern("IFI")
                .pattern("RBR")
                .pattern("LLL")
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Fairy Power Relay
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FAIRY_POWER_RELAY.get(), 1)
                .define('I', Items.GOLD_INGOT)
                .define('R', ModItems.FAIRY_POWER_RECEPTOR.get())
                .define('E', ModItems.FAIRY_POWER_EMITTER.get())
                .define('F', ModItems.FAIRY_DUST.get())
                .pattern(" F ")
                .pattern("FEF")
                .pattern("IRI")
                .unlockedBy(getHasName(ModItems.FAIRY_POWER_RECEPTOR.get()), has(ModItems.FAIRY_POWER_RECEPTOR.get()))
                .save(consumer);

        //Syrup Extractor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SYRUP_EXTRACTOR.get(), 1)
                .define('P', ModBlocks.GENTLIT_PLANKS.get())
                .pattern("P P")
                .pattern("P P")
                .pattern(" P ")
                .unlockedBy(getHasName(ModBlocks.GENTLIT_PLANKS.get()), has(ModBlocks.GENTLIT_PLANKS.get()))
                .save(consumer);

        //Fairy Staff
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FAIRY_STAFF.get(), 1)
                .define('S', Items.STICK)
                .define('G', Items.GOLD_INGOT)
                .define('A', Items.AMETHYST_SHARD)
                .pattern(" AS")
                .pattern(" SG")
                .pattern("S  ")
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(consumer);

        //Binding staff
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BINDING_STAFF.get(), 1)
                .define('S', Items.STICK)
                .define('G', Items.GOLD_INGOT)
                .define('F', ModItems.FAIRY_DUST.get())
                .pattern(" GF")
                .pattern(" SG")
                .pattern("S  ")
                .unlockedBy(getHasName(ModItems.FAIRY_DUST.get()), has(ModItems.FAIRY_DUST.get()))
                .save(consumer);

        //Binding staff
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CLOCKWORK_CARD_PUNCHER.get(), 1)
                .define('S', Items.PAPER)
                .define('E', ModItems.CLOCKWORK_PROCESSOR.get())
                .define('G', ModItems.CLOCKWORK_GEAR.get())
                .define('F', ModItems.CLOCKWORK_GEAR.get())
                .pattern("GEG")
                .pattern("FSF")
                .unlockedBy(getHasName(ModItems.FAIRY_DUST.get()), has(ModItems.FAIRY_DUST.get()))
                .save(consumer);

        //Hootcat plume
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HOOTCAT_PLUME.get(), 1)
                .define('H', ModItems.HOOTCAT_FEATHER.get())
                .define('G', Items.GOLD_INGOT)
                .pattern("HHH")
                .pattern(" G ")
                .unlockedBy(getHasName(ModItems.HOOTCAT_FEATHER.get()), has(ModItems.HOOTCAT_FEATHER.get()))
                .save(consumer);

        //Silky Stuff
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_SWORD.get(), 1)
                .define('S', Items.STICK)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("I")
                .pattern("C")
                .pattern("S")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_AXE.get(), 1)
                .define('C', Items.STICK)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('S', ModItems.SILKY_CRYSTAL.get())
                .pattern("CI")
                .pattern("SI")
                .pattern("S ")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_PICKAXE.get(), 1)
                .define('S', Items.STICK)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("ICI")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_SHOVEL.get(), 1)
                .define('S', Items.STICK)
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("C")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_HOE.get(), 1)
                .define('S', Items.STICK)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("CI")
                .pattern("S ")
                .pattern("S ")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_HELMET.get(), 1)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("ICI")
                .pattern("I I")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_CHESTPLATE.get(), 1)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("I I")
                .pattern("ICI")
                .pattern("III")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_LEGGINGS.get(), 1)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("ICI")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILKY_BOOTS.get(), 1)
                .define('I', ModItems.SILKY_INGOT.get())
                .define('C', ModItems.SILKY_CRYSTAL.get())
                .pattern("C C")
                .pattern("I I")
                .unlockedBy(getHasName(ModItems.SILKY_INGOT.get()), has(ModItems.SILKY_INGOT.get()))
                .unlockedBy(getHasName(ModItems.SILKY_CRYSTAL.get()), has(ModItems.SILKY_CRYSTAL.get()))
                .save(consumer);

        //Hootcat armor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HOOTCAT_HELMET.get(), 1)
                .define('H', ModItems.HOOTCAT_FEATHER.get())
                .define('G', Items.GOLD_INGOT)
                .define('P', ModItems.HOOTCAT_PLUME.get())
                .pattern("GPG")
                .pattern("H H")
                .unlockedBy(getHasName(ModItems.HOOTCAT_FEATHER.get()), has(ModItems.HOOTCAT_FEATHER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HOOTCAT_CHESTPLATE.get(), 1)
                .define('H', ModItems.HOOTCAT_FEATHER.get())
                .define('G', Items.GOLD_INGOT)
                .define('P', ModItems.HOOTCAT_PLUME.get())
                .pattern("G G")
                .pattern("GPG")
                .pattern("HHH")
                .unlockedBy(getHasName(ModItems.HOOTCAT_FEATHER.get()), has(ModItems.HOOTCAT_FEATHER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HOOTCAT_LEGGINGS.get(), 1)
                .define('H', ModItems.HOOTCAT_FEATHER.get())
                .define('G', Items.GOLD_INGOT)
                .define('P', ModItems.HOOTCAT_PLUME.get())
                .pattern("GPG")
                .pattern("H H")
                .pattern("H H")
                .unlockedBy(getHasName(ModItems.HOOTCAT_FEATHER.get()), has(ModItems.HOOTCAT_FEATHER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HOOTCAT_BOOTS.get(), 1)
                .define('H', ModItems.HOOTCAT_FEATHER.get())
                .define('G', Items.GOLD_INGOT)
                .pattern("G G")
                .pattern("H H")
                .unlockedBy(getHasName(ModItems.HOOTCAT_FEATHER.get()), has(ModItems.HOOTCAT_FEATHER.get()))
                .save(consumer);

        //Cosmetics
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BUNNY_EARS.get(), 1)
                .define('F', ModItems.SILK_FABRIC.get())
                .pattern("F F")
                .pattern("F F")
                .pattern("F F")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CAT_EARS.get(), 1)
                .define('F', ModItems.SILK_FABRIC.get())
                .define('B', Items.BLACK_DYE)
                .pattern("FBF")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEAD_BOW.get(), 1)
                .define('F', ModItems.SILK_FABRIC.get())
                .define('P', Items.PINK_DYE)
                .pattern("FPF")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GAS_MASK.get(), 1)
                .define('F', ModItems.SILK_FABRIC.get())
                .define('C', Items.COAL)
                .define('R', ModItems.TOUGH_SILK_FABRIC.get())
                .pattern(" F ")
                .pattern("RFR")
                .pattern("CFC")
                .unlockedBy(getHasName(ModItems.SILK_FABRIC.get()), has(ModItems.SILK_FABRIC.get()))
                .save(consumer);
    }

    private void cookingFoodRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        smeltingFoodRecipe(material, result, consumer);
        smokingFoodRecipe(material, result, consumer);
        campfireFoodRecipe(material, result, consumer);
    }

    private void smeltingFoodRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material),
                        RecipeCategory.FOOD, result, (float) 0.35, 200, RecipeSerializer.SMELTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smelting");
    }

    private void smokingFoodRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material),
                        RecipeCategory.FOOD, result, (float) 0.35, 100, RecipeSerializer.SMOKING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smoking");
    }

    private void campfireFoodRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material),
                        RecipeCategory.FOOD, result, (float) 0.35, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_campfire_cooking");
    }

    private void smeltingRecipe(ItemLike material, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material),
                        RecipeCategory.MISC, result, experience, 200, RecipeSerializer.SMELTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smelting_" + getItemName(material));
    }

    private void oreSmeltingRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material),
                        RecipeCategory.MISC, result, (float) 0.35, 200, RecipeSerializer.BLASTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_blasting");
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material),
                        RecipeCategory.MISC, result, (float) 0.35, 200, RecipeSerializer.SMELTING_RECIPE)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_from_smelting");
    }

    private void compact2By2(ItemLike material, ItemLike result, int count, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(material) + "_compacting");
    }

    private void brickBlock(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 4)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(material) + "_bricking");
    }

    private void compact3By3(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 1)
                .define('#', material)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(material) + "_compacting");
    }

    private void uncompact(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 9)
                .requires(material, 1)
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_uncompacting");
    }

    private void upgradeSmithing(ItemLike template, ItemLike upgradable, ItemLike material, Item result, Consumer<FinishedRecipe> consumer) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(upgradable),
                        Ingredient.of(material),
                        RecipeCategory.MISC, result)
                .unlocks("has_" + getItemName(material), has(material))
                .unlocks("has_" + getItemName(template), has(template))
                .save(consumer, BuntsyMod.MODID + ":" + getItemName(result) + "_smithing");
    }

    private void planksRecipe(TagKey<Item> tags, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 4)
                .requires(Ingredient.of(tags), 1)
                .unlockedBy(tags.toString(), has(tags))
                .save(consumer);
    }

    private void trapdoorRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 2)
                .define('#', material)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void slabRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 6)
                .define('#', material)
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void wallRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 6)
                .define('#', material)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void stairsRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 4)
                .define('#', material)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void doorRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void fenceRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .define('S', Items.STICK)
                .pattern("#S#")
                .pattern("#S#")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void fencegateRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .define('S', Items.STICK)
                .pattern("S#S")
                .pattern("S#S")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void pressurePlateRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 3)
                .define('#', material)
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer);
    }

    private void buttonRecipe(ItemLike material, ItemLike result, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 1)
                .requires(Ingredient.of(material), 1)
                .unlockedBy(material.toString(), has(material))
                .save(consumer);
    }
}
