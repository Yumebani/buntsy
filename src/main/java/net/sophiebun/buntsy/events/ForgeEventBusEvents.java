package net.sophiebun.buntsy.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Silkbun;

@EventBusSubscriber(modid = BuntsyMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEventBusEvents {

    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        ServerLevelAccessor level = event.getLevel();
        Entity entity = event.getEntity();

        if (entity instanceof Fairy) {
            runCheck(event, level, 48, 16, 4);
        } else if (entity instanceof Silkbun){
            runCheck(event, level, 32, 16, 5);
        }
    }

    private static void runCheck(FinalizeSpawnEvent event, ServerLevelAccessor level, int size, int sizeY, int count){
        double spawnX = event.getX();
        double spawnY = event.getY();
        double spawnZ = event.getZ();

        AABB scanArea = new AABB(
                spawnX - size, spawnY - sizeY, spawnZ - size,
                spawnX + size, spawnY + sizeY, spawnZ + size
        );

        if (level.getEntitiesOfClass(event.getEntity().getClass(), scanArea).size() >= count) {
            event.setSpawnCancelled(true);
            return;
        }

        if (level.getRawBrightness(event.getEntity().blockPosition(), 0) < 4) {
            event.setSpawnCancelled(true);
        }
    }
}
