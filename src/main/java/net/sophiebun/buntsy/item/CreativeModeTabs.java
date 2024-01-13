package net.sophiebun.buntsy.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class CreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BuntsyMod.MODID);

    public static final RegistryObject<CreativeModeTab> BUNTSY_TAB = CREATIVE_MODE_TAB.register("buntsy_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STRAWBERRY.get()))
                    .title(Component.translatable("creativetab.buntsy_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModBlocks.PINK_BLOOM_GRASS_BLOCK.get());

                        pOutput.accept(ModBlocks.GENTLIT_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_GENTLIT_LOG.get());
                        pOutput.accept(ModBlocks.GENTLIT_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_GENTLIT_LOG.get());

                        pOutput.accept(ModBlocks.GENTLIT_LEAVES.get());
                        pOutput.accept(ModBlocks.GENTLIT_SAPLING.get());

                        pOutput.accept(ModBlocks.GENTLIT_PLANKS.get());
                        pOutput.accept(ModBlocks.GENTLIT_STAIRS.get());
                        pOutput.accept(ModBlocks.GENTLIT_SLAB.get());
                        pOutput.accept(ModBlocks.GENTLIT_TRAPDOOR.get());
                        pOutput.accept(ModBlocks.GENTLIT_DOOR.get());
                        pOutput.accept(ModBlocks.GENTLIT_FENCE.get());
                        pOutput.accept(ModBlocks.GENTLIT_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.GENTLIT_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.GENTLIT_BUTTON.get());

                        pOutput.accept(ModBlocks.BRAVOT_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_BRAVOT_LOG.get());
                        pOutput.accept(ModBlocks.BRAVOT_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_BRAVOT_LOG.get());

                        pOutput.accept(ModBlocks.BRAVOT_LEAVES.get());
                        pOutput.accept(ModBlocks.BRAVOT_SAPLING.get());

                        pOutput.accept(ModBlocks.BRAVOT_PLANKS.get());
                        pOutput.accept(ModBlocks.BRAVOT_STAIRS.get());
                        pOutput.accept(ModBlocks.BRAVOT_SLAB.get());
                        pOutput.accept(ModBlocks.BRAVOT_TRAPDOOR.get());
                        pOutput.accept(ModBlocks.BRAVOT_DOOR.get());
                        pOutput.accept(ModBlocks.BRAVOT_FENCE.get());
                        pOutput.accept(ModBlocks.BRAVOT_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.BRAVOT_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.BRAVOT_BUTTON.get());

                        pOutput.accept(ModBlocks.GRINDING_WHEEL.get());
                        pOutput.accept(ModBlocks.THREAD_REELER.get());
                        pOutput.accept(ModBlocks.FAIRY_TERRARIUM.get());


                        pOutput.accept(ModItems.AMETHYST_DUST.get());
                        pOutput.accept(ModItems.FAIRY_DUST.get());
                        pOutput.accept(ModItems.COCOON.get());
                        pOutput.accept(ModItems.SILK.get());
                        pOutput.accept(ModItems.SILK_SPOOL.get());
                        pOutput.accept(ModItems.SILK_FABRIC.get());
                        pOutput.accept(ModItems.MOLTED_MOTH_WINGS.get());
                        pOutput.accept(ModItems.MOTH_WING_THREAD.get());
                        pOutput.accept(ModItems.TOUGH_SILK_FABRIC.get());
                        pOutput.accept(ModItems.SILKY_NUGGET.get());
                        pOutput.accept(ModItems.SILKY_INGOT.get());
                        pOutput.accept(ModItems.SILKY_CRYSTAL.get());

                        pOutput.accept(ModItems.SILKY_SWORD.get());
                        pOutput.accept(ModItems.SILKY_PICKAXE.get());
                        pOutput.accept(ModItems.SILKY_AXE.get());
                        pOutput.accept(ModItems.SILKY_SHOVEL.get());
                        pOutput.accept(ModItems.SILKY_HOE.get());
                        pOutput.accept(ModItems.SILKY_HELMET.get());
                        pOutput.accept(ModItems.SILKY_CHESTPLATE.get());
                        pOutput.accept(ModItems.SILKY_LEGGINGS.get());
                        pOutput.accept(ModItems.SILKY_BOOTS.get());
                        pOutput.accept(ModItems.FAIRY_IN_A_BOTTLE.get());

                        pOutput.accept(ModItems.HOOTNIP.get());
                        pOutput.accept(ModItems.GROUND_HOOTNIP.get());
                        pOutput.accept(ModItems.HOOTNIP_CEREAL.get());
                        pOutput.accept(ModItems.BOWL_OF_ROCKCANDY.get());
                        pOutput.accept(ModItems.GENTLIT_SYRUP.get());
                        pOutput.accept(ModItems.SUGAR_BOWL.get());
                        pOutput.accept(ModItems.SYRUPY_MIXTURE_BOWL.get());
                        pOutput.accept(ModItems.BOWL_OF_CARAMEL.get());
                        pOutput.accept(ModItems.STRAWBERRY.get());
                        pOutput.accept(ModItems.CARAMEL_STRAWBERRIES.get());
                        pOutput.accept(ModItems.GOLDEN_STRAWBERRY.get());

                    })
                    .build());
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
