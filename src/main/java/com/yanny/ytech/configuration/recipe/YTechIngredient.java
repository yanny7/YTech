package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechIngredientTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

public class YTechIngredient implements ICustomIngredient {
    public static final YTechIngredient EMPTY = new YTechIngredient(Ingredient.EMPTY, 0);
    public static final MapCodec<YTechIngredient> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter((ingredient) -> ingredient.ingredient),
                Codec.INT.fieldOf("count").forGetter((ingredient) -> ingredient.count)
        ).apply(instance, YTechIngredient::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, YTechIngredient> STREAM_CODEC = StreamCodec.of(
            (buffer, ingredient) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient.ingredient);
                buffer.writeInt(ingredient.count);
            },
            (buffer) -> new YTechIngredient(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readInt())
    );

    private final Ingredient ingredient;
    private final int count;

    public static YTechIngredient of(Ingredient ingredient, int count) {
        return new YTechIngredient(ingredient, count);
    }

    public static YTechIngredient of(TagKey<Item> tag, int count) {
        return new YTechIngredient(Ingredient.of(tag), count);
    }

    public static YTechIngredient of(ItemLike item, int count) {
        return new YTechIngredient(Ingredient.of(item), count);
    }

    public YTechIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public boolean test(@NotNull ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    public int getCount() {
        return count;
    }

    @NotNull
    @Override
    public Stream<ItemStack> getItems() {
        return Arrays.stream(ingredient.getItems()).peek((itemStack -> itemStack.setCount(count)));
    }

    @Override
    public boolean isSimple() {
        return ingredient.isSimple();
    }

    @NotNull
    @Override
    public IngredientType<?> getType() {
        return YTechIngredientTypes.INGREDIENT_COUNT.get();
    }
}
