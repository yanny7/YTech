package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.data_component.BasketContents;
import com.yanny.ytech.registration.YTechDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BasketItem extends Item {
    public static final ResourceLocation FILLED_PREDICATE = Utils.modLoc("filled");
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public BasketItem() {
        super(new Properties().stacksTo(1).component(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY));
    }

    public static float getFullnessDisplay(@NotNull ItemStack stack) {
        BasketContents bundleContents = stack.getOrDefault(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY);
        return bundleContents.weight().floatValue();
    }

    public boolean overrideStackedOnOther(@NotNull ItemStack stack, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        } else {
            BasketContents bundleContents = stack.get(YTechDataComponentTypes.BASKET_CONTENTS);

            if (bundleContents == null) {
                return false;
            } else {
                ItemStack slotItem = slot.getItem();
                BasketContents.Mutable mutableBundleContents = new BasketContents.Mutable(bundleContents);

                if (slotItem.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack removed = mutableBundleContents.removeOne();
                    
                    if (removed != null) {
                        ItemStack insert = slot.safeInsert(removed);
                        mutableBundleContents.tryInsert(insert);
                    }
                } else if (slotItem.getItem().canFitInsideContainerItems()) {
                    int i = mutableBundleContents.tryTransfer(slot, player);

                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(YTechDataComponentTypes.BASKET_CONTENTS, mutableBundleContents.toImmutable());
                return true;
            }
        }
    }

    public boolean overrideOtherStackedOnMe(@NotNull ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot, 
                                            @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (stack.getCount() != 1) return false;
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            BasketContents bundleContents = stack.get(YTechDataComponentTypes.BASKET_CONTENTS);
            
            if (bundleContents == null) {
                return false;
            } else {
                BasketContents.Mutable mutableBundleContents = new BasketContents.Mutable(bundleContents);

                if (other.isEmpty()) {
                    ItemStack itemstack = mutableBundleContents.removeOne();

                    if (itemstack != null) {
                        this.playRemoveOneSound(player);
                        access.set(itemstack);
                    }
                } else {
                    int i = mutableBundleContents.tryInsert(other);

                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(YTechDataComponentTypes.BASKET_CONTENTS, mutableBundleContents.toImmutable());
                return true;
            }
        } else {
            return false;
        }
    }
    
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (dropContents(itemStack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemStack);
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
        return BAR_COLOR;
    }

    private static boolean dropContents(@NotNull ItemStack stack, @NotNull Player player) {
        BasketContents bundleContents = stack.get(YTechDataComponentTypes.BASKET_CONTENTS);

        if (bundleContents != null && !bundleContents.isEmpty()) {
            stack.set(YTechDataComponentTypes.BASKET_CONTENTS, BasketContents.EMPTY);

            if (player instanceof ServerPlayer) {
                bundleContents.itemsCopy().forEach(itemStack -> player.drop(itemStack, true));
            }

            return true;
        } else {
            return false;
        }
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
