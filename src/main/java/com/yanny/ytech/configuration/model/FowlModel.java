package com.yanny.ytech.configuration.model;

import com.google.common.collect.ImmutableList;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.FowlEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FowlModel extends AgeableListModel<FowlEntity> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("fowl"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart l_wing;
    @NotNull private final ModelPart r_wing;
    @NotNull private final ModelPart l_foot;
    @NotNull private final ModelPart r_foot;

    public FowlModel(@NotNull ModelPart root) {
        super(false, 5.0F, 2.0F);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.l_wing = root.getChild("l_wing");
        this.r_wing = root.getChild("r_wing");
        this.l_foot = root.getChild("l_foot");
        this.r_foot = root.getChild("r_foot");
    }

    @NotNull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head);
    }

    @NotNull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body, l_wing, r_wing, l_foot, r_foot);
    }

    public void setupAnim(@NotNull FowlEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * 0.017453292F;
        this.head.yRot = pNetHeadYaw * 0.017453292F;
        this.r_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.l_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + 3.1415927F) * 1.4F * pLimbSwingAmount;
        this.r_wing.zRot = pAgeInTicks;
        this.l_wing.zRot = -pAgeInTicks;
    }

    @NotNull

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -11.0F, -4.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 15).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -3.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -4.0F));
        partdefinition.addOrReplaceChild("l_wing", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, 0.0F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 13.0F, 0.0F));
        partdefinition.addOrReplaceChild("r_wing", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 13.0F, 0.0F));
        partdefinition.addOrReplaceChild("l_foot", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(-3, 5).addBox(-1.0F, 5.0F, -2.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 19.0F, 0.0F));
        partdefinition.addOrReplaceChild("r_foot", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(-3, 5).addBox(-2.0F, 5.0F, -2.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 19.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
