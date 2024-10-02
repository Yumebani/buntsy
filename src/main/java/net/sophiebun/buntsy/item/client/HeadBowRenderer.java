package net.sophiebun.buntsy.item.client;

import net.sophiebun.buntsy.item.custom.HeadBowItem;
import net.sophiebun.buntsy.item.custom.HootcatArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HeadBowRenderer extends GeoArmorRenderer<HeadBowItem> {
    public HeadBowRenderer() {
        super(new HeadBowModel());
    }
}
