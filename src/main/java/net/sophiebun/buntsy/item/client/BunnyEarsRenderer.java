package net.sophiebun.buntsy.item.client;

import net.sophiebun.buntsy.item.custom.BunnyEarsItem;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BunnyEarsRenderer extends GeoArmorRenderer<BunnyEarsItem> {
    public BunnyEarsRenderer() {
        super(new BunnyEarsModel());
    }
}
