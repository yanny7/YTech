package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

    public static class MaterialHolder<U extends INameable & IMaterialModel<?, ?>> extends Holder {
        @NotNull
        public final U object;
        @NotNull
        public final MaterialType material;

        MaterialHolder(@NotNull U object, @NotNull MaterialType material) {
            super(
                    Holder.stringify(object.getKeyHolder(), langTransformer(object, material, true), "_"),
                    Holder.stringify(object.getNameHolder(), langTransformer(object, material, false), " ")
            );
            this.object = object;
            this.material = material;
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
        }

        return isKey ? material.key : material.name;
    }
}
