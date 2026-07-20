package net.sophiebun.buntsy.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.basicfairy.GrindingWheelBlockEntity;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class GrindingWheelModel extends GeoModel<GrindingWheelBlockEntity> {

    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "geo/grinding_wheel.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "animations/grinding_wheel.animation.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "textures/block/grinding_wheel.png");

    @Override
    public ResourceLocation getModelResource(GrindingWheelBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GrindingWheelBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(GrindingWheelBlockEntity animatable) {
        return ANIMATION;
    }
}
