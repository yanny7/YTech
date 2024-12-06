package com.yanny.ytech;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import com.yanny.ytech.configuration.block.GrassBedBlock;
import com.yanny.ytech.configuration.block.WoodenBoxBlock;
import com.yanny.ytech.configuration.recipe.YTechRecipeInput;
import com.yanny.ytech.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.block.CreateFluidSourceEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

@EventBusSubscriber(modid = YTechMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeBusSubscriber {
    @NotNull private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onPlayerSpawnSet(@NotNull PlayerSetSpawnEvent event) {
        BlockPos pos = event.getNewSpawn();

        if (pos != null && event.getEntity().level().getBlockState(pos).getBlock() instanceof GrassBedBlock) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLevelLoad(@NotNull LevelEvent.Load event) {
        LevelAccessor levelAccessor = event.getLevel();

        if (levelAccessor instanceof ServerLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.server().onLevelLoad(level);
        } else if (levelAccessor instanceof ClientLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.client().onLevelLoad(level);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(@NotNull LevelEvent.Unload event) {
        LevelAccessor levelAccessor = event.getLevel();

        if (levelAccessor instanceof ServerLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.server().onLevelUnload(level);
        } else if (levelAccessor instanceof ClientLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.client().onLevelUnload(level);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(@NotNull ServerStartingEvent event) {
        BuiltInRegistries.BLOCK.getOrThrow(YTechBlockTags.REQUIRE_VALID_TOOL).stream().map(Holder::value).forEach(ForgeBusSubscriber::setBlockRequireValidTool);
    }

    @SubscribeEvent
    public static void onPlayerLogIn(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        YTechMod.IRRIGATION_PROPAGATOR.server().onPlayerLogIn(event.getEntity());
    }

    @SubscribeEvent
    public static void onLevelPreTick(@NotNull LevelTickEvent.Pre event) {
        if (event.getLevel() instanceof ServerLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.server().getNetworks(level).values().forEach((network) -> network.tick(level));
        }
    }

    @SubscribeEvent
    public static void onLevelPostTick(@NotNull LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.server().tick(level);
        }
    }

    @SubscribeEvent
    public static void onChunkWatch(@NotNull ChunkWatchEvent.Watch event) {
        YTechMod.IRRIGATION_PROPAGATOR.server().onChunkWatch(event.getLevel(), event.getPlayer(), event.getChunk());
    }

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(@NotNull PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel() instanceof ServerLevel level) {
            Player player = event.getEntity();
            ItemStack heldItem = player.getMainHandItem();
            BlockState blockState = level.getBlockState(event.getPos());
            Direction direction = event.getFace();

            if (!level.isClientSide && !player.isCreative() && direction != null && event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.START && event.getHand() == InteractionHand.MAIN_HAND) {
                level.recipeAccess().getRecipeFor(YTechRecipeTypes.BLOCK_HIT.get(), new YTechRecipeInput(heldItem, blockState.getBlock().asItem().getDefaultInstance()), level).ifPresent((recipe) -> {
                    Block.popResourceFromFace(level, event.getPos(), direction, recipe.value().result().copy());
                    heldItem.shrink(1);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingBreatheEvent(@NotNull LivingBreatheEvent event) {
        if (event.getEntity().hasEffect(YTechMobEffects.ABYSS_WALKER) && event.getEntity().level().getGameTime() % 2 == 0) {
            event.setConsumeAirAmount(0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderHighlightEvent(RenderHighlightEvent.Block event) {
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;

        if (level != null && player != null) {
            if (level.getBlockState(event.getTarget().getBlockPos()).is(YTechBlocks.CRAFTING_WORKSPACE.get())) {
                Vec3 camera = event.getCamera().getPosition();
                PoseStack poseStack = event.getPoseStack();
                ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);

                int[] position = CraftingWorkspaceBlock.getPosition(event.getTarget(), itemStack.isEmpty());

                if (position == null) {
                    return;
                }

                poseStack.pushPose();
                poseStack.translate(-camera.x, -camera.y, -camera.z);
                poseStack.translate(position[0] / 3.0, position[1] / 3.0, position[2] / 3.0);

                float cR = 0.1f;
                float cG = 1.0f;
                float cB = 0.1f;
                BlockPos target = event.getTarget().getBlockPos();
                ShapeRenderer.renderLineBox(
                        poseStack,
                        event.getMultiBufferSource().getBuffer(RenderType.LINES),
                        CraftingWorkspaceBlock.BOX.bounds().move(target),
                        cR,
                        cG,
                        cB,
                        1f
                );

                poseStack.popPose();
            } else if (level.getBlockState(event.getTarget().getBlockPos()).is(YTechBlocks.WOODEN_BOX.get())) {
                Vec3 camera = event.getCamera().getPosition();
                PoseStack poseStack = event.getPoseStack();
                BlockHitResult hitResult = event.getTarget();

                int[] position = WoodenBoxBlock.getPosition(event.getTarget());

                if (position == null || hitResult.getDirection() != Direction.UP) {
                    return;
                }

                poseStack.pushPose();
                poseStack.translate(-camera.x, -camera.y, -camera.z);
                poseStack.translate(1/8.0 + position[0] / 4.0, 8/16.0, 1/8.0 + position[1] / 4.0);

                float cR = 0.1f;
                float cG = 0.1f;
                float cB = 0.1f;
                BlockPos target = event.getTarget().getBlockPos();
                ShapeRenderer.renderLineBox(
                        poseStack,
                        event.getMultiBufferSource().getBuffer(RenderType.LINES),
                        WoodenBoxBlock.BOX.bounds().move(target),
                        cR,
                        cG,
                        cB,
                        0.5f
                );

                poseStack.popPose();
            }
        }
    }

    @SubscribeEvent
    public static void onCreateFluidSourceEvent(@NotNull CreateFluidSourceEvent event) {
        if (event.getState().getFluidState().is(Fluids.WATER) && YTechMod.CONFIGURATION.hasFiniteWaterSource()
                && !event.getLevel().getBiome(event.getPos()).is(YTechBiomeTags.INFINITE_WATER_SOURCE_BIOMES)) {
            event.setCanConvert(false);
        }
    }

    @SubscribeEvent
    public static void onCropGrowEvent(@NotNull CropGrowEvent.Pre event) {
        if (YTechMod.CONFIGURATION.cropsNeedWateredFarmland()) {
            BlockState blockState = event.getLevel().getBlockState(event.getPos().below());

            if (blockState.hasProperty(BlockStateProperties.MOISTURE) && blockState.getValue(BlockStateProperties.MOISTURE) < FarmBlock.MAX_MOISTURE) {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            }
        }
    }

    private static void setBlockRequireValidTool(@NotNull Block block) {
        try {
            BlockState blockState = ObfuscationReflectionHelper.getPrivateValue(Block.class, block, "defaultBlockState"); // defaultBlockState

            if (blockState != null) {
                ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.BlockStateBase.class, blockState, Boolean.TRUE, "requiresCorrectToolForDrops"); // requiresCorrectToolForDrops
            }

            LOGGER.info("Set requiresCorrectToolForDrops on {}", Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)));
        } catch (Exception e) {
            LOGGER.warn("Unable to set requiresCorrectToolForDrops on block {}: {}", Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)), e.getMessage());
        }
    }
}
