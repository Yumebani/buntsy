package net.sophiebun.buntsy.events;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.ModEntities;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.server.PersistantAmbientMobSavedData;
import net.sophiebun.buntsy.tag.ModTags;

@Mod.EventBusSubscriber(modid = BuntsyMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        ServerLevelAccessor level = event.getLevel();
        Entity entity = event.getEntity();

        if (entity instanceof Fairy || entity instanceof Silkbun) {

            double spawnX = event.getX();
            double spawnY = event.getY();
            double spawnZ = event.getZ();

            AABB scanArea = new AABB(
                    spawnX - 32, spawnY - 16, spawnZ - 32,
                    spawnX + 32, spawnY + 16, spawnZ + 32
            );

            if (level.getEntitiesOfClass(event.getEntity().getClass(), scanArea).size() >= 5) {
                event.setSpawnCancelled(true);
                return;
            }

            if (level.getRawBrightness(event.getEntity().blockPosition(), 0) < 4) {
                event.setSpawnCancelled(true);
            }
        }
    }
}
