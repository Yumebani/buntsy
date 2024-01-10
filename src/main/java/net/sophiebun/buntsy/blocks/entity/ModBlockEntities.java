package net.sophiebun.buntsy.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> blockEntityRegister =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BuntsyMod.MODID);

    public static final RegistryObject<BlockEntityType<GrindingWheelBlockEntity>> GRINDING_WHEEL_BLOCK_ENTITY =
            blockEntityRegister.register("grinding_wheel_block_entity",
                    () -> BlockEntityType.Builder.of(GrindingWheelBlockEntity::new,
                            ModBlocks.GRINDING_WHEEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ThreadReelerBlockEntity>> THREAD_REELER_BLOCK_ENTITY =
            blockEntityRegister.register("thread_reeler_block_entity",
                    () -> BlockEntityType.Builder.of(ThreadReelerBlockEntity::new,
                            ModBlocks.THREAD_REELER.get()).build(null));
    /*
    public static final RegistryObject<BlockEntityType<GrindingWheelBlockEntity>> FAIRY_TERRARIUM_BLOCK_ENTITY =
            blockEntityRegister.register("fairy_terrarium_block_entity",
                    () -> BlockEntityType.Builder.of(GrindingWheelBlockEntity::new,
                            ModBlocks..get()).build(null))*/

    public static void register(IEventBus eventBus){

        blockEntityRegister.register(eventBus);
    }
}
