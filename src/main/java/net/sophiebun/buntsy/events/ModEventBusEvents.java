package net.sophiebun.buntsy.events;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.ModBlockEntities;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Hootcat;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.entity.monsters.Marionette;

@EventBusSubscriber(modid = BuntsyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.FUME_DISTILLERY_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.FUME_SPREADER_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MAGIC_CRYSTALIZER_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MIXER_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.GRINDING_WHEEL_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.THREAD_REELER_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_CRAFTER_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_FAIRY_TERMINAL_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_FISHER_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_GEYSER_COLLECTOR_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_POWDERED_SUGAR_COLLECTOR_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_SYRUP_EXTRACTOR_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLOCKWORK_WINDER_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.GIANT_COCOON_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.INFUSION_ALTAR_BASIC_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.INFUSION_ALTAR_ADVANCED_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.INFUSION_PEDESTAL_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.FAIRY_COLLECTION_TRAY_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.FAIRY_INFUSE_BENCH_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.OFFERING_BENCH_BLOCK_ENTITY.get(),
                (entity, side) -> entity != null ? entity.getItemHandler(side) : null
        );
    }

    @SubscribeEvent
    public static void registerAtributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SILKBUN_ENTITY.get(), Silkbun.createAtributes().build());
        event.put(ModEntities.FAIRY_ENTITY.get(), Fairy.createAtributes().build());
        event.put(ModEntities.HOOTCAT_ENTITY.get(), Hootcat.createAttributes().build());
        event.put(ModEntities.MARIONETTE.get(), Marionette.createAttributes().build());
        event.put(ModEntities.CLOCKWORK_MAIDEN_ENTITY.get(), Marionette.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.FAIRY_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Fairy::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(ModEntities.SILKBUN_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Silkbun::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(ModEntities.HOOTCAT_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Hootcat::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(ModEntities.MARIONETTE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }
}
