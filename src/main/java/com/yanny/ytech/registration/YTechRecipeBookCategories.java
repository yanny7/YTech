package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechRecipeBookCategories {
    private static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = DeferredRegister.create(BuiltInRegistries.RECIPE_BOOK_CATEGORY, YTechMod.MOD_ID);

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> ALLOYING = RECIPE_BOOK_CATEGORIES.register("alloying", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> BLOCK_HIT = RECIPE_BOOK_CATEGORIES.register("block_hit", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> CHOPPING = RECIPE_BOOK_CATEGORIES.register("chopping", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> DRYING = RECIPE_BOOK_CATEGORIES.register("drying", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> HAMMERING = RECIPE_BOOK_CATEGORIES.register("hammering", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> MILLING = RECIPE_BOOK_CATEGORIES.register("milling", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> POTTERY = RECIPE_BOOK_CATEGORIES.register("pottery", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> SMELTING = RECIPE_BOOK_CATEGORIES.register("smelting", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> TANNING = RECIPE_BOOK_CATEGORIES.register("tanning", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> WORKSPACE_CRAFTING = RECIPE_BOOK_CATEGORIES.register("workspace_crafting", RecipeBookCategory::new);

    public static void register(IEventBus eventBus) {
        RECIPE_BOOK_CATEGORIES.register(eventBus);
    }
}
