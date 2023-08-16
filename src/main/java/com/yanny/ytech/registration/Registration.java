package com.yanny.ytech.registration;

import com.mojang.serialization.Codec;
import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
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

    private static final RegistryObject<CreativeModeTab> TAB = registerCreativeTab();

    static {
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

        GLM_CODECS.register("add_item", AddItemModifier.CODEC);
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
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == TAB.getKey()) {
            GeneralUtils.mapToStream(HOLDER.items()).forEach((holder) -> event.accept(holder.item));
            GeneralUtils.mapToStream(HOLDER.blocks()).forEach((holder) -> event.accept(holder.block));
            GeneralUtils.mapToStream(HOLDER.fluids()).forEach((holder) -> event.accept(holder.bucket));
            HOLDER.simpleItems().values().forEach(h -> event.accept(h.item.get()));
            HOLDER.simpleBlocks().values().forEach(h -> event.accept(h.block.get()));
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

    private static Holder.BlockHolder registerBlock(MaterialBlockType blockType, MaterialType material) {
        return switch (blockType.type) {
            case BLOCK -> new Holder.BlockHolder(blockType, material, Registration::registerBlockItem);
            case ENTITY_BLOCK -> new Holder.EntityBlockHolder(blockType, material, Registration::registerBlockItem, Registration::registerBlockEntity);
            case MENU_BLOCK -> new Holder.MenuEntityBlockHolder(blockType, material, Registration::registerBlockItem, Registration::registerBlockEntity, Registration::registerMenuBlockEntity);
        };
    }

    private static Holder.SimpleBlockHolder registerBlock(SimpleBlockType blockType) {
        return switch (blockType.type) {
            case BLOCK -> new Holder.SimpleBlockHolder(blockType, Registration::registerBlockItem);
            case ENTITY_BLOCK -> new Holder.EntitySimpleBlockHolder(blockType, Registration::registerBlockItem, Registration::registerBlockEntity);
            case MENU_BLOCK -> new Holder.MenuEntitySimpleBlockHolder(blockType, Registration::registerBlockItem, Registration::registerBlockEntity, Registration::registerMenuBlockEntity);
        };
    }

    private static RegistryObject<Block> registerBlockItem(Holder.BlockHolder holder) {
        RegistryObject<Block> block = BLOCKS.register(holder.key, () -> holder.object.getBlock(holder));
        ITEMS.register(holder.key, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Block> registerBlockItem(Holder.SimpleBlockHolder holder) {
        RegistryObject<Block> block = BLOCKS.register(holder.key, () -> holder.object.getBlock(holder));
        ITEMS.register(holder.key, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<BlockEntityType<?>> registerBlockEntity(Holder.BlockHolder holder) {
        return BLOCK_ENTITY_TYPES.register(holder.key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                Objects.requireNonNull(((EntityBlock) holder.block.get()).newBlockEntity(pos, blockState)), holder.block.get()).build(null));
    }

    private static RegistryObject<BlockEntityType<?>> registerBlockEntity(Holder.SimpleBlockHolder holder) {
        return BLOCK_ENTITY_TYPES.register(holder.key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                Objects.requireNonNull(((EntityBlock) holder.block.get()).newBlockEntity(pos, blockState)), holder.block.get()).build(null));
    }

    private static RegistryObject<MenuType<?>> registerMenuBlockEntity(Holder.BlockHolder holder) {
        return MENU_TYPES.register(holder.key, () -> IForgeMenuType.create((windowId, inv, data) -> holder.object.getContainerMenu(holder, windowId, inv, data.readBlockPos())));
    }

    private static RegistryObject<MenuType<?>> registerMenuBlockEntity(Holder.SimpleBlockHolder holder) {
        return MENU_TYPES.register(holder.key, () -> IForgeMenuType.create((windowId, inv, data) -> holder.object.getContainerMenu(holder, windowId, inv, data.readBlockPos())));
    }

    private static Holder.FluidHolder registerFluid(MaterialFluidType fluidObject, MaterialType material) {
        String name = material.key;
        String flowingName = "flowing_" + name;
        String bucketName = name + "_bucket";
        String blockName = name + "_fluid";
        RegistryObject<Fluid> sourceFluid = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, name), ForgeRegistries.FLUIDS);
        RegistryObject<Fluid> flowingFluid = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, flowingName), ForgeRegistries.FLUIDS);
        RegistryObject<Item> bucket = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, bucketName), ForgeRegistries.ITEMS);
        RegistryObject<Block> block = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, blockName), ForgeRegistries.BLOCKS);
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
        Supplier<ItemStack> iconSupplier = () -> new ItemStack(GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.INGOT, MaterialType.GOLD).item.get());
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
        return h.object.getTintColors().getOrDefault(t, 0xFFFFFFFF);
    }

    private static int getTintColor(Holder.SimpleItemHolder h, int t) {
        return h.object.getTintColors().getOrDefault(t, 0xFFFFFFFF);
    }

    private static int getTintColor(Holder.SimpleBlockHolder h, int t) {
        return h.object.getTintColors().getOrDefault(t, 0xFFFFFFFF);
    }
}
