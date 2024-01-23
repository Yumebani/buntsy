package net.sophiebun.buntsy.blocks.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WaterLayer extends GeoRenderLayer<ThreadReelerBlockEntity> {
    public WaterLayer(GeoRenderer<ThreadReelerBlockEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ThreadReelerBlockEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType renderTypeTrans = RenderType.entityTranslucent(new ResourceLocation(BuntsyMod.MODID, "textures/block/thread_reeler_water.png"));

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderTypeTrans,
                bufferSource.getBuffer(renderTypeTrans), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 0.8f);
    }
}
