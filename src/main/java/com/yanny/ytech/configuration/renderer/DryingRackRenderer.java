package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yanny.ytech.configuration.block_entity.DryingRackBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class DryingRackRenderer implements BlockEntityRenderer<BlockEntity> {
    private final ItemRenderer itemRenderer;

    public DryingRackRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Level level = blockEntity.getLevel();

        poseStack.pushPose();

        if (facing.getAxis() == Direction.Axis.X) {
            poseStack.rotateAround(new Quaternionf().rotateY((float) Math.PI / 2F), 0.5f, 0.5f, 0.5f);
        }

        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(0, 0.38f, 0);

        if (level != null) {
            if (blockEntity instanceof DryingRackBlockEntity dryingRack) {
                if (!dryingRack.getItem().isEmpty()) {
                    BakedModel bakedmodel = itemRenderer.getModel(dryingRack.getItem(), level, null, 0);
                    itemRenderer.render(dryingRack.getItem(), ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);
                }
            }
        }

        poseStack.popPose();
    }
}
