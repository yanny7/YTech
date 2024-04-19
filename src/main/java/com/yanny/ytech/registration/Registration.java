package com.yanny.ytech.registration;

import com.mojang.serialization.Codec;
import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.recipe.*;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.loot_modifier.ReplaceItemModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registration {
    public static final RegistrationHolder HOLDER = new RegistrationHolder();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, YTechMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YTechMod.MOD_ID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, YTechMod.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, YTechMod.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, YTechMod.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, YTechMod.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, YTechMod.MOD_ID);
    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM_CODECS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, YTechMod.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, YTechMod.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, YTechMod.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YTechMod.MOD_ID);

    private static final RegistryObject<CreativeModeTab> TAB = registerCreativeTab();

    static {
        for (int i = 0; i < MaterialType.TIERS.size(); i++) {
            Tier tier = MaterialType.TIERS.get(i);

            if (tier instanceof YTechTier techTier) {
                List<Object> after = i > 0 ? List.of(getTierObject(MaterialType.TIERS.get(i - 1))) : List.of();
                List<Object> before = i + 1 < MaterialType.TIERS.size() ? List.of(getTierObject(MaterialType.TIERS.get(i + 1))) : List.of();

                TierSortingRegistry.registerTier(tier, techTier.getId(), after, before);
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
        RECIPE_SERIALIZERS.register("remaining_shapeless_crafting", () -> RemainingShapelessRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register("remaining_shaped_crafting", () -> RemainingShapedRecipe.SERIALIZER);

        GLM_CODECS.register("add_item", AddItemModifier.CODEC);
        GLM_CODECS.register("remove_item", ReplaceItemModifier.CODEC);
    }

    public static void init(IEventBus eventBus) {
        YTechItems.register(eventBus);
        YTechBlocks.register(eventBus);
        YTechBlockEntityTypes.register(eventBus);
        YTechMenuTypes.register(eventBus);
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
            GeneralUtils.mapToStream(HOLDER.blocks()).forEach((holder) -> event.accept(holder.block));
            GeneralUtils.mapToStream(HOLDER.fluids()).forEach((holder) -> event.accept(holder.bucket));
            YTechItems.getRegisteredItems().forEach((object) -> event.accept(object.get()));
            HOLDER.entities().values().forEach(h -> event.accept(h.spawnEgg.get()));
        }
    }

    public static void addBlockColors(RegisterColorHandlersEvent.Block event) {
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> event.register((b, g, p, t) -> getTintColor(h, t), h.block.get()));
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((i, t) -> t == 1 ? 0xF54D0C : 0xFFFFFFFF, YTechItems.LAVA_CLAY_BUCKET.get());
        event.register((i, t) -> t == 1 ? 0x0C4DF5 : 0xFFFFFFFF, YTechItems.WATER_CLAY_BUCKET.get());

        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.block.get()));
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.bucket.get()));
    }

    @NotNull
    public static Item item(@NotNull MaterialBlockType type, @NotNull MaterialType material) {
        return Objects.requireNonNull(Objects.requireNonNull(HOLDER.blocks().get(type), "Missing item type " + type).get(material), "Missing material " + material).block.get().asItem();
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
    public static <T extends Entity> EntityType<T> entityType(@NotNull AnimalEntityType type) {
        return Objects.requireNonNull(HOLDER.entities().get(type), "Missing entity type " + type).getEntityType();
    }

    @NotNull
    public static <T extends Entity> EntityType<T> entityType(@NotNull SimpleEntityType type) {
        return Objects.requireNonNull(HOLDER.simpleEntities().get(type), "Missing entity type " + type).getEntityType();
    }

    private static Holder.BlockHolder registerBlock(MaterialBlockType blockType, MaterialType material) {
        return switch (blockType.type) {
            case BLOCK -> new Holder.BlockHolder(blockType, material, Registration::registerBlock, Registration::registerItem);
            case ENTITY_BLOCK -> new Holder.EntityBlockHolder(blockType, material, Registration::registerBlock, Registration::registerItem, Registration::registerBlockEntity);
            case MENU_BLOCK -> new Holder.MenuEntityBlockHolder(blockType, material, Registration::registerBlock, Registration::registerItem, Registration::registerBlockEntity, Registration::registerMenuBlockEntity);
        };
    }

    private static Holder.SimpleEntityHolder registerSimpleEntity(SimpleEntityType type) {
        return new Holder.SimpleEntityHolder(type, Registration::registerSimpleEntityType);
    }

    private static Holder.AnimalEntityHolder registerAnimalEntity(AnimalEntityType type) {
        return new Holder.AnimalEntityHolder(type, Registration::registerEntityType, Registration::registerSpawnEgg);
    }

    private static RegistryObject<Item> registerSpawnEgg(Holder.AnimalEntityHolder holder) {
        return ITEMS.register(holder.key + "_spawn_egg", () -> holder.object.getSpawnEgg(holder));
    }

    private static RegistryObject<EntityType<? extends Entity>> registerSimpleEntityType(Holder.SimpleEntityHolder holder) {
        return ENTITY_TYPES.register(holder.key, () -> {
            EntityType.Builder<Entity> builder = EntityType.Builder.of(holder.object::getEntity, MobCategory.MISC);
            holder.object.entityTypeBuilder.accept(builder);
            return builder.build(holder.key);
        });
    }

    private static RegistryObject<EntityType<Animal>> registerEntityType(Holder.AnimalEntityHolder holder) {
        return ENTITY_TYPES.register(holder.key, () -> {
            EntityType.Builder<Animal> builder = EntityType.Builder.of((entityType, level) -> holder.object.getEntity(holder, entityType, level), MobCategory.CREATURE);
            holder.object.entityTypeBuilder.accept(builder);
            return builder.build(holder.key);
        });
    }

    private static RegistryObject<Block> registerBlock(Holder.BlockHolder holder) {
        return BLOCKS.register(holder.key, () -> holder.object.getBlock(holder));
    }

    private static RegistryObject<Item> registerItem(Holder.BlockHolder holder) {
        return ITEMS.register(holder.key, () -> holder.object.getItem(holder));
    }

    private static RegistryObject<BlockEntityType<?>> registerBlockEntity(Holder.BlockHolder holder) {
        return BLOCK_ENTITY_TYPES.register(holder.key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                Objects.requireNonNull(((EntityBlock) holder.block.get()).newBlockEntity(pos, blockState)), holder.block.get()).build(null));
    }

    private static RegistryObject<MenuType<?>> registerMenuBlockEntity(Holder.BlockHolder holder) {
        return MENU_TYPES.register(holder.key, () -> IForgeMenuType.create((windowId, inv, data) -> holder.object.getContainerMenu(holder, windowId, inv, data.readBlockPos())));
    }

    private static Holder.FluidHolder registerFluid(MaterialFluidType fluidObject, MaterialType material) {
        String name = material.key;
        String flowingName = "flowing_" + name;
        String bucketName = name + "_bucket";
        String blockName = name + "_fluid";
        RegistryObject<Fluid> sourceFluid = RegistryObject.create(Utils.modLoc(name), ForgeRegistries.FLUIDS);
        RegistryObject<Fluid> flowingFluid = RegistryObject.create(Utils.modLoc(flowingName), ForgeRegistries.FLUIDS);
        RegistryObject<Item> bucket = RegistryObject.create(Utils.modLoc(bucketName), ForgeRegistries.ITEMS);
        RegistryObject<Block> block = RegistryObject.create(Utils.modLoc(blockName), ForgeRegistries.BLOCKS);
        FluidType fluidType = new YTechFluidType(material);
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(() ->
                fluidType, sourceFluid, flowingFluid).bucket(bucket).tickRate(20).block(() -> (LiquidBlock) block.get());

        return new Holder.FluidHolder(
                fluidObject,
                material,
                BLOCKS.register(blockName, () -> new LiquidBlock(() -> (FlowingFluid)flowingFluid.get(), BlockBehaviour.Properties.of())),
                FLUID_TYPES.register(name, () -> fluidType),
                FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(properties)),
                FLUIDS.register(flowingName, () -> new ForgeFlowingFluid.Flowing(properties)),
                ITEMS.register(bucketName, () -> new BucketItem(sourceFluid, new Item.Properties()))
        );
    }

    private static RegistryObject<CreativeModeTab> registerCreativeTab() {
        Supplier<ItemStack> iconSupplier = () -> new ItemStack(YTechBlocks.PRIMITIVE_SMELTER.get());
        return CREATIVE_TABS.register(YTechMod.MOD_ID, () -> CreativeModeTab.builder().title(Component.translatable("creativeTab.ytech.title")).icon(iconSupplier).build());
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

    private static Object getTierObject(@NotNull Tier tier) {
        return tier instanceof YTechTier techTier ? techTier.getId() : tier;
    }
}
