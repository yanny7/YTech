package com.yanny.ytech.configuration.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpearModel extends Model {
   @NotNull private final ModelPart root;

   public SpearModel(@NotNull ModelPart root) {
      super(RenderType::entitySolid);
      this.root = root;
   }

   @NotNull
   public static LayerDefinition createLayer(int xOffset, int yOffset) {
      MeshDefinition meshDefinition = new MeshDefinition();
      PartDefinition rootPart = meshDefinition.getRoot();

      PartDefinition polePart = rootPart.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0)
              .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.ZERO);
      polePart.addOrReplaceChild("base", CubeListBuilder.create().texOffs(4 + xOffset, yOffset)
              .addBox(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.ZERO);
      polePart.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(4 + xOffset, 2 + yOffset)
              .addBox(-1.0F, -2.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.ZERO);
      polePart.addOrReplaceChild("k", CubeListBuilder.create().texOffs(4 + xOffset, 4 + yOffset)
              .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

      return LayerDefinition.create(meshDefinition, 32, 32);
   }

   public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}