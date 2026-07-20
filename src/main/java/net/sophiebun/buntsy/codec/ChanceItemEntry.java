package net.sophiebun.buntsy.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record ChanceItemEntry(float left, ItemStack right) {

    public static final MapCodec<ChanceItemEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Codec.FLOAT.fieldOf("chance").forGetter(ChanceItemEntry::left),
                ItemStack.STRICT_CODEC.fieldOf("item").forGetter(ChanceItemEntry::right)
        ).apply(instance, ChanceItemEntry::new);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, ChanceItemEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ChanceItemEntry::left,
            ItemStack.STREAM_CODEC, ChanceItemEntry::right,
            ChanceItemEntry::new
    );
}
