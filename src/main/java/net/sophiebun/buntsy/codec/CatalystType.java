package net.sophiebun.buntsy.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CatalystType(int type, String function) {

    public static final Codec<CatalystType> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("type").forGetter(CatalystType::type),
                    Codec.STRING.fieldOf("function").forGetter(CatalystType::function)
            ).apply(instance, CatalystType::new)
    );

    public static final StreamCodec<ByteBuf, CatalystType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CatalystType::type,
            ByteBufCodecs.STRING_UTF8, CatalystType::function,
            CatalystType::new
    );
}
