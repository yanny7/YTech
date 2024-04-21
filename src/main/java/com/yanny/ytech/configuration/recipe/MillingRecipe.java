package com.yanny.ytech.configuration.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public record MillingRecipe(Ingredient ingredient, ItemStack result, float bonusChance) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return ingredient.test(container.getItem(0));
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return YTechRecipeSerializers.MILLING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.MILLING.get();
    }

    public static class Serializer implements RecipeSerializer<MillingRecipe> {
        private static final Codec<MillingRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((millingRecipe) -> millingRecipe.ingredient),
                ExtraCodecs.either(BuiltInRegistries.ITEM.byNameCodec(), CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC)
                        .xmap((either) -> either.map(ItemStack::new, Function.identity()), (stack) -> {
                            if (stack.getCount() != 1) {
                                return Either.right(stack);
                            } else {
                                return ItemStack.matches(stack, new ItemStack(stack.getItem())) ? Either.left(stack.getItem()) : Either.right(stack);
                            }
                        }).fieldOf("result").forGetter((millingRecipe) -> millingRecipe.result),
                Codec.FLOAT.fieldOf("bonusChance").forGetter((smeltingRecipe) -> smeltingRecipe.bonusChance)
        ).apply(recipe, MillingRecipe::new));

        @Override
        @NotNull
        public Codec<MillingRecipe> codec() {
            return CODEC;
        }

        @Override
        @NotNull
        public MillingRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float bonusChance = buffer.readFloat();
            return new MillingRecipe(ingredient, result, bonusChance);
        }
        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull MillingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.bonusChance);
        }
    }

    public record Result(@NotNull ResourceLocation id, @NotNull Ingredient ingredient, @NotNull Item result, int count, float bonusChance,
                         @NotNull AdvancementHolder advancementHolder) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("ingredient", ingredient.toJson(false));

            JsonObject resultItemStack = new JsonObject();
            resultItemStack.addProperty("item", Utils.loc(result).toString());
            resultItemStack.addProperty("count", count);
            json.add("result", resultItemStack);
            json.addProperty("bonusChance", bonusChance);
        }

        @NotNull
        @Override
        public AdvancementHolder advancement() {
            return advancementHolder;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> type() {
            return YTechRecipeSerializers.MILLING.get();
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        private float bonusChance = 0.0f;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, @NotNull Item result, int count) {
            this.ingredient = ingredient;
            this.result = result;
            this.count = count;
        }

        public static Builder milling(@NotNull TagKey<Item> input, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(input), result, count);
        }

        public static Builder milling(@NotNull ItemLike input, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(input), result, count);
        }

        public RecipeBuilder bonusChance(float bonusChance) {
            this.bonusChance = bonusChance;
            return this;
        }

        @NotNull
        @Override
        public RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
            this.criteria.put(criterionName, criterionTrigger);
            return this;
        }

        @NotNull
        @Override
        public RecipeBuilder group(@Nullable String groupName) {
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
            finishedRecipeConsumer.accept(new MillingRecipe.Result(recipeId, ingredient, result, count, bonusChance, builder.build(recipeId.withPrefix("recipes/milling/"))));
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
