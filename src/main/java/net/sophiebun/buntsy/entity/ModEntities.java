package net.sophiebun.buntsy.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Hootcat;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.entity.clockwork_maiden.ClockworkMaiden;
import net.sophiebun.buntsy.entity.monsters.Marionette;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> enitityRegister =
            DeferredRegister.create(Registries.ENTITY_TYPE, BuntsyMod.MODID);


    public static final DeferredHolder<EntityType<?>, EntityType<Silkbun>> SILKBUN_ENTITY =
            enitityRegister.register("silkbun", () -> EntityType.Builder.of(Silkbun::new, MobCategory.AMBIENT)
                    .sized(0.5f, 0.8f).build("silkbun"));
    public static final DeferredHolder<EntityType<?>, EntityType<Fairy>> FAIRY_ENTITY =
            enitityRegister.register("fairy", () -> EntityType.Builder.of(Fairy::new, MobCategory.AMBIENT)
                    .sized(0.25f, 0.25f).build("fairy"));
    public static final DeferredHolder<EntityType<?>, EntityType<Hootcat>> HOOTCAT_ENTITY =
            enitityRegister.register("hootcat", () -> EntityType.Builder.of(Hootcat::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.8f).build("hootcat"));
    public static final DeferredHolder<EntityType<?>, EntityType<Marionette>> MARIONETTE =
            enitityRegister.register("marionette", () -> EntityType.Builder.of(Marionette::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.6f).build("marionette"));
    public static final DeferredHolder<EntityType<?>, EntityType<ClockworkMaiden>> CLOCKWORK_MAIDEN_ENTITY =
            enitityRegister.register("clockwork_maiden", () -> EntityType.Builder.of(ClockworkMaiden::new, MobCategory.MISC)
                    .sized(0.5f, 1.6f).build("clockwork_maiden"));

    public static void register(IEventBus eventBus){
        enitityRegister.register(eventBus);
    }
}
