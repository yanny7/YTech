package com.yanny.ytech.compatibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapedRecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapelessRecipeSchema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

public class KubeJsCompatibility extends KubeJSPlugin {
    static RecipeComponent<InputItem> YTECH_INPUT = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return InputItem.class;
        }

        @Override
        public JsonElement write(RecipeJS recipeJS, InputItem inputItem) {
            JsonObject result = new JsonObject();
            result.add("ingredient", inputItem.ingredient.toJson());
            result.addProperty("count", inputItem.count);
            return result;
        }

        @Override
        public InputItem read(RecipeJS recipeJS, Object o) {
            if (o instanceof JsonObject json && json.has("ingredient")) {
                return InputItem.of(IngredientJS.of(json.get("ingredient")), json.get("count").getAsInt());
            } else {
                return InputItem.of(o);
            }
        }
    };

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(ref(YTechRecipeTypes.ALLOYING), AlloyingJS.SCHEMA);
        event.register(ref(YTechRecipeTypes.BLOCK_HIT), BlockHitJS.SCHEMA);
        event.register(ref(YTechRecipeTypes.DRYING), DryingJS.SCHEMA);
        event.register(ref(YTechRecipeTypes.HAMMERING), HammeringJS.SCHEMA);
        event.register(ref(YTechRecipeTypes.MILLING), MillingJS.SCHEMA);
        event.register(ref(YTechRecipeTypes.POTTERY), PotteryJS.SCHEMA);
        event.register(Utils.modLoc("remaining_shaped_crafting"), ShapedRecipeSchema.SCHEMA);
        event.register(Utils.modLoc("remaining_shapeless_crafting"), ShapelessRecipeSchema.SCHEMA);
        event.register(ref(YTechRecipeTypes.SMELTING), SmeltingJS.SCHEMA);
        event.register(ref(YTechRecipeTypes.TANNING), TanningJS.SCHEMA);
    }

    private static <T extends Recipe<?>> ResourceLocation ref(RegistryObject<RecipeType<T>> recipeType) {
        return new ResourceLocation(recipeType.get().toString());
    }

    private static class AlloyingJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT1 = YTECH_INPUT.key("ingredient1");
        private static final RecipeKey<InputItem> INGREDIENT2 = YTECH_INPUT.key("ingredient2");
        private static final RecipeKey<Integer> MIN_TEMPERATURE = NumberComponent.INT.key("minTemp").optional(1000).exclude();
        private static final RecipeKey<Integer> SMELTING_TIME = NumberComponent.INT.key("smeltingTime").optional(200).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(AlloyingJS.class, AlloyingJS::new, RESULT, INGREDIENT1, INGREDIENT2, MIN_TEMPERATURE, SMELTING_TIME);
    }

    private static class BlockHitJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
        private static final RecipeKey<InputItem> BLOCK = ItemComponents.INPUT.key("block");
        private static final RecipeSchema SCHEMA = new RecipeSchema(BlockHitJS.class, BlockHitJS::new, RESULT, INGREDIENT, BLOCK);
    }

    private static class DryingJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
        private static final RecipeKey<Integer> DRYING_TIME = NumberComponent.INT.key("dryingTime").optional(1200).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(DryingJS.class, DryingJS::new, RESULT, INGREDIENT, DRYING_TIME);
    }

    private static class HammeringJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
        private static final RecipeKey<InputItem> TOOL = ItemComponents.INPUT.key("tool").optional(InputItem.EMPTY).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(HammeringJS.class, HammeringJS::new, RESULT, INGREDIENT, TOOL);
    }

    private static class MillingJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
        private static final RecipeKey<Float> BONUS_CHANCE = NumberComponent.FLOAT.key("bonusChance").optional(0.5f).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(MillingJS.class, MillingJS::new, RESULT, INGREDIENT, BONUS_CHANCE);
    }

    private static class PotteryJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<Integer> COUNT = NumberComponent.INT.key("count");
        private static final RecipeSchema SCHEMA = new RecipeSchema(PotteryJS.class, PotteryJS::new, RESULT, COUNT);
    }

    private static class SmeltingJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
        private static final RecipeKey<Integer> INPUT_COUNT = NumberComponent.INT.key("inputCount").optional(1).exclude();
        private static final RecipeKey<InputItem> MOLD = ItemComponents.INPUT.key("mold").allowEmpty().optional(InputItem.EMPTY).exclude();
        private static final RecipeKey<Integer> MIN_TEMP = NumberComponent.INT.key("minTemp").optional(1000).exclude();
        private static final RecipeKey<Integer> SMELTING_TIME = NumberComponent.INT.key("smeltingTime").optional(200).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(SmeltingJS.class, SmeltingJS::new, RESULT, INGREDIENT, INPUT_COUNT, MOLD, MIN_TEMP, SMELTING_TIME);
    }

    private static class TanningJS extends RecipeJS {
        private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
        private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
        private static final RecipeKey<InputItem> TOOL = ItemComponents.INPUT.key("tool").optional(InputItem.EMPTY).exclude();
        private static final RecipeKey<Integer> HIT_COUNT = NumberComponent.INT.key("hitCount").optional(5).exclude();
        private static final RecipeSchema SCHEMA = new RecipeSchema(TanningJS.class, TanningJS::new, RESULT, INGREDIENT, TOOL, HIT_COUNT);
    }
}
