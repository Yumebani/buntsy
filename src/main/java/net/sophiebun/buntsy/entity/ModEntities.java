package net.sophiebun.buntsy.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Hootcat;
import net.sophiebun.buntsy.entity.animals.Silkbun;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> enitityRegister =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BuntsyMod.MODID);


    public static final RegistryObject<EntityType<Silkbun>> SILKBUN_ENTITY =
            enitityRegister.register("silkbun", () -> EntityType.Builder.of(Silkbun::new, MobCategory.AMBIENT)
                    .sized(0.5f, 0.8f).build("silkbun"));
    public static final RegistryObject<EntityType<Fairy>> FAIRY_ENTITY =
            enitityRegister.register("fairy", () -> EntityType.Builder.of(Fairy::new, MobCategory.AMBIENT)
                    .sized(0.25f, 0.25f).build("fairy"));
    public static final RegistryObject<EntityType<Hootcat>> HOOTCAT_ENTITY =
            enitityRegister.register("hootcat", () -> EntityType.Builder.of(Hootcat::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.8f).build("hootcat"));

    public static void register(IEventBus eventBus){
        enitityRegister.register(eventBus);
    }
}
