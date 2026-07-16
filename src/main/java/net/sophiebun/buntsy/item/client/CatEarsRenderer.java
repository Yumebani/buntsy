package net.sophiebun.buntsy.item.client;

import net.sophiebun.buntsy.item.custom.CatEarsItem;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CatEarsRenderer extends GeoArmorRenderer<CatEarsItem> {
    public CatEarsRenderer() {
        super(new CatEarsModel());
    }
}
