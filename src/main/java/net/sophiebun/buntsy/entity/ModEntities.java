package net.sophiebun.buntsy.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.SilkbunEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> enitityRegister =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BuntsyMod.MODID);

    public static final RegistryObject<EntityType<SilkbunEntity>> SILKBUN_ENTITY =
            enitityRegister.register("silkbun", () -> EntityType.Builder.of(SilkbunEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 0.4f).build("silkbun"));

    public static void register(IEventBus eventBus){
        enitityRegister.register(eventBus);
    }
}
