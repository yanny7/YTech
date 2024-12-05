package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.entity.SpearEntity;
import com.yanny.ytech.configuration.model.SpearModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpearRenderer extends EntityRenderer<SpearEntity, ThrownTridentRenderState> {
    private final SpearModel model;

    public SpearRenderer(@NotNull EntityRendererProvider.Context context, ModelLayerLocation layerLocation) {
        super(context);
        this.model = new SpearModel(context.bakeLayer(layerLocation));
    }

    @Override
    public void render(@NotNull ThrownTridentRenderState renderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(buffer, this.model.renderType(SpearType.TEXTURE_LOCATION), false, renderState.isFoil);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, buffer, packedLight);
    }

    @NotNull
    @Override
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SpearEntity p_362162_, @NotNull ThrownTridentRenderState p_360843_, float p_361066_) {
        super.extractRenderState(p_362162_, p_360843_, p_361066_);
        p_360843_.yRot = p_362162_.getYRot(p_361066_);
        p_360843_.xRot = p_362162_.getXRot(p_361066_);
        p_360843_.isFoil = p_362162_.isFoil();
    }
}
