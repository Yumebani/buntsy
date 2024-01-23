package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.sophiebun.buntsy.entity.animals.Silkbun;
import net.sophiebun.buntsy.entity.animations.ModAnimationDefinitions;
import org.joml.Vector3f;

public class SilkbunModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart Body;
	private final ModelPart Head;

	public SilkbunModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Head = root.getChild("Body").getChild("Head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(2.0F, 15.0F, -0.5F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(20, 20).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.5F, -5.0F));

		PartDefinition Whiskers_r1 = Head.addOrReplaceChild("Whiskers_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.5F, -0.5F, 0.0F, 0.0F, 0.3927F));

		PartDefinition Whiskers_r2 = Head.addOrReplaceChild("Whiskers_r2", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.5F, -0.5F, 0.0F, 0.0F, -0.3927F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-1.0F, -3.0F, 0.0F));

		PartDefinition RightEar_r1 = RightEar.addOrReplaceChild("RightEar_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 0.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, 0.0F, 0.1309F, 0.0F, -0.2356F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(1.0F, -3.0F, 0.0F));

		PartDefinition LeftEar_r1 = LeftEar.addOrReplaceChild("LeftEar_r1", CubeListBuilder.create().texOffs(0, 26).addBox(-0.5F, -8.0F, 0.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 0.0F, 0.1309F, 0.0F, 0.2356F));

		PartDefinition Eyes = Head.addOrReplaceChild("Eyes", CubeListBuilder.create().texOffs(29, 32).addBox(-1.9F, -1.0F, 0.475F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(29, 32).addBox(0.9F, -1.0F, 0.475F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

		PartDefinition FrontLegs = Body.addOrReplaceChild("FrontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftLeg = FrontLegs.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(6, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, 7.0F, -2.5F));

		PartDefinition RightLeg = FrontLegs.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(29, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.75F, 7.0F, -2.5F));

		PartDefinition BackLegs = Body.addOrReplaceChild("BackLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 7.0F));

		PartDefinition BackLeftLeg = BackLegs.addOrReplaceChild("BackLeftLeg", CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, 7.0F, -2.5F));

		PartDefinition BackRightLeg = BackLegs.addOrReplaceChild("BackRightLeg", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.75F, 7.0F, -2.5F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(-2.0F, 6.0F, 6.5F));

		PartDefinition Tail_r1 = Tail.addOrReplaceChild("Tail_r1", CubeListBuilder.create().texOffs(17, 28).addBox(-1.0F, -2.25F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -0.5F, -0.3927F, 0.0F, 0.0F));

		PartDefinition MainBody = Body.addOrReplaceChild("MainBody", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.0F, 0.0F));

		PartDefinition Wings = MainBody.addOrReplaceChild("Wings", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, -2.0F));

		PartDefinition RightWing = Wings.addOrReplaceChild("RightWing", CubeListBuilder.create(), PartPose.offset(-1.0F, -9.0F, 2.0F));

		PartDefinition RightWing_r1 = RightWing.addOrReplaceChild("RightWing_r1", CubeListBuilder.create().texOffs(29, 17).addBox(-8.0F, 0.5F, -3.0F, 9.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, -2.0F, 0.0873F, 0.1745F, 0.5857F));

		PartDefinition LeftWing = Wings.addOrReplaceChild("LeftWing", CubeListBuilder.create(), PartPose.offset(1.0F, -9.0F, 2.0F));

		PartDefinition LeftWing_r1 = LeftWing.addOrReplaceChild("LeftWing_r1", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, 0.5F, -3.0F, 9.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, -2.0F, 0.0873F, -0.1745F, -0.5857F));

		PartDefinition Cocoon = MainBody.addOrReplaceChild("Cocoon", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		//this.animateWalk(ModAnimationDefinitions.SILKBUN_JUMP, 0, 0, 2f, 2.5f);
		this.animate(((Silkbun) entity).initAnimationState, ModAnimationDefinitions.SILKBUN_INIT, ageInTicks, 1);
		this.animate(((Silkbun) entity).idleAnimationState, ModAnimationDefinitions.SILKBUN_IDLE, ageInTicks, 1);
		this.animate(((Silkbun) entity).jumpAnimationState, ModAnimationDefinitions.SILKBUN_JUMP, ageInTicks, 1);
		this.animate(((Silkbun) entity).startSleepAnimationState, ModAnimationDefinitions.SILKBUN_ENTER_SLEEP, ageInTicks, 1);
		this.animate(((Silkbun) entity).endSleepAnimationState, ModAnimationDefinitions.SILKBUN_EXIT_SLEEP, ageInTicks, 1);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Body;
	}
}