package net.sophiebun.buntsy.item.client;

import net.sophiebun.buntsy.item.custom.BunnyEarsItem;
import net.sophiebun.buntsy.item.custom.GasMaskItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GasMaskRenderer extends GeoArmorRenderer<GasMaskItem> {
    public GasMaskRenderer() {
        super(new GasMaskModel());
    }
}
