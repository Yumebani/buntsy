package net.sophiebun.buntsy.item.client;

import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HootcatArmorRenderer extends GeoArmorRenderer<HootcatArmorItem> {
    public HootcatArmorRenderer() {
        super(new HootcatArmorModel());
    }
}
