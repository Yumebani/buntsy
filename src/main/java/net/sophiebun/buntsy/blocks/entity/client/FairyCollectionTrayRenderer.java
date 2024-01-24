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
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyCollectionTrayBlockEntity;
import net.sophiebun.buntsy.blocks.entity.directfairy.FairyOfferingBenchBlockEntity;

import java.util.List;

public class FairyCollectionTrayRenderer implements BlockEntityRenderer<FairyCollectionTrayBlockEntity> {

    public FairyCollectionTrayRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(FairyCollectionTrayBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        List<ItemStack> items = pBlockEntity.getRenderItems();

        if (!items.isEmpty()){

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            List<Integer> rotations = pBlockEntity.getRandomRotations();

            for (int i = 0; i < items.size(); i++){
                pPoseStack.pushPose();
                pPoseStack.translate(0.375f + (0.25f * (i % 2)), 0.78f, 0.375f + (0.25f * (i / 2)));
                pPoseStack.scale(0.35f, 0.35f, 0.35f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(rotations.get(i)));
                pPoseStack.mulPose(Axis.XN.rotationDegrees(270));

                itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                        OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);

                pPoseStack.popPose();
            }
        }
    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
