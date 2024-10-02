package net.sophiebun.buntsy.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;

public class ModModelLayers {
    public static final ModelLayerLocation SILKBUN_LOCATION = new ModelLayerLocation(
            new ResourceLocation(BuntsyMod.MODID, "silkbun_layer"), "main");
    public static final ModelLayerLocation FAIRY_LAYER = new ModelLayerLocation(
            new ResourceLocation(BuntsyMod.MODID, "fairy_layer"), "main");
    public static final ModelLayerLocation HOOTCAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(BuntsyMod.MODID, "hootcat_layer"), "main");
    public static final ModelLayerLocation HOOTCAT_COLLAR_LAYER = new ModelLayerLocation(
            new ResourceLocation(BuntsyMod.MODID, "hootcat_collar_layer"), "main");
}
