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
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

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
                ExtraCodecs.either(BuiltInRegistries.ITEM.byNameCodec(), CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC)
                        .xmap((either) -> either.map(ItemStack::new, Function.identity()), (stack) -> {
                            if (stack.getCount() != 1) {
                                return Either.right(stack);
                            } else {
                                return ItemStack.matches(stack, new ItemStack(stack.getItem())) ? Either.left(stack.getItem()) : Either.right(stack);
                            }
                        }).fieldOf("result").forGetter((dryingRecipe) -> dryingRecipe.result)
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

    public record Result(@NotNull ResourceLocation id, int count, @NotNull Item result,
                         @NotNull AdvancementHolder advancementHolder) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            JsonObject resultItemStack = new JsonObject();
            resultItemStack.addProperty("item", Utils.loc(result).toString());
            json.add("result", resultItemStack);

            json.addProperty("count", count);
        }

        @NotNull
        @Override
        public AdvancementHolder advancement() {
            return advancementHolder;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> type() {
            return YTechRecipeSerializers.POTTERY.get();
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
            finishedRecipeConsumer.accept(new PotteryRecipe.Result(recipeId, count, result, builder.build(recipeId.withPrefix("recipes/pottery/"))));
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
