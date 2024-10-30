package com.yanny.ytech;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.yanny.ytech.configuration.block.GrassBedBlock;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechMobEffects;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

import static com.yanny.ytech.configuration.block.CraftingWorkspaceBlock.BOX;
import static com.yanny.ytech.configuration.block.CraftingWorkspaceBlock.getPosition;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        if (YTechMod.CONFIGURATION.shouldRequireValidTool()) {
            YTechMod.CONFIGURATION.getBlocksRequiringValidTool().forEach((tag) -> Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(tag).forEach(ForgeBusSubscriber::setBlockRequireValidTool));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogIn(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        YTechMod.IRRIGATION_PROPAGATOR.server().onPlayerLogIn(event.getEntity());
    }

    @SubscribeEvent
    public static void onLevelTick(@NotNull TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER && event.level instanceof ServerLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.server().getNetworks(level).values().forEach((network) -> network.tick((ServerLevel) event.level));
        }

        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER && event.level instanceof ServerLevel level) {
            YTechMod.IRRIGATION_PROPAGATOR.server().tick(level);
        }
    }

    @SubscribeEvent
    public static void onChunkWatch(@NotNull ChunkWatchEvent.Watch event) {
        YTechMod.IRRIGATION_PROPAGATOR.server().onChunkWatch(event.getLevel(), event.getPlayer(), event.getChunk());
    }

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(@NotNull PlayerInteractEvent.LeftClickBlock event) {
        if (YTechMod.CONFIGURATION.enableCraftingSharpFlint()) {
            Player player = event.getEntity();
            Level level = event.getLevel();
            ItemStack heldItem = player.getMainHandItem();
            BlockState blockState = level.getBlockState(event.getPos());
            Direction direction = event.getFace();

            if (!level.isClientSide && !player.isCreative() && direction != null && event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.START && event.getHand() == InteractionHand.MAIN_HAND) {
                level.getRecipeManager().getRecipeFor(YTechRecipeTypes.BLOCK_HIT.get(), new SimpleContainer(heldItem, blockState.getBlock().asItem().getDefaultInstance()), level).ifPresent((recipe) -> {
                    Block.popResourceFromFace(level, event.getPos(), direction, recipe.result().copy());
                    heldItem.shrink(1);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingBreatheEvent(@NotNull LivingBreatheEvent event) {
        if (event.getEntity().hasEffect(YTechMobEffects.ABYSS_WALKER.get()) && event.getEntity().level().getGameTime() % 2 == 0) {
            event.setConsumeAirAmount(0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void on(RenderHighlightEvent.Block event) {
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;

        if (level != null && player != null && level.getBlockState(event.getTarget().getBlockPos()).is(YTechBlocks.CRAFTING_WORKSPACE.get())) {
            Vec3 camera = event.getCamera().getPosition();
            PoseStack poseStack = event.getPoseStack();
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            boolean isBlock = itemStack.getItem() instanceof BlockItem;

            int[] position = getPosition(event.getTarget(), itemStack.isEmpty());

            if (position == null) {
                return;
            }

            poseStack.pushPose();
            poseStack.translate(-camera.x, -camera.y, -camera.z);
            poseStack.translate(position[0] / 3.0, position[1] / 3.0, position[2] / 3.0);

            float cR = isBlock ? 0.1f : 1.0f;
            float cG = isBlock ? 1.0f : 0.1f;
            float cB = 0.1f;
            BlockPos target = event.getTarget().getBlockPos();
            LevelRenderer.renderLineBox(
                    poseStack,
                    event.getMultiBufferSource().getBuffer(RenderType.LINES),
                    BOX.bounds().move(target),
                    cR,
                    cG,
                    cB,
                    1f
            );

            poseStack.popPose();
            event.setCanceled(false);
        }
    }

    private static void setBlockRequireValidTool(@NotNull Block block) {
        try {
            BlockState blockState = ObfuscationReflectionHelper.getPrivateValue(Block.class, block, "f_49786_"); // defaultBlockState

            if (blockState != null) {
                ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.BlockStateBase.class, blockState, Boolean.TRUE, "f_60600_"); // requiresCorrectToolForDrops
            }

            LOGGER.info("Set requiresCorrectToolForDrops on {}", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)));
        } catch (Exception e) {
            LOGGER.warn("Unable to set requiresCorrectToolForDrops on block {}: {}", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)), e.getMessage());
        }
    }
}
