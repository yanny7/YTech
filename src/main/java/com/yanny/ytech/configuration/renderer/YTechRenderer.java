package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.configuration.model.SpearModel;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.yanny.ytech.configuration.model.SpearModel.*;

@OnlyIn(Dist.CLIENT)
public class YTechRenderer extends BlockEntityWithoutLevelRenderer {
    @NotNull public static final BlockEntityWithoutLevelRenderer INSTANCE = new YTechRenderer();

    @NotNull private final ItemModelShaper itemModelShaper;
    @NotNull private final ItemRenderer itemRenderer;
    @NotNull private final BakedModel missingModel;
    private final Map<SpearType, SpearModel> spearModels = new HashMap<>();

    private YTechRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        itemModelShaper = Minecraft.getInstance().getItemRenderer().getItemModelShaper();
        itemRenderer = Minecraft.getInstance().getItemRenderer();
        missingModel = Minecraft.getInstance().getModelManager().getMissingModel();

        for (SpearType type : SpearType.values()) {
            spearModels.put(type, new SpearModel(Minecraft.getInstance().getEntityModels().bakeLayer(LAYER_LOCATIONS.get(type))));
        }
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, 
                             @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // reset previous stack avoiding duplicate call to applyTransform
        poseStack.popPose();
        poseStack.pushPose();

        if (stack.getItem() instanceof SpearItem) {
            renderStatic(stack, displayContext, packedLight, packedOverlay, poseStack, buffer, Minecraft.getInstance().level, 0);
        } else {
            super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        }
    }

    public void render(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, boolean leftHand, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, @NotNull BakedModel bakedModel) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            boolean is2dModel = displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED;

            if (is2dModel) {
                for (SpearType spearType : spearModels.keySet()) {
                    if (stack.is(YTechItems.SPEARS.get(spearType.materialType).get())) {
                        bakedModel = itemModelShaper.getModelManager().getModel(MODEL_LOCATIONS.get(spearType));
                        break;
                    }
                }
            }

            bakedModel = bakedModel.applyTransform(displayContext, poseStack, leftHand);
            poseStack.translate(-0.5F, -0.5F, -0.5F);

            for (Map.Entry<SpearType, SpearModel> entry : spearModels.entrySet()) {
                if (stack.is(YTechItems.SPEARS.get(entry.getKey().materialType).get()) && !is2dModel) {
                    poseStack.pushPose();
                    poseStack.scale(1.0F, -1.0F, -1.0F);
                    VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, entry.getValue().renderType(SpearType.TEXTURE_LOCATION), false, stack.hasFoil());
                    entry.getValue().renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 0xFFFFFFFF);
                    poseStack.popPose();
                } else {
                    boolean isFabulous;

                    if (displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson() && stack.getItem() instanceof BlockItem) {
                        Block block = ((BlockItem)stack.getItem()).getBlock();

                        isFabulous = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                    } else {
                        isFabulous = true;
                    }

                    for (var model : bakedModel.getRenderPasses(stack, isFabulous)) {
                        for (var rendertype : model.getRenderTypes(stack, isFabulous)) {
                            VertexConsumer vertexConsumer;

                            if (isFabulous) {
                                vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
                            } else {
                                vertexConsumer = ItemRenderer.getFoilBuffer(buffer, rendertype, true, stack.hasFoil());
                            }

                            itemRenderer.renderModelLists(model, stack, packedLight, packedOverlay, poseStack, vertexConsumer);
                        }
                    }
                }
            }

            poseStack.popPose();
        }
    }

    public void renderStatic(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, int packedLight, int packedOverlay,
                             @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, @Nullable Level pLevel, int seed) {
        boolean leftHand = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        renderStatic(Minecraft.getInstance().player, stack, displayContext, leftHand, poseStack, buffer, pLevel, packedLight, packedOverlay, seed);
    }

    public void renderStatic(@Nullable LivingEntity pEntity, @NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, 
                             boolean pLeftHand, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, @Nullable Level pLevel,
                             int packedLight, int packedOverlay, int seed) {
        if (!stack.isEmpty()) {
            BakedModel bakedmodel = getModel(stack, pLevel, pEntity, seed);
            render(stack, displayContext, pLeftHand, poseStack, buffer, packedLight, packedOverlay, bakedmodel);
        }
    }

    @NotNull
    public BakedModel getModel(@NotNull ItemStack stack, @Nullable Level level, @Nullable LivingEntity pEntity, int seed) {
        BakedModel bakedModel = itemModelShaper.getItemModel(stack);

        for (SpearType spearType : spearModels.keySet()) {
            if (stack.is(YTechItems.SPEARS.get(spearType.materialType).get())) {
                bakedModel = itemModelShaper.getModelManager().getModel(MODEL_IN_HAND_LOCATIONS.get(spearType));
            }
        }

        ClientLevel clientlevel = level instanceof ClientLevel ? (ClientLevel)level : null;
        BakedModel modelOverride = bakedModel.getOverrides().resolve(bakedModel, stack, clientlevel, pEntity, seed);
        return modelOverride == null ? missingModel : modelOverride;
    }
}
