package com.yanny.ytech.configuration.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class TagStackIngredient extends Ingredient {
    public static final TagStackIngredient EMPTY = new TagStackIngredient(Stream.empty());
    public static final Codec<Value> CODEC = ExtraCodecs
            .xor(TagCountValue.CODEC, Ingredient.Value.CODEC)
            .xmap(
                    (either) -> either.map((tagCountValue) -> tagCountValue, (value) -> value),
                    (value) -> {
                        if (value instanceof TagCountValue ingredient$tagcountvalue) {
                            return Either.left(ingredient$tagcountvalue);
                        } else {
                            return Either.right(value);
                        }
                    }
            );
    public static final Codec<TagStackIngredient> CODEC_NONEMPTY = ExtraCodecs.either(Codec.list(TagStackIngredient.CODEC).comapFlatMap((list) -> {
                return list.isEmpty() ? DataResult.error(() -> {
                    return "Item array cannot be empty, at least one item must be defined";
                }) : DataResult.success(list.toArray(new Value[0]));
            }, List::of), TagStackIngredient.CODEC).flatComapMap((either) -> {
                return either.map((values) -> new TagStackIngredient(Stream.of(values)), (value) -> {
                    return new TagStackIngredient(Stream.of(value));
                });
            }, (tagStackIngredient) -> {
                if (tagStackIngredient.values.length == 1) {
                    return DataResult.success(Either.right(tagStackIngredient.values[0]));
                } else {
                    return tagStackIngredient.values.length == 0 ? DataResult.error(() -> {
                        return "Item array cannot be empty, at least one item must be defined";
                    }) : DataResult.success(Either.left(tagStackIngredient.values));
                }
            });

    protected TagStackIngredient(@NotNull Stream<? extends Value> pValues) {
        super(pValues);
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return test(stack, false);
    }

    public boolean test(@Nullable ItemStack stack, boolean ignoreCount) {
        if (stack == null) {
            return false;
        } else if (this.isEmpty()) {
            return stack.isEmpty();
        } else {
            for(ItemStack itemstack : this.getItems()) {
                if (itemstack.is(stack.getItem()) && (ignoreCount || stack.getCount() >= itemstack.getCount())) {
                    return true;
                }
            }

            return false;
        }
    }

    @NotNull
    @Override
    public JsonElement toJson(boolean canBeEmpty) {
        return Util.getOrThrow(CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE, this), IllegalStateException::new);
    }

    @NotNull
    public static TagStackIngredient of() {
        return EMPTY;
    }

    @NotNull
    public static TagStackIngredient of(ItemLike... items) {
        return of(Arrays.stream(items).map(ItemStack::new));
    }

    @NotNull
    public static TagStackIngredient of(ItemStack... stacks) {
        return of(Arrays.stream(stacks));
    }

    @NotNull
    public static TagStackIngredient of(Stream<ItemStack> stacks) {
        return fromValues(stacks.filter((itemStack) -> !itemStack.isEmpty()).map(ItemValue::new));
    }

    @NotNull
    public static TagStackIngredient of(@NotNull TagKey<Item> tag) {
        return fromValues(Stream.of(new TagValue(tag)));
    }

    public static TagStackIngredient fromIngredient(@NotNull Ingredient ingredient) {
        return fromValues(Arrays.stream(ingredient.values));
    }

    @NotNull
    public static TagStackIngredient fromValues(Stream<? extends Value> stream) {
        TagStackIngredient ingredient = new TagStackIngredient(stream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    public record TagCountValue(TagKey<Item> tag, int count) implements Value {
        static final Codec<TagCountValue> CODEC = RecordCodecBuilder.create((valueInstance) -> valueInstance.group(
                TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter((tagCountValue) -> tagCountValue.tag),
                Codec.INT.fieldOf("count").forGetter((tagCountValue) -> tagCountValue.count)
        ).apply(valueInstance, TagCountValue::new));

        @Override
        public boolean equals(Object p_301162_) {
            boolean result;
            if (p_301162_ instanceof TagCountValue tagValue) {
                result = tagValue.tag.location().equals(this.tag.location()) && tagValue.count == this.count;
            } else {
                result = false;
            }

            return result;
        }

        @Override
        @NotNull
        public Collection<ItemStack> getItems() {
            List<ItemStack> list = Lists.newArrayList();

            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
                list.add(new ItemStack(holder, this.count));
            }

            if (list.isEmpty()) {
                list.add((new ItemStack(Blocks.BARRIER)).setHoverName(Component.literal("Empty Tag: " + this.tag.location())));
            }

            return list;
        }
    }
}