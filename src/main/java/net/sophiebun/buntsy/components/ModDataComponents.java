package net.sophiebun.buntsy.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.codec.CatalystType;
import net.sophiebun.buntsy.codec.CodecUtils;
import net.sophiebun.buntsy.codec.FumeType;
import net.sophiebun.buntsy.codec.UroContent;

import java.util.stream.IntStream;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, BuntsyMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ESSENCE_TYPE =
            COMPONENTS.register("essence_type", () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> PRISM_TYPE =
            COMPONENTS.register("prism_type", () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FumeType>> FUME_TYPE =
            COMPONENTS.register("fume_type", () -> DataComponentType.<FumeType>builder()
                    .persistent(FumeType.CODEC)
                    .networkSynchronized(FumeType.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CatalystType>> CATALYST_TYPE =
            COMPONENTS.register("catalyst_type", () -> DataComponentType.<CatalystType>builder()
                    .persistent(CatalystType.CODEC)
                    .networkSynchronized(CatalystType.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> URO_ID =
            COMPONENTS.register("uro_id", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UroContent>> URO_CONTENT =
            COMPONENTS.register("uro_content", () -> DataComponentType.<UroContent>builder()
                    .persistent(UroContent.CODEC)
                    .networkSynchronized(UroContent.STREAM_CODEC)
                    .build());

    public static void register(IEventBus eventBus){
        COMPONENTS.register(eventBus);
    }
}
