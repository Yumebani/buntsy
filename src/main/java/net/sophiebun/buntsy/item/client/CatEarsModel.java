package net.sophiebun.buntsy.item.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.custom.CatEarsItem;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CatEarsModel extends GeoModel<CatEarsItem> {

    private ResourceLocation model = new ResourceLocation(BuntsyMod.MODID, "geo/cat_ears.geo.json");
    private ResourceLocation texture = new ResourceLocation(BuntsyMod.MODID, "textures/armor/cat_ears.png");

    @Override
    public ResourceLocation getModelResource(CatEarsItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(CatEarsItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(CatEarsItem animatable) {
        return null;
    }
}
