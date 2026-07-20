package net.sophiebun.buntsy.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FumeType(int type, int level) {

    public static final Codec<FumeType> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("type").forGetter(FumeType::type),
                    Codec.INT.fieldOf("level").forGetter(FumeType::level)
            ).apply(instance, FumeType::new)
    );

    public static final StreamCodec<ByteBuf, FumeType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FumeType::type,
            ByteBufCodecs.INT, FumeType::level,
            FumeType::new
    );
}
