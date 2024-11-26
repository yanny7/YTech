package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.StrainerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class StrainerRenderer implements BlockEntityRenderer<StrainerBlockEntity> {
    private final BakedModel model;
    private final ModelBlockRenderer renderer;

    public StrainerRenderer(BlockEntityRendererProvider.Context context) {
        model = Minecraft.getInstance().getModelManager().getModel(Utils.modLoc("block/strainer_net"));
        renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
    }

    @Override
    public void render(@NotNull StrainerBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Level level = blockEntity.getLevel();

        if (level != null && blockEntity.hasMesh()) {
            poseStack.pushPose();
            poseStack.translate(0, 1, 0);

            int i = LevelRenderer.getLightColor(level, blockEntity.getBlockPos().above());

            renderer.renderModel(poseStack.last(), buffer.getBuffer(RenderType.cutout()),
                    blockState, model, 1, 1, 1, i, packedOverlay, ModelData.EMPTY, RenderType.cutout());

            poseStack.popPose();
        }
    }
}
