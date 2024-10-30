package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import com.yanny.ytech.configuration.block_entity.CraftingWorkspaceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CraftingWorkspaceRenderer implements BlockEntityRenderer<CraftingWorkspaceBlockEntity> {

    public CraftingWorkspaceRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull CraftingWorkspaceBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();

        poseStack.pushPose();
        poseStack.scale(1/3.0f, 1/3.0f, 1/3.0f);

        if (level != null) {
            int bitmask = blockEntity.getBitmask();
            NonNullList<ItemStack> items = blockEntity.getItems();

            for (int i = 0; i < 27; i++) {
                if ((bitmask >> i & 1) == 1) {
                    int[] position = CraftingWorkspaceBlock.getPosition(i);
                    ItemStack itemStack = items.get(i);

                    if (position == null || itemStack.isEmpty() || !(itemStack.getItem() instanceof BlockItem blockItem)) {
                        continue;
                    }

                    Block block = blockItem.getBlock();

                    poseStack.pushPose();
                    poseStack.translate(position[0], position[1], position[2]);
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                            block.defaultBlockState(),
                            poseStack,
                            buffer,
                            packedLight,
                            OverlayTexture.NO_OVERLAY
                    );
                    poseStack.popPose();
                }
            }
        }

        poseStack.popPose();
    }
}
