package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yanny.ytech.configuration.block_entity.BronzeAnvilBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BronzeAnvilRenderer implements BlockEntityRenderer<BlockEntity> {
    private final ItemRenderer itemRenderer;

    public BronzeAnvilRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Level level = blockEntity.getLevel();

        poseStack.pushPose();
        poseStack.rotateAround(facing.getRotation(), 0.5f, 0.5f, 0.5f);

        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 1, 0);


        if (level != null) {
            if (blockEntity instanceof BronzeAnvilBlockEntity anvil) {
                ItemStack input = anvil.getItemStackHandler().getStackInSlot(0);

                if (!input.isEmpty()) {
                    BakedModel bakedmodel = itemRenderer.getModel(input, level, null, 0);
                    itemRenderer.render(input, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);

                    if (input.getCount() > 1) {
                        poseStack.pushPose();
                        poseStack.translate(-0.1f, -0.1f, -0.02f);
                        itemRenderer.render(input, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);
                        poseStack.popPose();
                    }
                }
            }
        }

        poseStack.popPose();
    }
}
