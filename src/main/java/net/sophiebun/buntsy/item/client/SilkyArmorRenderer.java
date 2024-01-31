package net.sophiebun.buntsy.item.client;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.sophiebun.buntsy.item.custom.SilkyArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SilkyArmorRenderer extends GeoArmorRenderer<SilkyArmorItem> {
    public SilkyArmorRenderer() {
        super(new SilkyArmorModel());
    }
}
