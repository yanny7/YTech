package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record PotteryRecipe(int count, ItemStack result) implements Recipe<Container> {
    private static final Ingredient clay = Ingredient.of(Items.CLAY_BALL);
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        ItemStack item = container.getItem(0);
        return clay.test(item) && item.getCount() == count;
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
        return YTechRecipeSerializers.POTTERY.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.POTTERY.get();
    }

    public static class Serializer implements RecipeSerializer<PotteryRecipe> {
        private static final Codec<PotteryRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                Codec.INT.fieldOf("count").forGetter((dryingRecipe) -> dryingRecipe.count),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((millingRecipe) -> millingRecipe.result)
        ).apply(recipe, PotteryRecipe::new));

        @Override
        @NotNull
        public Codec<PotteryRecipe> codec() {
            return CODEC;
        }


        @NotNull
        @Override
        public PotteryRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            ItemStack result = buffer.readItem();
            int count = buffer.readInt();
            return new PotteryRecipe(count, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull PotteryRecipe recipe) {
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.count);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final int count;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(int count, @NotNull Item result) {
            this.count = count;
            this.result = result;
        }

        public static Builder pottery(int count, @NotNull Item result) {
            return new Builder(count, result);
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
            finishedRecipeConsumer.accept(
                    recipeId,
                    new PotteryRecipe(count, new ItemStack(result)),
                    builder.build(recipeId.withPrefix("recipes/pottery/"))
            );
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
