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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sophiebun.buntsy.blocks.custom.entityblocks.ThreadReelerBlock;
import net.sophiebun.buntsy.blocks.entity.basicfairy.ThreadReelerBlockEntity;
import net.sophiebun.buntsy.blocks.entity.clockwork.ClockworkFairyTerminalEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ClockworkFairyTerminalRenderer extends GeoBlockRenderer<ClockworkFairyTerminalEntity> {
    public ClockworkFairyTerminalRenderer(BlockEntityRendererProvider.Context context) {
        super(new ClockworkFairyTerminalModel());
    }

    @Override
    public void defaultRender(PoseStack poseStack, ClockworkFairyTerminalEntity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
