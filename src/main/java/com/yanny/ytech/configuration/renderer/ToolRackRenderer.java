package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yanny.ytech.configuration.block.ToolRackBlock;
import com.yanny.ytech.configuration.block_entity.ToolRackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ToolRackRenderer implements BlockEntityRenderer<ToolRackBlockEntity> {

    public ToolRackRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull ToolRackBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        Direction direction = blockEntity.getBlockState().getValue(ToolRackBlock.HORIZONTAL_FACING);
        int i = 0;

        poseStack.pushPose();
        poseStack.rotateAround(Axis.YN.rotationDegrees(direction.toYRot()), 0.5f, 0 ,0.5f);
        poseStack.scale(1/4.0f, 1/4.0f, 1/4.0f);

        if (level != null) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    int[] position = ToolRackBlock.getPosition(i++);

                    if (position == null) {
                        continue;
                    }

                    int index = ToolRackBlock.getIndex(position);
                    ItemStack itemStack = blockEntity.getItems().get(index);

                    if (itemStack.isEmpty()) {
                        continue;
                    }

                    poseStack.pushPose();
                    poseStack.translate((direction.getAxis() == Direction.Axis.X) ? 3 - x : x, y, 0);
                    poseStack.translate(0.5, 0.5, 0.5);
                    poseStack.pushPose();

                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    BakedModel bakedmodel = itemRenderer.getModel(itemStack, level, null, 0);
                    itemRenderer.render(itemStack, ItemDisplayContext.GUI, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);

                    poseStack.popPose();
                    poseStack.popPose();
                }
            }
        }

        poseStack.popPose();
    }
}
