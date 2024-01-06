package net.sophiebun.buntsy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.custom.ModFlammableRotatedPillarBlock;
import net.sophiebun.buntsy.blocks.custom.ModLeaves;
import net.sophiebun.buntsy.blocks.custom.ModPlanks;
import net.sophiebun.buntsy.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BlocksRegister =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BuntsyMod.MODID);

    public static final RegistryObject<Block> GENTLIT_LOG = registerBlock("gentlit_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_GENTLIT_LOG = registerBlock("stripped_gentlit_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> GENTLIT_WOOD = registerBlock("gentlit_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_GENTLIT_WOOD = registerBlock("stripped_gentlit_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> GENTLIT_LEAVES = registerBlock("gentlit_leaves",
            () -> new ModLeaves(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> GENTLIT_PLANKS = registerBlock("gentlit_planks",
            () -> new ModPlanks(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toRet = BlocksRegister.register(name, block);
        registerBlockItem(name, toRet);
        return toRet;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ItemsRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BlocksRegister.register(eventBus);
    }
}
