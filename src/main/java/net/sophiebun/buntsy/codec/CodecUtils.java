package net.sophiebun.buntsy.codec;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class CodecUtils {

    public static final StreamCodec<ByteBuf, Pair<Integer, Integer>> PAIR_INT_INT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Pair::getFirst,
            ByteBufCodecs.INT, Pair::getSecond,
            Pair::of
    );

    public static final StreamCodec<ByteBuf, Pair<Integer, String>> PAIR_INT_STRING_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Pair::getFirst,
            ByteBufCodecs.STRING_UTF8, Pair::getSecond,
            Pair::of
    );

    public static final StreamCodec<ByteBuf, Pair<Boolean, Boolean>> PAIR_BOOL_BOOL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, Pair::getFirst,
            ByteBufCodecs.BOOL, Pair::getSecond,
            Pair::of
    );
}
