package net.sophiebun.buntsy.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import org.apache.logging.log4j.core.jmx.Server;

@EventBusSubscriber(modid = BuntsyMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEventBusEvents {

    @SubscribeEvent
    public static void onJoinLevel(MobSpawnEvent.PositionCheck event) {
        ServerLevelAccessor level = event.getLevel();
        Entity entity = event.getEntity();

        if (entity instanceof Fairy) {
            runCheck(event, entity, level, 48, 16, 4);
        } else if (entity instanceof Silkbun){
            runCheck(event, entity, level, 32, 16, 5);
        }
    }

    private static void runCheck(MobSpawnEvent.PositionCheck event, Entity entity, ServerLevelAccessor level, int size, int sizeY, int count){
        double spawnX = entity.getX();
        double spawnY = entity.getY();
        double spawnZ = entity.getZ();

        AABB scanArea = new AABB(
                spawnX - size, spawnY - sizeY, spawnZ - size,
                spawnX + size, spawnY + sizeY, spawnZ + size
        );

        if (level.getEntitiesOfClass(event.getEntity().getClass(), scanArea).size() >= count) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }
}
