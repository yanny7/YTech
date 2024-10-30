package com.yanny.ytech.configuration.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public record WorkspaceCraftingRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, ItemStack result) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        boolean matches = true;

        // Facing NORTH
        for (int i = 0; i < 27; i++) {
            if (!recipeItems.get(i).test(container.getItem(i))) {
                matches = false;
            }
        }

        // Facing WEST
        if (!matches) {
            int i = 0;

            matches = true;

            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        if (!recipeItems.get(i).test(container.getItem(x + z * 3 + y * 9))) {
                            matches = false;
                        }

                        i++;
                    }
                }
            }
        }

        // Facing SOUTH
        if (!matches) {
            int i = 0;

            matches = true;

            for (int y = 0; y < 3; y++) {
                for (int z = 2; z >= 0; z--) {
                    for (int x = 2; x >= 0; x--) {
                        if (!recipeItems.get(i).test(container.getItem(x + z * 3 + y * 9))) {
                            matches = false;
                        }

                        i++;
                    }
                }
            }
        }

        // Facing EAST
        if (!matches) {
            int i = 0;

            matches = true;

            for (int y = 0; y < 3; y++) {
                for (int x = 2; x >= 0; x--) {
                    for (int z = 2; z >= 0; z--) {
                        if (!recipeItems.get(i).test(container.getItem(x + z * 3 + y * 9))) {
                            matches = false;
                        }

                        i++;
                    }
                }
            }
        }

        return matches;
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result;
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return YTechRecipeSerializers.WORKSPACE_CRAFTING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.WORKSPACE_CRAFTING.get();
    }

    public static class Serializer implements RecipeSerializer<WorkspaceCraftingRecipe> {
        @NotNull
        @Override
        public WorkspaceCraftingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject pJson) {
            Map<String, Ingredient> keys = keyFromJson(GsonHelper.getAsJsonObject(pJson, "key"));
            String[][] pattern = patternFromJson(GsonHelper.getAsJsonObject(pJson, "pattern"));
            NonNullList<Ingredient> ingredients = dissolvePattern(pattern, keys);
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));

            if (!(result.getItem() instanceof BlockItem)) {
                throw new JsonSyntaxException("Result isn't BlockItem");
            }

            ingredients.forEach((i) -> Arrays.stream(i.getItems()).forEach(itemStack -> {
                if (!(itemStack.getItem() instanceof BlockItem)) {
                    throw new JsonSyntaxException("Ingredient " + itemStack + " isn't BlockItem");
                }
            }));

            return new WorkspaceCraftingRecipe(recipeId, ingredients, result);
        }

        @Override
        public @Nullable WorkspaceCraftingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(27, Ingredient.EMPTY);

            for (int k = 0; k < ingredients.size(); ++k) {
                ingredients.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack result = buffer.readItem();
            return new WorkspaceCraftingRecipe(recipeId, ingredients, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull WorkspaceCraftingRecipe recipe) {
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
        }
    }

    public record Result(@NotNull ResourceLocation id, @NotNull Map<Character, Ingredient> keyMap, List<String> bottom, List<String> middle, List<String> top, @NotNull Item result, @NotNull Advancement.Builder advancement,
                         @NotNull ResourceLocation advancementId) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            JsonObject keyObject = new JsonObject();
            JsonObject resultObject = new JsonObject();

            pJson.add("pattern", getPattern());

            for(Map.Entry<Character, Ingredient> entry : keyMap.entrySet()) {
                keyObject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }

            pJson.add("key", keyObject);
            resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
            pJson.add("result", resultObject);
        }

        @NotNull
        private JsonObject getPattern() {
            JsonObject pattern = new JsonObject();
            JsonArray bottomArray = new JsonArray();
            JsonArray middleArray = new JsonArray();
            JsonArray topArray = new JsonArray();

            for(String s : bottom) {
                bottomArray.add(s);
            }
            for (String s : middle) {
                middleArray.add(s);
            }
            for (String s : top) {
                topArray.add(s);
            }

            pattern.add("bottom", bottomArray);
            pattern.add("middle", middleArray);
            pattern.add("top", topArray);
            return pattern;
        }

        @NotNull
        @Override
        public ResourceLocation getId() {
            return id;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
            return YTechRecipeSerializers.WORKSPACE_CRAFTING.get();
        }

        @NotNull
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @NotNull
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Item result;
        private final List<String> bottomRows = Lists.newArrayList();
        private final List<String> middleRows = Lists.newArrayList();
        private final List<String> topRows = Lists.newArrayList();
        private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
        private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

        protected Builder(ItemLike pResult) {
            result = pResult.asItem();
        }

        public static Builder recipe(ItemLike pResult) {
            return new Builder(pResult);
        }

        public Builder define(Character pSymbol, TagKey<Item> pTag) {
            return define(pSymbol, Ingredient.of(pTag));
        }

        public Builder define(Character pSymbol, ItemLike pItem) {
            return define(pSymbol, Ingredient.of(pItem));
        }

        public Builder define(Character pSymbol, Ingredient pIngredient) {
            if (key.containsKey(pSymbol)) {
                throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
            } else if (pSymbol == ' ') {
                throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
            } else {
                key.put(pSymbol, pIngredient);
                return this;
            }
        }

        public Builder bottomPattern(String pPattern) {
            return pattern(pPattern, bottomRows);
        }

        public Builder middlePattern(String pPattern) {
            return pattern(pPattern, middleRows);
        }

        public Builder topPattern(String pPattern) {
            return pattern(pPattern, topRows);
        }

        @NotNull
        @Override
        public Builder unlockedBy(@NotNull String pCriterionName, @NotNull CriterionTriggerInstance pCriterionTrigger) {
            advancement.addCriterion(pCriterionName, pCriterionTrigger);
            return this;
        }

        @NotNull
        @Override
        public Builder group(@Nullable String pGroupName) {
            return this;
        }

        @NotNull
        @Override
        public Item getResult() {
            return result;
        }

        @Override
        public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
            ensureValid(pRecipeId);
            advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
            pFinishedRecipeConsumer.accept(new WorkspaceCraftingRecipe.Result(pRecipeId, key, bottomRows, middleRows, topRows, result, advancement, pRecipeId.withPrefix("recipes/workspace_crafting/")));
        }

        private Builder pattern(String pPattern, List<String> list) {
            if (pPattern.length() != 3) {
                throw new IllegalArgumentException("Pattern must have exactly 3 characters!");
            } else if (list.size() == 3) {
                throw new IllegalArgumentException("Pattern must have exactly 3 rows!");
            } else {
                list.add(pPattern);
                return this;
            }
        }

        private void ensureValid(ResourceLocation pId) {
            if (bottomRows.isEmpty()) {
                throw new IllegalStateException("No pattern is defined for bottom layer for recipe " + pId + "!");
            }
            if (middleRows.isEmpty()) {
                throw new IllegalStateException("No pattern is defined for middle layer for recipe " + pId + "!");
            }
            if (topRows.isEmpty()) {
                throw new IllegalStateException("No pattern is defined for bottom layer for recipe " + pId + "!");
            }

            Set<Character> set = Sets.newHashSet(key.keySet());
            set.remove(' ');

            processList(pId, bottomRows, set);
            processList(pId, middleRows, set);
            processList(pId, topRows, set);

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + pId);
            } else if (bottomRows.size() != 3 || middleRows.size() != 3 || topRows.size() != 3) {
                throw new IllegalStateException("Recipe " + pId + " has invalid count of rows");
            } else if (advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + pId);
            }
        }

        private void processList(ResourceLocation pId, List<String> rows, Set<Character> set) {
            for(String s : rows) {
                for(int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);

                    if (!key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + pId + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }
        }
    }

    private static String[][] patternFromJson(JsonObject pJson) {
        return new String[][]{
                patternFromJson(GsonHelper.getAsJsonArray(pJson, "bottom")),
                patternFromJson(GsonHelper.getAsJsonArray(pJson, "middle")),
                patternFromJson(GsonHelper.getAsJsonArray(pJson, "top"))
        };
    }

    private static Map<String, Ingredient> keyFromJson(JsonObject pKeyEntry) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : pKeyEntry.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    private static String[] patternFromJson(JsonArray pPatternArray) {
        String[] strings = new String[pPatternArray.size()];
        if (strings.length != 3) {
            throw new JsonSyntaxException("Invalid pattern: expected 3 rows");
        } else {
            for (int i = 0; i < strings.length; ++i) {
                String s = GsonHelper.convertToString(pPatternArray.get(i), "pattern[" + i + "]");

                if (s.length() != 3) {
                    throw new JsonSyntaxException("Invalid pattern: expected 3 columns");
                }

                if (i > 0 && strings[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = s;
            }

            return strings;
        }
    }

    private static NonNullList<Ingredient> dissolvePattern(String[][] pPattern, Map<String, Ingredient> pKeys) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(27, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(pKeys.keySet());
        set.remove(" ");

        for (int i = 0; i < pPattern.length; ++i) {
            for (int j = 0; j < pPattern[i].length; ++j) {
                for (int k = 0; k < pPattern[i][j].length(); ++k) {
                    String s = pPattern[i][j].substring(k, k + 1);
                    Ingredient ingredient = pKeys.get(s);

                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    }

                    set.remove(s);
                    nonnulllist.set(k + j * 3 + 9 * i, ingredient);
                }
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }
}
