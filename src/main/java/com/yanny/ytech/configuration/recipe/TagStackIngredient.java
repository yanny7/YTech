package com.yanny.ytech.configuration.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class TagStackIngredient extends Ingredient {
    public static final TagStackIngredient EMPTY = new TagStackIngredient(Stream.empty());

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
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    @Override
    public JsonElement toJson() {
        JsonElement element = super.toJson();
        element.getAsJsonObject().addProperty("type", Objects.requireNonNull(CraftingHelper.getID(SERIALIZER)).toString());
        return element;
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
        return fromValues(stacks.filter((itemStack) -> !itemStack.isEmpty()).map(Ingredient.ItemValue::new));
    }

    @NotNull
    public static TagStackIngredient of(@NotNull TagKey<Item> tag) {
        return fromValues(Stream.of(new Ingredient.TagValue(tag)));
    }

    public static TagStackIngredient fromIngredient(@NotNull Ingredient ingredient) {
        return fromValues(Arrays.stream(ingredient.values));
    }

    @NotNull
    public static TagStackIngredient fromValues(Stream<? extends Ingredient.Value> stream) {
        TagStackIngredient ingredient = new TagStackIngredient(stream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    public static final IIngredientSerializer<Ingredient> SERIALIZER = new IIngredientSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, Ingredient ingredient) {
            ItemStack[] items = ingredient.getItems();
            buffer.writeVarInt(items.length);

            for (ItemStack stack : items) {
                buffer.writeItem(stack);
            }
        }

        @NotNull
        @Override
        public Ingredient parse(@NotNull FriendlyByteBuf buffer) {
            return Ingredient.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
        }

        @NotNull
        @Override
        public Ingredient parse(@NotNull JsonObject json) {
            ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
            int count = GsonHelper.getAsInt(json, "count", 1);
            TagCountValue value = new TagCountValue(TagKey.create(Registries.ITEM, resourceLocation), count);
            return Ingredient.fromValues(Stream.of(value));
        }

    };

    public static class TagCountValue extends Ingredient.TagValue {
        private final int count;

        public TagCountValue(@NotNull TagKey<Item> tag, int count) {
            super(tag);
            this.count = count;
        }

        @NotNull
        @Override
        public Collection<ItemStack> getItems() {
            var items = super.getItems();

            items.forEach(stack -> stack.setCount(count));
            return items;
        }

        @NotNull
        @Override
        public JsonObject serialize() {
            JsonObject object = super.serialize();

            object.addProperty("count", count);
            return object;
        }
    }


}