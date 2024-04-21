package com.yanny.ytech.configuration.recipe;

import com.google.gson.JsonObject;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class RemainingShapelessRecipe extends ShapelessRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    public RemainingShapelessRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getResultItem(null), recipe.getIngredients());
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
        private final ShapelessRecipe.Serializer serializer = new ShapelessRecipe.Serializer();

        @NotNull
        @Override
        public RemainingShapelessRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            return new RemainingShapelessRecipe(serializer.fromJson(id, json));
        }

        @NotNull
        @Override
        public RemainingShapelessRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
            return new RemainingShapelessRecipe(serializer.fromNetwork(id, buffer));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull RemainingShapelessRecipe recipe) {
            serializer.toNetwork(buf, recipe);
        }
    }

    private static class Result extends ShapelessRecipeBuilder.Result {
        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, CraftingBookCategory pCategory, List<Ingredient> pIngredients, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            super(pId, pResult, pCount, pGroup, pCategory, pIngredients, pAdvancement, pAdvancementId);
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
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
        public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
            super.save((t) -> {
                ShapelessRecipeBuilder.Result r = (ShapelessRecipeBuilder.Result)t;
                consumer.accept(new RemainingShapelessRecipe.Result(id, r.result, r.count, r.group, determineBookCategory(this.category), r.ingredients, r.advancement, r.advancementId));
            }, id);
        }
    }
}
