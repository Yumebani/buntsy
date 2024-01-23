package net.sophiebun.buntsy.blocks.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ThreadReelerBlockRenderer extends GeoBlockRenderer<ThreadReelerBlockEntity> {
    public ThreadReelerBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new ThreadReelerModel());

        addRenderLayer(new WaterLayer(this));
    }

    @Override
    public void defaultRender(PoseStack poseStack, ThreadReelerBlockEntity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);

        if (!animatable.getInputStack().isEmpty()){
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            poseStack.pushPose();
            poseStack.translate(0.5f, 0.4f, 0.5f);
            poseStack.scale(0.45f, 0.45f, 0.45f);
            poseStack.mulPose(animatable.getBlockState().getValue(ThreadReelerBlock.FACING).getRotation());

            itemRenderer.renderStatic(animatable.getInputStack(), ItemDisplayContext.FIXED, getLightLevel(animatable.getLevel(), animatable.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource, animatable.getLevel(), 1);

            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
