package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;
import net.sophiebun.buntsy.blocks.entity.basicfairy.GrindingWheelBlockEntity;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyCollectionTrayBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyInfusionBenchBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;

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

    public static void register(IEventBus eventBus){

        blockEntityRegister.register(eventBus);
    }
}
