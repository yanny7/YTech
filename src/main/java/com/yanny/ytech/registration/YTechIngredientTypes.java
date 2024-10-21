package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.YTechIngredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class YTechIngredientTypes {
    private static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, YTechMod.MOD_ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<YTechIngredient>> INGREDIENT_COUNT = INGREDIENT_TYPES.register("ingredient", () -> new IngredientType<>(YTechIngredient.CODEC, YTechIngredient.STREAM_CODEC));

    public static void register(IEventBus eventBus) {
        INGREDIENT_TYPES.register(eventBus);
    }
}
