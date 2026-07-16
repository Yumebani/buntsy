package net.sophiebun.buntsy.item.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class HootcatArmorModel extends GeoModel<HootcatArmorItem> {

    private ResourceLocation model = new ResourceLocation(BuntsyMod.MODID, "geo/hootcat_armor.geo.json");
    private ResourceLocation texture = new ResourceLocation(BuntsyMod.MODID, "textures/armor/hootcat_armor.png");

    @Override
    public ResourceLocation getModelResource(HootcatArmorItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(HootcatArmorItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(HootcatArmorItem animatable) {
        return null;
    }
}
