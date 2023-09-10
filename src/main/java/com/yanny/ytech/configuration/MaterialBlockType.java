package com.yanny.ytech.configuration;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.DryingRack;
import com.yanny.ytech.configuration.block.ShaftBlock;
import com.yanny.ytech.configuration.block.TanningRack;
import com.yanny.ytech.configuration.block.WaterWheelBlock;
import com.yanny.ytech.registration.Holder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanny.ytech.registration.Registration.HOLDER;

public enum MaterialBlockType implements INameable, IMaterialModel<Holder.BlockHolder, BlockStateProvider>, ILootable<Holder.BlockHolder, BlockLootSubProvider>,
        IRecipe<Holder.BlockHolder>, IMenu, IItemTag<Holder.BlockHolder>, IBlockTag<Holder.BlockHolder> {
    STONE_ORE(HolderType.BLOCK, "stone_ore", INameable.both("stone", "ore"), INameable.suffix("Ore"),
            (material) -> ItemTags.create(Utils.forgeLoc("ores/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("ores/" + material.key)),
            Tags.Items.ORES,
            Tags.Blocks.ORES,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)),
            (material) -> oreTexture(Utils.mcBlockLoc("stone"), material),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            (holder, provider) -> registerOreItemTag(holder, provider, Tags.Items.ORES_IN_GROUND_STONE),
            (holder, provider) -> registerOreBlockTag(holder, provider, Tags.Blocks.ORES_IN_GROUND_STONE),
            EnumSet.noneOf(MaterialType.class)),
    NETHERRACK_ORE(HolderType.BLOCK, "netherrack_ore", INameable.both("netherrack", "ore"), INameable.both("Netherrack", "Ore"),
            (material) -> ItemTags.create(Utils.forgeLoc("ores/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("ores/" + material.key)),
            Tags.Items.ORES,
            Tags.Blocks.ORES,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)),
            (material) -> oreTexture(Utils.mcBlockLoc("netherrack"), material),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            (holder, provider) -> registerOreItemTag(holder, provider, Tags.Items.ORES_IN_GROUND_NETHERRACK),
            (holder, provider) -> registerOreBlockTag(holder, provider, Tags.Blocks.ORES_IN_GROUND_NETHERRACK),
            EnumSet.noneOf(MaterialType.class)),
    DEEPSLATE_ORE(HolderType.BLOCK, "deepslate_ore", INameable.both("deepslate", "ore"), INameable.both("Deepslate", "Ore"),
            (material) -> ItemTags.create(Utils.forgeLoc("ores/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("ores/" + material.key)),
            Tags.Items.ORES,
            Tags.Blocks.ORES,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)),
            (material) -> oreTexture(Utils.mcBlockLoc("deepslate"), material),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            (holder, provider) -> registerOreItemTag(holder, provider, Tags.Items.ORES_IN_GROUND_DEEPSLATE),
            (holder, provider) -> registerOreBlockTag(holder, provider, Tags.Blocks.ORES_IN_GROUND_DEEPSLATE),
            EnumSet.noneOf(MaterialType.class)),
    STORAGE_BLOCK(HolderType.BLOCK, "storage_block", INameable.suffix("block"), INameable.prefix("Block of"),
            (material) -> ItemTags.create(Utils.forgeLoc("storage_blocks/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("storage_blocks/" + material.key)),
            Tags.Items.STORAGE_BLOCKS,
            Tags.Blocks.STORAGE_BLOCKS,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
            (material) -> basicTexture(Utils.modBlockLoc("storage_block"), material),
            MaterialBlockType::basicBlockStateProvider,
            ILootable::dropsSelfProvider,
            MaterialBlockType::storageBlockRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            EnumSet.of(MaterialType.ARSENICAL_BRONZE)),
    RAW_STORAGE_BLOCK(HolderType.BLOCK, "raw_storage_block", INameable.both("raw", "block"), INameable.both("Raw", "Block"),
            (material) -> ItemTags.create(Utils.forgeLoc("storage_blocks/raw_" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("storage_blocks/raw_" + material.key)),
            Tags.Items.STORAGE_BLOCKS,
            Tags.Blocks.STORAGE_BLOCKS,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)),
            (material) -> basicTexture(Utils.modBlockLoc("raw_storage_block"), material),
            MaterialBlockType::basicBlockStateProvider,
            ILootable::dropsSelfProvider,
            MaterialBlockType::rawStorageBlockRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            EnumSet.noneOf(MaterialType.class)),
    DRYING_RACK(HolderType.ENTITY_BLOCK, "drying_rack", INameable.suffix("drying_rack"), INameable.suffix("Drying Rack"),
            (material) -> ItemTags.create(Utils.modLoc("drying_racks/" + material.key)),
            (material) -> BlockTags.create(Utils.modLoc("drying_racks/" + material.key)),
            ItemTags.create(Utils.modLoc("drying_racks")),
            BlockTags.create(Utils.modLoc("drying_racks")),
            DryingRack::new,
            DryingRack::getTexture,
            DryingRack::registerModel,
            ILootable::dropsSelfProvider,
            DryingRack::registerRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            MaterialType.ALL_WOOD),
    TANNING_RACK(HolderType.ENTITY_BLOCK, "tanning_rack", INameable.suffix("tanning_rack"), INameable.suffix("Tanning Rack"),
            (material) -> ItemTags.create(Utils.modLoc("tanning_racks/" + material.key)),
            (material) -> BlockTags.create(Utils.modLoc("tanning_racks/" + material.key)),
            ItemTags.create(Utils.modLoc("tanning_racks")),
            BlockTags.create(Utils.modLoc("tanning_racks")),
            TanningRack::new,
            TanningRack::getTexture,
            TanningRack::registerModel,
            ILootable::dropsSelfProvider,
            TanningRack::registerRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            MaterialType.ALL_WOOD),
    SHAFT(HolderType.ENTITY_BLOCK, "shaft", INameable.suffix("shaft"), INameable.suffix("Shaft"),
            (material) -> ItemTags.create(Utils.modLoc("shafts/" + material.key)),
            (material) -> BlockTags.create(Utils.modLoc("shafts/" + material.key)),
            ItemTags.create(Utils.modLoc("shafts")),
            BlockTags.create(Utils.modLoc("shafts")),
            (holder) -> new ShaftBlock(holder.material, 2f),
            ShaftBlock::getTexture,
            ShaftBlock::registerModel,
            ILootable::dropsSelfProvider,
            ShaftBlock::registerRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            EnumSet.noneOf(MaterialType.class)),
    WATER_WHEEL(HolderType.ENTITY_BLOCK, "water_wheel", INameable.suffix("water_wheel"), INameable.suffix("Water Wheel"),
            (material) -> ItemTags.create(Utils.modLoc("water_wheels/" + material.key)),
            (material) -> BlockTags.create(Utils.modLoc("water_wheels/" + material.key)),
            ItemTags.create(Utils.modLoc("water_wheels")),
            BlockTags.create(Utils.modLoc("water_wheels")),
            (holder) -> new WaterWheelBlock(holder.material, 0.2f),
            WaterWheelBlock::getTexture,
            WaterWheelBlock::registerModel,
            ILootable::dropsSelfProvider,
            WaterWheelBlock::registerRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            EnumSet.noneOf(MaterialType.class)),
    ;

    @NotNull public final HolderType type;
    @NotNull public final String id;
    @NotNull private final NameHolder keyHolder;
    @NotNull private final NameHolder nameHolder;
    @NotNull public final Map<MaterialType, TagKey<Item>> itemTag;
    @NotNull public final Map<MaterialType, TagKey<Block>> blockTag;
    @NotNull public final TagKey<Item> groupItemTag;
    @NotNull public final TagKey<Block> groupBlockTag;
    @NotNull private final Function<Holder.BlockHolder, Block> blockGetter;
    @NotNull private final Map<MaterialType, Map<Integer, Integer>> tintColors;
    @NotNull private final Map<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, ItemTagsProvider> itemTagsGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockTagsProvider> blockTagsGetter;
    @Nullable private final IAbstractMenuGetter menuGetter;
    @Nullable private final IScreenGetter screenGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialBlockType(@NotNull HolderType type, @NotNull String id, @NotNull NameHolder keyHolder, @NotNull NameHolder nameHolder,
                      @NotNull Function<Holder.BlockHolder, Block> blockGetter, @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                      @NotNull Function<MaterialType, TagKey<Item>> itemTag, @NotNull Function<MaterialType, TagKey<Block>> blockTag, @NotNull TagKey<Item> groupItemTag,
                      @NotNull TagKey<Block> groupBlockTag, @NotNull BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockTagsProvider> blockTagsGetter, @NotNull IAbstractMenuGetter menuGetter,
                      @NotNull IScreenGetter screenGetter, @NotNull BiConsumer<Holder.BlockHolder, ItemTagsProvider> itemTagsGetter,
                      @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.keyHolder = keyHolder;
        this.nameHolder = nameHolder;
        this.itemTag = materials.stream().map((material) -> Pair.of(material, itemTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.blockTag = materials.stream().map((material) -> Pair.of(material, blockTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.blockGetter = blockGetter;
        this.type = type;
        this.groupItemTag = groupItemTag;
        this.groupBlockTag = groupBlockTag;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.blockTagsGetter = blockTagsGetter;
        this.menuGetter = menuGetter;
        this.screenGetter = screenGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.materials = materials;
        this.tintColors = new HashMap<>();
        this.textures = new HashMap<>();

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();
            Map<Integer, Integer> tintMap = new HashMap<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    tintMap.put(holder.tintIndex(), holder.color());
                }

                resources.add(holder.texture());
            }

            this.tintColors.put(material, tintMap);
            this.textures.computeIfAbsent(material, (m) -> resources.toArray(ResourceLocation[]::new));
        }
    }

    MaterialBlockType(@NotNull HolderType type, @NotNull String id, @NotNull NameHolder keyHolder, @NotNull NameHolder nameHolder,
                      @NotNull Function<MaterialType, TagKey<Item>> itemTag, @NotNull Function<MaterialType, TagKey<Block>> blockTag,
                      @NotNull TagKey<Item> groupItemTag, @NotNull TagKey<Block> groupBlockTag, @NotNull Function<Holder.BlockHolder, Block> blockGetter,
                      @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, ItemTagsProvider> itemTagsGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockTagsProvider> blockTagsGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.keyHolder = keyHolder;
        this.nameHolder = nameHolder;
        this.itemTag = materials.stream().map((material) -> Pair.of(material, itemTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.blockTag = materials.stream().map((material) -> Pair.of(material, blockTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.blockTagsGetter = blockTagsGetter;
        this.groupItemTag = groupItemTag;
        this.groupBlockTag = groupBlockTag;
        this.blockGetter = blockGetter;
        this.type = type;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.menuGetter = null;
        this.screenGetter = null;
        this.materials = materials;
        this.tintColors = new HashMap<>();
        this.textures = new HashMap<>();

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();
            Map<Integer, Integer> tintMap = new HashMap<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    tintMap.put(holder.tintIndex(), holder.color());
                }

                resources.add(holder.texture());
            }

            this.tintColors.put(material, tintMap);
            this.textures.computeIfAbsent(material, (m) -> resources.toArray(ResourceLocation[]::new));
        }
    }

    @NotNull
    @Override
    public NameHolder getKeyHolder() {
        return keyHolder;
    }

    @NotNull
    @Override
    public NameHolder getNameHolder() {
        return nameHolder;
    }

    @NotNull
    @Override
    public Map<Integer, Integer> getTintColors(@NotNull MaterialType material) {
        return tintColors.get(material);
    }

    @NotNull
    @Override
    public ResourceLocation[] getTextures(@NotNull MaterialType material) {
        return textures.get(material);
    }

    @Override
    public void registerModel(@NotNull Holder.BlockHolder holder, @NotNull BlockStateProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerLoot(@NotNull Holder.BlockHolder holder, @NotNull BlockLootSubProvider provider) {
        lootGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.BlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    @Override
    public void registerTag(@NotNull Holder.BlockHolder holder, @NotNull ItemTagsProvider provider) {
        itemTagsGetter.accept(holder, provider);
    }

    @Override
    public void registerTag(@NotNull Holder.BlockHolder holder, @NotNull BlockTagsProvider provider) {
        blockTagsGetter.accept(holder, provider);
    }

    @Override
    @Nullable
    public AbstractContainerMenu getContainerMenu(@NotNull Holder holder, int windowId, @NotNull Inventory inv, @NotNull BlockPos pos,
                                                  @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data) {
        if (menuGetter != null) {
            return menuGetter.getMenu(holder, windowId, inv, pos, itemStackHandler, data);
        } else {
            return null;
        }
    }

    @Override
    @NotNull
    public AbstractContainerScreen<AbstractContainerMenu> getScreen(@NotNull AbstractContainerMenu container, @NotNull Inventory inventory, @NotNull Component title) {
        if (screenGetter != null) {
            //noinspection unchecked
            return (AbstractContainerScreen<AbstractContainerMenu>) screenGetter.getScreen(container, inventory, title);
        } else {
            throw new IllegalStateException("Missing screen getter");
        }
    }

    public Block getBlock(@NotNull Holder.BlockHolder holder) {
        return blockGetter.apply(holder);
    }

    private static void basicBlockStateProvider(@NotNull Holder.BlockHolder holder, @NotNull BlockStateProvider provider) {
            ResourceLocation[] textures = holder.object.getTextures(holder.material);
            BlockModelBuilder model = provider.models().cubeAll(holder.key, textures[0]);

            model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction).tintindex(0));
            provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
            provider.itemModels().getBuilder(holder.key).parent(model);
    }

    private static void oreBlockStateProvider(@NotNull Holder.BlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        BlockModelBuilder model = provider.models().cubeAll(holder.key, textures[0]);

        model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction));
        model.renderType(provider.mcLoc("cutout"));
        model.texture("overlay", textures[1]);
        model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#overlay").cullface(direction).tintindex(0));
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    private static void oreLootProvider(@NotNull Holder.BlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.add(holder.block.get(), (block -> provider.createOreDrop(block, GeneralUtils.getFromMap(HOLDER.items(),
                MaterialItemType.RAW_MATERIAL, holder.material).item.get())));
    }

    private static TextureHolder[] basicTexture(ResourceLocation base, MaterialType material) {
        return List.of(new TextureHolder(0, material.color, base)).toArray(TextureHolder[]::new);
    }

    private static TextureHolder[] oreTexture(ResourceLocation base, MaterialType material) {
        return List.of(new TextureHolder(-1, -1, base),
                new TextureHolder(0, material.color, Utils.modBlockLoc("ore_overlay"))).toArray(TextureHolder[]::new);
    }

    private static void rawStorageBlockRecipe(Holder.BlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.RAW_MATERIAL, holder.material).item;
        TagKey<Item> unpackedTag = MaterialItemType.RAW_MATERIAL.itemTag.get(holder.material);
        TagKey<Item> packedTag = MaterialBlockType.RAW_STORAGE_BLOCK.itemTag.get(holder.material);
        nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
    }

    private static void storageBlockRecipe(Holder.BlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.INGOT, holder.material).item;
        TagKey<Item> unpackedTag = MaterialItemType.INGOT.itemTag.get(holder.material);
        TagKey<Item> packedTag = MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material);
        nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> recipeConsumer, RecipeCategory unpackedCategory,
                                                RegistryObject<?> unpacked, TagKey<Item> unpackedTag, RecipeCategory packedCategory,
                                                RegistryObject<?> packed, TagKey<Item> packedTag) {
        String unpackedPath = unpacked.getId().getPath();
        String packedPath = packed.getId().getPath();
        String unpackedName = unpackedPath + "_to_" + packedPath;
        String packedName = packedPath + "_to_" + unpackedPath;
        nineBlockStorageRecipes(recipeConsumer, unpackedCategory, (ItemLike) unpacked.get(), unpackedTag, packedCategory,
                (ItemLike) packed.get(), packedTag, packedName, packedPath, unpackedName, unpackedPath);
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> recipeConsumer, RecipeCategory unpackedCategory,
                                                ItemLike unpacked, TagKey<Item> unpackedTag, RecipeCategory packedCategory,
                                                ItemLike packed, TagKey<Item> packedTag, String packedName, @Nullable String packedGroup,
                                                String unpackedName, @Nullable String unpackedGroup) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9)
                .requires(packedTag)
                .group(unpackedGroup)
                .unlockedBy(RecipeProvider.getHasName(packed), RecipeProvider.has(packedTag))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('#', unpackedTag)
                .pattern("###").pattern("###").pattern("###")
                .group(packedGroup).unlockedBy(RecipeProvider.getHasName(unpacked), RecipeProvider.has(unpackedTag))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, packedName));
    }

    private static void registerItemTag(@NotNull Holder.BlockHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag.get(holder.material)).add(holder.block.get().asItem());
        provider.tag(holder.object.groupItemTag).addTag(holder.object.itemTag.get(holder.material));
    }

    private static void registerBlockTag(@NotNull Holder.BlockHolder holder, @NotNull BlockTagsProvider provider) {
        provider.tag(holder.object.blockTag.get(holder.material)).add(holder.block.get());
        provider.tag(holder.object.groupBlockTag).addTag(holder.object.blockTag.get(holder.material));

        if (holder.material.tier.getTag() != null) {
            provider.tag(holder.material.tier.getTag()).add(holder.block.get());
        }
    }

    private static void registerMineableBlockTag(@NotNull Holder.BlockHolder holder, @NotNull BlockTagsProvider provider) {
        registerBlockTag(holder, provider);

        switch (holder.material.tool) {
            case PICKAXE -> provider.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(holder.block.get());
            case AXE -> provider.tag(BlockTags.MINEABLE_WITH_AXE).add(holder.block.get());
            case HOE -> provider.tag(BlockTags.MINEABLE_WITH_HOE).add(holder.block.get());
            case SHOVEL -> provider.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(holder.block.get());
        }
    }

    private static void registerOreItemTag(@NotNull Holder.BlockHolder holder, @NotNull ItemTagsProvider provider, @NotNull TagKey<Item> baseStone) {
        provider.tag(holder.object.itemTag.get(holder.material)).add(holder.block.get().asItem());
        provider.tag(holder.object.groupItemTag).add(holder.block.get().asItem());
        provider.tag(baseStone).add(holder.block.get().asItem());

        switch (holder.material) {
            case IRON -> provider.tag(ItemTags.IRON_ORES).add(holder.block.get().asItem());
            case COPPER -> provider.tag(ItemTags.COPPER_ORES).add(holder.block.get().asItem());
            case GOLD -> provider.tag(ItemTags.GOLD_ORES).add(holder.block.get().asItem());
        }
    }

    private static void registerOreBlockTag(@NotNull Holder.BlockHolder holder, @NotNull BlockTagsProvider provider, @NotNull TagKey<Block> baseStone) {
        provider.tag(holder.object.blockTag.get(holder.material)).add(holder.block.get());
        provider.tag(holder.object.groupBlockTag).add(holder.block.get());
        provider.tag(baseStone).add(holder.block.get());
        provider.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(holder.block.get());

        switch (holder.material) {
            case IRON -> provider.tag(BlockTags.IRON_ORES).add(holder.block.get());
            case COPPER -> provider.tag(BlockTags.COPPER_ORES).add(holder.block.get());
            case GOLD -> provider.tag(BlockTags.GOLD_ORES).add(holder.block.get());
        }

        if (holder.material.tier.getTag() != null) {
            provider.tag(holder.material.tier.getTag()).add(holder.block.get());
        }
    }
}
