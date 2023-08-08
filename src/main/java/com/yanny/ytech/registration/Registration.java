package com.yanny.ytech.registration;

import com.mojang.serialization.Codec;
import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import com.yanny.ytech.machine.block.BlockFactory;
import com.yanny.ytech.machine.container.ContainerMenuFactory;
import com.yanny.ytech.network.kinetic.block.KineticBlockFactory;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Registration {
    public static final RegistrationHolder HOLDER = new RegistrationHolder();

    public static final Map<MaterialType, BlockItemHolder<TagKey<Block>, TagKey<Item>>> FORGE_ORE_TAGS = new HashMap<>();
    public static final Map<MaterialType, BlockItemHolder<TagKey<Block>, TagKey<Item>>> FORGE_STORAGE_BLOCK_TAGS = new HashMap<>();
    public static final Map<MaterialType, BlockItemHolder<TagKey<Block>, TagKey<Item>>> FORGE_RAW_STORAGE_BLOCK_TAGS = new HashMap<>();
    public static final Map<MaterialType, TagKey<Item>> FORGE_RAW_MATERIAL_TAGS = new HashMap<>();
    public static final Map<MaterialType, TagKey<Item>> FORGE_INGOT_TAGS = new HashMap<>();
    public static final Map<MaterialType, TagKey<Item>> FORGE_DUST_TAGS = new HashMap<>();
    public static final Map<MaterialType, TagKey<Fluid>> FORGE_FLUID_TAGS = new HashMap<>();

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
                switch (itemObject) {
                    case INGOT -> FORGE_INGOT_TAGS.put(material, registerItemTag("forge", "ingots", material.key));
                    case DUST -> FORGE_DUST_TAGS.put(material, registerItemTag("forge", "dusts", material.key));
                    case RAW_MATERIAL -> FORGE_RAW_MATERIAL_TAGS.put(material, registerItemTag("forge", "raw_materials", material.key));
                }

                HOLDER.items().computeIfAbsent(itemObject, (p) -> new HashMap<>()).compute(material, (k, v) -> uniqueKey(v, itemObject,
                        (object) -> new Holder.ItemHolder(object, material, (holder) -> ITEMS.register(holder.key, holder.object.itemGetter))));
            }
        }
        for (MaterialBlockType blockObject : MaterialBlockType.values()) {
            for (MaterialType material : blockObject.materials) {
                switch (blockObject) {
                    case STORAGE_BLOCK -> FORGE_STORAGE_BLOCK_TAGS.put(material, registerBlockItemTag("forge", "storage_blocks", material.key));
                    case RAW_STORAGE_BLOCK -> FORGE_RAW_STORAGE_BLOCK_TAGS.put(material, registerBlockItemTag("forge", "storage_blocks", "raw_" + material.key));
                    case STONE_ORE, NETHERRACK_ORE, DEEPSLATE_ORE -> FORGE_ORE_TAGS.computeIfAbsent(material, (m) -> registerBlockItemTag("forge", "ores", m.key));
                }

                HOLDER.blocks().computeIfAbsent(blockObject, (p) -> new HashMap<>()).compute(material, (k, v) -> uniqueKey(v, blockObject,
                        (object) -> new Holder.BlockHolder(object, material, Registration::registerBlockItem)));
            }
        }
        for (MaterialFluidType fluidObject : MaterialFluidType.values()) {
            for (MaterialType material : fluidObject.materials) {

                if (fluidObject == MaterialFluidType.FLUID) {
                    FORGE_FLUID_TAGS.put(material, registerFluidTag("forge", material.key));
                }

                HOLDER.fluids().computeIfAbsent(fluidObject, (p) -> new HashMap<>()).compute(material, (k, v) -> uniqueKey(v, fluidObject,
                        (object) -> registerFluid(object, material)));
            }
        }

        for (MachineType machine : MachineType.values()) {
            HOLDER.machine().put(machine, Arrays.stream(TierType.values())
                    .filter((tier) -> tier.ordinal() >= machine.fromTier.ordinal())
                    .map((tier) -> new Tuple<>(tier, registerMachine(machine, tier)))
                    .collect(Collectors.toMap(Tuple::getA, Tuple::getB, (a, b) -> a, HashMap::new)));
        }

        for (KineticBlockType kinetic : KineticBlockType.values()) {
            HashMap<MaterialType, KineticNetworkHolder> holderMap = new HashMap<>();

            HOLDER.kineticNetwork().put(kinetic, holderMap);

            for (KineticBlockType.KineticMaterial kineticMaterial : kinetic.materials) {
                holderMap.put(kineticMaterial.material(), registerKineticBlock(kinetic, kineticMaterial));
            }
        }

        for (SimpleItemType type : SimpleItemType.values()) {
            HOLDER.simpleItems().put(type, new Holder.SimpleItemHolder(type, ITEMS.register(type.key, type.itemGetter)));
        }
        for (SimpleBlockType type : SimpleBlockType.values()) {
            RegistryObject<Block> block = BLOCKS.register(type.key, type.blockSupplier);
            ITEMS.register(type.key, () -> new BlockItem(block.get(), new Item.Properties()));
            HOLDER.simpleBlocks().put(type, new Holder.SimpleBlockHolder(type, block));
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
            GeneralUtils.mapToStream(HOLDER.machine()).forEach(h -> event.accept(h.block.get()));
            GeneralUtils.mapToStream(HOLDER.kineticNetwork()).forEach(h -> event.accept(h.block.get()));
            HOLDER.simpleItems().values().forEach(h -> event.accept(h.item.get()));
            HOLDER.simpleBlocks().values().forEach(h -> event.accept(h.block.get()));
        }
    }

    public static void addBlockColors(RegisterColorHandlersEvent.Block event) {
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> event.register((b, g, p, t) -> getTintColor(h, t), h.block.get()));
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        GeneralUtils.mapToStream(HOLDER.items()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.item.get()));
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.block.get()));
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach(h -> event.register((i, t) -> getTintColor(h, t), h.bucket.get()));
    }

    private static RegistryObject<Block> registerBlockItem(Holder.BlockHolder holder) {
        RegistryObject<Block> block = BLOCKS.register(holder.key, holder.object.blockGetter);
        ITEMS.register(holder.key, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static MachineHolder registerMachine(MachineType machine, TierType tier) {
        String key = tier.key + "_" + machine.key;
        RegistryObject<Block> block = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, key), ForgeRegistries.BLOCKS);
        RegistryObject<BlockEntityType<? extends BlockEntity>> machineBlockEntity = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, key), ForgeRegistries.BLOCK_ENTITY_TYPES);

        return new MachineHolder(
                machine,
                tier,
                BLOCKS.register(key, () -> BlockFactory.create(machineBlockEntity, machine, tier)),
                ITEMS.register(key, () -> new BlockItem(block.get(), new Item.Properties())),
                BLOCK_ENTITY_TYPES.register(key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                        Objects.requireNonNull(((EntityBlock) block.get()).newBlockEntity(pos, blockState)), block.get()).build(null)),
                MENU_TYPES.register(key, () -> IForgeMenuType.create(((windowId, inv, data) -> ContainerMenuFactory.create(windowId, inv.player, data.readBlockPos(), machine, tier))))
        );
    }

    private static KineticNetworkHolder registerKineticBlock(KineticBlockType blockType, KineticBlockType.KineticMaterial kineticMaterial) {
        String key = kineticMaterial.material().key + "_" + blockType.key;
        RegistryObject<Block> block = RegistryObject.create(new ResourceLocation(YTechMod.MOD_ID, key), ForgeRegistries.BLOCKS);

        return new KineticNetworkHolder(
                blockType,
                kineticMaterial.material(),
                BLOCKS.register(key, () -> KineticBlockFactory.create(blockType, kineticMaterial)),
                ITEMS.register(key, () -> new BlockItem(block.get(), new Item.Properties())),
                BLOCK_ENTITY_TYPES.register(key, () -> BlockEntityType.Builder.of((pos, blockState) ->
                        Objects.requireNonNull(((EntityBlock) block.get()).newBlockEntity(pos, blockState)), block.get()).build(null))
        );
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

    private static RegistryObject<CreativeModeTab> registerCreativeTab() {
        Supplier<ItemStack> iconSupplier = () -> new ItemStack(GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.INGOT, MaterialType.GOLD).item.get());
        return CREATIVE_TABS.register(YTechMod.MOD_ID, () -> CreativeModeTab.builder().icon(iconSupplier).build());
    }

    private static <U extends MaterialEnumHolder, V extends Holder.MaterialHolder<U>> V uniqueKey(V v, U object, Function<U, V> consumer) {
        if (v == null) {
            return consumer.apply(object);
        } else {
            throw new IllegalStateException("Material already exists");
        }
    }

    private static <U extends MaterialEnumHolder> int getTintColor(Holder.MaterialHolder<U> h, int t) {
        if (h.object.getTintIndices().contains(t)) {
            return h.material.color;
        } else {
            return 0xFFFFFFFF;
        }
    }
}
