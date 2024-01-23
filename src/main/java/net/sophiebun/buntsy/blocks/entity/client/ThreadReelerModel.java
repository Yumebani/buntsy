package net.sophiebun.buntsy.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class ThreadReelerModel extends GeoModel<ThreadReelerBlockEntity> {

    private static final ResourceLocation MODEL = new ResourceLocation(BuntsyMod.MODID, "geo/thread_reeler.geo.json");
    private static final ResourceLocation ANIMATION = new ResourceLocation(BuntsyMod.MODID, "animations/thread_reeler.animation.json");

    private static final ResourceLocation TEXTURE_IDLE = new ResourceLocation(BuntsyMod.MODID, "textures/block/thread_reeler_idle.png");
    private static final ResourceLocation TEXTURE_STOCK = new ResourceLocation(BuntsyMod.MODID, "textures/block/thread_reeler_stock.png");
    private static final ResourceLocation TEXTURE_SPECIAL = new ResourceLocation(BuntsyMod.MODID, "textures/block/thread_reeler_moth_wings.png");

    @Override
    public ResourceLocation getModelResource(ThreadReelerBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ThreadReelerBlockEntity animatable) {
        if (animatable.getBlockState().getValue(ThreadReelerBlock.RUNNING)){
            if (animatable.getBlockState().getValue(ThreadReelerBlock.SPECIAL_PROCESS)){
                return TEXTURE_SPECIAL;
            }
            return TEXTURE_STOCK;
        }
        return TEXTURE_IDLE;
    }

    @Override
    public ResourceLocation getAnimationResource(ThreadReelerBlockEntity animatable) {
        return ANIMATION;
    }
}
