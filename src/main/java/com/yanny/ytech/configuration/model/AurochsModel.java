package com.yanny.ytech.configuration.model;

import com.google.common.collect.ImmutableList;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.AurochsEntity;
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
public class AurochsModel extends AgeableListModel<AurochsEntity> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("aurochs"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart fl_foot;
    @NotNull private final ModelPart bl_foot;
    @NotNull private final ModelPart fr_foot;
    @NotNull private final ModelPart br_foot;
    private float headXRot;

    public AurochsModel(@NotNull ModelPart root) {
        super(false, 10.0F, 5.0F);
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

    public void setupAnim(@NotNull AurochsEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = this.headXRot;
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.fl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.bl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.fr_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.br_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.head.getChild("horns").visible = !entity.isBaby();
    }

    @Override
    public void prepareMobModel(@NotNull AurochsEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
        head.y = 3.0F + pEntity.getHeadEatPositionScale(pPartialTick) * (pEntity.isBaby() ? 3.0F : 9.0F);
        headXRot = pEntity.getHeadEatAngleScale(pPartialTick);
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.75F, -6.5F, 12.0F, 13.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-5.0F, -6.75F, -8.5F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.75F, -1.5F));
        partdefinition.addOrReplaceChild("fl_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 12.0F, -6.0F));
        partdefinition.addOrReplaceChild("bl_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 12.0F, 9.0F));
        partdefinition.addOrReplaceChild("fr_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 12.0F, -6.0F));
        partdefinition.addOrReplaceChild("br_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 12.0F, 9.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(34, 49).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -10.0F));
        PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(0, 16).addBox(4.0F, -4.0F, -3.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 16).addBox(-8.0F, -4.0F, -3.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        horns.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 16).addBox(6.9853F, -3.5F, 3.4497F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 0).addBox(-4.7426F, -3.5F, -9.2782F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
