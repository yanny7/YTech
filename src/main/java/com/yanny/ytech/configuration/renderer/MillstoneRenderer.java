package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.MillstoneBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MillstoneRenderer implements BlockEntityRenderer<BlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final RandomSource randomSource = RandomSource.create(42);
    private final BakedModel bakedmodel = Minecraft.getInstance().getModelManager().getModel(Utils.modBlockLoc("millstone_upper_part"));

    public MillstoneRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Level level = blockEntity.getLevel();

        poseStack.pushPose();

        if (level != null && blockEntity instanceof MillstoneBlockEntity millstoneBlock) {
            float rotation = (millstoneBlock.getSpinTimeout() > 0 ? (millstoneBlock.getClientRotation() + partialTick) : millstoneBlock.getClientRotation()) / 20.0f;
            poseStack.rotateAround(Direction.UP.getRotation().rotateY(rotation), 0.5f, 0, 0.5f);
        }

        for (net.minecraft.client.renderer.RenderType rt : bakedmodel.getRenderTypes(blockState, randomSource, blockEntity.getModelData())) {
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(rt), blockState,
                    bakedmodel, 0, 0, 0, packedLight, packedOverlay, blockEntity.getModelData(), rt);
        }

        poseStack.popPose();
    }
}
