package com.yanny.ytech.compatibility;

import com.mojang.serialization.Codec;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.YTechIngredient;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
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

    @Override
    public void registerRecipeComponents(RecipeComponentFactoryRegistry registry) {
        registry.register(CountComponent.NON_EMPTY_INGREDIENT);
    }

    public static class CountComponent implements RecipeComponent<Ingredient> {
        public static final CountComponent NON_EMPTY_INGREDIENT = new CountComponent("non_empty_count_ingredient", Ingredient.CODEC_NONEMPTY);
        private static final TypeInfo TYPE_INFO = TypeInfo.of(Ingredient.class);

        public final String name;
        public final Codec<Ingredient> codec;

        public CountComponent(String name, Codec<Ingredient> codec) {
            this.name = name;
            this.codec = codec;
        }

        @Override
        public Codec<Ingredient> codec() {
            return codec;
        }

        @Override
        public Ingredient wrap(Context cx, KubeRecipe recipe, Object from) {
            if (from instanceof ItemStack itemStack) {
                return YTechIngredient.of(Ingredient.of(itemStack), itemStack.getCount()).toVanilla();
            }

            return RecipeComponent.super.wrap(cx, recipe, from);
        }

        @Override
        public TypeInfo typeInfo() {
            return TYPE_INFO;
        }
    }

    private static <T extends Recipe<?>> ResourceLocation ref(DeferredHolder<RecipeType<?>, RecipeType<T>> recipeType) {
        return ResourceLocation.parse(recipeType.get().toString());
    }

    private static class AlloyingJS {
        private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
        private static final RecipeKey<Ingredient> INGREDIENT1 = CountComponent.NON_EMPTY_INGREDIENT.key("ingredient1", ComponentRole.INPUT);
        private static final RecipeKey<Ingredient> INGREDIENT2 = CountComponent.NON_EMPTY_INGREDIENT.key("ingredient2", ComponentRole.INPUT);
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
