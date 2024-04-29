package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemainingShapedRecipe extends ShapedRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    public RemainingShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
    }

    public RemainingShapedRecipe(ShapedRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.pattern, recipe.getResultItem(null), recipe.showNotification());
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

                result.hurtAndBreak(1, RANDOM, null, () -> {
                    result.shrink(1);
                    result.setDamageValue(0);
                });
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
        private static final MapCodec<RemainingShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(
                (instance) -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                        ShapedRecipePattern.MAP_CODEC.forGetter((recipe) -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((p_311730_) -> p_311730_.getResultItem(null)),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
                ).apply(instance, RemainingShapedRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, RemainingShapedRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @NotNull
        @Override
        public MapCodec<RemainingShapedRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemainingShapedRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static RemainingShapedRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf friendlyByteBuf) {
            return new RemainingShapedRecipe(ShapedRecipe.Serializer.fromNetwork(friendlyByteBuf));
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buf, @NotNull RemainingShapedRecipe recipe) {
            ShapedRecipe.Serializer.toNetwork(buf, recipe);
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
            super.save(new RecipeOutput() {
                @NotNull
                @Override
                public Advancement.Builder advancement() {
                    return consumer.advancement();
                }

                @Override
                public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... iConditions) {
                    consumer.accept(id, new RemainingShapedRecipe((ShapedRecipe) recipe), advancementHolder);
                }
            }, id);
        }
    }
}
