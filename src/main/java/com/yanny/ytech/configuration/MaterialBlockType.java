package com.yanny.ytech.configuration;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.DryingRack;
import com.yanny.ytech.configuration.block.ShaftBlock;
import com.yanny.ytech.configuration.block.WaterWheelBlock;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.yanny.ytech.registration.Registration.HOLDER;

public enum MaterialBlockType implements INameable, IMaterialModel<Holder.BlockHolder, BlockStateProvider>,
        ILootable<Holder.BlockHolder, BlockLootSubProvider>, IRecipe<Holder.BlockHolder> {
    STONE_ORE(HolderType.BLOCK, "stone_ore", INameable.both("stone", "ore"), INameable.suffix("Ore"),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)),
            (material) -> oreTexture(IModel.mcBlockLoc("stone")),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    NETHERRACK_ORE(HolderType.BLOCK, "netherrack_ore", INameable.both("netherrack", "ore"), INameable.both("Netherrack", "Ore"),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)),
            (material) -> oreTexture(IModel.mcBlockLoc("netherrack")),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DEEPSLATE_ORE(HolderType.BLOCK, "deepslate_ore", INameable.both("deepslate", "ore"), INameable.both("Deepslate", "Ore"),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)),
            (material) -> oreTexture(IModel.mcBlockLoc("deepslate")),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            IRecipe::noRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    STORAGE_BLOCK(HolderType.BLOCK, "storage_block", INameable.suffix("block"), INameable.prefix("Block of"),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
            (material) -> basicTexture(IModel.modBlockLoc("storage_block")),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::dropsSelfProvider,
            MaterialBlockType::storageBlockRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    RAW_STORAGE_BLOCK(HolderType.BLOCK, "raw_storage_block", INameable.both("raw", "block"), INameable.both("Raw", "Block"),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)),
            (material) -> basicTexture(IModel.modBlockLoc("raw_storage_block")),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::dropsSelfProvider,
            MaterialBlockType::rawStorageBlockRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DRYING_RACK(HolderType.BLOCK, "drying_rack", INameable.suffix("drying_rack"), INameable.suffix("Drying Rack"),
            (holder) -> new DryingRack(),
            DryingRack::getTexture,
            DryingRack::registerModel,
            MaterialBlockType::dropsSelfProvider,
            DryingRack::registerRecipe,
            EnumSet.of(MaterialType.ACACIA_WOOD, MaterialType.OAK_WOOD)),
    SHAFT(HolderType.ENTITY_BLOCK, "shaft", INameable.suffix("shaft"), INameable.suffix("Shaft"),
            (holder) -> new ShaftBlock(holder.material, 2f),
            ShaftBlock::getTexture,
            ShaftBlock::registerModel,
            MaterialBlockType::dropsSelfProvider,
            ShaftBlock::registerRecipe,
            EnumSet.of(MaterialType.OAK_WOOD)),
    WATER_WHEEL(HolderType.ENTITY_BLOCK, "water_wheel", INameable.suffix("water_wheel"), INameable.suffix("Water Wheel"),
            (holder) -> new WaterWheelBlock(holder.material, 0.2f),
            WaterWheelBlock::getTexture,
            WaterWheelBlock::registerModel,
            MaterialBlockType::dropsSelfProvider,
            WaterWheelBlock::registerRecipe,
            EnumSet.of(MaterialType.OAK_WOOD))
    ;

    @NotNull public final HolderType type;
    @NotNull public final String id;
    @NotNull private final NameHolder keyHolder;
    @NotNull private final NameHolder nameHolder;
    @NotNull private final Function<Holder.BlockHolder, Block> blockGetter;
    @NotNull private final HashSet<Integer> tintIndices;
    @NotNull private final HashMap<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialBlockType(@NotNull HolderType type, @NotNull String id, @NotNull NameHolder keyHolder, @NotNull NameHolder nameHolder,
                      @NotNull Function<Holder.BlockHolder, Block> blockGetter, @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                      @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.keyHolder = keyHolder;
        this.nameHolder = nameHolder;
        this.blockGetter = blockGetter;
        this.type = type;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.materials = materials;
        this.tintIndices = new HashSet<>();
        this.textures = new HashMap<>();

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    this.tintIndices.add(holder.tintIndex());
                }
                resources.add(holder.texture());
            }

            this.textures.computeIfAbsent(material, (m) -> resources.toArray(ResourceLocation[]::new));
        }
    }

    public enum HolderType {
        BLOCK,
        ENTITY_BLOCK,
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
    public Set<Integer> getTintIndices() {
        return tintIndices;
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

    private static void dropsSelfProvider(@NotNull Holder.BlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.dropSelf(holder.block.get());
    }

    private static void oreLootProvider(@NotNull Holder.BlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.add(holder.block.get(), (block -> provider.createOreDrop(block, GeneralUtils.getFromMap(HOLDER.items(),
                MaterialItemType.RAW_MATERIAL, holder.material).item.get())));
    }

    private static TextureHolder[] basicTexture(ResourceLocation base) {
        return List.of(new TextureHolder(0, base)).toArray(TextureHolder[]::new);
    }

    private static TextureHolder[] oreTexture(ResourceLocation base) {
        return List.of(new TextureHolder(-1, base), new TextureHolder(0, IModel.modBlockLoc("ore_overlay"))).toArray(TextureHolder[]::new);
    }

    private static void rawStorageBlockRecipe(Holder.BlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.RAW_MATERIAL, holder.material).item;
        TagKey<Item> unpackedTag = Registration.FORGE_RAW_MATERIAL_TAGS.get(holder.material);
        TagKey<Item> packedTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(holder.material).item();
        nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
    }

    private static void storageBlockRecipe(Holder.BlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.INGOT, holder.material).item;
        TagKey<Item> unpackedTag = Registration.FORGE_INGOT_TAGS.get(holder.material);
        TagKey<Item> packedTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(holder.material).item();
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

}
