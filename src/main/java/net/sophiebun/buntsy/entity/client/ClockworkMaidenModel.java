package net.sophiebun.buntsy.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.sophiebun.buntsy.entity.animations.ClockworkMaidenAnimationDefinition;
import net.sophiebun.buntsy.entity.animations.MarionetteAnimationDefinition;
import net.sophiebun.buntsy.entity.clockwork_maiden.ClockworkMaiden;
import net.sophiebun.buntsy.entity.monsters.Marionette;

public class ClockworkMaidenModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart Root;
	private final ModelPart BodyB;
	private final ModelPart ClothesB;
	private final ModelPart WindupRoot;
	private final ModelPart Windup;
	private final ModelPart HeadB;
	private final ModelPart HeadLayer;
	private final ModelPart LegsB;
	private final ModelPart LegLB;
	private final ModelPart LegRB;
	private final ModelPart ArmsB;
	private final ModelPart ArmLB;
	private final ModelPart ArmRB;

	public ClockworkMaidenModel(ModelPart root) {
		this.Root = root.getChild("Root");
		this.BodyB = this.Root.getChild("BodyB");
		this.ClothesB = this.Root.getChild("ClothesB");
		this.WindupRoot = this.Root.getChild("WindupRoot");
		this.Windup = this.WindupRoot.getChild("Windup");
		this.HeadB = this.Root.getChild("HeadB");
		this.HeadLayer = this.HeadB.getChild("HeadLayer");
		this.LegsB = this.Root.getChild("LegsB");
		this.LegLB = this.LegsB.getChild("LegLB");
		this.LegRB = this.LegsB.getChild("LegRB");
		this.ArmsB = this.Root.getChild("ArmsB");
		this.ArmLB = this.ArmsB.getChild("ArmLB");
		this.ArmRB = this.ArmsB.getChild("ArmRB");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, -0.5F));

		PartDefinition BodyB = Root.addOrReplaceChild("BodyB", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -6.5F, -1.5F, 4.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.5F));

		PartDefinition ClothesB = Root.addOrReplaceChild("ClothesB", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.5F));

		PartDefinition SkirtFront_r1 = ClothesB.addOrReplaceChild("SkirtFront_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, -7.0F, -1.0F, 10.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 4.3F, 2.35F, -0.2618F, 3.1416F, 0.0F));

		PartDefinition SkirtFront_r2 = ClothesB.addOrReplaceChild("SkirtFront_r2", CubeListBuilder.create().texOffs(34, 55).addBox(-3.0F, -7.0F, -1.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.4F, -2.3F, -0.2618F, 0.0F, 0.0F));

		PartDefinition SkirtDiagBackR_r1 = ClothesB.addOrReplaceChild("SkirtDiagBackR_r1", CubeListBuilder.create().texOffs(50, 45).addBox(-1.5F, -7.0F, -5.25F, 0.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.25F, 4.5F, 0.3794F, 0.7056F, 0.5513F));

		PartDefinition SkirtDiagBackL_r1 = ClothesB.addOrReplaceChild("SkirtDiagBackL_r1", CubeListBuilder.create().texOffs(50, 45).addBox(0.0F, -7.0F, -2.5F, 0.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9172F, 3.6519F, 3.5281F, 2.7622F, 0.7056F, 2.5903F));

		PartDefinition SkirtDiagR_r1 = ClothesB.addOrReplaceChild("SkirtDiagR_r1", CubeListBuilder.create().texOffs(50, 33).addBox(-1.5F, -7.0F, 0.25F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.25F, -4.5F, -0.3794F, -0.7056F, 0.5513F));

		PartDefinition SkirtDiagL_r1 = ClothesB.addOrReplaceChild("SkirtDiagL_r1", CubeListBuilder.create().texOffs(50, 33).mirror().addBox(1.5F, -7.0F, 0.25F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 4.25F, -4.5F, -0.3794F, 0.7056F, -0.5513F));

		PartDefinition SkirtR_r1 = ClothesB.addOrReplaceChild("SkirtR_r1", CubeListBuilder.create().texOffs(20, 46).addBox(-1.0F, -7.0F, -3.0F, 0.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 4.5F, -0.5F, 0.0F, 0.0F, 0.3491F));

		PartDefinition SkirtL_r1 = ClothesB.addOrReplaceChild("SkirtL_r1", CubeListBuilder.create().texOffs(20, 46).mirror().addBox(1.0F, -7.0F, -3.0F, 0.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, 4.5F, -0.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition WindupRoot = Root.addOrReplaceChild("WindupRoot", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -9.35F, 2.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition Windup = WindupRoot.addOrReplaceChild("Windup", CubeListBuilder.create().texOffs(28, 19).addBox(0.0F, -2.4538F, -0.1373F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition WindupZ_r1 = Windup.addOrReplaceChild("WindupZ_r1", CubeListBuilder.create().texOffs(28, 19).addBox(1.1962F, -2.5F, -0.6373F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.15F, 0.5F, 0.0F, 0.0F, 1.5708F));

		PartDefinition HeadB = Root.addOrReplaceChild("HeadB", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.5F, 0.5F));

		PartDefinition HeadLayer = HeadB.addOrReplaceChild("HeadLayer", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -7.0F, -4.0F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.5F, 0.5F));

		PartDefinition LegsB = Root.addOrReplaceChild("LegsB", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LegLB = LegsB.addOrReplaceChild("LegLB", CubeListBuilder.create().texOffs(36, 13).addBox(-1.5F, -0.5F, -1.495F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -6.5F, 0.5F));

		PartDefinition LegRB = LegsB.addOrReplaceChild("LegRB", CubeListBuilder.create().texOffs(36, 13).mirror().addBox(-1.5F, -0.5F, -1.495F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -6.5F, 0.5F));

		PartDefinition ArmsB = Root.addOrReplaceChild("ArmsB", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ArmLB = ArmsB.addOrReplaceChild("ArmLB", CubeListBuilder.create(), PartPose.offset(2.75F, -15.0F, 0.5F));

		PartDefinition ArmL_r1 = ArmLB.addOrReplaceChild("ArmL_r1", CubeListBuilder.create().texOffs(28, 13).addBox(-2.0F, -9.0F, 0.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.25F, 7.0F, -1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition ArmRB = ArmsB.addOrReplaceChild("ArmRB", CubeListBuilder.create(), PartPose.offset(-2.75F, -15.0F, 0.5F));

		PartDefinition ArmR_r1 = ArmRB.addOrReplaceChild("ArmR_r1", CubeListBuilder.create().texOffs(28, 13).mirror().addBox(0.0F, -9.0F, 0.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.25F, 7.0F, -1.0F, 0.0F, 0.0F, 0.4363F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		ClockworkMaiden maiden = ((ClockworkMaiden) entity);

		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		if (!maiden.getCarriedItem().isEmpty()){
			animateWalk(MarionetteAnimationDefinition.walkCycleNoArms, limbSwing, limbSwingAmount, 2f, 3.5f);}
		else {
			animateWalk(MarionetteAnimationDefinition.walkCycle, limbSwing, limbSwingAmount, 2f, 3.5f);
		}

		this.animate(maiden.windUpAnimationState, MarionetteAnimationDefinition.windupSpin, ageInTicks, 1f);
		this.animate(maiden.idleAnimationState, MarionetteAnimationDefinition.idleArms, ageInTicks, 1.5f);
		this.animate(maiden.ItemCarryStanceAnimationState, ClockworkMaidenAnimationDefinition.itemHold, ageInTicks, 0.5f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.HeadB.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.HeadB.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Root;
	}

	public ModelPart getHand(HumanoidArm pArm) {
		if (pArm == HumanoidArm.RIGHT){
			return this.ArmRB;
		} else {
			return this.ArmLB;
		}
	}
}