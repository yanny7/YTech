package com.yanny.ytech.compatibility;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

public class KubeJsCompatibility implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(ref(YTechRecipeTypes.ALLOYING), AlloyingJS.SCHEMA);
        registry.register(ref(YTechRecipeTypes.BLOCK_HIT), BlockHitJS.SCHEMA);
        registry.register(ref(YTechRecipeTypes.DRYING), DryingJS.SCHEMA);
        registry.register(ref(YTechRecipeTypes.HAMMERING), HammeringJS.SCHEMA);
        registry.register(ref(YTechRecipeTypes.MILLING), MillingJS.SCHEMA);
        registry.register(ref(YTechRecipeTypes.POTTERY), PotteryJS.SCHEMA);
        registry.namespace(YTechMod.MOD_ID).shapeless("remaining_part_shapeless_crafting");
        registry.namespace(YTechMod.MOD_ID).shaped("remaining_shaped_crafting");
        registry.namespace(YTechMod.MOD_ID).shapeless("remaining_shapeless_crafting");
        registry.register(ref(YTechRecipeTypes.SMELTING), SmeltingJS.SCHEMA);
        registry.register(ref(YTechRecipeTypes.TANNING), TanningJS.SCHEMA);
    }

    private static <T extends Recipe<?>> ResourceLocation ref(DeferredHolder<RecipeType<?>, RecipeType<T>> recipeType) {
        return ResourceLocation.parse(recipeType.get().toString());
    }

    private static class AlloyingJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<SizedIngredient> INGREDIENT1 = SizedIngredientComponent.NESTED.key("ingredient1", ComponentRole.INPUT);
        private static final RecipeKey<SizedIngredient> INGREDIENT2 = SizedIngredientComponent.NESTED.key("ingredient2", ComponentRole.INPUT);
        private static final RecipeKey<Integer> MIN_TEMPERATURE = NumberComponent.INT.key("minTemp", ComponentRole.OTHER).optional(1000).exclude();
        private static final RecipeKey<Integer> SMELTING_TIME = NumberComponent.INT.key("smeltingTime", ComponentRole.OTHER).optional(200).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT1, INGREDIENT2, MIN_TEMPERATURE, SMELTING_TIME);
    }

    private static class BlockHitJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.NON_EMPTY_INGREDIENT.key("ingredient", ComponentRole.INPUT);
        private static final RecipeKey<Ingredient> BLOCK = IngredientComponent.NON_EMPTY_INGREDIENT.key("block", ComponentRole.OTHER);
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, BLOCK);
    }

    private static class DryingJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.NON_EMPTY_INGREDIENT.key("ingredient", ComponentRole.INPUT);
        private static final RecipeKey<Integer> DRYING_TIME = NumberComponent.INT.key("dryingTime", ComponentRole.OTHER).optional(1200).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, DRYING_TIME);
    }

    private static class HammeringJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.NON_EMPTY_INGREDIENT.key("ingredient", ComponentRole.INPUT);
        private static final RecipeKey<Ingredient> TOOL = IngredientComponent.INGREDIENT.key("tool", ComponentRole.INPUT).allowEmpty().optional(Ingredient.EMPTY).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, TOOL);
    }

    private static class MillingJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.NON_EMPTY_INGREDIENT.key("ingredient", ComponentRole.INPUT);
        private static final RecipeKey<Float> BONUS_CHANCE = NumberComponent.FLOAT.key("bonusChance", ComponentRole.OTHER).optional(0.5f).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, BONUS_CHANCE);
    }

    private static class PotteryJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Integer> COUNT = NumberComponent.INT.key("count", ComponentRole.OTHER);
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, COUNT);
    }

    private static class SmeltingJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.NON_EMPTY_INGREDIENT.key("ingredient", ComponentRole.INPUT);
        private static final RecipeKey<Integer> INPUT_COUNT = NumberComponent.INT.key("inputCount", ComponentRole.OTHER).optional(1).exclude();
        private static final RecipeKey<Ingredient> MOLD = IngredientComponent.INGREDIENT.key("mold", ComponentRole.INPUT).allowEmpty().optional(Ingredient.EMPTY).exclude();
        private static final RecipeKey<Integer> MIN_TEMP = NumberComponent.INT.key("minTemp", ComponentRole.OTHER).optional(1000).exclude();
        private static final RecipeKey<Integer> SMELTING_TIME = NumberComponent.INT.key("smeltingTime", ComponentRole.OTHER).optional(200).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, INPUT_COUNT, MOLD, MIN_TEMP, SMELTING_TIME);
    }

    private static class TanningJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.NON_EMPTY_INGREDIENT.key("ingredient", ComponentRole.INPUT);
        private static final RecipeKey<Ingredient> TOOL = IngredientComponent.INGREDIENT.key("tool", ComponentRole.INPUT).allowEmpty().optional(Ingredient.EMPTY).exclude();
        private static final RecipeKey<Integer> HIT_COUNT = NumberComponent.INT.key("hitCount", ComponentRole.OTHER).optional(5).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, TOOL, HIT_COUNT);
    }
}
