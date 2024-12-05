package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RemainingShapelessRecipe extends ShapelessRecipe {
    public RemainingShapelessRecipe(String p_249640_, CraftingBookCategory p_249390_, ItemStack p_252071_, List<Ingredient> p_250689_) {
        super(p_249640_, p_249390_, p_252071_, p_250689_);
    }

    public RemainingShapelessRecipe(ShapelessRecipe recipe) {
        super(recipe.group(), recipe.category(), recipe.result, recipe.ingredients);
    }

    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.size(), ItemStack.EMPTY);

        for(int i = 0; i < list.size(); ++i) {
            ItemStack item = container.getItem(i);
            ItemStack craftingRemainder = item.getCraftingRemainder();

            if (!craftingRemainder.isEmpty()) {
                list.set(i, craftingRemainder);
            } else if (item.isDamageableItem()) {
                ItemStack result = item.copy();
                list.set(i, result);

                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

                if (server != null) {
                    result.hurtAndBreak(1, server.overworld(), null, (it) -> {});
                }
            }
        }

        return list;
    }

    @NotNull
    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return (RecipeSerializer<ShapelessRecipe>) ((Object)YTechRecipeSerializers.REMAINING_SHAPELESS.get());
    }

    public static class Serializer implements RecipeSerializer<RemainingShapelessRecipe> {
        private static final MapCodec<RemainingShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(
                (instance) -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::group),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessRecipe::category),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                        Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, 9)).fieldOf("ingredients").forGetter((recipe) -> recipe.ingredients)
                ).apply(instance, RemainingShapelessRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, RemainingShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ShapelessRecipe::group,
                CraftingBookCategory.STREAM_CODEC, ShapelessRecipe::category,
                ItemStack.STREAM_CODEC, (recipe) -> recipe.result,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), (recipe) -> recipe.ingredients,
                RemainingShapelessRecipe::new
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
    }

    public static class Builder extends ShapelessRecipeBuilder {
        public Builder(HolderGetter<Item> holderGetter, RecipeCategory pCategory, ItemStack pResult) {
            super(holderGetter, pCategory, pResult);
        }

        public static Builder shapeless(@NotNull HolderGetter<Item> holderGetter, @NotNull RecipeCategory pCategory, @NotNull ItemStack pResult) {
            return new Builder(holderGetter, pCategory, pResult);
        }

        public static Builder shapeless(@NotNull HolderGetter<Item> holderGetter, @NotNull RecipeCategory pCategory, ItemLike pResult) {
            return shapeless(holderGetter, pCategory, pResult, 1);
        }

        public static Builder shapeless(@NotNull HolderGetter<Item> holderGetter, @NotNull RecipeCategory pCategory, ItemLike pResult, int pCount) {
            return new Builder(holderGetter, pCategory, pResult.asItem().getDefaultInstance().copyWithCount(pCount));
        }

        @Override
        public void save(@NotNull RecipeOutput consumer, @NotNull ResourceKey<Recipe<?>> id) {
            super.save(new RecipeOutput() {
                @NotNull
                @Override
                public Advancement.Builder advancement() {
                    return consumer.advancement();
                }

                @Override
                public void includeRootAdvancement() {
                    consumer.includeRootAdvancement();
                }

                @Override
                public void accept(@NotNull ResourceKey<Recipe<?>> id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... iConditions) {
                    consumer.accept(id, new RemainingShapelessRecipe((ShapelessRecipe) recipe), advancementHolder);
                }
            }, id);
        }
    }
}
