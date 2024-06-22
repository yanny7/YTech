package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemainingShapelessRecipe extends ShapelessRecipe {
    public RemainingShapelessRecipe(String p_249640_, CraftingBookCategory p_249390_, ItemStack p_252071_, NonNullList<Ingredient> p_250689_) {
        super(p_249640_, p_249390_, p_252071_, p_250689_);
    }

    public RemainingShapelessRecipe(ShapelessRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getResultItem(null), recipe.getIngredients());
    }

    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.size(), ItemStack.EMPTY);

        for(int i = 0; i < list.size(); ++i) {
            ItemStack item = container.getItem(i);

            if (item.hasCraftingRemainingItem()) {
                list.set(i, item.getCraftingRemainingItem());
            } else if (item.isDamageableItem()) {
                ItemStack result = item.copy();
                list.set(i, result);

                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

                if (server != null) {
                    result.hurtAndBreak(1, server.overworld(), null, (it) -> {
                        result.shrink(1);
                        result.setDamageValue(0);
                    });
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
        private static final MapCodec<RemainingShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(
                (instance) -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((recipe) -> recipe.category),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
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
        private static final StreamCodec<RegistryFriendlyByteBuf, RemainingShapelessRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @NotNull
        @Override
        public MapCodec<RemainingShapelessRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemainingShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static RemainingShapelessRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf friendlyByteBuf) {
            return new RemainingShapelessRecipe(ShapelessRecipe.Serializer.fromNetwork(friendlyByteBuf));
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buf, @NotNull RemainingShapelessRecipe recipe) {
            ShapelessRecipe.Serializer.toNetwork(buf, recipe);
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
                public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... iConditions) {
                    consumer.accept(id, new RemainingShapelessRecipe((ShapelessRecipe) recipe), advancementHolder);
                }
            }, id);
        }
    }
}
