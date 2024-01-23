package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Silkbun;

public class FairyRenderer extends MobRenderer<Fairy, FairyModel<Fairy>> {
    public FairyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new FairyModel<>(pContext.bakeLayer(ModModelLayers.FAIRY_LAYER)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(Fairy fairy) {
        return new ResourceLocation(BuntsyMod.MODID, "textures/entity/fairy.png");
    }

    @Override
    public void render(Fairy pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        if (pEntity.isBaby()){
            pPoseStack.scale(0.75f, 0.75f, 0.75f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, 15728640);
    }
}
