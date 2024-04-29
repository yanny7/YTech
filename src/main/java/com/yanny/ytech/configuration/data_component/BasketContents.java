package com.yanny.ytech.configuration.data_component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.yanny.ytech.registration.YTechDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BasketContents implements TooltipComponent {
    public static final BasketContents EMPTY = new BasketContents(List.of());
    public static final Codec<BasketContents> CODEC = ItemStack.CODEC.listOf().xmap(BasketContents::new, contents -> contents.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, BasketContents> STREAM_CODEC = ItemStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(BasketContents::new, contents -> contents.items);
    public static final int MAX_WEIGHT = 16;
    private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
    private static final int NO_STACK_INDEX = -1;
    final List<ItemStack> items;
    final Fraction weight;

    BasketContents(List<ItemStack> itemStacks, Fraction fraction) {
        this.items = itemStacks;
        this.weight = fraction;
    }

    public BasketContents(List<ItemStack> itemStacks) {
        this(itemStacks, computeContentWeight(itemStacks));
    }

    private static Fraction computeContentWeight(List<ItemStack> itemStacks) {
        Fraction fraction = Fraction.ZERO;

        for (ItemStack itemstack : itemStacks) {
            fraction = fraction.add(getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
        }

        return fraction;
    }

    static Fraction getWeight(ItemStack itemStack) {
        BasketContents BasketContents = itemStack.get(YTechDataComponentTypes.BASKET_CONTENTS);
        if (BasketContents != null) {
            return BUNDLE_IN_BUNDLE_WEIGHT.add(BasketContents.weight());
        } else {
            List<BeehiveBlockEntity.Occupant> list = itemStack.getOrDefault(DataComponents.BEES, List.of());
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, Math.min(itemStack.getMaxStackSize(), 16));
        }
    }

    public ItemStack getItemUnsafe(int p_330802_) {
        return this.items.get(p_330802_);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> items() {
        return this.items;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public int size() {
        return this.items.size();
    }

    public Fraction weight() {
        return this.weight;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            return object instanceof BasketContents BasketContents && this.weight.equals(BasketContents.weight) && ItemStack.listMatches(this.items, BasketContents.items);
        }
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    @Override
    public String toString() {
        return "BasketContents" + this.items;
    }

    public static class Mutable {
        private final List<ItemStack> items;
        private Fraction weight;

        public Mutable(BasketContents p_332039_) {
            this.items = new ArrayList<>(p_332039_.items);
            this.weight = p_332039_.weight;
        }

        public BasketContents.Mutable clearItems() {
            this.items.clear();
            this.weight = Fraction.ZERO;
            return this;
        }

        private int findStackIndex(ItemStack itemStack) {
            if (!itemStack.isStackable()) {
                return NO_STACK_INDEX;
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    if (ItemStack.isSameItemSameComponents(this.items.get(i), itemStack)) {
                        return i;
                    }
                }

                return NO_STACK_INDEX;
            }
        }

        private int getMaxAmountToAdd(ItemStack itemStack) {
            Fraction fraction = Fraction.ONE.subtract(this.weight);
            return Math.clamp(fraction.divideBy(BasketContents.getWeight(itemStack)).intValue(), 0, 16);
        }

        public int tryInsert(ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem().canFitInsideContainerItems()) {
                int i = Math.min(itemStack.getCount(), this.getMaxAmountToAdd(itemStack));

                if (i == 0) {
                    return 0;
                } else {
                    this.weight = this.weight.add(BasketContents.getWeight(itemStack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.findStackIndex(itemStack);

                    if (j != NO_STACK_INDEX) {
                        ItemStack toRemove = this.items.remove(j);
                        ItemStack removed = toRemove.copyWithCount(toRemove.getCount() + i);
                        itemStack.shrink(i);
                        this.items.addFirst(removed);
                    } else {
                        this.items.addFirst(itemStack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
        }

        public int tryTransfer(Slot p_330834_, Player p_331924_) {
            ItemStack itemstack = p_330834_.getItem();
            int i = this.getMaxAmountToAdd(itemstack);
            return this.tryInsert(p_330834_.safeTake(itemstack.getCount(), i, p_331924_));
        }

        @Nullable
        public ItemStack removeOne() {
            if (this.items.isEmpty()) {
                return null;
            } else {
                ItemStack itemstack = this.items.removeFirst().copy();
                this.weight = this.weight.subtract(BasketContents.getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
                return itemstack;
            }
        }

        public Fraction weight() {
            return this.weight;
        }

        public BasketContents toImmutable() {
            return new BasketContents(List.copyOf(this.items), this.weight);
        }
    }

    public record BasketTooltip(BasketContents contents) implements TooltipComponent {
    }
}
