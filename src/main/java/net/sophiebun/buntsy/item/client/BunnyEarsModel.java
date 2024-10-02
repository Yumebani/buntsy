package net.sophiebun.buntsy.item.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.custom.BunnyEarsItem;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class BunnyEarsModel extends GeoModel<BunnyEarsItem> {

    private ResourceLocation model = new ResourceLocation(BuntsyMod.MODID, "geo/bunny_ears.geo.json");
    private ResourceLocation texture = new ResourceLocation(BuntsyMod.MODID, "textures/armor/silky_armor.png");

    @Override
    public ResourceLocation getModelResource(BunnyEarsItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(BunnyEarsItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(BunnyEarsItem animatable) {
        return null;
    }
}
