package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.configuration.block_entity.MillstoneBlockEntity;
import net.minecraft.client.model.LeashKnotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MillstoneRenderer implements BlockEntityRenderer<BlockEntity> {
    private static final ResourceLocation KNOT_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/lead_knot.png");
    private final LeashKnotModel<LeashFenceKnotEntity> model;

    public MillstoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LeashKnotModel<>(context.bakeLayer(ModelLayers.LEASH_KNOT));
    }

    @Override
    public void render(@NotNull BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        MillstoneBlockEntity millstone = (MillstoneBlockEntity) blockEntity;

        if (millstone.isLeashed()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.51, 0.5);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            VertexConsumer $$6 = buffer.getBuffer(this.model.renderType(KNOT_LOCATION));
            this.model.renderToBuffer(poseStack, $$6, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            poseStack.popPose();
        }
    }
}
