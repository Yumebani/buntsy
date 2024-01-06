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

                        pOutput.accept(ModBlocks.GENTLIT_LEAVES.get());
                        pOutput.accept(ModBlocks.GENTLIT_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_GENTLIT_LOG.get());
                        pOutput.accept(ModBlocks.GENTLIT_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_GENTLIT_LOG.get());
                        pOutput.accept(ModBlocks.GENTLIT_PLANKS.get());

                        pOutput.accept(ModItems.STRAWBERRY.get());
                        pOutput.accept(ModItems.GOLDEN_STRAWBERRY.get());

                    })
                    .build());
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
