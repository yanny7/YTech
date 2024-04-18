package com.yanny.ytech.configuration.item;

import com.yanny.ytech.registration.YTechItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class LavaClayBucketItem extends ClayBucketItem {
    public LavaClayBucketItem() {
        super(() -> Fluids.LAVA, new Item.Properties().craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 20000;
    }
}
