package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.blocks.MaterialBlock;
import com.yanny.ytech.blocks.OreBlock;
import com.yanny.ytech.items.RawMetalItem;
import com.yanny.ytech.model.YTechConfigLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, YTechMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YTechMod.MOD_ID);

    public static final Map<YTechConfigLoader.Material, Map<Block, RegistryObject<Block>>> REGISTERED_ORE_BLOCKS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, RegistryObject<Block>> REGISTERED_RAW_STORAGE_BLOCKS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, RegistryObject<Block>> REGISTERED_STORAGE_BLOCKS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, Map<Item, RegistryObject<Item>>> REGISTERED_ORE_ITEMS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, RegistryObject<Item>> REGISTERED_RAW_METAL_ITEMS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, RegistryObject<Item>> REGISTERED_RAW_STORAGE_BLOCK_ITEMS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, RegistryObject<Item>> REGISTERED_STORAGE_BLOCK_ITEMS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Block>> ORE_BLOCK_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Block>> STORAGE_BLOCK_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Item>> ORE_BLOCK_ITEM_TAGS = new HashMap<>();

    static {
        for (YTechConfigLoader.Material element : YTechMod.CONFIGURATION.getElements()) {
            if (YTechMod.CONFIGURATION.isOre(element)) {
                REGISTERED_ORE_BLOCKS.put(element, new HashMap<>());
                REGISTERED_ORE_ITEMS.put(element, new HashMap<>());
                registerOre(element, Blocks.STONE);
                registerOre(element, Blocks.DEEPSLATE);
                registerOre(element, Blocks.NETHERRACK);
                registerRawMetal(element);
                ORE_BLOCK_TAGS.put(element, Objects.requireNonNull(BlockTags.create(new ResourceLocation(YTechMod.MOD_ID, "ores/" + element.id()))));
                ORE_BLOCK_ITEM_TAGS.put(element, Objects.requireNonNull(ItemTags.create(new ResourceLocation(YTechMod.MOD_ID, "ores/" + element.id()))));
            }
            if (YTechMod.CONFIGURATION.isMetal(element)) {
                registerStorageBlock(element);
                STORAGE_BLOCK_TAGS.put(element, Objects.requireNonNull(BlockTags.create(new ResourceLocation(YTechMod.MOD_ID, "storage_blocks/" + element.id()))));
            }
        }
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    private static void registerOre(YTechConfigLoader.Material material, Block baseStone) {
        String oreName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(baseStone)).getPath() + "_" + material.id() + "_ore";
        RegistryObject<Block> block = BLOCKS.register(oreName, () -> new OreBlock(material, baseStone));
        RegistryObject<Item> blockItem = ITEMS.register(oreName, () -> new BlockItem(block.get(), new Item.Properties()));

        REGISTERED_ORE_BLOCKS.get(material).put(baseStone, block);
        REGISTERED_ORE_ITEMS.get(material).put(baseStone.asItem(), blockItem);
    }

    private static void registerRawMetal(YTechConfigLoader.Material material) {
        String rawMetalBlock = "raw_" + material.id() + "_block";
        String rawMetalItem = "raw_" + material.id() + "_item";
        RegistryObject<Block> block = BLOCKS.register(rawMetalBlock, () -> new MaterialBlock(material));
        RegistryObject<Item> blockItem = ITEMS.register(rawMetalBlock, () -> new BlockItem(block.get(), new Item.Properties()));
        RegistryObject<Item> item = ITEMS.register(rawMetalItem, () -> new RawMetalItem(material));

        REGISTERED_RAW_STORAGE_BLOCKS.put(material, block);
        REGISTERED_RAW_STORAGE_BLOCK_ITEMS.put(material, blockItem);
        REGISTERED_RAW_METAL_ITEMS.put(material, item);
    }

    private static void registerStorageBlock(YTechConfigLoader.Material material) {
        String oreName = material.id() + "_block";
        RegistryObject<Block> block = BLOCKS.register(oreName, () -> new MaterialBlock(material));
        RegistryObject<Item> blockItem = ITEMS.register(oreName, () -> new BlockItem(block.get(), new Item.Properties()));

        REGISTERED_STORAGE_BLOCKS.put(material, block);
        REGISTERED_STORAGE_BLOCK_ITEMS.put(material, blockItem);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            REGISTERED_ORE_BLOCKS.forEach(((material, stoneMap) -> stoneMap.forEach((block, registry) -> event.accept(registry.get()))));
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            REGISTERED_RAW_STORAGE_BLOCKS.forEach((material, registry) -> event.accept(registry.get()));
            REGISTERED_STORAGE_BLOCKS.forEach((material, registry) -> event.accept(registry.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            REGISTERED_RAW_METAL_ITEMS.forEach((material, registry) -> event.accept(registry.get()));
        }
    }

    public static void addBlockColors(RegisterColorHandlersEvent.Block event) {
        REGISTERED_ORE_BLOCKS.forEach((material, stoneMap) -> stoneMap.forEach((block, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get())));
        REGISTERED_RAW_STORAGE_BLOCKS.forEach((material, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
        REGISTERED_STORAGE_BLOCKS.forEach((material, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        REGISTERED_ORE_BLOCKS.forEach((material, stoneMap) -> stoneMap.forEach((block, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get())));
        REGISTERED_RAW_METAL_ITEMS.forEach((material, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
        REGISTERED_RAW_STORAGE_BLOCKS.forEach((material, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
        REGISTERED_STORAGE_BLOCKS.forEach((material, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
    }
}
