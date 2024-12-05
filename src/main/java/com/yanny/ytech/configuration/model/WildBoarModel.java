package com.yanny.ytech.configuration.model;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WildBoarModel extends EntityModel<LivingEntityRenderState> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("wild_boar"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart fl_foot;
    @NotNull private final ModelPart bl_foot;
    @NotNull private final ModelPart fr_foot;
    @NotNull private final ModelPart br_foot;

    public WildBoarModel(@NotNull ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.fl_foot = root.getChild("fl_foot");
        this.bl_foot = root.getChild("bl_foot");
        this.fr_foot = root.getChild("fr_foot");
        this.br_foot = root.getChild("br_foot");
    }

    public void setupAnim(@NotNull LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * 0.017453292F;
        this.head.yRot = state.yRot * 0.017453292F;
        float pLimbSwing = state.walkAnimationPos;
        float pLimbSwingAmount = state.walkAnimationSpeed;
        this.fl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.bl_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.fr_foot.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.br_foot.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -14.0F, -9.0F, 10.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("fl_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 18.0F, -6.0F));
        partdefinition.addOrReplaceChild("fr_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 18.0F, -6.0F));
        partdefinition.addOrReplaceChild("bl_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 18.0F, 6.0F));
        partdefinition.addOrReplaceChild("br_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 18.0F, 6.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 0).addBox(-4.0F, -3.0F, -6.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-2.0F, 0.0F, -8.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, -9.0F));

        head.addOrReplaceChild("tusks", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -11.0F, -17.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -11.0F, -17.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 9.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
