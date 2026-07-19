package net.sophiebun.buntsy.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkWinderEntity;
import software.bernie.geckolib.model.GeoModel;

public class ClockworkWinderModel extends GeoModel<ClockworkWinderEntity> {

    private static final ResourceLocation MODEL = new ResourceLocation(BuntsyMod.MODID, "geo/clockwork_winder.geo.json");
    private static final ResourceLocation ANIMATION = new ResourceLocation(BuntsyMod.MODID, "animations/clockwork_winder.animation.json");

    private static final ResourceLocation TEXTURE_OFF = new ResourceLocation(BuntsyMod.MODID, "textures/block/clockwork_winder_full_off.png");
    private static final ResourceLocation TEXTURE_ON = new ResourceLocation(BuntsyMod.MODID, "textures/block/clockwork_winder_full_on.png");

    @Override
    public ResourceLocation getModelResource(ClockworkWinderEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ClockworkWinderEntity animatable) {
        return animatable.isBurning() ? TEXTURE_ON : TEXTURE_OFF;
    }

    @Override
    public ResourceLocation getAnimationResource(ClockworkWinderEntity animatable) {
        return ANIMATION;
    }
}
