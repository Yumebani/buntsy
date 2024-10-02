package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Hootcat;

public class HootcatRenderer extends MobRenderer<Hootcat, HootCatModel<Hootcat>> {

    private static final ResourceLocation HOOTCAT_TEXTURE = new ResourceLocation(BuntsyMod.MODID, "textures/entity/hootcat.png");
    private static final ResourceLocation PHELINIX_TEXTURE = new ResourceLocation(BuntsyMod.MODID, "textures/entity/phelinix.png");

    public HootcatRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HootCatModel<>(pContext.bakeLayer(ModModelLayers.HOOTCAT_LAYER)), 0.0f);
        this.addLayer(new HootcatCollarLayer(this, pContext.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Hootcat hootcat) {
        return hootcat.isPhelinix() ? PHELINIX_TEXTURE : HOOTCAT_TEXTURE;
    }

    @Override
    public void render(Hootcat pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        if (pEntity.isBaby()){
            pPoseStack.scale(0.75f, 0.75f, 0.75f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
