package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yanny.ytech.configuration.block_entity.FirePitBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FirePitRenderer implements BlockEntityRenderer<FirePitBlockEntity> {
    private final ItemRenderer itemRenderer;

    public FirePitRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull FirePitBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(0, 0.38f, 0);

        if (level != null) {
            if (!blockEntity.getItem().isEmpty()) {
                BakedModel bakedmodel = itemRenderer.getModel(blockEntity.getItem(), level, null, 0);
                itemRenderer.render(blockEntity.getItem(), ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);
            }
        }

        poseStack.popPose();
    }
}
