package net.sophiebun.buntsy.item.client;

import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.item.custom.BunnyEarsItem;
import net.sophiebun.buntsy.item.custom.GasMaskItem;
import software.bernie.geckolib.model.GeoModel;

public class GasMaskModel extends GeoModel<GasMaskItem> {

    private ResourceLocation model = new ResourceLocation(BuntsyMod.MODID, "geo/gas_mask.geo.json");
    private ResourceLocation texture = new ResourceLocation(BuntsyMod.MODID, "textures/armor/gas_mask.png");

    @Override
    public ResourceLocation getModelResource(GasMaskItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(GasMaskItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(GasMaskItem animatable) {
        return null;
    }
}
