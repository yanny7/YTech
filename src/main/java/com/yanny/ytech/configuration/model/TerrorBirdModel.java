package com.yanny.ytech.configuration.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.TerrorBirdEntity;
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
public class TerrorBirdModel extends EntityModel<TerrorBirdEntity> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("terror_bird"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart l_foot;
    @NotNull private final ModelPart r_foot;

    public TerrorBirdModel(@NotNull ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.l_foot = root.getChild("l_foot");
        this.r_foot = root.getChild("r_foot");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packetLight, int packetOverlay, int color) {
        parts().forEach((modelPart) -> modelPart.render(poseStack, vertexConsumer, packetLight, packetOverlay, color));
    }

    @Override
    public void setupAnim(@NotNull TerrorBirdEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.r_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.l_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + 3.1415927F) * 1.4F * pLimbSwingAmount;
    }

    @NotNull
    private Iterable<ModelPart> parts() {
        return ImmutableList.of(head, body, l_foot, r_foot);
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -19.0F, -6.0F, 10.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("l_foot", CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(1.0F, 9.0F, -3.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));
        partdefinition.addOrReplaceChild("r_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, 9.0F, -3.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, -4.0F));

        head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 21).addBox(-3.0F, -4.0F, -8.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -4.0F, 0.1745F, 0.0F, 0.0F));
        head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -2.0F, 0.4363F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
