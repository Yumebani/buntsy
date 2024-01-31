package net.sophiebun.buntsy.item.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.custom.SilkyArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class SilkyArmorModel extends GeoModel<SilkyArmorItem> {

    private ResourceLocation model = new ResourceLocation(BuntsyMod.MODID, "geo/silky_armor.geo.json");
    private ResourceLocation texture = new ResourceLocation(BuntsyMod.MODID, "textures/armor/silky_armor.png");

    @Override
    public ResourceLocation getModelResource(SilkyArmorItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(SilkyArmorItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(SilkyArmorItem animatable) {
        return null;
    }
}
