package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.clockwork_maiden.ClockworkMaiden;
import net.sophiebun.buntsy.entity.monsters.Marionette;

public class ClockworkMaidenRenderer extends MobRenderer<ClockworkMaiden, ClockworkMaidenModel<ClockworkMaiden>> {
    public ClockworkMaidenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ClockworkMaidenModel<>(pContext.bakeLayer(ModModelLayers.CLOCKWORK_MAIDEN_LAYER)), 0.5f);
        this.addLayer(new ClockworkMaidenHandItemLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(ClockworkMaiden maiden) {
        return new ResourceLocation(BuntsyMod.MODID, "textures/entity/clockwork_maiden.png");
    }

    @Override
    public void render(ClockworkMaiden pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
