package com.yanny.ytech.configuration.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public record WorkspaceCraftingRecipe(PatternHolder patternHolder, ItemStack result) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return patternHolder.matches(container);
    }

    @NotNull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return patternHolder.ingredients;
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull Container container, @NotNull HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return result;
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
        private static final MapCodec<WorkspaceCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        PatternHolder.CODEC.forGetter((recipe) -> recipe.patternHolder),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result)
                ).apply(instance, WorkspaceCraftingRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, WorkspaceCraftingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        @NotNull
        public MapCodec<WorkspaceCraftingRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WorkspaceCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }


        @NotNull
        private static WorkspaceCraftingRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            PatternHolder pattern = PatternHolder.STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new WorkspaceCraftingRecipe(pattern, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull WorkspaceCraftingRecipe recipe) {
            PatternHolder.STREAM_CODEC.encode(buffer, recipe.patternHolder);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Item result;
        private final List<String> bottomRows = Lists.newArrayList();
        private final List<String> middleRows = Lists.newArrayList();
        private final List<String> topRows = Lists.newArrayList();
        private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

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
        public RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
            this.criteria.put(criterionName, criterionTrigger);
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
        public void save(@NotNull RecipeOutput finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
            ensureValid(recipeId);
            Advancement.Builder builder = finishedRecipeConsumer.advancement().addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(builder::addCriterion);
            finishedRecipeConsumer.accept(
                    recipeId,
                    new WorkspaceCraftingRecipe(PatternHolder.of(key, bottomRows, middleRows, topRows), new ItemStack(result)),
                    builder.build(recipeId.withPrefix("recipes/workspace_crafting/"))
            );
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
            } else if (criteria.isEmpty()) {
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

    private record PatternHolder(NonNullList<Ingredient> ingredients, Optional<Pattern> data) {
        public static final MapCodec<PatternHolder> CODEC = Pattern.MAP_CODEC
                .flatXmap(
                        PatternHolder::unpack,
                        pattern -> pattern.data().map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe"))
                );
        public static final StreamCodec<RegistryFriendlyByteBuf, PatternHolder> STREAM_CODEC = StreamCodec.ofMember(
                PatternHolder::toNetwork, PatternHolder::fromNetwork
        );

        public boolean matches(Container container) {
            boolean matches = true;

            // Facing NORTH
            for (int i = 0; i < 27; i++) {
                if (!ingredients.get(i).test(container.getItem(i))) {
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
                            if (!ingredients.get(i).test(container.getItem(x + z * 3 + y * 9))) {
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
                            if (!ingredients.get(i).test(container.getItem(x + z * 3 + y * 9))) {
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
                            if (!ingredients.get(i).test(container.getItem(x + z * 3 + y * 9))) {
                                matches = false;
                            }

                            i++;
                        }
                    }
                }
            }

            return matches;

        }

        private void toNetwork(RegistryFriendlyByteBuf p_320098_) {
            for (Ingredient ingredient : this.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(p_320098_, ingredient);
            }
        }

        public static PatternHolder of(Map<Character, Ingredient> keys, List<String> bottom, List<String> middle, List<String> top) {
            Pattern data = new Pattern(keys, new Data(bottom, middle, top));
            return unpack(data).getOrThrow();
        }

        private static DataResult<PatternHolder> unpack(Pattern pattern) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(27, Ingredient.EMPTY);
            CharSet charSet = new CharArraySet(pattern.keys.keySet());
            int i = 0;

            for (int k = 0; k < pattern.data.bottom.size(); k++) {
                String s = pattern.data.bottom.get(k);

                for (int l = 0; l < s.length(); l++) {
                    char c0 = s.charAt(l);
                    Ingredient ingredient = c0 == ' ' ? Ingredient.EMPTY : pattern.keys.get(c0);

                    if (ingredient == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + c0 + "' but it's not defined in the key");
                    }

                    charSet.remove(c0);
                    ingredients.set(i, ingredient);
                }

                i++;
            }

            for (int k = 0; k < pattern.data.middle.size(); k++) {
                String s = pattern.data.middle.get(k);

                for (int l = 0; l < s.length(); l++) {
                    char c0 = s.charAt(l);
                    Ingredient ingredient = c0 == ' ' ? Ingredient.EMPTY : pattern.keys.get(c0);

                    if (ingredient == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + c0 + "' but it's not defined in the key");
                    }

                    charSet.remove(c0);
                    ingredients.set(i, ingredient);
                }

                i++;
            }

            for (int k = 0; k < pattern.data.top.size(); k++) {
                String s = pattern.data.top.get(k);

                for (int l = 0; l < s.length(); l++) {
                    char c0 = s.charAt(l);
                    Ingredient ingredient = c0 == ' ' ? Ingredient.EMPTY : pattern.keys.get(c0);

                    if (ingredient == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + c0 + "' but it's not defined in the key");
                    }

                    charSet.remove(c0);
                    ingredients.set(i, ingredient);
                }

                i++;
            }

            return !charSet.isEmpty()
                    ? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charSet)
                    : DataResult.success(new PatternHolder(ingredients, Optional.of(pattern)));
        }

        private static PatternHolder fromNetwork(RegistryFriendlyByteBuf p_319788_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(27, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319733_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(p_319788_));
            return new PatternHolder(nonnulllist, Optional.empty());
        }
    }

    private record Data(List<String> bottom, List<String> middle, List<String> top) {
        private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(pattern -> {
            if (pattern.size() != 3) {
                return DataResult.error(() -> "Invalid pattern: %s is expected".formatted(pattern.size()));
            } else {
                int i = pattern.getFirst().length();

                for (String s : pattern) {
                    if (s.length() > 3) {
                        return DataResult.error(() -> "Invalid pattern: too many columns, %s is expected".formatted(s.length()));
                    }

                    if (i != s.length()) {
                        return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                    }
                }

                return DataResult.success(pattern);
            }
        }, Function.identity());
        public static final MapCodec<Data> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                PATTERN_CODEC.fieldOf("bottom").forGetter(data -> data.bottom),
                                PATTERN_CODEC.fieldOf("middle").forGetter(data -> data.middle),
                                PATTERN_CODEC.fieldOf("top").forGetter(data -> data.top)
                        )
                        .apply(instance, Data::new)
        );
    }

    private record Pattern(Map<Character, Ingredient> keys, Data data) {
        private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(key -> {
            if (key.length() != 1) {
                return DataResult.error(() -> "Invalid key entry: '" + key + "' is an invalid symbol (must be 1 character only).");
            } else {
                return " ".equals(key) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(key.charAt(0));
            }
        }, String::valueOf);
        public static final MapCodec<Pattern> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(pattern -> pattern.keys),
                                Data.MAP_CODEC.fieldOf("pattern").forGetter(pattern -> pattern.data)
                        )
                        .apply(instance, Pattern::new)
        );
    }
}
