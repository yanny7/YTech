package com.yanny.ytech.configuration.recipe;

import com.google.gson.JsonObject;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RemainingShapedRecipe extends ShapedRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    public RemainingShapedRecipe(ShapedRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(null), recipe.showNotification());
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
            } else if (item.is(YTechItemTags.PARTS.tag)) {
                list.set(i, item.copy());
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

        @NotNull
        @Override
        public RemainingShapedRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            return new RemainingShapedRecipe(serializer.fromJson(resourceLocation, jsonObject));
        }

        @NotNull
        @Override
        public RemainingShapedRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
            return new RemainingShapedRecipe(serializer.fromNetwork(id, buffer));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull RemainingShapedRecipe recipe) {
            serializer.toNetwork(buf, recipe);
        }
    }

    private static class Result extends ShapedRecipeBuilder.Result {
        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, CraftingBookCategory pCategory, List<String> pPattern, Map<Character, Ingredient> pKey, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId, boolean pShowNotification) {
            super(pId, pResult, pCount, pGroup, pCategory, pPattern, pKey, pAdvancement, pAdvancementId, pShowNotification);
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
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
        public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
            super.save((t) -> {
                ShapedRecipeBuilder.Result r = (ShapedRecipeBuilder.Result)t;
                consumer.accept(new RemainingShapedRecipe.Result(id, r.result, r.count, r.group, determineBookCategory(this.category), r.pattern, r.key, r.advancement, r.advancementId, r.showNotification));
            }, id);
        }
    }
}
