package com.yanny.ytech.registration;

import com.google.common.collect.ImmutableMap;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registration {
    public static final RegistrationHolder REGISTRATION_HOLDER = new RegistrationHolder();

    public static final Map<YTechConfigLoader.Material, BlockItemHolder<TagKey<Block>, TagKey<Item>>> FORGE_ORE_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, BlockItemHolder<TagKey<Block>, TagKey<Item>>> FORGE_STORAGE_BLOCK_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, BlockItemHolder<TagKey<Block>, TagKey<Item>>> FORGE_RAW_STORAGE_BLOCK_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Item>> FORGE_RAW_MATERIAL_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Item>> FORGE_INGOT_TAGS = new HashMap<>();
    public static final Map<YTechConfigLoader.Material, TagKey<Fluid>> FORGE_FLUID_TAGS = new HashMap<>();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, YTechMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YTechMod.MOD_ID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, YTechMod.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, YTechMod.MOD_ID);

    static {
        for (YTechConfigLoader.Material element : YTechMod.CONFIGURATION.getElements()) {
            if (YTechMod.CONFIGURATION.isOre(element)) {
                REGISTRATION_HOLDER.ore().put(element, new HashMap<>(ImmutableMap.<Block, RegistryObject<Block>>builder()
                        .put(Blocks.STONE, registerBlockItem(element, Blocks.STONE, getPathOf(Blocks.STONE) + "_" + element.id() + "_ore"))
                        .put(Blocks.DEEPSLATE, registerBlockItem(element, Blocks.DEEPSLATE, getPathOf(Blocks.DEEPSLATE) + "_" + element.id() + "_ore"))
                        .put(Blocks.NETHERRACK, registerBlockItem(element, Blocks.NETHERRACK, getPathOf(Blocks.NETHERRACK) + "_" + element.id() + "_ore"))
                        .build()));
                REGISTRATION_HOLDER.rawStorageBlock().put(element, registerBlockItem(element, Blocks.RAW_IRON_BLOCK, "raw_" + element.id() + "_block"));
                REGISTRATION_HOLDER.rawMaterial().put(element, registerItem("raw_" + element.id()));

                FORGE_ORE_TAGS.put(element, registerBlockItemTag("forge", "ores", element.id()));
                FORGE_RAW_STORAGE_BLOCK_TAGS.put(element, registerBlockItemTag("forge", "storage_blocks", "raw_" + element.id()));
                FORGE_RAW_MATERIAL_TAGS.put(element, registerItemTag("forge", "raw_materials", element.id()));
            }
            if (YTechMod.CONFIGURATION.isMetal(element)) {
                REGISTRATION_HOLDER.storageBlock().put(element, registerBlockItem(element, Blocks.IRON_BLOCK, element.id() + "_block"));
                REGISTRATION_HOLDER.ingot().put(element, registerItem(element.id() + "_ingot"));

                FORGE_STORAGE_BLOCK_TAGS.put(element, registerBlockItemTag("forge", "storage_blocks", element.id()));
                FORGE_INGOT_TAGS.put(element, registerItemTag("forge", "ingots", element.id()));
            }
            if (YTechMod.CONFIGURATION.isFluid(element)) {
                REGISTRATION_HOLDER.fluid().put(element, registerFluid(element));
                FORGE_FLUID_TAGS.put(element, registerFluidTag("forge", element.id()));
            }
        }
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
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
            REGISTRATION_HOLDER.ingot().forEach((material, registry) -> event.accept(registry.get()));
        }
    }

    public static void addBlockColors(RegisterColorHandlersEvent.Block event) {
        REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((stone, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> material.getColor(), registry.get())));
        REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> material.getColor(), registry.get()));
        REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> event.register((blockState, blockAndTintGetter, blockPos, tint) -> material.getColor(), registry.get()));
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((block, registry) -> event.register((itemStack, tint) -> material.getColor(), registry.get())));
        REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> event.register((itemStack, tint) -> material.getColor(), registry.get()));
        REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> event.register((itemStack, tint) -> material.getColor(), registry.get()));
        REGISTRATION_HOLDER.rawMaterial().forEach((material, registry) -> event.register((itemStack, tint) -> material.getColor(), registry.get()));
        REGISTRATION_HOLDER.ingot().forEach((material, registry) -> event.register((itemStack, tint) -> material.getColor(), registry.get()));
        REGISTRATION_HOLDER.fluid().forEach(((material, holder) -> event.register((itemStack, tint) -> tint == 1 ? material.getColor() : 0xFFFFFF, holder.bucket().get())));
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

    private static FluidHolder registerFluid(YTechConfigLoader.Material material) {
        String name = material.id();
        String flowingName = "flowing_" + name;
        String bucketName = name + "_bucket";
        String blockName = name + "_fluid";
        RegistryObject<Fluid> sourceFluid = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, name), ForgeRegistries.FLUIDS);
        RegistryObject<Fluid> flowingFluid = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, flowingName), ForgeRegistries.FLUIDS);
        RegistryObject<Item> bucket = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, bucketName), ForgeRegistries.ITEMS);
        RegistryObject<Block> block = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, blockName), ForgeRegistries.BLOCKS);
        FluidType fluidType = new YTechModFluidType(material);
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(() -> fluidType, sourceFluid, flowingFluid).bucket(bucket).tickRate(20).block(() -> (LiquidBlock) block.get());

        return new FluidHolder(
                FLUID_TYPES.register(name, () -> fluidType),
                FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(properties)),
                FLUIDS.register(flowingName, () -> new ForgeFlowingFluid.Flowing(properties)),
                ITEMS.register(bucketName, () -> new BucketItem(sourceFluid, new Item.Properties())),
                BLOCKS.register(blockName, () -> new LiquidBlock(() -> (FlowingFluid)flowingFluid.get(), BlockBehaviour.Properties.of()))
        );
    }

    private static BlockItemHolder<TagKey<Block>, TagKey<Item>> registerBlockItemTag(String modId, String group, String name) {
        ResourceLocation resourceLocation = new ResourceLocation(modId, group + "/" + name);
        return new BlockItemHolder<>(BlockTags.create(resourceLocation), ItemTags.create(resourceLocation));
    }

    private static TagKey<Item> registerItemTag(String modId, String group, String name) {
        return ItemTags.create(new ResourceLocation(modId, group + "/" + name));
    }

    private static TagKey<Fluid> registerFluidTag(String modId, String name) {
        return FluidTags.create(new ResourceLocation(modId, name));
    }

    private static String getPathOf(Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
    }
}
