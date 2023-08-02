package com.yanny.ytech;

import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static boolean shouldNotRemove(Recipe<?> recipe, Set<ResourceLocation> toRemove) {
        if (toRemove.contains(recipe.getId())) {
            LOGGER.info(MessageFormat.format("Removing recipe {0}", recipe.getId().toString()));
            return false;
        } else {
            return true;
        }
    }
}
