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
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RemainingShapedRecipe extends ShapedRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    public RemainingShapedRecipe(ShapedRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(null), recipe.showNotification());
    }

    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
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
        return YTechRecipeSerializers.REMAINING_SHAPED.get();
    }

    public static class Serializer implements RecipeSerializer<RemainingShapedRecipe> {
        private static final ShapedRecipe.Serializer serializer = new ShapedRecipe.Serializer();
        private static final Codec<RemainingShapedRecipe> CODEC = new Codec<>() {
            @Override
            public <T> DataResult<Pair<RemainingShapedRecipe, T>> decode(DynamicOps<T> dynamicOps, T t) {
                DataResult<Pair<ShapedRecipe, T>> result = serializer.codec().decode(dynamicOps, t);
                return result.map((p) -> Pair.of(new RemainingShapedRecipe(p.getFirst()), p.getSecond()));
            }

            @Override
            public <T> DataResult<T> encode(RemainingShapedRecipe remainingShapedRecipe, DynamicOps<T> dynamicOps, T t) {
                return serializer.codec().encode(remainingShapedRecipe, dynamicOps, t);
            }
        };

        @NotNull
        @Override
        public Codec<RemainingShapedRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public RemainingShapedRecipe fromNetwork(@NotNull FriendlyByteBuf friendlyByteBuf) {
            return new RemainingShapedRecipe(serializer.fromNetwork(friendlyByteBuf));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull RemainingShapedRecipe recipe) {
            serializer.toNetwork(buf, recipe);
        }
    }

    private static class Result extends ShapedRecipeBuilder.Result {
        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, CraftingBookCategory pCategory, List<String> pPattern, Map<Character, Ingredient> pKey, AdvancementHolder advancementHolder, boolean pShowNotification) {
            super(pId, pResult, pCount, pGroup, pCategory, pPattern, pKey, advancementHolder, pShowNotification);
        }

        @NotNull
        @Override
        public RecipeSerializer<?> type() {
            return YTechRecipeSerializers.REMAINING_SHAPED.get();
        }
    }

    public static class Builder extends ShapedRecipeBuilder {
        public Builder(RecipeCategory pCategory, ItemLike pResult, int pCount) {
            super(pCategory, pResult, pCount);
        }

        public static Builder shaped(@NotNull RecipeCategory pCategory, ItemLike pResult) {
            return shaped(pCategory, pResult, 1);
        }

        public static Builder shaped(@NotNull RecipeCategory pCategory, ItemLike pResult, int pCount) {
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
                    ShapedRecipeBuilder.Result r = (ShapedRecipeBuilder.Result)t;
                    consumer.accept(new RemainingShapedRecipe.Result(r.id, r.result, r.count, r.group, determineBookCategory(category), r.pattern, r.key, r.advancement, r.showNotification));
                }
            }, id);
        }
    }
}
