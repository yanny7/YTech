package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechRecipeBookCategories;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record BlockHitRecipe(Ingredient ingredient, Ingredient block, ItemStack result) implements Recipe<RecipeInput> {
    @Override
    public boolean matches(@NotNull RecipeInput recipeInput, @NotNull Level level) {
        return ingredient.test(recipeInput.getItem(0)) && block.test(recipeInput.getItem(1));
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull RecipeInput recipeInput, @NotNull HolderLookup.Provider provider) {
        return result.copy();
    }

    @NotNull
    @Override
    public RecipeSerializer<BlockHitRecipe> getSerializer() {
        return YTechRecipeSerializers.BLOCK_HIT.get();
    }

    @NotNull
    @Override
    public RecipeType<BlockHitRecipe> getType() {
        return YTechRecipeTypes.BLOCK_HIT.get();
    }

    @NotNull
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @NotNull
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return YTechRecipeBookCategories.BLOCK_HIT.get();
    }

    public static class Serializer implements RecipeSerializer<BlockHitRecipe> {
        private static final MapCodec<BlockHitRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter((blockHitRecipe) -> blockHitRecipe.ingredient),
                        Ingredient.CODEC.fieldOf("block").forGetter((blockHitRecipe) -> blockHitRecipe.block),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((blockHitRecipe) -> blockHitRecipe.result)
                ).apply(instance, BlockHitRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, BlockHitRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        @NotNull
        public MapCodec<BlockHitRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockHitRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static BlockHitRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient block = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new BlockHitRecipe(ingredient, block, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull BlockHitRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.block);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final Ingredient block;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, @NotNull Ingredient block, @NotNull Item result) {
            this.ingredient = ingredient;
            this.block = block;
            this.result = result;
        }

        public static Builder blockUse(@NotNull HolderGetter<Item> items, @NotNull ItemLike input, TagKey<Item> block, @NotNull Item result) {
            return new Builder(Ingredient.of(input), Ingredient.of(items.getOrThrow(block)), result);
        }

        @NotNull
        @Override
        public Builder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
            this.criteria.put(criterionName, criterionTrigger);
            return this;
        }

        @NotNull
        @Override
        public Builder group(@Nullable String groupName) {
            return this;
        }

        @NotNull
        @Override
        public Item getResult() {
            return result;
        }

        @Override
        public void save(@NotNull RecipeOutput finishedRecipeConsumer, @NotNull ResourceKey<Recipe<?>> recipeId) {
            ensureValid(recipeId);
            Advancement.Builder builder = finishedRecipeConsumer.advancement().addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(builder::addCriterion);
            finishedRecipeConsumer.accept(
                    recipeId,
                    new BlockHitRecipe(ingredient, block, new ItemStack(result)),
                    builder.build(Utils.modLoc("recipes/block_hit/" + recipeId.location().getPath()))
            );
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceKey<Recipe<?>> id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
