package com.yanny.ytech.configuration.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.WoollyRhinoEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WoollyRhinoModel extends EntityModel<WoollyRhinoEntity> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("woolly_rhino"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart fl_foot;
    @NotNull private final ModelPart bl_foot;
    @NotNull private final ModelPart fr_foot;
    @NotNull private final ModelPart br_foot;

    public WoollyRhinoModel(@NotNull ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.fl_foot = root.getChild("fl_foot");
        this.bl_foot = root.getChild("bl_foot");
        this.fr_foot = root.getChild("fr_foot");
        this.br_foot = root.getChild("br_foot");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packetLight, int packetOverlay, int color) {
        parts().forEach((modelPart) -> modelPart.render(poseStack, vertexConsumer, packetLight, packetOverlay, color));
    }

    @Override
    public void setupAnim(@NotNull WoollyRhinoEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.fl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.bl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.fr_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.br_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
    }

    @NotNull
    private Iterable<ModelPart> parts() {
        return ImmutableList.of(head, body, fl_foot, bl_foot, fr_foot, br_foot);
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 26).addBox(-7.0F, -20.0F, 3.0F, 14.0F, 12.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -22.0F, -9.0F, 14.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("fl_foot", CubeListBuilder.create().texOffs(53, 0).addBox(-3.0F, 0.0F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 16.0F, -6.0F));
        partdefinition.addOrReplaceChild("fr_foot", CubeListBuilder.create().texOffs(53, 0).addBox(-2.0F, 0.0F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 16.0F, -6.0F));
        partdefinition.addOrReplaceChild("bl_foot", CubeListBuilder.create().texOffs(53, 0).addBox(-3.0F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 16.0F, 11.5F));
        partdefinition.addOrReplaceChild("br_foot", CubeListBuilder.create().texOffs(53, 0).addBox(-2.0F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 16.0F, 11.5F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -9.0F));

        head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -14.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(47, 21).addBox(-3.0F, 2.0F, -14.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.1309F, 0.0F, 0.0F));
        head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(3, 29).addBox(-0.5F, -2.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(43, 42).addBox(-5.0F, 0.0F, -8.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }
}
