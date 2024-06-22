package com.yanny.ytech.configuration.tooltip;

import com.yanny.ytech.configuration.data_component.BasketContents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientBasketTooltip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private static final int MARGIN_Y = 4;
    private static final int BORDER_WIDTH = 1;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;
    private final BasketContents contents;

    public ClientBasketTooltip(BasketContents.BasketTooltip tooltip) {
        this.contents = tooltip.contents();
    }

    @Override
    public int getHeight() {
        return this.backgroundHeight() + MARGIN_Y;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * SLOT_SIZE_X + 2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y + 2;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, GuiGraphics guiGraphics) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
        boolean flag = this.contents.weight().compareTo(Fraction.ONE) >= 0;
        int k = 0;

        for (int l = 0; l < j; l++) {
            for (int i1 = 0; i1 < i; i1++) {
                int j1 = x + i1 * SLOT_SIZE_X + BORDER_WIDTH;
                int k1 = y + l * SLOT_SIZE_Y + BORDER_WIDTH;
                this.renderSlot(j1, k1, k++, flag, guiGraphics, font);
            }
        }
    }

    private void renderSlot(int p_283180_, int p_282972_, int p_282547_, boolean p_283053_, GuiGraphics p_283625_, Font p_281863_) {
        if (p_282547_ >= this.contents.size()) {
            this.blit(p_283625_, p_283180_, p_282972_, p_283053_ ? ClientBasketTooltip.Texture.BLOCKED_SLOT : ClientBasketTooltip.Texture.SLOT);
        } else {
            ItemStack itemstack = this.contents.getItemUnsafe(p_282547_);
            this.blit(p_283625_, p_283180_, p_282972_, ClientBasketTooltip.Texture.SLOT);
            p_283625_.renderItem(itemstack, p_283180_ + BORDER_WIDTH, p_282972_ + BORDER_WIDTH, p_282547_);
            p_283625_.renderItemDecorations(p_281863_, itemstack, p_283180_ + BORDER_WIDTH, p_282972_ + BORDER_WIDTH);
            if (p_282547_ == 0) {
                AbstractContainerScreen.renderSlotHighlight(p_283625_, p_283180_ + BORDER_WIDTH, p_282972_ + BORDER_WIDTH, 0);
            }
        }
    }

    private void blit(GuiGraphics p_281273_, int p_282428_, int p_281897_, ClientBasketTooltip.Texture p_281917_) {
        p_281273_.blitSprite(p_281917_.sprite, p_282428_, p_281897_, 0, p_281917_.w, p_281917_.h);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.contents.size() + 1.0)));
    }

    private int gridSizeY() {
        return (int)Math.ceil(((double)this.contents.size() + 1.0) / (double)this.gridSizeX());
    }

    @OnlyIn(Dist.CLIENT)
    static enum Texture {
        BLOCKED_SLOT(ResourceLocation.withDefaultNamespace("container/bundle/blocked_slot"), SLOT_SIZE_X, SLOT_SIZE_Y),
        SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), SLOT_SIZE_X, SLOT_SIZE_Y);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        private Texture(ResourceLocation p_295000_, int p_169928_, int p_169929_) {
            this.sprite = p_295000_;
            this.w = p_169928_;
            this.h = p_169929_;
        }
    }
}
