package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.pipeline.RemappingVertexPipeline;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class FluidVertexConsumer implements VertexConsumer {
    private final VertexConsumer parent;
    private final PoseStack.Pose pose;

    public FluidVertexConsumer(@NotNull MultiBufferSource buffer, @NotNull FluidState fluid, @NotNull PoseStack.Pose pose) {
        RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluid);
        VertexConsumer superParent = buffer.getBuffer(RenderTypeHelper.getEntityRenderType(renderType));
        this.parent = new RemappingVertexPipeline(superParent, DefaultVertexFormat.NEW_ENTITY);
        this.pose = pose;
    }

    @NotNull
    @Override
    public VertexConsumer addVertex(float pX, float pY, float pZ) {
        return addVertex(pose, pX, pY, pZ);
    }

    @NotNull
    @Override
    public VertexConsumer setColor(int pRed, int pGreen, int pBlue, int pAlpha) {
        parent.setColor(pRed, pGreen, pBlue, pAlpha);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv(float pU, float pV) {
        parent.setUv(pU, pV);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv1(int pU, int pV) {
        parent.setUv1(pU, pV);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv2(int pU, int pV) {
        parent.setUv2(pU, pV);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setNormal(float pX, float pY, float pZ) {
        return setNormal(pose, pX, pY, pZ);
    }

    @NotNull
    @Override
    public VertexConsumer addVertex(PoseStack.Pose pose, float pX, float pY, float pZ) {
        Vector4f vector4f = pose.pose().transform(new Vector4f(pX, pY, pZ, 1.0F));
        parent.addVertex(vector4f.x, vector4f.y, vector4f.z);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setNormal(PoseStack.Pose pose, float pX, float pY, float pZ) {
        Vector3f vector3f = pose.normal().transform(new Vector3f(pX, pY, pZ));
        parent.setNormal(vector3f.x(), vector3f.y(), vector3f.z());
        return this;
    }
}
