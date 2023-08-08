package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.SharpFlintItem;
import com.yanny.ytech.registration.Holder;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public enum SimpleItemType {
    GRASS_FIBERS("grass_fibers", "Grass Fibers",
            () -> new Item(new Item.Properties()),
            SimpleItemType::basicItemModelProvider),
    GRASS_TWINE("grass_twine", "Grass Twine",
            () -> new Item(new Item.Properties()),
            SimpleItemType::basicItemModelProvider),
    SHARP_FLINT("sharp_flint", "Sharp Flint",
            SharpFlintItem::new,
            SimpleItemType::basicItemModelProvider),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> model;

    SimpleItemType(@NotNull String key, @NotNull String name, @NotNull Supplier<Item> itemGetter, @NotNull BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> model) {
        this.key = key;
        this.name = name;
        this.itemGetter = itemGetter;
        this.model = model;
    }

    public void registerModel(Holder.SimpleItemHolder holder, ItemModelProvider provider) {
        model.accept(holder, provider);
    }

    private static void basicItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.modLoc(ItemModelProvider.ITEM_FOLDER + "/" + holder.key));
    }
}
