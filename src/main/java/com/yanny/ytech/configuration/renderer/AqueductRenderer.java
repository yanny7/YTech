package com.yanny.ytech.configuration.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.AqueductBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationClientNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

@OnlyIn(Dist.CLIENT)
public class AqueductRenderer implements BlockEntityRenderer<AqueductBlockEntity> {
    private final TextureAtlasSprite sprite;
    private final float r, g, b, a;
    private final float minU, minV, maxU, maxV;

    public AqueductRenderer(BlockEntityRendererProvider.Context context) {
        Fluid fluid = Fluids.WATER;
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        int color = extensions.getTintColor(fluidStack);

        sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(extensions.getStillTexture(fluidStack));

        a = (color >> 24 & 0xFF) / 255.0f;
        r = (color >> 16 & 0xFF) / 255.0f;
        g = (color >> 8 & 0xFF) / 255.0f;
        b = (color & 0xFF) / 255.0f;

        minU = sprite.getU0();
        minV = sprite.getV0();

        maxU = sprite.getU1();
        maxV = sprite.getV1();
    }

    @Override
    @NotNull
    public AABB getRenderBoundingBox(@NotNull AqueductBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos());
    }

    @Override
    public void render(@NotNull AqueductBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        IrrigationClientNetwork network = YTechMod.IRRIGATION_PROPAGATOR.client().getNetwork(blockEntity);

        if (network != null && network.getCapacity() > 0 && network.getAmount() > 0) {
            float height = 2/16f + (network.getAmount() / (float)network.getCapacity()) * (12.0f/16.0f);
            renderFluidBlock(blockEntity.getLevel(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.TRANSLUCENT), height, packedOverlay);
        }
    }
    private static BakedQuad createQuad(List<Vec3> vectors, float[] cols, TextureAtlasSprite sprite, Direction face, float u1, float u2, float v1, float v2) {
        QuadBakingVertexConsumer quadBaker = new QuadBakingVertexConsumer();
        Vec3 normal = Vec3.atLowerCornerOf(face.getUnitVec3i());

        putVertex(quadBaker, normal, vectors.get(0).x, vectors.get(0).y, vectors.get(0).z, u1, v1, sprite, cols, face);
        putVertex(quadBaker, normal, vectors.get(1).x, vectors.get(1).y, vectors.get(1).z, u1, v2, sprite, cols, face);
        putVertex(quadBaker, normal, vectors.get(2).x, vectors.get(2).y, vectors.get(2).z, u2, v2, sprite, cols, face);
        putVertex(quadBaker, normal, vectors.get(3).x, vectors.get(3).y, vectors.get(3).z, u2, v1, sprite, cols, face);

        return quadBaker.bakeQuad();
    }

    private static void putVertex(QuadBakingVertexConsumer quadBaker, Vec3 normal, double x, double y, double z, float u, float v,
                                  TextureAtlasSprite sprite, float[] cols, Direction face) {
        quadBaker.addVertex((float) x, (float) y, (float) z);
        quadBaker.setNormal((float) normal.x, (float) normal.y, (float) normal.z);
        quadBaker.setColor(cols[0], cols[1], cols[2], cols[3]);
        quadBaker.setUv(u, v);
        quadBaker.setSprite(sprite);
        quadBaker.setDirection(face);
    }

    public void renderFluidBlock(Level level, BlockPos pos, PoseStack matrixStackIn, VertexConsumer builder, float height, int packedOverlay) {
        int brightness = getLightColor(level, pos);

        float x = 0.0f;
        float z = 0.0f;

        float x2 = 1.0f;
        float z2 = 1.0f;

        float[] cols = new float[]{r, g, b, a};

        BakedQuad quad;
        matrixStackIn.pushPose();

        quad = createQuad(ImmutableList.of(new Vec3(x, height, z), new Vec3(x, height, z2), new Vec3(x2, height, z2), new Vec3(x2, height, z)), cols, sprite, Direction.UP, minU, maxU, minV, maxV);
        builder.putBulkData(matrixStackIn.last(), quad, r, g, b, a, brightness, packedOverlay, false);

        matrixStackIn.popPose();

    }
}
