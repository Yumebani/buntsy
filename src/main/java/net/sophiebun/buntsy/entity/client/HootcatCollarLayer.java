package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.entity.animals.Hootcat;

@OnlyIn(Dist.CLIENT)
public class HootcatCollarLayer extends RenderLayer<Hootcat, HootCatModel<Hootcat>> {
    private static final ResourceLocation HOOTCAT_COLLAR_LOCATION = new ResourceLocation(BuntsyMod.MODID,"textures/entity/hootcat_collar.png");
    private final HootCatModel<Hootcat> hootcatModel;

    public HootcatCollarLayer(RenderLayerParent<Hootcat, HootCatModel<Hootcat>> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.hootcatModel = new HootCatModel<>(pModelSet.bakeLayer(ModModelLayers.HOOTCAT_COLLAR_LAYER));
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Hootcat pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isTame()) {
            float[] afloat = pLivingEntity.getCollarColor().getTextureDiffuseColors();
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.hootcatModel, HOOTCAT_COLLAR_LOCATION, pPoseStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks, afloat[0], afloat[1], afloat[2]);
        }
    }
}