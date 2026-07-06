package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.monsters.Marionette;

public class MarionetteRenderer extends MobRenderer<Marionette, MarionetteModel<Marionette>> {
    public MarionetteRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MarionetteModel<>(pContext.bakeLayer(ModModelLayers.MARIONETTE_LAYER)), 0.5f);
        this.addLayer(new MarionetteHandItemLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Marionette maiden) {
        return new ResourceLocation(BuntsyMod.MODID, "textures/entity/marionette.png");
    }

    @Override
    public void render(Marionette pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
