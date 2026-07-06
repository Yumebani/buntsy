package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sophiebun.buntsy.entity.monsters.Marionette;

@OnlyIn(Dist.CLIENT)
public class ClockworkMaidenHandItemLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public ClockworkMaidenHandItemLayer(RenderLayerParent<T, M> pRenderer, ItemInHandRenderer pItemInHandRenderer) {
        super(pRenderer);
        this.itemInHandRenderer = pItemInHandRenderer;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstackRight = pLivingEntity.getMainHandItem();
        ItemStack itemstackLeft = pLivingEntity.getOffhandItem();
        if (!itemstackRight.isEmpty() || !itemstackLeft.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 1.15F, 0.0F);
            pPoseStack.scale(0.75F, 0.75F, 0.75F);
            this.renderArmWithItem(pLivingEntity, itemstackRight, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, pPoseStack, pBuffer, pPackedLight);
            this.renderArmWithItem(pLivingEntity, itemstackLeft, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, pPoseStack, pBuffer, pPackedLight);
            pPoseStack.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (!pItemStack.isEmpty()) {
            pPoseStack.pushPose();
            ((ClockworkMaidenModel<Marionette>) this.getParentModel()).getHand(pArm).translateAndRotate(pPoseStack);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean flag = pArm == HumanoidArm.LEFT;
            pPoseStack.mulPose(Axis.YP.rotationDegrees(flag? 17.5f : -17.5f));
            pPoseStack.translate((float)(flag ? -1 : 1) / 6F, 0.125F, -0.625F);
            this.itemInHandRenderer.renderItem(pLivingEntity, pItemStack, pDisplayContext, flag, pPoseStack, pBuffer, pPackedLight);
            pPoseStack.popPose();
        }
    }
}
