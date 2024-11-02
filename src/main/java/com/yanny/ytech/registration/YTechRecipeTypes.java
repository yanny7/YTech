package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, YTechMod.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AlloyingRecipe>> ALLOYING = create("alloying");
    public static final DeferredHolder<RecipeType<?>, RecipeType<BlockHitRecipe>> BLOCK_HIT = create("block_hit");
    public static final DeferredHolder<RecipeType<?>, RecipeType<DryingRecipe>> DRYING = create("drying");
    public static final DeferredHolder<RecipeType<?>, RecipeType<HammeringRecipe>> HAMMERING = create("hammering");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MillingRecipe>> MILLING = create("milling");
    public static final DeferredHolder<RecipeType<?>, RecipeType<PotteryRecipe>> POTTERY = create("pottery");
    public static final DeferredHolder<RecipeType<?>, RecipeType<SmeltingRecipe>> SMELTING = create("smelting");
    public static final DeferredHolder<RecipeType<?>, RecipeType<TanningRecipe>> TANNING = create("tanning");
    public static final DeferredHolder<RecipeType<?>, RecipeType<WorkspaceCraftingRecipe>> WORKSPACE_CRAFTING = create("workspace_crafting");

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> create(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
                    @Override
                    public String toString() {
                        return Utils.modLoc(name).toString();
                    }
                }
        );
    }
}
