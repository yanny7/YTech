package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yanny.ytech.configuration.block.WoodenBoxBlock;
import com.yanny.ytech.configuration.block_entity.WoodenBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WoodenBoxRenderer implements BlockEntityRenderer<WoodenBoxBlockEntity> {

    public WoodenBoxRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull WoodenBoxBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        int i = 0;

        poseStack.pushPose();
        poseStack.scale(1/4.0f, 1/4.0f, 1/4.0f);

        if (level != null) {
            for (int z = 0; z < 3; z++) {
                for (int x = 0; x < 3; x++) {
                    int[] position = WoodenBoxBlock.getPosition(i++);

                    if (position == null) {
                        continue;
                    }

                    int index = WoodenBoxBlock.getIndex(position);
                    ItemStack itemStack = blockEntity.getItems().get(index);

                    if (itemStack.isEmpty()) {
                        continue;
                    }

                    poseStack.pushPose();
                    poseStack.translate(0.5 + x, 2.3, 0.5 + z);
                    poseStack.translate(0.5, 0.5, 0.5);
                    poseStack.pushPose();

                    poseStack.rotateAround(Axis.XN.rotationDegrees(90), 0,0,0);

                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    BakedModel bakedmodel = itemRenderer.getModel(itemStack, level, null, 0);
                    itemRenderer.render(itemStack, ItemDisplayContext.GUI, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);

                    poseStack.popPose();

                    // Rendering hell...
                    //poseStack.translate(0, 2, 0);
                    //DebugRenderer.renderFloatingText(poseStack, buffer, String.valueOf(itemStack.getCount()), pos.getX(), pos.getY(), pos.getZ(), rgba(0, 255, 255, 255));

                    poseStack.popPose();
                }
            }
        }

        poseStack.popPose();
    }

    private static int rgba(int r, int g, int b, int a) {
        return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }
}
