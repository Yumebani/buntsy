package net.sophiebun.buntsy.events;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Hootcat;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.entity.monsters.ClockworkMaiden;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAtributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SILKBUN_ENTITY.get(), Silkbun.createAtributes().build());
        event.put(ModEntities.FAIRY_ENTITY.get(), Fairy.createAtributes().build());
        event.put(ModEntities.HOOTCAT_ENTITY.get(), Hootcat.createAttributes().build());
        event.put(ModEntities.CLOCKWORK_MAIDEN_ENTITY.get(), ClockworkMaiden.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.FAIRY_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Fairy::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.SILKBUN_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Silkbun::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.HOOTCAT_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Hootcat::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.CLOCKWORK_MAIDEN_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
