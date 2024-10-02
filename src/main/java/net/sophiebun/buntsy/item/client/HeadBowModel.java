package net.sophiebun.buntsy.item.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.custom.HeadBowItem;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class HeadBowModel extends GeoModel<HeadBowItem> {

    private ResourceLocation model = new ResourceLocation(BuntsyMod.MODID, "geo/head_bow.geo.json");
    private ResourceLocation texture = new ResourceLocation(BuntsyMod.MODID, "textures/armor/head_bow.png");

    @Override
    public ResourceLocation getModelResource(HeadBowItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(HeadBowItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(HeadBowItem animatable) {
        return null;
    }
}
