package com.yanny.ytech.configuration.tooltip;

import com.yanny.ytech.configuration.data_component.BasketContents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientBasketTooltip implements ClientTooltipComponent {
    private static final ResourceLocation PROGRESSBAR_BORDER_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_border");
    private static final ResourceLocation PROGRESSBAR_FILL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
    private static final ResourceLocation PROGRESSBAR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_full");
    private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_highlight_back");
    private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_highlight_front");
    private static final ResourceLocation SLOT_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_background");
    private static final int SLOT_MARGIN = 4;
    private static final int SLOT_SIZE = 24;
    private static final int GRID_WIDTH = 96;
    private static final int PROGRESSBAR_HEIGHT = 13;
    private static final int PROGRESSBAR_WIDTH = 96;
    private static final int PROGRESSBAR_BORDER = 1;
    private static final int PROGRESSBAR_FILL_MAX = 94;
    private static final int PROGRESSBAR_MARGIN_Y = 4;
    private static final Component BUNDLE_FULL_TEXT = Component.translatable("item.minecraft.bundle.full");
    private static final Component BUNDLE_EMPTY_TEXT = Component.translatable("item.minecraft.bundle.empty");
    private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.minecraft.bundle.empty.description");
    private final BasketContents contents;

    public ClientBasketTooltip(BasketTooltip tooltip) {
        this.contents = tooltip.contents();
    }

    @Override
    public int getHeight(@NotNull Font font) {
        return this.contents.isEmpty() ? getEmptyBundleBackgroundHeight(font) : this.backgroundHeight();
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return GRID_WIDTH;
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

    private static int getEmptyBundleBackgroundHeight(Font p_361305_) {
        return getEmptyBundleDescriptionTextHeight(p_361305_) + PROGRESSBAR_HEIGHT + 8;
    }

    private int backgroundHeight() {
        return this.itemGridHeight() + PROGRESSBAR_HEIGHT + 8;
    }

    private int itemGridHeight() {
        return this.gridSizeY() * SLOT_SIZE;
    }

    private int getContentXOffset(int p_368639_) {
        return (p_368639_ - GRID_WIDTH) / 2;
    }

    private int gridSizeY() {
        return Mth.positiveCeilDiv(this.slotCount(), SLOT_MARGIN);
    }

    private int slotCount() {
        return Math.min(12, this.contents.size());
    }

    @Override
    public void renderImage(@NotNull Font p_194042_, int p_194043_, int p_194044_, int p_368730_, int p_368543_, @NotNull GuiGraphics p_282522_) {
        if (this.contents.isEmpty()) {
            this.renderEmptyBundleTooltip(p_194042_, p_194043_, p_194044_, p_368730_, p_368543_, p_282522_);
        } else {
            this.renderBundleWithItemsTooltip(p_194042_, p_194043_, p_194044_, p_368730_, p_368543_, p_282522_);
        }
    }

    private void renderEmptyBundleTooltip(Font p_365081_, int p_364144_, int p_364357_, int p_368704_, int p_368751_, GuiGraphics p_365036_) {
        drawEmptyBundleDescriptionText(p_364144_ + this.getContentXOffset(p_368704_), p_364357_, p_365081_, p_365036_);
        this.drawProgressbar(
                p_364144_ + this.getContentXOffset(p_368704_), p_364357_ + getEmptyBundleDescriptionTextHeight(p_365081_) + SLOT_MARGIN, p_365081_, p_365036_
        );
    }

    private void renderBundleWithItemsTooltip(Font p_364080_, int p_360498_, int p_363327_, int p_368677_, int p_368508_, GuiGraphics p_360526_) {
        boolean flag = this.contents.size() > 12;
        List<ItemStack> list = this.getShownItems(this.contents.getNumberOfItemsToShow());
        int i = p_360498_ + this.getContentXOffset(p_368677_) + GRID_WIDTH;
        int j = p_363327_ + this.gridSizeY() * SLOT_SIZE;
        int k = 1;

        for (int l = 1; l <= this.gridSizeY(); l++) {
            for (int i1 = 1; i1 <= 4; i1++) {
                int j1 = i - i1 * SLOT_SIZE;
                int k1 = j - l * SLOT_SIZE;
                if (shouldRenderSurplusText(flag, i1, l)) {
                    renderCount(j1, k1, this.getAmountOfHiddenItems(list), p_364080_, p_360526_);
                } else if (shouldRenderItemSlot(list, k)) {
                    this.renderSlot(k, j1, k1, list, k, p_364080_, p_360526_);
                    k++;
                }
            }
        }

        this.drawSelectedItemTooltip(p_364080_, p_360526_, p_360498_, p_363327_, p_368677_);
        this.drawProgressbar(p_360498_ + this.getContentXOffset(p_368677_), p_363327_ + this.itemGridHeight() + PROGRESSBAR_MARGIN_Y, p_364080_, p_360526_);
    }

    private List<ItemStack> getShownItems(int p_364960_) {
        int i = Math.min(this.contents.size(), p_364960_);
        return this.contents.itemCopyStream().toList().subList(0, i);
    }

    private static boolean shouldRenderSurplusText(boolean p_361034_, int p_363348_, int p_360653_) {
        return p_361034_ && p_363348_ * p_360653_ == 1;
    }

    private static boolean shouldRenderItemSlot(List<ItemStack> p_362150_, int p_364466_) {
        return p_362150_.size() >= p_364466_;
    }

    private int getAmountOfHiddenItems(List<ItemStack> p_362700_) {
        return this.contents.itemCopyStream().skip((long)p_362700_.size()).mapToInt(ItemStack::getCount).sum();
    }

    private void renderSlot(int p_283180_, int p_282972_, int p_282547_, List<ItemStack> p_361523_, int p_360587_, Font p_281863_, GuiGraphics p_283625_) {
        int i = p_361523_.size() - p_283180_;
        boolean flag = i == this.contents.getSelectedItem();
        ItemStack itemstack = p_361523_.get(i);
        if (flag) {
            p_283625_.blitSprite(RenderType::guiTextured, SLOT_HIGHLIGHT_BACK_SPRITE, p_282972_, p_282547_, SLOT_SIZE, SLOT_SIZE);
        } else {
            p_283625_.blitSprite(RenderType::guiTextured, SLOT_BACKGROUND_SPRITE, p_282972_, p_282547_, SLOT_SIZE, SLOT_SIZE);
        }

        p_283625_.renderItem(itemstack, p_282972_ + SLOT_MARGIN, p_282547_ + SLOT_MARGIN, p_360587_);
        p_283625_.renderItemDecorations(p_281863_, itemstack, p_282972_ + PROGRESSBAR_MARGIN_Y, p_282547_ + PROGRESSBAR_MARGIN_Y);
        if (flag) {
            p_283625_.blitSprite(RenderType::guiTexturedOverlay, SLOT_HIGHLIGHT_FRONT_SPRITE, p_282972_, p_282547_, SLOT_SIZE, SLOT_SIZE);
        }
    }

    private static void renderCount(int p_363359_, int p_364432_, int p_364090_, Font p_363903_, GuiGraphics p_363709_) {
        p_363709_.drawCenteredString(p_363903_, "+" + p_364090_, p_363359_ + 12, p_364432_ + 10, 16777215);
    }

    private void drawSelectedItemTooltip(Font p_360616_, GuiGraphics p_364594_, int p_362065_, int p_363779_, int p_368494_) {
        if (contents.hasSelectedItem()) {
            ItemStack itemstack = this.contents.getItemUnsafe(-1);
            Component component = itemstack.getStyledHoverName();
            int i = p_360616_.width(component.getVisualOrderText());
            int j = p_362065_ + p_368494_ / 2 - 12;
            p_364594_.renderTooltip(p_360616_, component, j - i / 2, p_363779_ - 15, itemstack.get(DataComponents.TOOLTIP_STYLE));
        }
    }

    private void drawProgressbar(int p_362365_, int p_364597_, Font p_363606_, GuiGraphics p_362696_) {
        p_362696_.blitSprite(RenderType::guiTextured, this.getProgressBarTexture(), p_362365_ + PROGRESSBAR_BORDER, p_364597_, this.getProgressBarFill(), PROGRESSBAR_HEIGHT);
        p_362696_.blitSprite(RenderType::guiTextured, PROGRESSBAR_BORDER_SPRITE, p_362365_, p_364597_, PROGRESSBAR_WIDTH, PROGRESSBAR_HEIGHT);
        Component component = this.getProgressBarFillText();
        if (component != null) {
            p_362696_.drawCenteredString(p_363606_, component, p_362365_ + 48, p_364597_ + 3, 16777215);
        }
    }

    private static void drawEmptyBundleDescriptionText(int p_363213_, int p_362527_, Font p_361041_, GuiGraphics p_360386_) {
        p_360386_.drawWordWrap(p_361041_, BUNDLE_EMPTY_DESCRIPTION, p_363213_, p_362527_, GRID_WIDTH, 11184810);
    }

    private static int getEmptyBundleDescriptionTextHeight(Font p_363613_) {
        return p_363613_.split(BUNDLE_EMPTY_DESCRIPTION, GRID_WIDTH).size() * 9;
    }

    private int getProgressBarFill() {
        return Mth.clamp(Mth.mulAndTruncate(this.contents.weight(), PROGRESSBAR_FILL_MAX), 0, PROGRESSBAR_FILL_MAX);
    }

    private ResourceLocation getProgressBarTexture() {
        return this.contents.weight().compareTo(Fraction.ONE) >= 0 ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
    }

    @Nullable
    private Component getProgressBarFillText() {
        if (this.contents.isEmpty()) {
            return BUNDLE_EMPTY_TEXT;
        } else {
            return this.contents.weight().compareTo(Fraction.ONE) >= 0 ? BUNDLE_FULL_TEXT : null;
        }
    }
}
