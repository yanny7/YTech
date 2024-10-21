package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, YTechMod.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DryingRecipe>> DRYING = RECIPE_SERIALIZERS.register("drying", DryingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TanningRecipe>> TANNING = RECIPE_SERIALIZERS.register("tanning", TanningRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MillingRecipe>> MILLING = RECIPE_SERIALIZERS.register("milling", MillingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmeltingRecipe>> SMELTING = RECIPE_SERIALIZERS.register("smelting", SmeltingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlockHitRecipe>> BLOCK_HIT = RECIPE_SERIALIZERS.register("block_hit", BlockHitRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AlloyingRecipe>> ALLOYING = RECIPE_SERIALIZERS.register("alloying", AlloyingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HammeringRecipe>> HAMMERING = RECIPE_SERIALIZERS.register("hammering", HammeringRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RemainingPartShapelessRecipe>> REMAINING_PART_SHAPELESS = RECIPE_SERIALIZERS.register("remaining_part_shapeless_crafting", RemainingPartShapelessRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RemainingShapelessRecipe>> REMAINING_SHAPELESS = RECIPE_SERIALIZERS.register("remaining_shapeless_crafting", RemainingShapelessRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RemainingShapedRecipe>> REMAINING_SHAPED = RECIPE_SERIALIZERS.register("remaining_shaped_crafting", RemainingShapedRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PotteryRecipe>> POTTERY = RECIPE_SERIALIZERS.register("pottery", PotteryRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
