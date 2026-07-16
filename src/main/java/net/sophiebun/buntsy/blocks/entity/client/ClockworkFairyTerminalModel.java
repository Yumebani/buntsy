package net.sophiebun.buntsy.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkFairyTerminalEntity;
import software.bernie.geckolib.model.GeoModel;

public class ClockworkFairyTerminalModel extends GeoModel<ClockworkFairyTerminalEntity> {

    private static final ResourceLocation MODEL = new ResourceLocation(BuntsyMod.MODID, "geo/clockwork_fairy_terminal.geo.json");
    private static final ResourceLocation ANIMATION = new ResourceLocation(BuntsyMod.MODID, "animations/clockwork_fairy_terminal.animation.json");

    private static final ResourceLocation TEXTURE_IDLE = new ResourceLocation(BuntsyMod.MODID, "textures/block/clockwork_fairy_terminal.png");

    @Override
    public ResourceLocation getModelResource(ClockworkFairyTerminalEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ClockworkFairyTerminalEntity animatable) {
        return TEXTURE_IDLE;
    }

    @Override
    public ResourceLocation getAnimationResource(ClockworkFairyTerminalEntity animatable) {
        return ANIMATION;
    }
}
