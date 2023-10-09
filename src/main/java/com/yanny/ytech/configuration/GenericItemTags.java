package com.yanny.ytech.configuration;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum GenericItemTags {
    FERTILIZER(ItemTags.create(Utils.modLoc("fertilizer")), List.of(Items.BONE_MEAL))
    ;

    public final TagKey<Item> tag;
    private final List<Item> items;

    GenericItemTags(@NotNull TagKey<Item> tag, List<Item> items) {
        this.tag = tag;
        this.items = items;
    }

    public void registerTags(ItemTagsProvider provider) {
        provider.tag(tag).add(items.toArray(Item[]::new));
    }
}
