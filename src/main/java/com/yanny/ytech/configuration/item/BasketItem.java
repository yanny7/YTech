package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BasketItem extends Item {
    public static final ResourceLocation FILLED_PREDICATE = Utils.modLoc("filled");
    private static final String TAG_ITEMS = "items";
    public static final int MAX_WEIGHT = 16;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public BasketItem() {
        super(new Properties().stacksTo(1));
    }

    public static float getFullnessDisplay(@NotNull ItemStack stack) {
        return (float)getContentWeight(stack) / (float)MAX_WEIGHT;
    }

    public boolean overrideStackedOnOther(@NotNull ItemStack stack, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = slot.getItem();
            if (itemstack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeOne(stack).ifPresent((itemStack) -> add(stack, slot.safeInsert(itemStack)));
            } else if (itemstack.getItem().canFitInsideContainerItems()) {
                int i = (MAX_WEIGHT - getContentWeight(stack)) / getWeight(itemstack);
                int j = add(stack, slot.safeTake(itemstack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    public boolean overrideOtherStackedOnMe(@NotNull ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot, 
                                            @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (stack.getCount() != 1) return false;
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (other.isEmpty()) {
                removeOne(stack).ifPresent((itemStack) -> {
                    this.playRemoveOneSound(player);
                    access.set(itemStack);
                });
            } else {
                int i = add(stack, other);
                if (i > 0) {
                    this.playInsertSound(player);
                    other.shrink(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }
    
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (dropContents(itemstack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getContentWeight(stack) > 0;
    }

    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.min(1 + 12 * getContentWeight(stack) / MAX_WEIGHT, 13);
    }

    public int getBarColor(@NotNull ItemStack stack) {
        return BAR_COLOR;
    }

    private static int add(@NotNull ItemStack bundleStack, @NotNull ItemStack insertedStack) {
        if (!insertedStack.isEmpty() && insertedStack.getItem().canFitInsideContainerItems()) {
            CompoundTag compoundtag = bundleStack.getOrCreateTag();

            if (!compoundtag.contains(TAG_ITEMS)) {
                compoundtag.put(TAG_ITEMS, new ListTag());
            }

            int i = getContentWeight(bundleStack);
            int j = getWeight(insertedStack);
            int k = Math.min(insertedStack.getCount(), (MAX_WEIGHT - i) / j);

            if (k == 0) {
                return 0;
            } else {
                ListTag list = compoundtag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);
                Optional<CompoundTag> optional = getMatchingItem(insertedStack, list);

                if (optional.isPresent()) {
                    CompoundTag matching = optional.get();
                    ItemStack itemStack = ItemStack.of(matching);

                    itemStack.grow(k);
                    itemStack.save(matching);
                    list.remove(matching);
                    list.add(0, matching);
                } else {
                    ItemStack itemStack = insertedStack.copyWithCount(k);
                    CompoundTag tag = new CompoundTag();
                    itemStack.save(tag);
                    list.add(0, tag);
                }

                return k;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> getMatchingItem(@NotNull ItemStack stack, @NotNull ListTag list) {
        return stack.is(Items.BUNDLE) ? Optional.empty() : list.stream().filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast).filter((tag) -> ItemStack.isSameItemSameTags(ItemStack.of(tag), stack)).findFirst();
    }

    private static int getWeight(@NotNull ItemStack stack) {
        if (stack.is(Items.BUNDLE)) {
            return BUNDLE_IN_BUNDLE_WEIGHT + getContentWeight(stack);
        } else {
            if ((stack.is(Items.BEEHIVE) || stack.is(Items.BEE_NEST)) && stack.hasTag()) {
                CompoundTag compoundtag = BlockItem.getBlockEntityData(stack);

                if (compoundtag != null && !compoundtag.getList("Bees", Tag.TAG_COMPOUND).isEmpty()) {
                    return MAX_WEIGHT;
                }
            }

            return MAX_WEIGHT / Math.min(stack.getMaxStackSize(), MAX_WEIGHT);
        }
    }

    private static int getContentWeight(ItemStack stack) {
        return getContents(stack).mapToInt((itemStack) -> getWeight(itemStack) * itemStack.getCount()).sum();
    }

    private static Optional<ItemStack> removeOne(@NotNull ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();

        if (!compoundtag.contains(TAG_ITEMS)) {
            return Optional.empty();
        } else {
            ListTag list = compoundtag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);

            if (list.isEmpty()) {
                return Optional.empty();
            } else {
                CompoundTag itemTag = list.getCompound(0);
                ItemStack itemStack = ItemStack.of(itemTag);

                list.remove(0);

                if (list.isEmpty()) {
                    stack.removeTagKey(TAG_ITEMS);
                }

                return Optional.of(itemStack);
            }
        }
    }

    private static boolean dropContents(@NotNull ItemStack stack, @NotNull Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        
        if (!tag.contains(TAG_ITEMS)) {
            return false;
        } else {
            if (player instanceof ServerPlayer) {
                ListTag list = tag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);

                for(int i = 0; i < list.size(); ++i) {
                    CompoundTag compoundTag = list.getCompound(i);
                    ItemStack itemstack = ItemStack.of(compoundTag);
                    
                    player.drop(itemstack, true);
                }
            }

            stack.removeTagKey(TAG_ITEMS);
            return true;
        }
    }

    private static Stream<ItemStack> getContents(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag();
        
        if (tag == null) {
            return Stream.empty();
        } else {
            ListTag list = tag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);
            return list.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    @NotNull
    public Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(stack).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(stack)));
    }

    /**
     * Allows items to add custom lines of information to the mouseover description.
     */
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(stack), MAX_WEIGHT).withStyle(ChatFormatting.GRAY));
    }

    
    @SuppressWarnings("deprecation")
    public void onDestroyed(@NotNull ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, getContents(itemEntity.getItem()));
    }

    private void playRemoveOneSound(@NotNull Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(@NotNull Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(@NotNull Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
