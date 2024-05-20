package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yanny.ytech.configuration.block_entity.PottersWheelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class PottersWheelRenderer implements BlockEntityRenderer<PottersWheelBlockEntity> {
    private final ItemRenderer itemRenderer;
    private final ItemStack clayBlock = Items.CLAY.getDefaultInstance();

    public PottersWheelRenderer(BlockEntityRendererProvider.Context context) {
         itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull PottersWheelBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        long pGameTime = Objects.requireNonNull(blockEntity.getLevel()).getGameTime();
        float rotation = (float)Math.floorMod(pGameTime, 360) + partialTick;
        ItemStack itemStack = clayBlock;

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 2, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2));

        if (blockEntity.getResult() != null) {
            itemStack = blockEntity.getResult();
        }

        if (!blockEntity.getItem().isEmpty()) {
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, null, 0);
        }

        poseStack.popPose();
    }
}
