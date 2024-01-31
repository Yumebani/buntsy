package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Silkbun;

public class SilkbunRenderer extends MobRenderer<Silkbun, SilkbunModel<Silkbun>> {

    public static final ResourceLocation WHITE_SILKBUN = new ResourceLocation(BuntsyMod.MODID, "textures/entity/white_silkbun.png");
    public static final ResourceLocation PINK_SILKBUN = new ResourceLocation(BuntsyMod.MODID, "textures/entity/pink_silkbun.png");
    public static final ResourceLocation BLUE_SILKBUN = new ResourceLocation(BuntsyMod.MODID, "textures/entity/blue_silkbun.png");
    public SilkbunRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SilkbunModel<>(pContext.bakeLayer(ModModelLayers.SILKBUN_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Silkbun silkbunEntity) {
        return switch (silkbunEntity.getVariant()){
            case BLUE -> BLUE_SILKBUN;
            case PINK -> PINK_SILKBUN;
            default -> WHITE_SILKBUN;
        };
    }

    @Override
    public void render(Silkbun pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        if (pEntity.isBaby()){
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
