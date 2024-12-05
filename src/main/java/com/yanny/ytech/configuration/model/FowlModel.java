package com.yanny.ytech.configuration.model;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FowlModel extends EntityModel<ChickenRenderState> {
    @NotNull public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Utils.modLoc("fowl"), "main");

    @NotNull private final ModelPart head;
    @NotNull private final ModelPart body;
    @NotNull private final ModelPart l_wing;
    @NotNull private final ModelPart r_wing;
    @NotNull private final ModelPart l_foot;
    @NotNull private final ModelPart r_foot;

    public FowlModel(@NotNull ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.l_wing = root.getChild("l_wing");
        this.r_wing = root.getChild("r_wing");
        this.l_foot = root.getChild("l_foot");
        this.r_foot = root.getChild("r_foot");
    }

    @Override
    public void setupAnim(@NotNull ChickenRenderState state) {
        super.setupAnim(state);
        float f = (Mth.sin(state.flap) + 1.0F) * state.flapSpeed;
        this.head.xRot = state.xRot * 0.017453292F;
        this.head.yRot = state.yRot * 0.017453292F;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.walkAnimationPos;
        this.r_foot.xRot = Mth.cos(f2 * 0.6662F) * 1.4F * f1;
        this.l_foot.xRot = Mth.cos(f2 * 0.6662F + 3.1415927F) * 1.4F * f1;
        this.r_wing.zRot = f;
        this.l_wing.zRot = -f;
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
