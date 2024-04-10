package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.configuration.block_entity.AbstractPrimitiveMachineBlockEntity;
import com.yanny.ytech.configuration.container.AqueductFertilizerMenu;
import com.yanny.ytech.configuration.container.PrimitiveAlloySmelterContainerMenu;
import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.yanny.ytech.YTechMod.CONFIGURATION;
import static net.minecraft.ChatFormatting.DARK_GRAY;

public enum SimpleBlockType implements ISimpleModel<Holder.SimpleBlockHolder, BlockStateProvider>, ILootable<Holder.SimpleBlockHolder, BlockLootSubProvider>,
        IRecipe<Holder.SimpleBlockHolder>, IMenu, IItemTag<Holder.SimpleBlockHolder>, IBlockTag<Holder.SimpleBlockHolder> {
    BRONZE_ANVIL(HolderType.ENTITY_BLOCK, "bronze_anvil", "Bronze Anvil",
            ItemTags.ANVIL,
            BlockTags.ANVIL,
            BronzeAnvilBlock::new,
            SimpleBlockType::simpleBlockItem,
            BronzeAnvilBlock::textureHolder,
            BronzeAnvilBlock::registerModel,
            ILootable::dropsSelfProvider,
            BronzeAnvilBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    MILLSTONE(HolderType.ENTITY_BLOCK, "millstone", "Millstone",
            ItemTags.create(Utils.modLoc("millstones")),
            BlockTags.create(Utils.modLoc("millstones")),
            MillstoneBlock::new,
            (holder) -> SimpleBlockType.simpleDescrBlockItem(holder, List.of(Component.translatable("text.ytech.hover.millstone").withStyle(DARK_GRAY))),
            MillstoneBlock::textureHolder,
            MillstoneBlock::registerModel,
            ILootable::dropsSelfProvider,
            MillstoneBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    PRIMITIVE_SMELTER(HolderType.MENU_BLOCK, "primitive_smelter", "Primitive Smelter",
            ItemTags.create(Utils.modLoc("primitive_smelters")),
            BlockTags.create(Utils.modLoc("primitive_smelters")),
            PrimitiveSmelterBlock::new,
            (holder) -> SimpleBlockType.simpleDescrBlockItem(holder, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))),
            PrimitiveSmelterBlock::textureHolder,
            PrimitiveSmelterBlock::registerModel,
            ILootable::dropsSelfProvider,
            PrimitiveSmelterBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new PrimitiveSmelterContainerMenu(holder, windowId, inv.player, pos, stack, data)),
    PRIMITIVE_ALLOY_SMELTER(HolderType.MENU_BLOCK, "primitive_alloy_smelter", "Primitive Alloy Smelter",
            ItemTags.create(Utils.modLoc("primitive_alloy_smelters")),
            BlockTags.create(Utils.modLoc("primitive_alloy_smelters")),
            PrimitiveAlloySmelterBlock::new,
            (holder) -> SimpleBlockType.simpleDescrBlockItem(holder, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))),
            PrimitiveAlloySmelterBlock::textureHolder,
            PrimitiveAlloySmelterBlock::registerModel,
            ILootable::dropsSelfProvider,
            PrimitiveAlloySmelterBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new PrimitiveAlloySmelterContainerMenu(holder, windowId, inv.player, pos, stack, data)),
    BRICK_CHIMNEY(HolderType.ENTITY_BLOCK, "brick_chimney", "Brick Chimney",
            ItemTags.create(Utils.modLoc("brick_chimneys")),
            BlockTags.create(Utils.modLoc("brick_chimneys")),
            BrickChimneyBlock::new,
            (holder) -> SimpleBlockType.simpleDescrBlockItem(holder,
                    List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))),
            BrickChimneyBlock::getTexture,
            BrickChimneyBlock::registerModel,
            ILootable::dropsSelfProvider,
            BrickChimneyBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    REINFORCED_BRICKS(HolderType.BLOCK, "reinforced_bricks", "Reinforced Bricks",
            ItemTags.create(Utils.modLoc("reinforced_bricks")),
            BlockTags.create(Utils.modLoc("reinforced_bricks")),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)),
            SimpleBlockType::simpleBlockItem,
            () -> bottomTopTexture(Utils.modBlockLoc("reinforced_bricks"), Utils.blockLoc(Blocks.BRICKS), Utils.blockLoc(Blocks.BRICKS)),
            SimpleBlockType::bottomTopBlockStateProvider,
            ILootable::dropsSelfProvider,
            SimpleBlockType::registerReinforcedBricksRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    REINFORCED_BRICK_CHIMNEY(HolderType.ENTITY_BLOCK, "reinforced_brick_chimney", "Reinforced Brick Chimney",
            ItemTags.create(Utils.modLoc("reinforced_brick_chimneys")),
            BlockTags.create(Utils.modLoc("reinforced_brick_chimneys")),
            ReinforcedBrickChimneyBlock::new,
            (holder) -> SimpleBlockType.simpleDescrBlockItem(holder,
                    List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))),
            ReinforcedBrickChimneyBlock::getTexture,
            ReinforcedBrickChimneyBlock::registerModel,
            ILootable::dropsSelfProvider,
            ReinforcedBrickChimneyBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    TERRACOTTA_BRICKS(HolderType.BLOCK, "terracotta_bricks", "Terracotta Bricks",
            ItemTags.create(Utils.modLoc("terracotta_bricks")),
            BlockTags.create(Utils.modLoc("terracotta_bricks")),
            (holder) -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)),
            SimpleBlockType::simpleBlockItem,
            () -> simpleTexture(Utils.modBlockLoc("terracotta_bricks")),
            SimpleBlockType::simpleBlockStateProvider,
            ILootable::dropsSelfProvider,
            SimpleBlockType::registerTerracottaBricksRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    TERRACOTTA_BRICK_SLAB(HolderType.BLOCK, "terracotta_brick_slab", "Terracotta Brick Slab",
            ItemTags.SLABS,
            BlockTags.SLABS,
            (holder) -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BRICK_SLAB)),
            SimpleBlockType::simpleBlockItem,
            () -> simpleTexture(Utils.modBlockLoc("terracotta_bricks")),
            SimpleBlockType::registerSlabBlockState,
            SimpleBlockType::registerSlabLootTable,
            SimpleBlockType::registerTerracottaBrickSlabRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    TERRACOTTA_BRICK_STAIRS(HolderType.BLOCK, "terracotta_brick_stairs", "Terracotta Brick Stairs",
            ItemTags.STAIRS,
            BlockTags.STAIRS,
            (holder) -> new StairBlock(() -> Registration.block(TERRACOTTA_BRICKS).defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.BRICK_STAIRS)),
            SimpleBlockType::simpleBlockItem,
            () -> simpleTexture(Utils.modBlockLoc("terracotta_bricks")),
            SimpleBlockType::registerStairsBlockState,
            ILootable::dropsSelfProvider,
            SimpleBlockType::registerTerracottaBrickStairsRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    AQUEDUCT(HolderType.ENTITY_BLOCK, "aqueduct", "Aqueduct",
            ItemTags.create(Utils.modLoc("aqueducts")),
            BlockTags.create(Utils.modLoc("aqueducts")),
            holder -> new AqueductBlock(),
            SimpleBlockType::aqueductBlockItem,
            AqueductBlock::getTexture,
            AqueductBlock::registerModel,
            ILootable::dropsSelfProvider,
            AqueductBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    AQUEDUCT_VALVE(HolderType.ENTITY_BLOCK, "aqueduct_valve", "Aqueduct Valve",
            ItemTags.create(Utils.modLoc("aqueduct_valves")),
            BlockTags.create(Utils.modLoc("aqueduct_valves")),
            holder -> new AqueductValveBlock(),
            SimpleBlockType::aqueductValveBlockItem,
            AqueductValveBlock::getTexture,
            AqueductValveBlock::registerModel,
            ILootable::dropsSelfProvider,
            AqueductValveBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    AQUEDUCT_HYDRATOR(HolderType.ENTITY_BLOCK, "aqueduct_hydrator", "Aqueduct Hydrator",
            ItemTags.create(Utils.modLoc("aqueduct_hydrators")),
            BlockTags.create(Utils.modLoc("aqueduct_hydrators")),
            AqueductHydratorBlock::new,
            SimpleBlockType::aqueductHydratorBlockItem,
            AqueductHydratorBlock::getTexture,
            AqueductHydratorBlock::registerModel,
            ILootable::dropsSelfProvider,
            AqueductHydratorBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag),
    AQUEDUCT_FERTILIZER(HolderType.MENU_BLOCK, "aqueduct_fertilizer", "Aqueduct Fertilizer",
            ItemTags.create(Utils.modLoc("aqueduct_fertilizers")),
            BlockTags.create(Utils.modLoc("aqueduct_fertilizers")),
            AqueductFertilizerBlock::new,
            SimpleBlockType::aqueductFertilizerBlockItem,
            AqueductFertilizerBlock::getTexture,
            AqueductFertilizerBlock::registerModel,
            ILootable::dropsSelfProvider,
            AqueductFertilizerBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerStonePickaxeBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new AqueductFertilizerMenu(holder, windowId, inv.player, pos, stack, data)),
    FIRE_PIT(HolderType.BLOCK, "fire_pit", "Fire Pit",
            ItemTags.create(Utils.modLoc("fire_pits")),
            BlockTags.create(Utils.modLoc("fire_pits")),
            FirePitBlock::new,
            SimpleBlockType::firePitBlockItem,
            FirePitBlock::getTexture,
            FirePitBlock::registerModel,
            ILootable::dropsSelfProvider,
            FirePitBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag),
    GRASS_BED(HolderType.BLOCK, "grass_bed", "Grass Bed",
            ItemTags.create(Utils.modLoc("grass_bed")),
            BlockTags.BEDS,
            holder -> new GrassBedBlock(),
            SimpleBlockType::grassBedBlockItem,
            GrassBedBlock::getTexture,
            GrassBedBlock::registerModel,
            ILootable::dropsSelfProvider,
            GrassBedBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag),
    /*STONE_FURNACE(HolderType.MENU_BLOCK, "stone_furnace", "Stone Furnace",
            ItemTags.create(Utils.modLoc("furnaces")),
            BlockTags.create(Utils.modLoc("furnaces")),
            FurnaceBlock::new,
            SimpleBlockType::simpleBlockItem,
            () -> MachineBlock.getTexture("stone", "furnace"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            FurnaceBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new FurnaceContainerMenu(holder, windowId, inv.player, pos, stack, data),
            FurnaceScreen::new),
    STEAM_FURNACE(HolderType.MENU_BLOCK, "steam_furnace", "Steam Furnace",
            ItemTags.create(Utils.modLoc("furnaces")),
            BlockTags.create(Utils.modLoc("furnaces")),
            FurnaceBlock::new,
            SimpleBlockType::simpleBlockItem,
            () -> MachineBlock.getTexture("steam", "furnace"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            FurnaceBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new FurnaceContainerMenu(holder, windowId, inv.player, pos, stack, data),
            FurnaceScreen::new),
    STONE_CRUSHER(HolderType.MENU_BLOCK, "stone_crusher", "Stone Crusher",
            ItemTags.create(Utils.modLoc("crushers")),
            BlockTags.create(Utils.modLoc("crushers")),
            StoneCrusherBlock::new,
            SimpleBlockType::simpleBlockItem,
            () -> MachineBlock.getTexture("stone", "crusher"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            StoneCrusherBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new CrusherContainerMenu(holder, windowId, inv.player, pos, stack, data),
            CrusherScreen::new),
    STEAM_CRUSHER(HolderType.MENU_BLOCK, "steam_crusher", "Steam Crusher",
            ItemTags.create(Utils.modLoc("crushers")),
            BlockTags.create(Utils.modLoc("crushers")),
            CrusherBlock::new,
            SimpleBlockType::simpleBlockItem,
            () -> MachineBlock.getTexture("steam", "crusher"),
            MachineBlock::registerModel,
            ILootable::dropsSelfProvider,
            CrusherBlock::registerRecipe,
            SimpleBlockType::registerItemTag,
            SimpleBlockType::registerBlockTag,
            (holder, windowId, inv, pos, stack, data) -> new CrusherContainerMenu(holder, windowId, inv.player, pos, stack, data),
            CrusherScreen::new),*/
    ;

    @NotNull public final HolderType type;
    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TagKey<Item> itemTag;
    @NotNull public final TagKey<Block> blockTag;
    @NotNull private final Function<Holder.SimpleBlockHolder, Block> blockGetter;
    @NotNull private final Function<Holder.SimpleBlockHolder, Item> itemGetter;
    @NotNull private final HashMap<Integer, Integer> tintColors;
    @NotNull private final ResourceLocation[] textures;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, ItemTagsProvider> itemTagsGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockTagsProvider> blockTagsGetter;
    @Nullable private final IAbstractMenuGetter menuGetter;

    SimpleBlockType(@NotNull HolderType type, @NotNull String key, @NotNull String name,
                    @NotNull TagKey<Item> itemTag, @NotNull TagKey<Block> blockTag,
                    @NotNull Function<Holder.SimpleBlockHolder, Block> blockGetter,
                    @NotNull Function<Holder.SimpleBlockHolder, Item> itemGetter,
                    @NotNull Supplier<TextureHolder[]> textureGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, ItemTagsProvider> itemTagsGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockTagsProvider> blockTagsGetter) {
        this.type = type;
        this.key = key;
        this.name = name;
        this.blockGetter = blockGetter;
        this.itemGetter = itemGetter;
        this.itemTag = itemTag;
        this.blockTag = blockTag;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.blockTagsGetter = blockTagsGetter;
        this.menuGetter = null;
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

    SimpleBlockType(@NotNull HolderType type, @NotNull String key, @NotNull String name,
                    @NotNull TagKey<Item> itemTag, @NotNull TagKey<Block> blockTag,
                    @NotNull Function<Holder.SimpleBlockHolder, Block> blockGetter,
                    @NotNull Function<Holder.SimpleBlockHolder, Item> itemGetter,
                    @NotNull Supplier<TextureHolder[]> textureGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, ItemTagsProvider> itemTagsGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockTagsProvider> blockTagsGetter,
                    @NotNull IAbstractMenuGetter menuGetter) {
        this.type = type;
        this.key = key;
        this.name = name;
        this.itemTag = itemTag;
        this.blockTag = blockTag;
        this.blockGetter = blockGetter;
        this.itemGetter = itemGetter;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.blockTagsGetter = blockTagsGetter;
        this.menuGetter = menuGetter;
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
    public AbstractContainerMenu getContainerMenu(@NotNull Holder holder, int windowId, @NotNull Inventory inv, @NotNull BlockPos pos,
                                                  @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data) {
        if (menuGetter != null) {
            return menuGetter.getMenu(holder, windowId, inv, pos, itemStackHandler, data);
        } else {
            return null;
        }
    }

    public Block getBlock(@NotNull Holder.SimpleBlockHolder holder) {
        return blockGetter.apply(holder);
    }

    public Item getItem(@NotNull Holder.SimpleBlockHolder holder) {
        return itemGetter.apply(holder);
    }

    private static Item simpleBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties());
    }

    private static Item simpleDescrBlockItem(@NotNull Holder.SimpleBlockHolder holder, @NotNull List<Component> description) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }
        };
    }

    private static Item aqueductBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct1", CONFIGURATION.getBaseFluidStoragePerBlock()).withStyle(DARK_GRAY));

                if (CONFIGURATION.shouldRainingFillAqueduct()) {
                    tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct2",
                            CONFIGURATION.getRainingFillAmount(), CONFIGURATION.getRainingFillPerNthTick()).withStyle(DARK_GRAY));
                }
            }
        };
    }

    private static Item aqueductValveBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve2",
                        CONFIGURATION.getValveFillAmount(), CONFIGURATION.getValveFillPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static Item aqueductHydratorBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static Item aqueductFertilizerBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static Item firePitBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit2").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit3").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit4").withStyle(DARK_GRAY));
            }
        };
    }

    private static Item grassBedBlockItem(@NotNull Holder.SimpleBlockHolder holder) {
        return new BlockItem(holder.block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.grass_bed").withStyle(DARK_GRAY));
            }
        };
    }

    private static void bottomTopBlockStateProvider(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        BlockModelBuilder model = provider.models().cubeBottomTop(holder.key, textures[0], textures[1], textures[2]);

        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    private static void simpleBlockStateProvider(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        BlockModelBuilder model = provider.models().cubeAll(holder.key, textures[0]);

        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    private static void registerSlabBlockState(Holder.SimpleBlockHolder holder, BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile bottom = provider.models().slab(holder.key, textures[0], textures[0], textures[0]);
        ModelFile top = provider.models().slabTop(holder.key + "_top", textures[0], textures[0], textures[0]);
        ModelFile doubleSlab = provider.models().cubeAll(holder.key + "_double", textures[0]);

        provider.getVariantBuilder(holder.block.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleSlab));
        provider.itemModels().getBuilder(holder.key).parent(bottom);
    }

    private static void registerStairsBlockState(Holder.SimpleBlockHolder holder, BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile stairs = provider.models().stairs(holder.key, textures[0], textures[0], textures[0]);
        ModelFile stairsInner = provider.models().stairsInner(holder.key + "_inner", textures[0], textures[0], textures[0]);
        ModelFile stairsOuter = provider.models().stairsOuter(holder.key + "_outer", textures[0], textures[0], textures[0]);

        provider.stairsBlock((StairBlock)holder.block.get(), stairs, stairsInner, stairsOuter);
        provider.itemModels().getBuilder(holder.key).parent(stairs);
    }

    private static TextureHolder[] bottomTopTexture(ResourceLocation base, ResourceLocation bottom, ResourceLocation top) {
        return List.of(
                new TextureHolder(-1, -1, base),
                new TextureHolder(-1, -1, bottom),
                new TextureHolder(-1, -1, top)
        ).toArray(TextureHolder[]::new);
    }

    private static TextureHolder[] simpleTexture(ResourceLocation texture) {
        return List.of(new TextureHolder(-1, -1, texture)).toArray(TextureHolder[]::new);
    }

    private static void registerItemTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag).add(holder.block.get().asItem());
    }

    private static void registerBlockTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockTagsProvider provider) {
        provider.tag(holder.object.blockTag).add(holder.block.get());
    }

    private static void registerStonePickaxeBlockTag(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockTagsProvider provider) {
        provider.tag(holder.object.blockTag).add(holder.block.get());
        provider.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(holder.block.get());
        provider.tag(BlockTags.NEEDS_STONE_TOOL).add(holder.block.get());
    }

    private static void registerReinforcedBricksRecipe(Holder.SimpleBlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, holder.block.get())
                .define('B', Items.BRICKS)
                .define('P', Registration.item(MaterialItemType.PLATE, MaterialType.COPPER))
                .define('#', Registration.item(MaterialItemType.BOLT, MaterialType.COPPER))
                .pattern("#P#")
                .pattern("PBP")
                .pattern("#P#")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), RecipeProvider.has(Items.BRICKS))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerTerracottaBricksRecipe(Holder.SimpleBlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, holder.block.get())
                .define('B', Items.TERRACOTTA)
                .pattern("BB")
                .pattern("BB")
                .unlockedBy(RecipeProvider.getHasName(Items.TERRACOTTA), RecipeProvider.has(Items.TERRACOTTA))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerTerracottaBrickSlabRecipe(Holder.SimpleBlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, holder.block.get(), 6)
                .define('B', TERRACOTTA_BRICKS.itemTag)
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(TERRACOTTA_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(TERRACOTTA_BRICKS.itemTag), RecipeCategory.BUILDING_BLOCKS, holder.block.get(), 2)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(TERRACOTTA_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_stonecutting"));
    }

    private static void registerTerracottaBrickStairsRecipe(Holder.SimpleBlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, holder.block.get(), 4)
                .define('B', TERRACOTTA_BRICKS.itemTag)
                .pattern("B  ")
                .pattern("BB ")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(TERRACOTTA_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(TERRACOTTA_BRICKS.itemTag), RecipeCategory.BUILDING_BLOCKS, holder.block.get())
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(TERRACOTTA_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_stonecutting"));
    }

    private static void registerSlabLootTable(Holder.SimpleBlockHolder holder, BlockLootSubProvider provider) {
        provider.add(holder.block.get(), provider::createSlabItemTable);
    }
}
