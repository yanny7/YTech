package com.yanny.ytech.configuration.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import com.yanny.ytech.configuration.block_entity.CraftingWorkspaceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class CraftingWorkspaceRenderer implements BlockEntityRenderer<CraftingWorkspaceBlockEntity> {
    private static final FakeCraftingWorkspaceLevel FAKE_LEVEL;

    static {
        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator<FakeCraftingWorkspaceLevel> instantiator = objenesis.getInstantiatorOf(FakeCraftingWorkspaceLevel.class);
        FAKE_LEVEL = instantiator.newInstance();
        FAKE_LEVEL.init();
    }

    public CraftingWorkspaceRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull CraftingWorkspaceBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();

        poseStack.pushPose();
        poseStack.scale(1/3.0f, 1/3.0f, 1/3.0f);

        if (level != null) {
            int bitmask = blockEntity.getBitmask();
            int i = 0;
            NonNullList<ItemStack> items = blockEntity.getItems();
            NonNullList<BlockState> states = blockEntity.getBlockStates();

            FAKE_LEVEL.setData(blockEntity.getBlockPos(), level, items, states);
            ModelBlockRenderer.enableCaching();

            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    for (int x = 0; x < 3; x++) {
                        if ((bitmask >> i & 1) == 1) {
                            int[] position = CraftingWorkspaceBlock.getPosition(i);
                            ItemStack itemStack = items.get(i);

                            if (position == null || itemStack.isEmpty()) {
                                continue;
                            }

                            poseStack.pushPose();
                            poseStack.translate(x, y, z);

                            if (itemStack.getItem() instanceof BlockItem blockItem) {
                                BlockState state = states.get(i);
                                BlockPos pos = new BlockPos(x + 1, y + 1, z + 1);

                                BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);

                                for (RenderType renderType : model.getRenderTypes(state, RandomSource.create(state.getSeed(pos)), ModelData.EMPTY)) {
                                    VertexConsumer vertexConsumer = buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType));
                                    Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                                            .tesselateBlock(FAKE_LEVEL, model, state, pos, poseStack, vertexConsumer, true,
                                                    RandomSource.create(), state.getSeed(pos), packedOverlay, ModelData.EMPTY, renderType);
                                }

                                // Rendering BEWLR
                                /*if (blockItem.getBlock() instanceof EntityBlock entityBlock) {
                                    BlockEntity be = entityBlock.newBlockEntity(pos, state);
                                    if (be != null) {
                                        be.setLevel(FAKE_LEVEL);
                                        BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(be);

                                        if (renderer != null) {
                                            renderer.render(be, partialTick, poseStack, buffer, packedLight, packedOverlay);
                                        }
                                    }
                                }*/
                            } else {
                                poseStack.translate(0.5, 0.5, 0.5);
                                poseStack.pushPose();
                                poseStack.rotateAround(Axis.YN.rotationDegrees(level.getGameTime() % 360), 0, 0, 0);

                                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                                BakedModel bakedmodel = itemRenderer.getModel(itemStack, level, null, 0);
                                itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, bakedmodel);

                                poseStack.popPose();
                            }

                            poseStack.popPose();
                        }

                        i++;
                    }
                }
            }

            FAKE_LEVEL.clearData();
            ModelBlockRenderer.clearCache();
        }

        poseStack.popPose();
    }
}
