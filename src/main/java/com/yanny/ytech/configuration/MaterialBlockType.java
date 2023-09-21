package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.DryingRack;
import com.yanny.ytech.configuration.block.ShaftBlock;
import com.yanny.ytech.configuration.block.TanningRack;
import com.yanny.ytech.configuration.block.WaterWheelBlock;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MaterialBlockType implements INameable, IMaterialModel<Holder.BlockHolder, BlockStateProvider>, ILootable<Holder.BlockHolder, BlockLootSubProvider>,
        IRecipe<Holder.BlockHolder>, IMenu, IItemTag<Holder.BlockHolder>, IBlockTag<Holder.BlockHolder> {
    STONE_ORE(HolderType.BLOCK, "stone_ore", INameable.suffix("ore"), INameable.suffix("Ore"),
            (material) -> ItemTags.create(Utils.forgeLoc("ores/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("ores/" + material.key)),
            Tags.Items.ORES,
            Tags.Blocks.ORES,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modBlockLoc("ore/" + material.key))).toArray(TextureHolder[]::new),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            (holder, provider) -> registerOreItemTag(holder, provider, Tags.Items.ORES_IN_GROUND_STONE),
            (holder, provider) -> registerOreBlockTag(holder, provider, Tags.Blocks.ORES_IN_GROUND_STONE),
            Utils.exclude(MaterialType.ALL_ORES, MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON)),
    NETHER_ORE(HolderType.BLOCK, "nether_ore", INameable.both("nether", "ore"), INameable.both("Nether", "Ore"),
            (material) -> ItemTags.create(Utils.forgeLoc("ores/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("ores/" + material.key)),
            Tags.Items.ORES,
            Tags.Blocks.ORES,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHER_GOLD_ORE)),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modBlockLoc("nether_ore/" + material.key))).toArray(TextureHolder[]::new),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            (holder, provider) -> registerOreItemTag(holder, provider, Tags.Items.ORES_IN_GROUND_NETHERRACK),
            (holder, provider) -> registerOreBlockTag(holder, provider, Tags.Blocks.ORES_IN_GROUND_NETHERRACK),
            Utils.exclude(MaterialType.ALL_ORES, MaterialType.GOLD)),
    DEEPSLATE_ORE(HolderType.BLOCK, "deepslate_ore", INameable.both("deepslate", "ore"), INameable.both("Deepslate", "Ore"),
            (material) -> ItemTags.create(Utils.forgeLoc("ores/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("ores/" + material.key)),
            Tags.Items.ORES,
            Tags.Blocks.ORES,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modBlockLoc("deepslate_ore/" + material.key))).toArray(TextureHolder[]::new),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            (holder, provider) -> registerOreItemTag(holder, provider, Tags.Items.ORES_IN_GROUND_DEEPSLATE),
            (holder, provider) -> registerOreBlockTag(holder, provider, Tags.Blocks.ORES_IN_GROUND_DEEPSLATE),
            Utils.exclude(MaterialType.ALL_ORES, MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON)),
    STORAGE_BLOCK(HolderType.BLOCK, "storage_block", INameable.suffix("block"), INameable.prefix("Block of"),
            (material) -> ItemTags.create(Utils.forgeLoc("storage_blocks/" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("storage_blocks/" + material.key)),
            Tags.Items.STORAGE_BLOCKS,
            Tags.Blocks.STORAGE_BLOCKS,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
            (material) -> basicTexture(Utils.modBlockLoc("storage_block"), material),
            MaterialBlockType::basicBlockStateProvider,
            ILootable::dropsSelfProvider,
            MaterialBlockType::registerStorageBlockRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            Utils.exclude(MaterialType.ALL_METALS, MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON)),
    RAW_STORAGE_BLOCK(HolderType.BLOCK, "raw_storage_block", INameable.both("raw", "block"), INameable.prefix("Block of Raw"),
            (material) -> ItemTags.create(Utils.forgeLoc("storage_blocks/raw_" + material.key)),
            (material) -> BlockTags.create(Utils.forgeLoc("storage_blocks/raw_" + material.key)),
            Tags.Items.STORAGE_BLOCKS,
            Tags.Blocks.STORAGE_BLOCKS,
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)),
            (material) -> basicTexture(Utils.modBlockLoc("raw_storage_block"), material),
            MaterialBlockType::basicBlockStateProvider,
            ILootable::dropsSelfProvider,
            MaterialBlockType::registerRawStorageBlockRecipe,
            MaterialBlockType::registerItemTag,
            MaterialBlockType::registerMineableBlockTag,
            Utils.exclude(MaterialType.ALL_ORES, MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON)),
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
            MaterialType.ALL_WOODS),
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
            MaterialType.ALL_WOODS),
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

    // inject vanilla item tags
    static {
        STORAGE_BLOCK.itemTag.put(MaterialType.COPPER, Tags.Items.STORAGE_BLOCKS_COPPER);
        STORAGE_BLOCK.itemTag.put(MaterialType.GOLD, Tags.Items.STORAGE_BLOCKS_GOLD);
        STORAGE_BLOCK.itemTag.put(MaterialType.IRON, Tags.Items.STORAGE_BLOCKS_IRON);

        RAW_STORAGE_BLOCK.itemTag.put(MaterialType.COPPER, Tags.Items.STORAGE_BLOCKS_RAW_COPPER);
        RAW_STORAGE_BLOCK.itemTag.put(MaterialType.GOLD, Tags.Items.STORAGE_BLOCKS_RAW_GOLD);
        RAW_STORAGE_BLOCK.itemTag.put(MaterialType.IRON, Tags.Items.STORAGE_BLOCKS_RAW_IRON);
    }

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
    @NotNull public final EnumSet<MaterialType> materials;

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

    private static void oreLootProvider(@NotNull Holder.BlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.add(holder.block.get(), (block -> provider.createOreDrop(block, Registration.item(MaterialItemType.RAW_MATERIAL, holder.material))));
    }

    private static TextureHolder[] basicTexture(ResourceLocation base, MaterialType material) {
        return List.of(new TextureHolder(0, material.color, base)).toArray(TextureHolder[]::new);
    }

    private static void registerRawStorageBlockRecipe(Holder.BlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('#', MaterialItemType.RAW_MATERIAL.itemTag.get(holder.material))
                .pattern("###").pattern("###").pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.RAW_MATERIAL.itemTag.get(holder.material)))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
    }

    private static void registerStorageBlockRecipe(Holder.BlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('#', MaterialItemType.INGOT.itemTag.get(holder.material))
                .pattern("###").pattern("###").pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.INGOT.itemTag.get(holder.material)))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
    }

    private static void registerItemTag(@NotNull Holder.BlockHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag.get(holder.material)).add(holder.block.get().asItem());
        provider.tag(holder.object.groupItemTag).addTag(holder.object.itemTag.get(holder.material));
    }

    private static void registerBlockTag(@NotNull Holder.BlockHolder holder, @NotNull BlockTagsProvider provider) {
        provider.tag(holder.object.blockTag.get(holder.material)).add(holder.block.get());
        provider.tag(holder.object.groupBlockTag).addTag(holder.object.blockTag.get(holder.material));

        if (holder.material.getTier().getTag() != null) {
            provider.tag(holder.material.getTier().getTag()).add(holder.block.get());
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

        // ore must be mineable with lesser tier as material tier
        TagKey<Block> tierTag = Utils.getPreviousTier(holder.material.getTier()).getTag();

        if (tierTag != null) {
            provider.tag(tierTag).add(holder.block.get());
        }
    }
}
