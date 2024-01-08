package com.yanny.ytech.registration;

import com.mojang.serialization.Codec;
import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.recipe.*;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.loot_modifier.ReplaceItemModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.TierSortingRegistry;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registration {
    public static final RegistrationHolder HOLDER = new RegistrationHolder();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, YTechMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, YTechMod.MOD_ID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, YTechMod.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, YTechMod.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, YTechMod.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, YTechMod.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, YTechMod.MOD_ID);
    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, YTechMod.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, YTechMod.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, YTechMod.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, YTechMod.MOD_ID);

    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = registerCreativeTab();

    static {
        for (int i = 0; i < MaterialType.TIERS.size(); i++) {
            Tier tier = MaterialType.TIERS.get(i);

            if (tier instanceof YTechTier techTier) {
                List<Object> after = i > 0 ? List.of(getTierObject(MaterialType.TIERS.get(i - 1))) : List.of();
                List<Object> before = i + 1 < MaterialType.TIERS.size() ? List.of(getTierObject(MaterialType.TIERS.get(i + 1))) : List.of();

                TierSortingRegistry.registerTier(tier, techTier.getId(), after, before);
            }
        }

        for (MaterialItemType itemObject : MaterialItemType.values()) {
            for (MaterialType material : itemObject.materials) {
                HOLDER.items().computeIfAbsent(itemObject, (p) -> new HashMap<>()).compute(material, (k, v) -> uniqueKey(v, itemObject,
                        (object) -> new Holder.ItemHolder(object, material, (holder) -> ITEMS.register(holder.key, () -> holder.object.getItem(holder)))));
            }
        }
        for (MaterialBlockType blockObject : MaterialBlockType.values()) {
            for (MaterialType material : blockObject.materials) {
                HOLDER.blocks().computeIfAbsent(blockObject, (p) -> new HashMap<>()).compute(material, (k, v) -> uniqueKey(v, blockObject,
                        (blockType) -> registerBlock(blockType, material)));
            }
        }
        for (MaterialFluidType fluidObject : MaterialFluidType.values()) {
            for (MaterialType material : fluidObject.materials) {
                HOLDER.fluids().computeIfAbsent(fluidObject, (p) -> new HashMap<>()).compute(material, (k, v) -> uniqueKey(v, fluidObject,
                        (object) -> registerFluid(object, material)));
            }
        }

        for (SimpleItemType type : SimpleItemType.values()) {
            HOLDER.simpleItems().put(type, new Holder.SimpleItemHolder(type, ITEMS.register(type.key, type.itemGetter)));
        }
        for (SimpleBlockType type : SimpleBlockType.values()) {
            HOLDER.simpleBlocks().put(type, registerBlock(type));
        }

        for (SimpleEntityType type : SimpleEntityType.values()) {
            HOLDER.simpleEntities().put(type, registerSimpleEntity(type));
        }
        for (AnimalEntityType type : AnimalEntityType.values()) {
            HOLDER.entities().put(type, registerAnimalEntity(type));
        }

        RECIPE_TYPES.register("drying", () -> DryingRecipe.RECIPE_TYPE);
        RECIPE_TYPES.register("tanning", () -> TanningRecipe.RECIPE_TYPE);
        RECIPE_TYPES.register("milling", () -> MillingRecipe.RECIPE_TYPE);
        RECIPE_TYPES.register("smelting", () -> SmeltingRecipe.RECIPE_TYPE);
        RECIPE_TYPES.register("block_hit", () -> BlockHitRecipe.RECIPE_TYPE);
        RECIPE_TYPES.register("alloying", () -> AlloyingRecipe.RECIPE_TYPE);
        RECIPE_TYPES.register("hammering", () -> HammeringRecipe.RECIPE_TYPE);

        RECIPE_SERIALIZERS.register("drying", () -> DryingRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("tanning", () -> TanningRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("milling", () -> MillingRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("smelting", () -> SmeltingRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("block_hit", () -> BlockHitRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("alloying", () -> AlloyingRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("hammering", () -> HammeringRecipe.SERIALIZER);

        GLM_CODECS.register("add_item", AddItemModifier.CODEC);
        GLM_CODECS.register("remove_item", ReplaceItemModifier.CODEC);
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
        GLM_CODECS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == TAB.getKey()) {
            GeneralUtils.mapToStream(HOLDER.items()).forEach((holder) -> event.accept(holder.item.get()));
            GeneralUtils.mapToStream(HOLDER.blocks()).forEach((holder) -> event.accept(holder.block.get()));
            GeneralUtils.mapToStream(HOLDER.fluids()).forEach((holder) -> event.accept(holder.bucket.get()));
            HOLDER.simpleItems().values().forEach(h -> event.accept(h.item.get()));
            HOLDER.simpleBlocks().values().forEach(h -> event.accept(h.block.get()));
            HOLDER.entities().values().forEach(h -> event.accept(h.spawnEgg.get()));
        }
    }

    public static void addBlockColors(RegisterColorHandlersEvent.Block event) {
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> event.register((b, g, p, t) -> getTintColor(h, t), h.block.get()));
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        HOLDER.simpleItems().values().forEach((h) -> event.register((i, t) -> getTintColor(h, t), h.item.get()));
        HOLDER.simpleBlocks().values().forEach((h) -> event.register((i, t) -> getTintColor(h, t), h.block.get()));
        GeneralUtils.mapToStream(HOLDER.items()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.item.get()));
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.block.get()));
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.bucket.get()));
    }

    @NotNull
    public static Item item(@NotNull SimpleItemType type) {
        return Objects.requireNonNull(HOLDER.simpleItems().get(type), "Missing item type " + type).item.get();
    }

    @NotNull
    public static Item item(@NotNull SimpleBlockType type) {
        return Objects.requireNonNull(HOLDER.simpleBlocks().get(type), "Missing item type " + type).block.get().asItem();
    }

    @NotNull
    public static Item item(@NotNull MaterialItemType type, @NotNull MaterialType material) {
        Item item = null;

        switch (type) {
            case INGOT -> {
                switch (material) {
                    case COPPER -> item = Items.COPPER_INGOT;
                    case GOLD -> item = Items.GOLD_INGOT;
                    case IRON -> item = Items.IRON_INGOT;
                }
            }
            case RAW_MATERIAL -> {
                switch (material) {
                    case COPPER -> item = Items.RAW_COPPER;
                    case GOLD -> item = Items.RAW_GOLD;
                    case IRON -> item = Items.RAW_IRON;
                }
            }
        }

        if (item != null) {
            return item;
        }

        return Objects.requireNonNull(Objects.requireNonNull(HOLDER.items().get(type), "Missing item type " + type).get(material), "Missing material " + material).item.get();
    }

    @NotNull
    public static Item item(@NotNull MaterialBlockType type, @NotNull MaterialType material) {
        return Objects.requireNonNull(Objects.requireNonNull(HOLDER.blocks().get(type), "Missing item type " + type).get(material), "Missing material " + material).block.get().asItem();
    }

    @NotNull
    public static Item bucket(@NotNull MaterialFluidType type, @NotNull MaterialType material) {
        return Objects.requireNonNull(Objects.requireNonNull(HOLDER.fluids().get(type), "Missing bucket type " + type).get(material), "Missing material " + material).bucket.get();
    }

    @NotNull
    public static Block block(@NotNull SimpleBlockType type) {
        return Objects.requireNonNull(HOLDER.simpleBlocks().get(type), "Missing item type " + type).block.get();
    }

    @NotNull
    public static Block block(@NotNull MaterialBlockType type, @NotNull MaterialType material) {
        Block block = null;

        switch (type) {
            case STORAGE_BLOCK -> {
                switch (material) {
                    case COPPER -> block = Blocks.COPPER_BLOCK;
                    case GOLD -> block = Blocks.GOLD_BLOCK;
                    case IRON -> block = Blocks.IRON_BLOCK;
                }
            }
            case RAW_STORAGE_BLOCK -> {
                switch (material) {
                    case COPPER -> block = Blocks.RAW_COPPER_BLOCK;
                    case GOLD -> block = Blocks.RAW_GOLD_BLOCK;
                    case IRON -> block = Blocks.RAW_IRON_BLOCK;
                }
            }
        }

        if (block != null) {
            return block;
        }

        return Objects.requireNonNull(Objects.requireNonNull(HOLDER.blocks().get(type), "Missing item type " + type).get(material), "Missing material " + material).block.get();
    }

    @NotNull
    public static EntityType<?> entityType(@NotNull AnimalEntityType type) {
        return Objects.requireNonNull(HOLDER.entities().get(type), "Missing entity type " + type).entityType.get();
    }

    @NotNull
    public static EntityType<?> entityType(@NotNull SimpleEntityType type) {
        return Objects.requireNonNull(HOLDER.simpleEntities().get(type), "Missing entity type " + type).entityType.get();
    }

    private static Holder.BlockHolder registerBlock(MaterialBlockType blockType, MaterialType material) {
        return switch (blockType.type) {
            case BLOCK -> new Holder.BlockHolder(blockType, material, Registration::registerBlock, Registration::registerItem);
            case ENTITY_BLOCK -> new Holder.EntityBlockHolder(blockType, material, Registration::registerBlock, Registration::registerItem, Registration::registerBlockEntity);
            case MENU_BLOCK -> new Holder.MenuEntityBlockHolder(blockType, material, Registration::registerBlock, Registration::registerItem, Registration::registerBlockEntity, Registration::registerMenuBlockEntity);
        };
    }

    private static Holder.SimpleBlockHolder registerBlock(SimpleBlockType blockType) {
        return switch (blockType.type) {
            case BLOCK -> new Holder.SimpleBlockHolder(blockType, Registration::registerBlock, Registration::registerItem);
            case ENTITY_BLOCK -> new Holder.EntitySimpleBlockHolder(blockType, Registration::registerBlock, Registration::registerItem, Registration::registerBlockEntity);
            case MENU_BLOCK -> new Holder.MenuEntitySimpleBlockHolder(blockType, Registration::registerBlock, Registration::registerItem, Registration::registerBlockEntity, Registration::registerMenuBlockEntity);
        };
    }

    private static Holder.SimpleEntityHolder registerSimpleEntity(SimpleEntityType type) {
        return new Holder.SimpleEntityHolder(type, Registration::registerSimpleEntityType);
    }

    private static Holder.AnimalEntityHolder registerAnimalEntity(AnimalEntityType type) {
        return new Holder.AnimalEntityHolder(type, Registration::registerEntityType, Registration::registerSpawnEgg);
    }

    private static Supplier<Item> registerSpawnEgg(Holder.AnimalEntityHolder holder) {
        return ITEMS.register(holder.key + "_spawn_egg", () -> holder.object.getSpawnEgg(holder));
    }

    private static Supplier<EntityType<? extends Entity>> registerSimpleEntityType(Holder.SimpleEntityHolder holder) {
        return ENTITY_TYPES.register(holder.key, () -> {
            EntityType.Builder<Entity> builder = EntityType.Builder.of(holder.object::getEntity, MobCategory.MISC);
            holder.object.entityTypeBuilder.accept(builder);
            return builder.build(holder.key);
        });
    }

    private static Supplier<EntityType<Animal>> registerEntityType(Holder.AnimalEntityHolder holder) {
        return ENTITY_TYPES.register(holder.key, () -> {
            EntityType.Builder<Animal> builder = EntityType.Builder.of((entityType, level) -> holder.object.getEntity(holder, entityType, level), MobCategory.CREATURE);
            holder.object.entityTypeBuilder.accept(builder);
            return builder.build(holder.key);
        });
    }

    private static Supplier<Block> registerBlock(Holder.BlockHolder holder) {
        return BLOCKS.register(holder.key, () -> holder.object.getBlock(holder));
    }

    private static Supplier<Item> registerItem(Holder.BlockHolder holder) {
        return ITEMS.register(holder.key, () -> holder.object.getItem(holder));
    }

    private static Supplier<Block> registerBlock(Holder.SimpleBlockHolder holder) {
        return BLOCKS.register(holder.key, () -> holder.object.getBlock(holder));
    }

    private static Supplier<Item> registerItem(Holder.SimpleBlockHolder holder) {
        return ITEMS.register(holder.key, () -> holder.object.getItem(holder));
    }

    private static Supplier<BlockEntityType<?>> registerBlockEntity(Holder.BlockHolder holder) {
        return BLOCK_ENTITY_TYPES.register(holder.key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                Objects.requireNonNull(((EntityBlock) holder.block.get()).newBlockEntity(pos, blockState)), holder.block.get()).build(null));
    }

    private static Supplier<BlockEntityType<?>> registerBlockEntity(Holder.SimpleBlockHolder holder) {
        return BLOCK_ENTITY_TYPES.register(holder.key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                Objects.requireNonNull(((EntityBlock) holder.block.get()).newBlockEntity(pos, blockState)), holder.block.get()).build(null));
    }

    private static Supplier<MenuType<?>> registerMenuBlockEntity(Holder.BlockHolder holder) {
        return MENU_TYPES.register(holder.key, () -> IMenuTypeExtension.create((windowId, inv, data) -> holder.object.getContainerMenu(holder, windowId, inv, data.readBlockPos())));
    }

    private static Supplier<MenuType<?>> registerMenuBlockEntity(Holder.SimpleBlockHolder holder) {
        return MENU_TYPES.register(holder.key, () -> IMenuTypeExtension.create((windowId, inv, data) -> holder.object.getContainerMenu(holder, windowId, inv, data.readBlockPos())));
    }

    private static Holder.FluidHolder registerFluid(MaterialFluidType fluidObject, MaterialType material) {
        String name = material.key;
        String flowingName = "flowing_" + name;
        String bucketName = name + "_bucket";
        String blockName = name + "_fluid";
        Supplier<Fluid> sourceFluid = DeferredHolder.create(Utils.modLoc(name), BuiltInRegistries.FLUID.getDefaultKey());
        Supplier<Fluid> flowingFluid = DeferredHolder.create(Utils.modLoc(flowingName), BuiltInRegistries.FLUID.getDefaultKey());
        Supplier<Item> bucket = DeferredHolder.create(Utils.modLoc(bucketName), BuiltInRegistries.ITEM.getDefaultKey());
        Supplier<Block> block = DeferredHolder.create(Utils.modLoc(blockName), BuiltInRegistries.BLOCK.getDefaultKey());
        FluidType fluidType = new YTechFluidType(material);
        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(() ->
                fluidType, sourceFluid, flowingFluid).bucket(bucket).tickRate(20).block(() -> (LiquidBlock) block.get());

        return new Holder.FluidHolder(
                fluidObject,
                material,
                BLOCKS.register(blockName, () -> new LiquidBlock(() -> (FlowingFluid)flowingFluid.get(), BlockBehaviour.Properties.of())),
                FLUID_TYPES.register(name, () -> fluidType),
                FLUIDS.register(name, () -> new BaseFlowingFluid.Source(properties)),
                FLUIDS.register(flowingName, () -> new BaseFlowingFluid.Flowing(properties)),
                ITEMS.register(bucketName, () -> new BucketItem(sourceFluid, new Item.Properties()))
        );
    }

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerCreativeTab() {
        Supplier<ItemStack> iconSupplier = () -> new ItemStack(item(SimpleBlockType.PRIMITIVE_SMELTER));
        return CREATIVE_TABS.register(YTechMod.MOD_ID, () -> CreativeModeTab.builder().icon(iconSupplier).build());
    }

    private static <U extends INameable & IMaterialModel<?, ?>, V extends Holder.MaterialHolder<U>> V uniqueKey(V v, U object, Function<U, V> consumer) {
        if (v == null) {
            return consumer.apply(object);
        } else {
            throw new IllegalStateException("Material already exists");
        }
    }

    private static <U extends INameable & IMaterialModel<?, ?>> int getTintColor(Holder.MaterialHolder<U> h, int t) {
        return h.object.getTintColors(h.material).getOrDefault(t, 0xFFFFFFFF);
    }

    private static int getTintColor(Holder.SimpleItemHolder h, int t) {
        return h.object.getTintColors().getOrDefault(t, 0xFFFFFFFF);
    }

    private static int getTintColor(Holder.SimpleBlockHolder h, int t) {
        return h.object.getTintColors().getOrDefault(t, 0xFFFFFFFF);
    }

    private static Object getTierObject(@NotNull Tier tier) {
        return tier instanceof YTechTier techTier ? techTier.getId() : tier;
    }
}
