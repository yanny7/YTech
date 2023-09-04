package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.configuration.container.CrusherContainerMenu;
import com.yanny.ytech.configuration.container.FurnaceContainerMenu;
import com.yanny.ytech.configuration.screen.BaseScreen;
import com.yanny.ytech.configuration.screen.CrusherScreen;
import com.yanny.ytech.configuration.screen.FurnaceScreen;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public enum SimpleBlockType implements ISimpleModel<Holder.SimpleBlockHolder, BlockStateProvider>, ILootable<Holder.SimpleBlockHolder, BlockLootSubProvider>,
        IRecipe<Holder.SimpleBlockHolder>, IMenu, IItemTag<Holder.SimpleBlockHolder>, IBlockTag<Holder.SimpleBlockHolder> {
    MILLSTONE(HolderType.ENTITY_BLOCK, "millstone", "Millstone",
            ItemTags.create(Utils.modLoc("millstones")),
            BlockTags.create(Utils.modLoc("millstones")),
            MillstoneBlock::new,
            MillstoneBlock::textureHolder,
            MillstoneBlock::registerModel,
            ILootable::dropsSelfProvider,
            MillstoneBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag),
    STONE_FURNACE(HolderType.MENU_BLOCK, "stone_furnace", "Stone Furnace",
            ItemTags.create(Utils.modLoc("furnaces")),
            BlockTags.create(Utils.modLoc("furnaces")),
            FurnaceBlock::new,
            () -> MachineBlock.getTexture("stone", "furnace"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            FurnaceBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos) -> new FurnaceContainerMenu(holder, windowId, inv.player, pos),
            FurnaceScreen::new),
    STEAM_FURNACE(HolderType.MENU_BLOCK, "steam_furnace", "Steam Furnace",
            ItemTags.create(Utils.modLoc("furnaces")),
            BlockTags.create(Utils.modLoc("furnaces")),
            FurnaceBlock::new,
            () -> MachineBlock.getTexture("steam", "furnace"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            FurnaceBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos) -> new FurnaceContainerMenu(holder, windowId, inv.player, pos),
            FurnaceScreen::new),
    STONE_CRUSHER(HolderType.MENU_BLOCK, "stone_crusher", "Stone Crusher",
            ItemTags.create(Utils.modLoc("crushers")),
            BlockTags.create(Utils.modLoc("crushers")),
            StoneCrusherBlock::new,
            () -> MachineBlock.getTexture("stone", "crusher"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            StoneCrusherBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos) -> new CrusherContainerMenu(holder, windowId, inv.player, pos),
            CrusherScreen::new),
    STEAM_CRUSHER(HolderType.MENU_BLOCK, "steam_crusher", "Steam Crusher",
            ItemTags.create(Utils.modLoc("crushers")),
            BlockTags.create(Utils.modLoc("crushers")),
            CrusherBlock::new,
            () -> MachineBlock.getTexture("steam", "crusher"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            CrusherBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos) -> new CrusherContainerMenu(holder, windowId, inv.player, pos),
            CrusherScreen::new),
    ;

    @NotNull public final HolderType type;
    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TagKey<Item> itemTag;
    @NotNull public final TagKey<Block> blockTag;
    @NotNull private final Function<Holder.SimpleBlockHolder, Block> blockGetter;
    @NotNull private final HashMap<Integer, Integer> tintColors;
    @NotNull private final ResourceLocation[] textures;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, ItemTagsProvider> itemTagsGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockTagsProvider> blockTagsGetter;
    @Nullable private final IAbstractMenuGetter menuGetter;
    @Nullable private final IScreenGetter screenGetter;

    SimpleBlockType(@NotNull HolderType type, @NotNull String key, @NotNull String name, @NotNull TagKey<Item> itemTag, @NotNull TagKey<Block> blockTag,
                    @NotNull Function<Holder.SimpleBlockHolder, Block> blockGetter, @NotNull Supplier<TextureHolder[]> textureGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, ItemTagsProvider> itemTagsGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockTagsProvider> blockTagsGetter) {
        this.type = type;
        this.key = key;
        this.name = name;
        this.blockGetter = blockGetter;
        this.itemTag = itemTag;
        this.blockTag = blockTag;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.blockTagsGetter = blockTagsGetter;
        this.menuGetter = null;
        this.screenGetter = null;
        this.tintColors = new HashMap<>();

        TextureHolder[] holders = textureGetter.get();
        ArrayList<ResourceLocation> resources = new ArrayList<>();

        for (TextureHolder holder : holders) {
            if (holder.tintIndex() >= 0) {
                this.tintColors.put(holder.tintIndex(), holder.color());
            }
            resources.add(holder.texture());
        }

        this.textures = resources.toArray(ResourceLocation[]::new);
    }

    SimpleBlockType(@NotNull HolderType type, @NotNull String key, @NotNull String name, @NotNull TagKey<Item> itemTag, @NotNull TagKey<Block> blockTag,
                    @NotNull Function<Holder.SimpleBlockHolder, Block> blockGetter, @NotNull Supplier<TextureHolder[]> textureGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, ItemTagsProvider> itemTagsGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockTagsProvider> blockTagsGetter,
                    @NotNull IAbstractMenuGetter menuGetter, @NotNull IScreenGetter screenGetter) {
        this.type = type;
        this.key = key;
        this.name = name;
        this.itemTag = itemTag;
        this.blockTag = blockTag;
        this.blockGetter = blockGetter;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.blockTagsGetter = blockTagsGetter;
        this.menuGetter = menuGetter;
        this.screenGetter = screenGetter;
        this.tintColors = new HashMap<>();

        TextureHolder[] holders = textureGetter.get();
        ArrayList<ResourceLocation> resources = new ArrayList<>();

        for (TextureHolder holder : holders) {
            if (holder.tintIndex() >= 0) {
                this.tintColors.put(holder.tintIndex(), holder.color());
            }
            resources.add(holder.texture());
        }

        this.textures = resources.toArray(ResourceLocation[]::new);
    }

    @Override
    public void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerLoot(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockLootSubProvider provider) {
        lootGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    @Override
    public void registerTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull ItemTagsProvider provider) {
        itemTagsGetter.accept(holder, provider);
    }

    @Override
    public void registerTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockTagsProvider provider) {
        blockTagsGetter.accept(holder, provider);
    }

    @NotNull
    @Override
    public Map<Integer, Integer> getTintColors() {
        return tintColors;
    }

    @NotNull
    @Override
    public ResourceLocation[] getTextures() {
        return textures;
    }

    @Override
    @Nullable
    public AbstractContainerMenu getContainerMenu(Holder holder, int windowId, Inventory inv, BlockPos data) {
        if (menuGetter != null) {
            return menuGetter.getMenu(holder, windowId, inv, data);
        } else {
            return null;
        }
    }

    @Override
    @NotNull
    public BaseScreen getScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        if (screenGetter != null) {
            return screenGetter.getScreen(container, inventory, title);
        } else {
            throw new IllegalStateException("Missing screen getter");
        }
    }

    public Block getBlock(@NotNull Holder.SimpleBlockHolder holder) {
        return blockGetter.apply(holder);
    }

    private static void dropsSelfProvider(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.dropSelf(holder.block.get());
    }

    private static void registerItemTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag).add(holder.block.get().asItem());
    }

    private static void registerBlockTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockTagsProvider provider) {
        provider.tag(holder.object.blockTag).add(holder.block.get());
    }
}
