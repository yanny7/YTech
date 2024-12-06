package com.yanny.ytech.configuration.tooltip;

import com.yanny.ytech.configuration.data_component.BasketContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record BasketTooltip(BasketContents contents) implements TooltipComponent {
}
