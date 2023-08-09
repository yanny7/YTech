package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Holder {
    @NotNull public final String key;
    @NotNull public final String name;

    Holder(@NotNull String key, @NotNull String name) {
        this.key = key;
        this.name = name;
    }

    public static class SimpleItemHolder extends Holder {
        @NotNull public final SimpleItemType object;
        @NotNull public final RegistryObject<Item> item;

        SimpleItemHolder(@NotNull SimpleItemType object, @NotNull RegistryObject<Item> item) {
            super(object.key, object.name);
            this.object = object;
            this.item = item;
        }
    }

    public static class SimpleBlockHolder extends Holder implements IBlockHolder {
        @NotNull public final SimpleBlockType object;
        @NotNull public final RegistryObject<Block> block;

        SimpleBlockHolder(@NotNull SimpleBlockType object, @NotNull Function<SimpleBlockHolder, RegistryObject<Block>> blockSupplier) {
            super(object.key, object.name);
            this.object = object;
            this.block = blockSupplier.apply(this);
        }

        @Override
        public @NotNull RegistryObject<Block> getBlockRegistry() {
            return block;
        }

        @Override
        public @NotNull IMenu getMenu() {
            return object;
        }
    }

    public static class EntitySimpleBlockHolder extends SimpleBlockHolder implements IEntityBlockHolder {
        @NotNull public final RegistryObject<BlockEntityType<?>> entityType;


        public EntitySimpleBlockHolder(@NotNull SimpleBlockType product, @NotNull Function<SimpleBlockHolder, RegistryObject<Block>> blockSupplier,
                                       @NotNull Function<EntitySimpleBlockHolder, RegistryObject<BlockEntityType<?>>> entityType) {
            super(product, blockSupplier);
            this.entityType = entityType.apply(this);
        }

        @Override
        public RegistryObject<BlockEntityType<?>> getEntityTypeRegistry() {
            return entityType;
        }
    }

    public static class MenuEntitySimpleBlockHolder extends EntitySimpleBlockHolder implements IMenuEntityBlockHolder {
        public final RegistryObject<MenuType<? extends AbstractContainerMenu>> menuType;

        public MenuEntitySimpleBlockHolder(@NotNull SimpleBlockType product,
                                           @NotNull Function<SimpleBlockHolder, RegistryObject<Block>> blockSupplier,
                                           @NotNull Function<EntitySimpleBlockHolder, RegistryObject<BlockEntityType<?>>> entityTypeSupplier,
                                           @NotNull Function<MenuEntitySimpleBlockHolder, RegistryObject<MenuType<? extends AbstractContainerMenu>>> menuType) {
            super(product, blockSupplier, entityTypeSupplier);
            this.menuType = menuType.apply(this);
        }

        @Override
        public RegistryObject<MenuType<? extends AbstractContainerMenu>> getMenuRegistry() {
            return menuType;
        }
    }

    public static class MaterialHolder<U extends INameable & IMaterialModel<?, ?>> extends Holder {
        @NotNull public final U object;
        @NotNull public final MaterialType material;

        MaterialHolder(@NotNull U object, @NotNull MaterialType material) {
            super(
                    Holder.stringify(object.getKeyHolder(), material.key, "_"),
                    Holder.stringify(object.getNameHolder(), material.name, " ")
            );
            this.object = object;
            this.material = material;
        }
    }

    public static class ItemHolder extends MaterialHolder<MaterialItemType> {
        @NotNull public final RegistryObject<Item> item;

        public ItemHolder(@NotNull MaterialItemType product, @NotNull MaterialType materialHolder, @NotNull Function<ItemHolder, RegistryObject<Item>> item) {
            super(product, materialHolder);
            this.item = item.apply(this);
        }
    }

    public static class BlockHolder extends MaterialHolder<MaterialBlockType> implements IBlockHolder {
        @NotNull public final RegistryObject<Block> block;

        public BlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder, @NotNull Function<BlockHolder, @NotNull RegistryObject<Block>> block) {
            super(product, materialHolder);
            this.block = block.apply(this);
        }

        @Override
        public @NotNull RegistryObject<Block> getBlockRegistry() {
            return block;
        }

        @Override
        public @NotNull IMenu getMenu() {
            return object;
        }
    }

    public static class EntityBlockHolder extends BlockHolder implements IEntityBlockHolder {
        @NotNull public final RegistryObject<BlockEntityType<?>> entityType;


        public EntityBlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                                 @NotNull Function<BlockHolder, RegistryObject<Block>> blockSupplier,
                                 @NotNull Function<EntityBlockHolder, RegistryObject<BlockEntityType<?>>> entityType) {
            super(product, materialHolder, blockSupplier);
            this.entityType = entityType.apply(this);
        }

        @Override
        public RegistryObject<BlockEntityType<?>> getEntityTypeRegistry() {
            return entityType;
        }
    }

    public static class MenuEntityBlockHolder extends EntityBlockHolder implements IMenuEntityBlockHolder {
        public final RegistryObject<MenuType<? extends AbstractContainerMenu>> menuType;

        public MenuEntityBlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                                     @NotNull Function<BlockHolder, RegistryObject<Block>> blockSupplier,
                                     @NotNull Function<EntityBlockHolder, RegistryObject<BlockEntityType<?>>> entityTypeSupplier,
                                     @NotNull Function<MenuEntityBlockHolder, RegistryObject<MenuType<? extends AbstractContainerMenu>>> menuType) {
            super(product, materialHolder, blockSupplier, entityTypeSupplier);
            this.menuType = menuType.apply(this);
        }

        @Override
        public RegistryObject<MenuType<? extends AbstractContainerMenu>> getMenuRegistry() {
            return menuType;
        }
    }

    public static class FluidHolder extends MaterialHolder<MaterialFluidType> {
        @NotNull public final RegistryObject<Block> block;
        @NotNull public final RegistryObject<FluidType> type;
        @NotNull public final RegistryObject<Fluid> source;
        @NotNull public final RegistryObject<Fluid> flowing;
        @NotNull public final RegistryObject<Item> bucket;

        public FluidHolder(@NotNull MaterialFluidType product, @NotNull MaterialType materialHolder,
                           @NotNull RegistryObject<Block> block, @NotNull RegistryObject<FluidType> type, @NotNull RegistryObject<Fluid> source,
                           @NotNull RegistryObject<Fluid> flowing, @NotNull RegistryObject<Item> bucket) {
            super(product, materialHolder);
            this.block = block;
            this.type = type;
            this.source = source;
            this.flowing = flowing;
            this.bucket = bucket;
        }
    }

    private static String stringify(NameHolder holder, String material, String separator) {
        return (holder.prefix() != null ? holder.prefix() + separator : "") + material + (holder.suffix() != null ? separator + holder.suffix() : "");
    }
}
