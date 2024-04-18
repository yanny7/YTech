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

    public static class SimpleBlockHolder extends Holder implements IBlockHolder {
        @NotNull public final SimpleBlockType object;
        @NotNull public final RegistryObject<Block> block;
        @NotNull public final RegistryObject<Item> item;

        SimpleBlockHolder(@NotNull SimpleBlockType object, @NotNull Function<SimpleBlockHolder, RegistryObject<Block>> blockSupplier,
                          @NotNull Function<SimpleBlockHolder, RegistryObject<Item>> itemSupplier) {
            super(object.key, object.name);
            this.object = object;
            this.block = blockSupplier.apply(this);
            this.item = itemSupplier.apply(this);
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
        @NotNull private final RegistryObject<BlockEntityType<? extends BlockEntity>> entityType;

        public EntitySimpleBlockHolder(@NotNull SimpleBlockType product, @NotNull Function<SimpleBlockHolder, RegistryObject<Block>> blockSupplier,
                                       @NotNull Function<SimpleBlockHolder, RegistryObject<Item>> itemSupplier,
                                       @NotNull Function<EntitySimpleBlockHolder, RegistryObject<BlockEntityType<?>>> entityType) {
            super(product, blockSupplier, itemSupplier);
            this.entityType = entityType.apply(this);
        }

        @Override
        public <T extends BlockEntity> BlockEntityType<T> getBlockEntityType() {
            return (BlockEntityType<T>) entityType.get();
        }
    }

    public static class MenuEntitySimpleBlockHolder extends EntitySimpleBlockHolder implements IMenuEntityBlockHolder {
        private final RegistryObject<MenuType<? extends AbstractContainerMenu>> menuType;

        public MenuEntitySimpleBlockHolder(@NotNull SimpleBlockType product,
                                           @NotNull Function<SimpleBlockHolder, RegistryObject<Block>> blockSupplier,
                                           @NotNull Function<SimpleBlockHolder, RegistryObject<Item>> itemSupplier,
                                           @NotNull Function<EntitySimpleBlockHolder, RegistryObject<BlockEntityType<?>>> entityTypeSupplier,
                                           @NotNull Function<MenuEntitySimpleBlockHolder, RegistryObject<MenuType<? extends AbstractContainerMenu>>> menuType) {
            super(product, blockSupplier, itemSupplier, entityTypeSupplier);
            this.menuType = menuType.apply(this);
        }

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

    public static class BlockHolder extends MaterialHolder<MaterialBlockType> implements IBlockHolder {
        @NotNull public final RegistryObject<Block> block;
        @NotNull public final RegistryObject<Item> item;

        public BlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                           @NotNull Function<BlockHolder, RegistryObject<Block>> blockSupplier,
                           @NotNull Function<BlockHolder, RegistryObject<Item>> itemSupplier) {
            super(product, materialHolder);
            this.block = blockSupplier.apply(this);
            this.item = itemSupplier.apply(this);
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
        @NotNull private final RegistryObject<BlockEntityType<?>> entityType;


        public EntityBlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                                 @NotNull Function<BlockHolder, RegistryObject<Block>> blockSupplier,
                                 @NotNull Function<BlockHolder, RegistryObject<Item>> itemSupplier,
                                 @NotNull Function<EntityBlockHolder, RegistryObject<BlockEntityType<?>>> entityType) {
            super(product, materialHolder, blockSupplier, itemSupplier);
            this.entityType = entityType.apply(this);
        }

        @Override
        public <T extends BlockEntity> BlockEntityType<T> getBlockEntityType() {
            return (BlockEntityType<T>) entityType.get();
        }
    }

    public static class MenuEntityBlockHolder extends EntityBlockHolder implements IMenuEntityBlockHolder {
        private final RegistryObject<MenuType<? extends AbstractContainerMenu>> menuType;

        public MenuEntityBlockHolder(@NotNull MaterialBlockType product, @NotNull MaterialType materialHolder,
                                     @NotNull Function<BlockHolder, RegistryObject<Block>> blockSupplier,
                                     @NotNull Function<BlockHolder, RegistryObject<Item>> itemSupplier,
                                     @NotNull Function<EntityBlockHolder, RegistryObject<BlockEntityType<?>>> entityTypeSupplier,
                                     @NotNull Function<MenuEntityBlockHolder, RegistryObject<MenuType<? extends AbstractContainerMenu>>> menuType) {
            super(product, materialHolder, blockSupplier, itemSupplier, entityTypeSupplier);
            this.menuType = menuType.apply(this);
        }

        public <T extends AbstractContainerMenu> MenuType<T> getMenuType() {
            return (MenuType<T>) menuType.get();
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

    public static class SimpleEntityHolder extends Holder {
        @NotNull public final SimpleEntityType object;
        @NotNull private final RegistryObject<EntityType<? extends Entity>> entityType;

        SimpleEntityHolder(@NotNull SimpleEntityType object, @NotNull Function<SimpleEntityHolder, RegistryObject<EntityType<? extends Entity>>> typeSupplier) {
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
        @NotNull private final RegistryObject<EntityType<Animal>> entityType;
        @NotNull public final RegistryObject<Item> spawnEgg;

        AnimalEntityHolder(@NotNull AnimalEntityType object, @NotNull Function<AnimalEntityHolder, RegistryObject<EntityType<Animal>>> typeSupplier,
                           @NotNull Function<AnimalEntityHolder, RegistryObject<Item>> spawnEggSupplier) {
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
