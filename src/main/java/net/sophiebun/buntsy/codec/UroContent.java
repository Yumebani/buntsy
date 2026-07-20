package net.sophiebun.buntsy.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record UroContent(CompoundTag content, boolean incoming, boolean outgoing) {

    public static final Codec<UroContent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CompoundTag.CODEC.fieldOf("content").forGetter(UroContent::content),
                    Codec.BOOL.fieldOf("incoming").forGetter(UroContent::incoming),
                    Codec.BOOL.fieldOf("outgoing").forGetter(UroContent::outgoing)
            ).apply(instance, UroContent::new)
    );

    public static final StreamCodec<ByteBuf, UroContent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, UroContent::content,
            ByteBufCodecs.BOOL, UroContent::incoming,
            ByteBufCodecs.BOOL, UroContent::outgoing,
            UroContent::new
    );

    public UroContent setIncoming(boolean value){
        return new UroContent(content, value, outgoing);
    }

    public UroContent setOutgoing(boolean value){
        return new UroContent(content, incoming, value);
    }
}
