package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class Holder {
    @NotNull public final String key;
    @NotNull public final String name;

    Holder(@NotNull String key, @NotNull String name) {
        this.key = key;
        this.name = name;
    }

    public static class SimpleItemHolder extends Holder {
        @NotNull public final SimpleItemType object;
        @NotNull public final Supplier<Item> item;

        SimpleItemHolder(@NotNull SimpleItemType object, @NotNull Supplier<Item> item) {
            super(object.key, object.name);
            this.object = object;
            this.item = item;
        }
    }

    public static class SimpleBlockHolder extends Holder implements IBlockHolder {
        @NotNull public final SimpleBlockType object;
        @NotNull public final Supplier<Block> block;
        @NotNull public final Supplier<Item> item;

        SimpleBlockHolder(@NotNull SimpleBlockType object, @NotNull Function<SimpleBlockHolder, Supplier<Block>> blockSupplier,
                          @NotNull Function<SimpleBlockHolder, Supplier<Item>> itemSupplier) {
            super(object.key, object.name);
            this.object = object;
            this.block = blockSupplier.apply(this);
            this.item = itemSupplier.apply(this);
        }

        @Override
        public @NotNull Supplier<Block> getBlockRegistry() {
            return block;
        }

        @Override
        public @NotNull IMenu getMenu() {
            return object;
        }
    }

    public static class EntitySimpleBlockHolder extends SimpleBlockHolder implements IEntityBlockHolder {
        @NotNull private final Supplier<BlockEntityType<?>> entityType;

        public EntitySimpleBlockHolder(@NotNull SimpleBlockType product, @NotNull Function<SimpleBlockHolder, Supplier<Block>> blockSupplier,
                                       @NotNull Function<SimpleBlockHolder, Supplier<Item>> itemSupplier,
                                       @NotNull Function<EntitySimpleBlockHolder, Supplier<BlockEntityType<?>>> entityType) {
            super(product, blockSupplier, itemSupplier);
            this.entityType = entityType.apply(this);
        }

        @Override
        public <T extends BlockEntity> BlockEntityType<T> getBlockEntityType() {
            return (BlockEntityType<T>) entityType.get();
        }
    }

    public static class MenuEntitySimpleBlockHolder extends EntitySimpleBlockHolder implements IMenuEntityBlockHolder {
        private final Supplier<MenuType<? extends AbstractContainerMenu>> menuType;

        public MenuEntitySimpleBlockHolder(@NotNull SimpleBlockType product,
                                           @NotNull Function<SimpleBlockHolder, Supplier<Block>> blockSupplier,
                                           @NotNull Function<SimpleBlockHolder, Supplier<Item>> itemSupplier,
                                           @NotNull Function<EntitySimpleBlockHolder, Supplier<BlockEntityType<?>>> entityTypeSupplier,
                                           @NotNull Function<MenuEntitySimpleBlockHolder, Supplier<MenuType<? extends AbstractContainerMenu>>> menuType) {
            super(product, blockSupplier, itemSupplier, entityTypeSupplier);
            this.menuType = menuType.apply(this);
        }

        @Override
        public <T extends AbstractContainerMenu> MenuType<T> getMenuType() {
            return (MenuType<T>) menuType.get();
        }
    }

    public static class MaterialHolder<U extends INameable & IMaterialModel<?, ?>> extends Holder {
        @NotNull public final U object;
        @NotNull public final MaterialType material;

        MaterialHolder(@NotNull U object, @NotNull MaterialType material) {
            super(
                    Holder.stringify(object.getKeyHolder(), langTransformer(object, material, true), "_"),
                    Holder.stringify(object.getNameHolder(), langTransformer(object, material, false), " ")
            );
            this.object = object;
            this.material = material;
        }
    }

    public static class ItemHolder extends MaterialHolder<MaterialItemType> {
        @NotNull public final Supplier<Item> item;

        public ItemHolder(@NotNull MaterialItemType product, @NotNull MaterialType materialHolder, @NotNull Function<ItemHolder, Supplier<Item>> item) {
            super(product, materialHolder);
            this.item = item.apply(this);
        }
    }

    public static class BlockHolder extends MaterialHolder<MaterialBlockType> implements IBlockHolder {
        @NotNull public final Supplier<Block> block;
        @NotNull public final Supplier<Item> item;

        public BlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                           @NotNull Function<BlockHolder, Supplier<Block>> blockSupplier,
                           @NotNull Function<BlockHolder, Supplier<Item>> itemSupplier) {
            super(product, materialHolder);
            this.block = blockSupplier.apply(this);
            this.item = itemSupplier.apply(this);
        }

        @Override
        public @NotNull Supplier<Block> getBlockRegistry() {
            return block;
        }

        @Override
        public @NotNull IMenu getMenu() {
            return object;
        }
    }

    public static class EntityBlockHolder extends BlockHolder implements IEntityBlockHolder {
        @NotNull private final Supplier<BlockEntityType<?>> entityType;


        public EntityBlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                                 @NotNull Function<BlockHolder, Supplier<Block>> blockSupplier,
                                 @NotNull Function<BlockHolder, Supplier<Item>> itemSupplier,
                                 @NotNull Function<EntityBlockHolder, Supplier<BlockEntityType<?>>> entityType) {
            super(product, materialHolder, blockSupplier, itemSupplier);
            this.entityType = entityType.apply(this);
        }

        @Override
        public <T extends BlockEntity> BlockEntityType<T> getBlockEntityType() {
            return (BlockEntityType<T>) entityType.get();
        }
    }

    public static class MenuEntityBlockHolder extends EntityBlockHolder implements IMenuEntityBlockHolder {
        private final Supplier<MenuType<? extends AbstractContainerMenu>> menuType;

        public MenuEntityBlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                                     @NotNull Function<BlockHolder, Supplier<Block>> blockSupplier,
                                     @NotNull Function<BlockHolder, Supplier<Item>> itemSupplier,
                                     @NotNull Function<EntityBlockHolder, Supplier<BlockEntityType<?>>> entityTypeSupplier,
                                     @NotNull Function<MenuEntityBlockHolder, Supplier<MenuType<? extends AbstractContainerMenu>>> menuType) {
            super(product, materialHolder, blockSupplier, itemSupplier, entityTypeSupplier);
            this.menuType = menuType.apply(this);
        }

        public <T extends AbstractContainerMenu> MenuType<T> getMenuType() {
            return (MenuType<T>) menuType.get();
        }
    }

    public static class FluidHolder extends MaterialHolder<MaterialFluidType> {
        @NotNull public final Supplier<Block> block;
        @NotNull public final Supplier<FluidType> type;
        @NotNull public final Supplier<Fluid> source;
        @NotNull public final Supplier<Fluid> flowing;
        @NotNull public final Supplier<Item> bucket;

        public FluidHolder(@NotNull MaterialFluidType product, @NotNull MaterialType materialHolder,
                           @NotNull Supplier<Block> block, @NotNull Supplier<FluidType> type, @NotNull Supplier<Fluid> source,
                           @NotNull Supplier<Fluid> flowing, @NotNull Supplier<Item> bucket) {
            super(product, materialHolder);
            this.block = block;
            this.type = type;
            this.source = source;
            this.flowing = flowing;
            this.bucket = bucket;
        }
    }

    public static class SimpleEntityHolder extends Holder {
        @NotNull public final SimpleEntityType object;
        @NotNull private final Supplier<EntityType<? extends Entity>> entityType;

        SimpleEntityHolder(@NotNull SimpleEntityType object, @NotNull Function<SimpleEntityHolder, Supplier<EntityType<? extends Entity>>> typeSupplier) {
            super(object.key, object.name);
            this.object = object;
            this.entityType = typeSupplier.apply(this);
        }

        public <T extends Entity> EntityType<T> getEntityType() {
            return (EntityType<T>) entityType.get();
        }
    }

    public static class AnimalEntityHolder extends Holder {
        @NotNull public final AnimalEntityType object;
        @NotNull private final Supplier<EntityType<Animal>> entityType;
        @NotNull public final Supplier<Item> spawnEgg;

        AnimalEntityHolder(@NotNull AnimalEntityType object, @NotNull Function<AnimalEntityHolder, Supplier<EntityType<Animal>>> typeSupplier,
                           @NotNull Function<AnimalEntityHolder, Supplier<Item>> spawnEggSupplier) {
            super(object.key, object.name);
            this.object = object;
            this.entityType = typeSupplier.apply(this);
            this.spawnEgg = spawnEggSupplier.apply(this);
        }

        public <T extends Entity> EntityType<T> getEntityType() {
            return (EntityType<T>) entityType.get();
        }
    }

    private static String stringify(NameHolder holder, String material, String separator) {
        return (holder.prefix() != null ? holder.prefix() + separator : "") + material + (holder.suffix() != null ? separator + holder.suffix() : "");
    }

    private static <U extends INameable> String langTransformer(@NotNull U product, @NotNull MaterialType material, boolean isKey) {
        if (material == MaterialType.GOLD) {
            if (product instanceof MaterialItemType type) {
                switch (type) {
                    case CRUSHED_MATERIAL -> {
                    }
                    default -> {
                        return (isKey ? material.key : material.name) + "en";
                    }
                }
            }
            if (product instanceof MaterialBlockType type) {
                switch (type) {
                    default -> {
                        return (isKey ? material.key : material.name) + "en";
                    }
                }
            }
        }

        return isKey ? material.key : material.name;
    }
}
