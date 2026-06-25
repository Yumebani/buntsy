package net.sophiebun.buntsy.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.item.custom.Essence;
import net.sophiebun.buntsy.item.custom.FumeBottle;
import net.sophiebun.buntsy.item.custom.Prism;

public class CreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BuntsyMod.MODID);

    public static final RegistryObject<CreativeModeTab> BUNTSY_TAB = CREATIVE_MODE_TAB.register("buntsy_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STRAWBERRY.get()))
                    .title(Component.translatable("creativetab.buntsy_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModBlocks.PINK_FLUF_CHARMIL_SOIL.get());
                        pOutput.accept(ModBlocks.CHARMIL_SOIL.get());
                        pOutput.accept(ModBlocks.SWEET_CORAL_SAND.get());

                        pOutput.accept(ModBlocks.SWEET_CANDY_ROCK.get());
                        pOutput.accept(ModBlocks.SOUR_CANDY_ROCK.get());
                        pOutput.accept(ModBlocks.BITTER_CANDY_ROCK.get());

                        pOutput.accept(ModBlocks.SWEET_CORAL.get());
                        pOutput.accept(ModBlocks.SWEET_CORAL_BLOCK.get());
                        pOutput.accept(ModBlocks.SWEET_CORAL_FAN.get());
                        pOutput.accept(ModBlocks.DEAD_SWEET_CORAL.get());
                        pOutput.accept(ModBlocks.DEAD_SWEET_CORAL_BLOCK.get());
                        pOutput.accept(ModBlocks.DEAD_SWEET_CORAL_FAN.get());

                        pOutput.accept(ModBlocks.BITTER_CORAL.get());
                        pOutput.accept(ModBlocks.BITTER_CORAL_BLOCK.get());
                        pOutput.accept(ModBlocks.BITTER_CORAL_FAN.get());
                        pOutput.accept(ModBlocks.DEAD_BITTER_CORAL.get());
                        pOutput.accept(ModBlocks.DEAD_BITTER_CORAL_BLOCK.get());
                        pOutput.accept(ModBlocks.DEAD_BITTER_CORAL_FAN.get());

                        pOutput.accept(ModBlocks.SWEETGRASS.get());
                        pOutput.accept(ModBlocks.SWEET_PICKLE.get());
                        pOutput.accept(ModBlocks.COTTON_VINE.get());

                        pOutput.accept(ModBlocks.PINK_CHARMIL_GRASS.get());
                        pOutput.accept(ModBlocks.BLUE_CHARMIL_GRASS.get());
                        pOutput.accept(ModBlocks.PINK_BLOOM.get());
                        pOutput.accept(ModBlocks.BLUE_BLOOM.get());
                        pOutput.accept(ModBlocks.LOVESHROOM.get());
                        pOutput.accept(ModBlocks.LOVESHROOM_BLOCK.get());
                        pOutput.accept(ModBlocks.GLOWSHROOM.get());
                        pOutput.accept(ModBlocks.GLOWSHROOM_BLOCK.get());

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

                        pOutput.accept(ModBlocks.MALVOR_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_MALVOR_LOG.get());
                        pOutput.accept(ModBlocks.MALVOR_WOOD.get());
                        pOutput.accept(ModBlocks.STRIPPED_MALVOR_LOG.get());

                        pOutput.accept(ModBlocks.MALVOR_LEAVES.get());
                        pOutput.accept(ModBlocks.MALVOR_SAPLING.get());

                        pOutput.accept(ModBlocks.MALVOR_PLANKS.get());
                        pOutput.accept(ModBlocks.MALVOR_STAIRS.get());
                        pOutput.accept(ModBlocks.MALVOR_SLAB.get());
                        pOutput.accept(ModBlocks.MALVOR_TRAPDOOR.get());
                        pOutput.accept(ModBlocks.MALVOR_DOOR.get());
                        pOutput.accept(ModBlocks.MALVOR_FENCE.get());
                        pOutput.accept(ModBlocks.MALVOR_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.MALVOR_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.MALVOR_BUTTON.get());

                        pOutput.accept(ModItems.AMETHYST_DUST.get());
                        pOutput.accept(ModItems.PRISTINE_AMETHYST_GRAIN.get());
                        pOutput.accept(ModBlocks.GROWABLE_AMETHYST_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_GROWABLE_AMETHYST_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_GROWABLE_AMETHYST_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_GROWABLE_AMETHYST_CLUSTER.get());

                        pOutput.accept(ModItems.IRON_CRYSTAL.get());
                        pOutput.accept(ModItems.IRON_DUST.get());
                        pOutput.accept(ModItems.PRISTINE_IRON_SAMPLE.get());
                        pOutput.accept(ModBlocks.IRON_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_IRON_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_IRON_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_IRON_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.COPPER_CRYSTAL.get());
                        pOutput.accept(ModItems.COPPER_DUST.get());
                        pOutput.accept(ModItems.PRISTINE_COPPER_SAMPLE.get());
                        pOutput.accept(ModBlocks.COPPER_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_COPPER_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_COPPER_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_COPPER_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.GOLD_CRYSTAL.get());
                        pOutput.accept(ModItems.GOLD_DUST.get());
                        pOutput.accept(ModItems.PRISTINE_GOLD_SAMPLE.get());
                        pOutput.accept(ModBlocks.GOLD_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_GOLD_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_GOLD_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_GOLD_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.DEBRIS_SHARD.get());
                        pOutput.accept(ModItems.NETHERITE_DUST.get());
                        pOutput.accept(ModItems.PRISTINE_DEBRIS_SAMPLE.get());
                        pOutput.accept(ModBlocks.DEBRIS_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_DEBRIS_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_DEBRIS_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_DEBRIS_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.PRISTINE_QUARTZ_SAMPLE.get());
                        pOutput.accept(ModBlocks.QUARTZ_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_QUARTZ_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_QUARTZ_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_QUARTZ_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.GLOWSTONE_CRYSTAL.get());
                        pOutput.accept(ModItems.PRISTINE_GLOWSTONE_SAMPLE.get());
                        pOutput.accept(ModBlocks.GLOWSTONE_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_GLOWSTONE_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_GLOWSTONE_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_GLOWSTONE_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.REDSTONE_CRYSTAL.get());
                        pOutput.accept(ModItems.PRISTINE_REDSTONE_SAMPLE.get());
                        pOutput.accept(ModBlocks.REDSTONE_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_REDSTONE_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_REDSTONE_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_REDSTONE_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.LAPIS_CRYSTAL.get());
                        pOutput.accept(ModItems.PRISTINE_LAPIS_SAMPLE.get());
                        pOutput.accept(ModBlocks.LAPIS_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_LAPIS_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_LAPIS_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_LAPIS_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.DIAMOND_SHARD.get());
                        pOutput.accept(ModItems.PRISTINE_DIAMOND_SAMPLE.get());
                        pOutput.accept(ModBlocks.DIAMOND_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_DIAMOND_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_DIAMOND_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_DIAMOND_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModItems.EMERALD_SHARD.get());
                        pOutput.accept(ModItems.PRISTINE_EMERALD_SAMPLE.get());
                        pOutput.accept(ModBlocks.EMERALD_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.LARGE_EMERALD_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.MEDIUM_EMERALD_CRYSTAL_CLUSTER.get());
                        pOutput.accept(ModBlocks.SMALL_EMERALD_CRYSTAL_CLUSTER.get());

                        pOutput.accept(ModBlocks.FAIRY_OFFERING_BENCH.get());
                        pOutput.accept(ModBlocks.GRINDING_WHEEL.get());
                        pOutput.accept(ModBlocks.THREAD_REELER.get());
                        pOutput.accept(ModBlocks.FAIRY_COLLECTION_TRAY.get());
                        pOutput.accept(ModBlocks.FAIRY_INFUSION_BENCH.get());
                        pOutput.accept(ModBlocks.MAGIC_CRYSTALIZER.get());
                        pOutput.accept(ModBlocks.FUME_DISTILLERY.get());
                        pOutput.accept(ModBlocks.FUME_SPREADER.get());
                        pOutput.accept(ModBlocks.INFUSION_PEDESTAL.get());
                        pOutput.accept(ModBlocks.FAIRY_POWER_RELAY.get());
                        pOutput.accept(ModBlocks.INFUSION_ALTAR_BASIC.get());
                        pOutput.accept(ModBlocks.INFUSION_ALTAR_ADVANCED.get());
                        pOutput.accept(ModBlocks.MIXER_BLOCK.get());
                        pOutput.accept(ModBlocks.GIANT_COCOON.get());
                        pOutput.accept(ModBlocks.SYRUP_EXTRACTOR.get());

                        pOutput.accept(ModBlocks.PRISMATIC_BEACON.get());
                        pOutput.accept(ModBlocks.PRISMATIC_BEACON_BASE.get());
                        pOutput.accept(ModBlocks.BEACON_WATER_BREATHING_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_STRENGTH_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_SPEED_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_JUMP_BOOST_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_RESISTANCE_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_HEALTH_BOOST_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_FIRE_RESISTANCE_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_HASTE_MODIFIER.get());
                        pOutput.accept(ModBlocks.BEACON_REGENERATION_MODIFIER.get());

                        pOutput.accept(ModItems.FAIRY_STAFF.get());
                        pOutput.accept(ModItems.BINDING_STAFF.get());

                        pOutput.accept(ModItems.FAIRY_POWER_RECEPTOR.get());
                        pOutput.accept(ModItems.FAIRY_POWER_EMITTER.get());
                        pOutput.accept(ModItems.FAIRY_DUST.get());
                        pOutput.accept(ModItems.HOOTCAT_FEATHER.get());
                        pOutput.accept(ModItems.HOOTCAT_PLUME.get());
                        pOutput.accept(ModItems.PHELINIX_FEATHER.get());
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
                        pOutput.accept(ModItems.CLOCKWORK_SCRAP.get());
                        pOutput.accept(ModItems.CLOCKWORK_BRASS.get());
                        pOutput.accept(ModItems.CLOCKWORK_GEAR.get());
                        pOutput.accept(ModItems.CLOCKWORK_PROCESSOR.get());
                        pOutput.accept(ModItems.SPEED_BLEND.get());
                        pOutput.accept(ModItems.EFFICIENCY_BLEND.get());
                        pOutput.accept(ModItems.GROWTH_BLEND.get());
                        pOutput.accept(ModItems.SLOTH_BLEND.get());
                        pOutput.accept(ModItems.ROTTEN_BLEND.get());

                        pOutput.accept(ModItems.STRANGE_COCOON.get());
                        pOutput.accept(ModItems.COCOON_BAG.get());

                        pOutput.accept(ModItems.SILKY_SWORD.get());
                        pOutput.accept(ModItems.SILKY_PICKAXE.get());
                        pOutput.accept(ModItems.SILKY_AXE.get());
                        pOutput.accept(ModItems.SILKY_SHOVEL.get());
                        pOutput.accept(ModItems.SILKY_HOE.get());
                        pOutput.accept(ModItems.SILKY_HELMET.get());
                        pOutput.accept(ModItems.SILKY_CHESTPLATE.get());
                        pOutput.accept(ModItems.SILKY_LEGGINGS.get());
                        pOutput.accept(ModItems.SILKY_BOOTS.get());
                        pOutput.accept(ModItems.HOOTCAT_HELMET.get());
                        pOutput.accept(ModItems.HOOTCAT_CHESTPLATE.get());
                        pOutput.accept(ModItems.HOOTCAT_LEGGINGS.get());
                        pOutput.accept(ModItems.HOOTCAT_BOOTS.get());
                        pOutput.accept(ModItems.BUNNY_EARS.get());
                        pOutput.accept(ModItems.CAT_EARS.get());
                        pOutput.accept(ModItems.HEAD_BOW.get());
                        pOutput.accept(ModItems.GAS_MASK.get());

                        for (String essenceType : Essence.EssenceTypes){

                            CompoundTag nbt = new CompoundTag();
                            nbt.putString("buntsy.essenceType", essenceType);
                            ItemStack stack = new ItemStack(ModItems.ESSENCE.get());
                            stack.setTag(nbt);
                            pOutput.accept(stack);
                        }

                        for (String prismType : Prism.PrismTypes){

                            CompoundTag nbt = new CompoundTag();
                            nbt.putString("buntsy.prismType", prismType);
                            ItemStack stack = new ItemStack(ModItems.PRISM.get());
                            stack.setTag(nbt);
                            pOutput.accept(stack);
                        }

                        for (FumeBottle.FumeType fumeType : FumeBottle.FumeType.values()){

                            for (int i = 1; i < 4; i++){
                                CompoundTag nbt = new CompoundTag();
                                nbt.putInt("buntsy.fumeType", fumeType.ordinal());
                                nbt.putInt("buntsy.fumeLevel", i);

                                ItemStack stack = new ItemStack(ModItems.FUME_BOTTLE.get());
                                stack.setTag(nbt);
                                pOutput.accept(stack);
                            }
                        }

                        pOutput.accept(ModItems.EMPTY_CATALYST.get());

                        for (FumeBottle.FumeType fumeType : FumeBottle.FumeType.values()){

                            CompoundTag nbt = new CompoundTag();
                            nbt.putInt("buntsy.fumeType", fumeType.ordinal());
                            nbt.putString("buntsy.catalystType", "creation");

                            ItemStack stack = new ItemStack(ModItems.CATALYST.get());
                            stack.setTag(nbt);
                            pOutput.accept(stack);

                            nbt = new CompoundTag();
                            nbt.putInt("buntsy.fumeType", fumeType.ordinal());
                            nbt.putString("buntsy.catalystType", "enhancer");

                            stack = new ItemStack(ModItems.CATALYST.get());
                            stack.setTag(nbt);
                            pOutput.accept(stack);
                        }

                        pOutput.accept(ModItems.HOOTNIP.get());
                        pOutput.accept(ModItems.BLAZING_HOOTNIP.get());
                        pOutput.accept(ModItems.HOOTNIP_SEEDS.get());
                        pOutput.accept(ModItems.GROUND_HOOTNIP.get());
                        pOutput.accept(ModItems.HOOTNIP_CEREAL.get());
                        pOutput.accept(ModItems.BOWL_OF_ROCKCANDY.get());
                        pOutput.accept(ModItems.GENTLIT_SYRUP.get());
                        pOutput.accept(ModItems.SUGAR_BOWL.get());
                        pOutput.accept(ModItems.SYRUPY_MIXTURE_BOWL.get());
                        pOutput.accept(ModItems.BOWL_OF_CARAMEL.get());
                        pOutput.accept(ModItems.STRAWBERRY.get());
                        pOutput.accept(ModItems.STRAWBERRY_SEEDS.get());
                        pOutput.accept(ModItems.CARAMEL_STRAWBERRIES.get());
                        pOutput.accept(ModItems.GOLDEN_STRAWBERRY.get());

                        pOutput.accept(ModItems.SILKBUN_SPAWN_EGG.get());
                        pOutput.accept(ModItems.FAIRY_SPAWN_EGG.get());
                        pOutput.accept(ModItems.HOOTCAT_SPAWN_EGG.get());
                        pOutput.accept(ModItems.CLOCKWORK_MAIDEN_SPAWN_EGG.get());

                    })
                    .build());
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
