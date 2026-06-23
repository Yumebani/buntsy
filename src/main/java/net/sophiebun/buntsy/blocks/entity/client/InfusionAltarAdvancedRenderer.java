package net.sophiebun.buntsy.blocks.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarAdvancedBlockEntity;
import net.sophiebun.buntsy.blocks.entity.custom.InfusionAltarBasicBlockEntity;

public class InfusionAltarAdvancedRenderer implements BlockEntityRenderer<InfusionAltarAdvancedBlockEntity> {

    public InfusionAltarAdvancedRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(InfusionAltarAdvancedBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = pBlockEntity.getItem();
        int rotation = pBlockEntity.getRandomRotation();

        if (!item.isEmpty()){
            pPoseStack.pushPose();
            pPoseStack.translate(0.5f, 0.9f, 0.5f);
            pPoseStack.scale(0.35f, 0.35f, 0.35f);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            pPoseStack.mulPose(Axis.XN.rotationDegrees(270));

            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);

            pPoseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
