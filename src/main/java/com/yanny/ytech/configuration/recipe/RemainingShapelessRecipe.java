package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemainingShapelessRecipe extends ShapelessRecipe {
    public static final Serializer SERIALIZER = new Serializer();
    private static final RandomSource RANDOM = RandomSource.create();

    public RemainingShapelessRecipe(String p_249640_, CraftingBookCategory p_249390_, ItemStack p_252071_, NonNullList<Ingredient> p_250689_) {
        super(p_249640_, p_249390_, p_252071_, p_250689_);
    }

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
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<RemainingShapelessRecipe> {
        private final ShapelessRecipe.Serializer serializer = new ShapelessRecipe.Serializer();
        private static final Codec<RemainingShapelessRecipe> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(ShapelessRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((recipe) -> recipe.category),
                        ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                        Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(
                                (ingredients) -> {
                                    Ingredient[] aingredient = ingredients.toArray(Ingredient[]::new);

                                    if (aingredient.length == 0) {
                                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                                    } else {
                                        return aingredient.length > 3*3 ? DataResult.error(() -> {
                                            return "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(3*3);
                                        }) : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                    }
                                },
                                DataResult::success).forGetter(ShapelessRecipe::getIngredients)
                ).apply(instance, RemainingShapelessRecipe::new)
        );

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
            super.save(new RecipeOutput() {
                @NotNull
                @Override
                public Advancement.Builder advancement() {
                    return consumer.advancement();
                }

                @Override
                public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                    consumer.accept(id, new RemainingShapelessRecipe((ShapelessRecipe) recipe), advancementHolder);
                }
            }, id);
        }
    }
}
