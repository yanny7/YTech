package com.yanny.ytech.configuration.model;

import com.google.common.collect.ImmutableList;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.MouflonEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MouflonModel extends AgeableListModel<MouflonEntity> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("mouflon"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart fl_foot;
    @NotNull private final ModelPart bl_foot;
    @NotNull private final ModelPart fr_foot;
    @NotNull private final ModelPart br_foot;

    public MouflonModel(@NotNull ModelPart root) {
        super(false, 8.0F, 5.0F);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.fl_foot = root.getChild("fl_foot");
        this.bl_foot = root.getChild("bl_foot");
        this.fr_foot = root.getChild("fr_foot");
        this.br_foot = root.getChild("br_foot");
    }

    @NotNull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head);
    }

    @NotNull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body, fl_foot, bl_foot, fr_foot, br_foot);
    }

    public void setupAnim(@NotNull MouflonEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.fl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.bl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.fr_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.br_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.head.getChild("horns").visible = !entity.isBaby();
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(-5.0F, -19.0F, -9.0F, 10.0F, 9.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("fl_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, -6.0F));
        partdefinition.addOrReplaceChild("fr_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 14.0F, -6.0F));
        partdefinition.addOrReplaceChild("bl_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 6.0F));
        partdefinition.addOrReplaceChild("br_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 14.0F, 9.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(38, 0).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -9.0F));

        PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(38, 13).addBox(-7.75F, -19.0F, -15.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 13).addBox(6.75F, -19.0F, -15.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 9.0F));

        horns.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-5.0F, -0.5F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.75F, -22.0F, -10.0F, 0.0F, -0.4363F, -0.8727F));
        horns.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, -0.5F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.75F, -22.0F, -10.0F, 0.0F, 0.4363F, 0.8727F));
        horns.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 15).addBox(-5.0F, 0.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -21.0F, -10.0F, 0.0F, 0.0F, 0.3054F));
        horns.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, 0.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -21.0F, -10.0F, 0.0F, 0.0F, -0.3054F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
