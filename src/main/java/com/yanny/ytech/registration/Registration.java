package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public static final RegistrationHolder REGISTRATION_HOLDER = new RegistrationHolder();

    public static final Map<YTechConfigLoader.Material, TagKey<Block>> ORE_BLOCK_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Block>> STORAGE_BLOCK_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Item>> ORE_BLOCK_ITEM_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Item>> STORAGE_BLOCK_ITEM_TAGS = new HashMap<>();

    static {
        for (YTechConfigLoader.Material element : YTechMod.CONFIGURATION.getElements()) {
            if (YTechMod.CONFIGURATION.isOre(element)) {
                REGISTRATION_HOLDER.ore().put(element, new HashMap<>(Map.of(
                        Blocks.STONE, registerBlockItem(element, Blocks.STONE, getPathOf(Blocks.STONE) + "_" + element.id() + "_ore"),
                        Blocks.DEEPSLATE, registerBlockItem(element, Blocks.DEEPSLATE, getPathOf(Blocks.DEEPSLATE) + "_" + element.id() + "_ore"),
                        Blocks.NETHERRACK, registerBlockItem(element, Blocks.NETHERRACK, getPathOf(Blocks.NETHERRACK) + "_" + element.id() + "_ore")
                )));
                REGISTRATION_HOLDER.rawStorageBlock().put(element, registerBlockItem(element, Blocks.RAW_IRON_BLOCK, "raw_" + element.id() + "_block"));
                REGISTRATION_HOLDER.rawMaterial().put(element, registerItem("raw_" + element.id() + "_item"));
                ORE_BLOCK_TAGS.put(element, Objects.requireNonNull(BlockTags.create(new ResourceLocation(YTechMod.MOD_ID, "ores/" + element.id()))));
                ORE_BLOCK_ITEM_TAGS.put(element, Objects.requireNonNull(ItemTags.create(new ResourceLocation(YTechMod.MOD_ID, "ores/" + element.id()))));
            }
            if (YTechMod.CONFIGURATION.isMetal(element)) {
                REGISTRATION_HOLDER.storageBlock().put(element, registerBlockItem(element, Blocks.IRON_BLOCK, element.id() + "_block"));
                STORAGE_BLOCK_TAGS.put(element, Objects.requireNonNull(BlockTags.create(new ResourceLocation(YTechMod.MOD_ID, "storage_blocks/" + element.id()))));
                STORAGE_BLOCK_ITEM_TAGS.put(element, Objects.requireNonNull(ItemTags.create(new ResourceLocation(YTechMod.MOD_ID, "storage_blocks/" + element.id()))));
            }
        }
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Block> registerBlockItem(YTechConfigLoader.Material material, Block base, String name) {
        Float hardness = material.hardness();
        assert hardness != null;
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.copy(base).strength(hardness)));
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            REGISTRATION_HOLDER.ore().forEach(((material, stoneMap) -> stoneMap.forEach((stone, registry) -> event.accept(registry.get()))));
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> event.accept(registry.get()));
            REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> event.accept(registry.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            REGISTRATION_HOLDER.rawMaterial().forEach((material, registry) -> event.accept(registry.get()));
        }
    }

    public static void addBlockColors(RegisterColorHandlersEvent.Block event) {
        REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((stone, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get())));
        REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
        REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((block, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get())));
        REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
        REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
        REGISTRATION_HOLDER.rawMaterial().forEach((material, registry) -> event.register((itemStack, tint) -> YTechMod.CONFIGURATION.getElement(material.id()).getColor(), registry.get()));
    }

    private static String getPathOf(Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
    }
}
