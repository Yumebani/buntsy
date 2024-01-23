package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.sophiebun.buntsy.entity.animals.Fairy;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.entity.animations.ModAnimationDefinitions;

public class FairyModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart Fairy;

	public FairyModel(ModelPart root) {
		this.Fairy = root.getChild("Fairy");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Fairy = partdefinition.addOrReplaceChild("Fairy", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Body = Fairy.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.75F, -4.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, -1.0F, 2.0F));

		PartDefinition RightWing = Body.addOrReplaceChild("RightWing", CubeListBuilder.create(), PartPose.offset(-1.75F, 0.0F, 0.0F));

		PartDefinition RightWing_r1 = RightWing.addOrReplaceChild("RightWing_r1", CubeListBuilder.create().texOffs(6, 3).addBox(0.25F, -6.0F, 0.0F, 0.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -0.9163F, 0.0F));

		PartDefinition LeftWing = Body.addOrReplaceChild("LeftWing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftWing_r1 = LeftWing.addOrReplaceChild("LeftWing_r1", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, -6.0F, 0.0F, 0.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.9163F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animate(((Fairy) entity).flyAnimationState, ModAnimationDefinitions.FAIRY_FLY, ageInTicks, 1);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Fairy.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Fairy;
	}
}