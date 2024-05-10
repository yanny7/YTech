package com.yanny.ytech.configuration.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.DeerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DeerModel extends EntityModel<DeerEntity> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("deer"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart fl_foot;
    @NotNull private final ModelPart bl_foot;
    @NotNull private final ModelPart fr_foot;
    @NotNull private final ModelPart br_foot;
    private float headXRot;

    public DeerModel(@NotNull ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.fl_foot = root.getChild("fl_foot");
        this.bl_foot = root.getChild("bl_foot");
        this.fr_foot = root.getChild("fr_foot");
        this.br_foot = root.getChild("br_foot");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packetLight, int packetOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        parts().forEach((modelPart) -> modelPart.render(poseStack, vertexConsumer, packetLight, packetOverlay, pRed, pGreen, pBlue, pAlpha));
    }

    @Override
    public void setupAnim(@NotNull DeerEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = this.headXRot;
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.fl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.bl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.fr_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.br_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
    }

    @Override
    public void prepareMobModel(@NotNull DeerEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
        head.y = 3.0F + pEntity.getHeadEatPositionScale(pPartialTick) * (pEntity.isBaby() ? 3.0F : 9.0F);
        headXRot = pEntity.getHeadEatAngleScale(pPartialTick);
        this.head.getChild("antlers").visible = pEntity.hasAntlers();
    }

    @NotNull
    private Iterable<ModelPart> parts() {
        return ImmutableList.of(head, body, fl_foot, bl_foot, fr_foot, br_foot);
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, -5.0F));
        head.addOrReplaceChild("antlers", CubeListBuilder.create().texOffs(0, 54).addBox(-8.0F, -19.0F, -6.0F, 16.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 0).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -8.0F, -7.0F, 0.0F, 0.0F, 0.7854F));
        head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, -1.0F, -1.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, -7.0F, 0.0F, 0.0F, -0.7854F));
        head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 28).addBox(-2.0F, -8.0F, -3.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -5.0F, -2.3562F, 0.0F, 0.0F));
        head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -13.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -13.0F, -1.0F, 10.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, -3.0F, -1.5708F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("fl_foot", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 13.0F, -7.0F));
        partdefinition.addOrReplaceChild("bl_foot", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 13.0F, 7.0F));
        partdefinition.addOrReplaceChild("fr_foot", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 13.0F, -7.0F));
        partdefinition.addOrReplaceChild("br_foot", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 13.0F, 7.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
