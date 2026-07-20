package net.sophiebun.buntsy.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkFairyTerminalEntity;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkMaidenTerminalEntity;
import software.bernie.geckolib.model.GeoModel;

public class ClockworkMaidenTerminalModel extends GeoModel<ClockworkMaidenTerminalEntity> {

    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "geo/clockwork_maiden_terminal.geo.json");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "animations/clockwork_maiden_terminal.animation.json");

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BuntsyMod.MODID, "textures/block/clockwork_maiden_terminal.png");

    @Override
    public ResourceLocation getModelResource(ClockworkMaidenTerminalEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ClockworkMaidenTerminalEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ClockworkMaidenTerminalEntity animatable) {
        return ANIMATION;
    }
}
