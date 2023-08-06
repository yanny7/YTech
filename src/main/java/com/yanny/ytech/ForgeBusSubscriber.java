package com.yanny.ytech;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.configuration.SimpleToolType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yanny.ytech.registration.Registration.HOLDER;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusSubscriber {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        LevelAccessor levelAccessor = event.getLevel();

        if (levelAccessor instanceof ServerLevel level) {
            YTechMod.KINETIC_PROPAGATOR.server().onLevelLoad(level);
        } else if (levelAccessor instanceof ClientLevel level) {
            YTechMod.KINETIC_PROPAGATOR.client().onLevelLoad(level);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        LevelAccessor levelAccessor = event.getLevel();

        if (levelAccessor instanceof ServerLevel level) {
            YTechMod.KINETIC_PROPAGATOR.server().onLevelUnload(level);
        } else if (levelAccessor instanceof ClientLevel level) {
            YTechMod.KINETIC_PROPAGATOR.client().onLevelUnload(level);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        if (YTechMod.CONFIGURATION.shouldRequireValidTool()) {
            YTechMod.CONFIGURATION.getBlocksRequiringValidTool().forEach((block) ->
                    setBlockRequireValidTool(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(block))));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        YTechMod.KINETIC_PROPAGATOR.server().onPlayerLogIn(event.getEntity());
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(new SimplePreparableReloadListener<Set<ResourceLocation>>() {
            @NotNull
            @Override
            protected Set<ResourceLocation> prepare(@NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
                return YTechMod.CONFIGURATION.getRemoveMinecraftRecipesList();
            }

            @Override
            protected void apply(@NotNull Set<ResourceLocation> toRemove, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
                if (YTechMod.CONFIGURATION.shouldRemoveMinecraftRecipes()) {
                    RecipeManager recipeManager = event.getServerResources().getRecipeManager();
                    recipeManager.replaceRecipes(recipeManager.getRecipes().stream().filter((r) -> shouldNotRemove(r, toRemove)).collect(Collectors.toList()));
                }
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack heldItem = player.getMainHandItem();
        BlockState blockState = level.getBlockState(event.getPos());
        Direction direction = event.getFace();

        if (!level.isClientSide && direction != null && heldItem.is(Items.FLINT) && blockState.is(Tags.Blocks.STONE)) {
            Block.popResourceFromFace(level, event.getPos(), direction, new ItemStack(HOLDER.simpleTools().get(SimpleToolType.SHARP_FLINT).item.get()));
            heldItem.shrink(1);
        }
    }

    private static boolean shouldNotRemove(Recipe<?> recipe, Set<ResourceLocation> toRemove) {
        if (toRemove.contains(recipe.getId())) {
            LOGGER.info(MessageFormat.format("Removing recipe {0}", recipe.getId().toString()));
            return false;
        } else {
            return true;
        }
    }

    private static void setBlockRequireValidTool(Block block) {
        try {
            BlockState blockState = ObfuscationReflectionHelper.getPrivateValue(Block.class, block, "defaultBlockState");

            if (blockState != null) {
                ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.BlockStateBase.class, blockState, Boolean.TRUE, "requiresCorrectToolForDrops");
            }

            LOGGER.info("Set requiresCorrectToolForDrops on {}", block.getName());
        } catch (Exception e) {
            LOGGER.warn("Unable to set requiresCorrectToolForDrops on block " + block.getName().getString() + ": " + e.getMessage());
        }
    }
}
