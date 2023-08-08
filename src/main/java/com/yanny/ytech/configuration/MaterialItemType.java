package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public enum MaterialItemType implements MaterialEnumHolder {
    INGOT("ingot", MaterialEnumHolder.suffix("ingot"), MaterialEnumHolder.suffix("Ingot"),
            () -> new Item(new Item.Properties()),
            Set.of(0),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DUST("dust", MaterialEnumHolder.suffix("dust"), MaterialEnumHolder.suffix("Dust"),
            () -> new Item(new Item.Properties()),
            Set.of(0),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.TIN)),
    RAW_MATERIAL("raw_material", MaterialEnumHolder.prefix("raw"), MaterialEnumHolder.prefix("Raw"),
            () -> new Item(new Item.Properties()),
            Set.of(0),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    PLATE("plate", MaterialEnumHolder.suffix("plate"), MaterialEnumHolder.suffix("Plate"),
            () -> new Item(new Item.Properties()),
            Set.of(0),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.IRON)),

    AXE("axe", MaterialEnumHolder.suffix("axe"), MaterialEnumHolder.suffix("Axe"),
            () -> new AxeItem(Tiers.WOOD, 6.0f, -3.2f, new Item.Properties()),
            Set.of(1),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    PICKAXE("pickaxe", MaterialEnumHolder.suffix("pickaxe"), MaterialEnumHolder.suffix("Pickaxe"),
            () -> new PickaxeItem(Tiers.WOOD, 1, -2.8f, new Item.Properties()),
            Set.of(1),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    SHOVEL("shovel", MaterialEnumHolder.suffix("shovel"), MaterialEnumHolder.suffix("Shovel"),
            () -> new ShovelItem(Tiers.WOOD, 1.5f, -3.0f, new Item.Properties()),
            Set.of(1),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    HOE("hoe", MaterialEnumHolder.suffix("hoe"), MaterialEnumHolder.suffix("Hoe"),
            () -> new HoeItem(Tiers.WOOD, 0, -3.0f, new Item.Properties()),
            Set.of(1),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    SWORD("sword", MaterialEnumHolder.suffix("sword"), MaterialEnumHolder.suffix("Sword"),
            () -> new ShovelItem(Tiers.WOOD, 3, -2.4f, new Item.Properties()),
            Set.of(1),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull public final Set<Integer> tintIndices;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemModelProvider> model;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialItemType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Supplier<Item> itemGetter,
                     @NotNull Set<Integer> tintIndices, @NotNull BiConsumer<Holder.ItemHolder, ItemModelProvider> model, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.itemGetter = itemGetter;
        this.tintIndices = tintIndices;
        this.model = model;
        this.materials = materials;
    }

    @NotNull
    @Override
    public NameHolder getKeyHolder() {
        return key;
    }

    @NotNull
    @Override
    public NameHolder getNameHolder() {
        return name;
    }

    @NotNull
    @Override
    public Set<Integer> getTintIndices() {
        return tintIndices;
    }

    public void registerModel(Holder.ItemHolder holder, ItemModelProvider provider) {
        model.accept(holder, provider);
    }

    private static void basicItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.modLoc(ItemModelProvider.ITEM_FOLDER + "/" + holder.object.id));
    }

    private static void toolItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.mcLoc(ItemModelProvider.ITEM_FOLDER + "/wooden_" + holder.object.id));
        builder.texture("layer1", provider.modLoc(ItemModelProvider.ITEM_FOLDER + "/" + holder.object.id + "_overlay"));
    }
}
