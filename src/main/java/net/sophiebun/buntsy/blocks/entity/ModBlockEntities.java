package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.advancedfairy.FumeDistilleryBlockEntity;
import net.sophiebun.buntsy.blocks.entity.advancedfairy.FumeSpreaderBlockEntity;
import net.sophiebun.buntsy.blocks.entity.advancedfairy.MagicCrystalizerBlockEntity;
import net.sophiebun.buntsy.blocks.entity.basicfairy.GrindingWheelBlockEntity;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBasicBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionPedestalBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyCollectionTrayBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyInfusionBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyPowerRelayBlockEntity;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> blockEntityRegister =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BuntsyMod.MODID);

    public static final RegistryObject<BlockEntityType<FairyOfferingBenchBlockEntity>> OFFERING_BENCH_BLOCK_ENTITY =
            blockEntityRegister.register("offering_bench_block_entity",
                    () -> BlockEntityType.Builder.of(FairyOfferingBenchBlockEntity::new,
                            ModBlocks.FAIRY_OFFERING_BENCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<GrindingWheelBlockEntity>> GRINDING_WHEEL_BLOCK_ENTITY =
            blockEntityRegister.register("grinding_wheel_block_entity",
                    () -> BlockEntityType.Builder.of(GrindingWheelBlockEntity::new,
                            ModBlocks.GRINDING_WHEEL.get()).build(null));
    public static final RegistryObject<BlockEntityType<ThreadReelerBlockEntity>> THREAD_REELER_BLOCK_ENTITY =
            blockEntityRegister.register("thread_reeler_block_entity",
                    () -> BlockEntityType.Builder.of(ThreadReelerBlockEntity::new,
                            ModBlocks.THREAD_REELER.get()).build(null));
    public static final RegistryObject<BlockEntityType<FairyCollectionTrayBlockEntity>> FAIRY_COLLECTION_TRAY_BLOCK_ENTITY =
            blockEntityRegister.register("fairy_collection_tray_block_entity",
                    () -> BlockEntityType.Builder.of(FairyCollectionTrayBlockEntity::new,
                            ModBlocks.FAIRY_COLLECTION_TRAY.get()).build(null));
    public static final RegistryObject<BlockEntityType<FairyInfusionBenchBlockEntity>> FAIRY_INFUSE_BENCH_BLOCK_ENTITY =
            blockEntityRegister.register("fairy_infuse_bench_block_entity",
                    () -> BlockEntityType.Builder.of(FairyInfusionBenchBlockEntity::new,
                            ModBlocks.FAIRY_INFUSION_BENCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<MagicCrystalizerBlockEntity>> MAGIC_CRYSTALIZER_BLOCK_ENTITY =
            blockEntityRegister.register("magic_crystalizer_block_entity",
                    () -> BlockEntityType.Builder.of(MagicCrystalizerBlockEntity::new,
                            ModBlocks.MAGIC_CRYSTALIZER.get()).build(null));
    public static final RegistryObject<BlockEntityType<FumeDistilleryBlockEntity>> FUME_DISTILLERY_BLOCK_ENTITY =
            blockEntityRegister.register("fume_distillery_block_entity",
                    () -> BlockEntityType.Builder.of(FumeDistilleryBlockEntity::new,
                            ModBlocks.FUME_DISTILLERY.get()).build(null));
    public static final RegistryObject<BlockEntityType<FumeSpreaderBlockEntity>> FUME_SPREADER_BLOCK_ENTITY =
            blockEntityRegister.register("fume_spreader_block_entity",
                    () -> BlockEntityType.Builder.of(FumeSpreaderBlockEntity::new,
                            ModBlocks.FUME_SPREADER.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfusionPedestalBlockEntity>> INFUSION_PEDESTAL_BLOCK_ENTITY =
            blockEntityRegister.register("infusion_pedestal_block_entity",
                    () -> BlockEntityType.Builder.of(InfusionPedestalBlockEntity::new,
                            ModBlocks.INFUSION_PEDESTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<FairyPowerRelayBlockEntity>> FAIRY_POWER_RELAY_BLOCK_ENTITY =
            blockEntityRegister.register("fairy_power_relay_block_entity",
                    () -> BlockEntityType.Builder.of(FairyPowerRelayBlockEntity::new,
                            ModBlocks.FAIRY_POWER_RELAY.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfusionAltarBasicBlockEntity>> INFUSION_ALTAR_BASIC_BLOCK_ENTITY =
            blockEntityRegister.register("infusion_altar_basic_entity",
                    () -> BlockEntityType.Builder.of(InfusionAltarBasicBlockEntity::new,
                            ModBlocks.INFUSION_ALTAR_BASIC.get()).build(null));

    public static void register(IEventBus eventBus){

        blockEntityRegister.register(eventBus);
    }
}
