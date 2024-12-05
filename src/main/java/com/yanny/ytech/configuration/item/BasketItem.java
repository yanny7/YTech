package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.data_component.BasketContents;
import com.yanny.ytech.registration.YTechDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BasketItem extends Item {
    public static final ResourceLocation FILLED_PREDICATE = Utils.modLoc("filled");
    private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

    public BasketItem(Properties properties) {
        super(properties.stacksTo(1).component(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY));
    }

    public static float getFullnessDisplay(@NotNull ItemStack stack) {
        BasketContents bundleContents = stack.getOrDefault(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY);
        return bundleContents.weight().floatValue();
    }

    public boolean overrideStackedOnOther(@NotNull ItemStack stack, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        BasketContents basketContents = stack.get(YTechDataComponentTypes.BASKET_CONTENTS);

        if (basketContents != null && stack.getCount() == 1) {
            ItemStack itemStack = slot.getItem();
            BasketContents.Mutable mutable = new BasketContents.Mutable(basketContents);

            if (action == ClickAction.PRIMARY && !itemStack.isEmpty()) {
                if (mutable.tryTransfer(slot, player) > 0) {
                    playInsertSound(player);
                } else {
                    playInsertFailSound(player);
                }

                stack.set(YTechDataComponentTypes.BASKET_CONTENTS, mutable.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                return true;
            } else if (action == ClickAction.SECONDARY && itemStack.isEmpty()) {
                ItemStack itemStack1 = mutable.removeOne();

                if (itemStack1 != null) {
                    ItemStack itemStack2 = slot.safeInsert(itemStack1);

                    if (itemStack2.getCount() > 0) {
                        mutable.tryInsert(itemStack2);
                    } else {
                        playRemoveOneSound(player);
                    }
                }

                stack.set(YTechDataComponentTypes.BASKET_CONTENTS, mutable.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean overrideOtherStackedOnMe(@NotNull ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot,
                                            @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (stack.getCount() != 1) {
            return false;
        } else if (action == ClickAction.PRIMARY && other.isEmpty()) {
            toggleSelectedItem(stack, -1);
            return false;
        } else {
            BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);

            if (bundleContents == null) {
                return false;
            } else {
                BundleContents.Mutable bundlecontents$mutable = new BundleContents.Mutable(bundleContents);

                if (action == ClickAction.PRIMARY && !other.isEmpty()) {
                    if (slot.allowModification(player) && bundlecontents$mutable.tryInsert(other) > 0) {
                        playInsertSound(player);
                    } else {
                        playInsertFailSound(player);
                    }

                    stack.set(DataComponents.BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                    this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack itemstack = bundlecontents$mutable.removeOne();
                        if (itemstack != null) {
                            playRemoveOneSound(player);
                            access.set(itemstack);
                        }
                    }

                    stack.set(DataComponents.BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                    this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else {
                    toggleSelectedItem(stack, -1);
                    return false;
                }
            }
        }
    }

    @NotNull
    public InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.SUCCESS_SERVER;
        }
    }

    private void dropContent(Level level, Player player, ItemStack stack) {
        if (this.dropContent(stack, player)) {
            playDropContentsSound(level, player);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    public boolean isBarVisible(@NotNull ItemStack stack) {
        BasketContents contents = stack.getOrDefault(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY);
        return contents.weight().compareTo(Fraction.ZERO) > 0;
    }

    public int getBarWidth(@NotNull ItemStack stack) {
        BasketContents contents = stack.getOrDefault(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(contents.weight(), 12), 13);
    }

    public int getBarColor(@NotNull ItemStack stack) {
        BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundleContents.weight().compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
    }

    public static void toggleSelectedItem(ItemStack stack, int index) {
        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);

        if (bundleContents != null) {
            BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
            mutable.toggleSelectedItem(index);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        }

    }

    public static boolean hasSelectedItem(ItemStack stack) {
        BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundleContents.getSelectedItem() != -1;
    }

    public static int getSelectedItem(ItemStack stack) {
        BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundleContents.getSelectedItem();
    }

    public static ItemStack getSelectedItemStack(ItemStack stack) {
        BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundleContents.getItemUnsafe(bundleContents.getSelectedItem());
    }

    public static int getNumberOfItemsToShow(ItemStack stack) {
        BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundleContents.getNumberOfItemsToShow();
    }

    private boolean dropContent(@NotNull ItemStack stack, @NotNull Player player) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);

        if (contents != null && !contents.isEmpty()) {
            Optional<ItemStack> optional = removeOneItemFromBundle(stack, player, contents);

            if (optional.isPresent()) {
                player.drop(optional.get(), true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static Optional<ItemStack> removeOneItemFromBundle(ItemStack stack, Player player, BundleContents contents) {
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        ItemStack itemStack = mutable.removeOne();

        if (itemStack != null) {
            playRemoveOneSound(player);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
            return Optional.of(itemStack);
        } else {
            return Optional.empty();
        }
    }

    public void onUseTick(Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int tick) {
        if (!level.isClientSide && entity instanceof Player player) {
            int i = this.getUseDuration(stack, entity);
            boolean flag = tick == i;

            if (flag || tick < i - 10 && tick % 2 == 0) {
                this.dropContent(level, player, stack);
            }
        }

    }

    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 200;
    }

    @NotNull
    public Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(YTechDataComponentTypes.BASKET_CONTENTS)).map(BasketContents.BasketTooltip::new)
                : Optional.empty();
    }

    /**
     * Allows items to add custom lines of information to the mouseover description.
     */
    @Override
    public void appendHoverText(ItemStack stack, @NotNull Item.TooltipContext tooltipContext, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        BasketContents bundleContents = stack.get(YTechDataComponentTypes.BASKET_CONTENTS);

        if (bundleContents != null) {
            int i = Mth.mulAndTruncate(bundleContents.weight(), BasketContents.MAX_WEIGHT);
            components.add(Component.translatable("item.minecraft.bundle.fullness", i, BasketContents.MAX_WEIGHT).withStyle(ChatFormatting.GRAY));
        }
    }


    @SuppressWarnings("deprecation")
    public void onDestroyed(@NotNull ItemEntity itemEntity) {
        BasketContents bundleContents = itemEntity.getItem().get(YTechDataComponentTypes.BASKET_CONTENTS);

        if (bundleContents != null) {
            itemEntity.getItem().set(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY);
            ItemUtils.onContainerDestroyed(itemEntity, bundleContents.itemsCopy());
        }
    }

    private static void playRemoveOneSound(@NotNull Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(@NotNull Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private static void playDropContentsSound(Level level, @NotNull Entity entity) {
        level.playSound(null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void broadcastChangesOnContainerMenu(Player player) {
        player.containerMenu.slotsChanged(player.getInventory());
    }
}
