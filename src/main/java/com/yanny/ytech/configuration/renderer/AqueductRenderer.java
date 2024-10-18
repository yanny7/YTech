package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.AqueductBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationClientNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class AqueductRenderer implements BlockEntityRenderer<AqueductBlockEntity> {
    private static final FakeAqueductLevel level = new FakeAqueductLevel();
    private final BlockState water;

    public AqueductRenderer(BlockEntityRendererProvider.Context context) {
        water = Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 8);
    }

    @Override
    public void render(@NotNull AqueductBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        IrrigationClientNetwork network = YTechMod.IRRIGATION_PROPAGATOR.client().getNetwork(blockEntity);

        if (network != null && network.getCapacity() > 0 && network.getAmount() > 0) {
            level.setData(blockEntity, water);

            poseStack.pushPose();
            poseStack.translate(0, -12.0/16.0 + (network.getAmount() / (float)network.getCapacity()) * (12.0/16.0), 0);
            PoseStack.Pose pose = poseStack.last();

            VertexConsumer builder = new FluidVertexConsumer(buffer, water.getFluidState(), pose.pose(), pose.normal());
            Minecraft.getInstance().getBlockRenderer().renderLiquid(BlockPos.ZERO, level, builder, water, water.getFluidState());

            poseStack.popPose();
            level.clearData();
        }
    }
}
