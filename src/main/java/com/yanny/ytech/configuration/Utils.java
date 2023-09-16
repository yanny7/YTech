package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

public class Utils {
    @NotNull
    public static ResourceLocation forgeLoc(@NotNull String path) {
        return new ResourceLocation("forge", path);
    }

    @NotNull
    public static ResourceLocation mcLoc(@NotNull String path) {
        return new ResourceLocation(path);
    }

    @NotNull
    public static ResourceLocation mcBlockLoc(@NotNull String path) {
        return mcLoc(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation mcItemLoc(@NotNull String path) {
        return mcLoc(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation modLoc(@NotNull String path) {
        return  new ResourceLocation(YTechMod.MOD_ID, path);
    }

    @NotNull
    public static ResourceLocation modBlockLoc(@NotNull String path) {
        return modLoc(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation modItemLoc(@NotNull String path) {
        return modLoc(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation loc(@NotNull Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
    }

    @NotNull
    public static ResourceLocation loc(@NotNull Item item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
    }

    @NotNull
    public static ResourceLocation blockLoc(@NotNull Block block) {
        ResourceLocation loc = loc(block);
        return new ResourceLocation(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath());
    }

    @NotNull
    public static ResourceLocation blockLoc(@NotNull Item item) {
        ResourceLocation loc = loc(item);
        return new ResourceLocation(loc.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + loc.getPath());
    }

    @NotNull
    public static String getHasItem(Holder holder) {
        return "has_" + holder.key;
    }

    @NotNull
    public static String getHasName() {
        return "has_item";
    }

    @NotNull
    public static Block getLogFromMaterial(MaterialType material) {
        if (MaterialType.ALL_WOODS.contains(material)) {
            return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(material.key + "_log")));
        } else {
            throw new IllegalStateException("Not wood type provided!");
        }
    }

    @NotNull
    public static CompoundTag saveBlockPos(@NotNull BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    @NotNull
    public static BlockPos loadBlockPos(@NotNull CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> EnumSet<E> merge(@NotNull EnumSet<E> first, @NotNull EnumSet<E> ...list) {
        EnumSet<E> copy = first.clone();
        for (EnumSet<E> e : list) {
            copy.addAll(e);
        }
        return copy;
    }

    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> EnumSet<E> merge(@NotNull EnumSet<E> first, @NotNull E ...list) {
        EnumSet<E> copy = first.clone();
        copy.addAll(Arrays.asList(list));
        return copy;
    }

    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> EnumSet<E> exclude(@NotNull EnumSet<E> enumSet, @NotNull E ...list) {
        EnumSet<E> copy = enumSet.clone();
        for (E e : list) {
            copy.remove(e);
        }
        return copy;
    }

    @NotNull
    public static <E extends Enum<E>> EnumSet<E> exclude(@NotNull EnumSet<E> enumSet, @NotNull EnumSet<E> toRemove) {
        EnumSet<E> copy = enumSet.clone();
        copy.removeAll(toRemove);
        return copy;
    }

    @NotNull
    public static Tier getPreviousTier(@NotNull Tier tier) {
        int index = MaterialType.TIERS.indexOf(tier);

        if (index > 0) {
            return MaterialType.TIERS.get(index - 1);
        }

        throw new IllegalStateException("Setting invalid tier!");
    }
}
