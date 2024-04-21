package com.yanny.ytech.configuration.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemainingShapelessRecipe extends ShapelessRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    public RemainingShapelessRecipe(ShapelessRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getResultItem(null), recipe.getIngredients());
    }

    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < list.size(); ++i) {
            ItemStack item = container.getItem(i);

            if (item.hasCraftingRemainingItem()) {
                list.set(i, item.getCraftingRemainingItem());
            } else if (item.isDamageableItem()) {
                ItemStack result = item.copy();
                list.set(i, result);

                if (result.hurt(1, RANDOM, null)) {
                    result.shrink(1);
                    result.setDamageValue(0);
                }
            }
        }

        return list;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return YTechRecipeSerializers.REMAINING_SHAPELESS.get();
    }

    public static class Serializer implements RecipeSerializer<RemainingShapelessRecipe> {
        private static final ShapelessRecipe.Serializer serializer = new ShapelessRecipe.Serializer();
        private static final Codec<RemainingShapelessRecipe> CODEC = new Codec<>() {
            @Override
            public <T> DataResult<Pair<RemainingShapelessRecipe, T>> decode(DynamicOps<T> dynamicOps, T t) {
                DataResult<Pair<ShapelessRecipe, T>> result = serializer.codec().decode(dynamicOps, t);
                return result.map((p) -> Pair.of(new RemainingShapelessRecipe(p.getFirst()), p.getSecond()));
            }

            @Override
            public <T> DataResult<T> encode(RemainingShapelessRecipe remainingShapedRecipe, DynamicOps<T> dynamicOps, T t) {
                return serializer.codec().encode(remainingShapedRecipe, dynamicOps, t);
            }
        };

        @NotNull
        @Override
        public Codec<RemainingShapelessRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public RemainingShapelessRecipe fromNetwork(@NotNull FriendlyByteBuf friendlyByteBuf) {
            return new RemainingShapelessRecipe(serializer.fromNetwork(friendlyByteBuf));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull RemainingShapelessRecipe recipe) {
            serializer.toNetwork(buf, recipe);
        }
    }

    private static class Result extends ShapelessRecipeBuilder.Result {
        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, CraftingBookCategory pCategory, List<Ingredient> pIngredients, AdvancementHolder advancementHolder) {
            super(pId, pResult, pCount, pGroup, pCategory, pIngredients, advancementHolder);
        }

        @NotNull
        @Override
        public RecipeSerializer<?> type() {
            return YTechRecipeSerializers.REMAINING_SHAPELESS.get();
        }
    }

    public static class Builder extends ShapelessRecipeBuilder {
        public Builder(RecipeCategory pCategory, ItemLike pResult, int pCount) {
            super(pCategory, pResult, pCount);
        }

        public static Builder shapeless(@NotNull RecipeCategory pCategory, ItemLike pResult) {
            return new Builder(pCategory, pResult, 1);
        }

        public static Builder shapeless(@NotNull RecipeCategory pCategory, ItemLike pResult, int pCount) {
            return new Builder(pCategory, pResult, pCount);
        }

        @Override
        public void save(@NotNull RecipeOutput consumer, @NotNull ResourceLocation id) {
            RecipeCategory category = this.category;
            super.save(new RecipeOutput() {
                @NotNull
                @Override
                public Advancement.Builder advancement() {
                    return consumer.advancement();
                }

                @Override
                public void accept(FinishedRecipe t, ICondition... iConditions) {
                    ShapelessRecipeBuilder.Result r = (ShapelessRecipeBuilder.Result)t;
                    consumer.accept(new RemainingShapelessRecipe.Result(r.id, r.result, r.count, r.group, determineBookCategory(category), r.ingredients, r.advancement));
                }
            }, id);
        }
    }
}
