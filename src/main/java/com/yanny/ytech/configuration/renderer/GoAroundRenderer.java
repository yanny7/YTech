package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.configuration.block_entity.MillstoneBlockEntity;
import com.yanny.ytech.configuration.entity.GoAroundEntity;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class GoAroundRenderer extends MobRenderer<GoAroundEntity, GoAroundRenderer.GoAroundRenderState, CowModel> {
    private static final ResourceLocation COW_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/cow/cow.png");

    public GoAroundRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CowModel(pContext.bakeLayer(ModelLayers.COW)), 0.1F);
    }

    @NotNull
    @Override
    public GoAroundRenderState createRenderState() {
        return new GoAroundRenderState();
    }

    @Override
    public void render(@NotNull GoAroundRenderState state, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        MillstoneBlockEntity leashEntity = state.millstone;
        Mob entity = state.mob;

        if (leashEntity != null && entity != null) {
            double offset = state.y - entity.getY();
            this.renderLeash2(entity, offset, state.partialTick, pPoseStack, pBuffer, leashEntity);
        }
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull GoAroundRenderState state) {
        return COW_LOCATION;
    }

    @Override
    public void extractRenderState(@NotNull GoAroundEntity goAroundEntity, @NotNull GoAroundRenderState renderState, float p_361157_) {
        super.extractRenderState(goAroundEntity, renderState, p_361157_);
        renderState.millstone = goAroundEntity.getDevice();
        renderState.mob = (Mob) goAroundEntity.getVehicle();
    }

    public void renderLeash2(Mob pEntityLiving, double offset, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, BlockEntity pLeashHolder) {
        pPoseStack.pushPose();
        Vec3 $$5 = pLeashHolder.getBlockPos().getCenter().add(0, 0.2, 0);
        double $$6 = (double)(Mth.lerp(pPartialTicks, pEntityLiving.yBodyRotO, pEntityLiving.yBodyRot) * 0.017453292F) + 1.5707963267948966;
        Vec3 $$7 = pEntityLiving.getLeashOffset(pPartialTicks);
        double $$8 = Math.cos($$6) * $$7.z + Math.sin($$6) * $$7.x;
        double $$9 = Math.sin($$6) * $$7.z - Math.cos($$6) * $$7.x;
        double $$10 = Mth.lerp(pPartialTicks, pEntityLiving.xo, pEntityLiving.getX()) + $$8;
        double $$11 = Mth.lerp(pPartialTicks, pEntityLiving.yo, pEntityLiving.getY()) + $$7.y;
        double $$12 = Mth.lerp(pPartialTicks, pEntityLiving.zo, pEntityLiving.getZ()) + $$9;
        pPoseStack.translate($$8, $$7.y - offset, $$9);
        float $$13 = (float)($$5.x - $$10);
        float $$14 = (float)($$5.y - $$11);
        float $$15 = (float)($$5.z - $$12);
        float $$16 = 0.025F;
        VertexConsumer $$17 = pBuffer.getBuffer(RenderType.leash());
        Matrix4f $$18 = pPoseStack.last().pose();
        float $$19 = Mth.invSqrt($$13 * $$13 + $$15 * $$15) * $$16 / 2.0F;
        float $$20 = $$15 * $$19;
        float $$21 = $$13 * $$19;
        BlockPos $$22 = BlockPos.containing(pEntityLiving.getEyePosition(pPartialTicks));
        BlockPos $$23 = pLeashHolder.getBlockPos();
        int $$24 = this.getBlockLightLevel2(pEntityLiving, $$22);
        int $$25 = this.getBlockLightLevel2(pEntityLiving, $$23);
        int $$26 = pEntityLiving.level().getBrightness(LightLayer.SKY, $$22);
        int $$27 = pEntityLiving.level().getBrightness(LightLayer.SKY, $$23);

        int $$29;
        for($$29 = 0; $$29 <= 24; ++$$29) {
            addVertexPair($$17, $$18, $$13, $$14, $$15, $$24, $$25, $$26, $$27, $$16, $$16, $$20, $$21, $$29, false);
        }

        for($$29 = 24; $$29 >= 0; --$$29) {
            addVertexPair($$17, $$18, $$13, $$14, $$15, $$24, $$25, $$26, $$27, $$16, 0.0F, $$20, $$21, $$29, true);
        }

        pPoseStack.popPose();
    }

    private int getBlockLightLevel2(Entity pEntity, BlockPos pPos) {
        return pEntity.isOnFire() ? 15 : pEntity.level().getBrightness(LightLayer.BLOCK, pPos);

    }

    public static class GoAroundRenderState extends LivingEntityRenderState {
        MillstoneBlockEntity millstone;
        Mob mob;
    }
}
