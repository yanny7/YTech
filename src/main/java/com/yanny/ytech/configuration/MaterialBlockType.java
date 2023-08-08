package com.yanny.ytech.configuration;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.yanny.ytech.registration.Registration.HOLDER;

public enum MaterialBlockType implements MaterialEnumHolder {
    STONE_ORE("stone_ore", MaterialEnumHolder.both("stone", "ore"), MaterialEnumHolder.suffix("Ore"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)),
            Set.of(0),
            (holder, provider) -> oreBlockStateProvider(holder, provider, provider.mcLoc("block/stone")),
            MaterialBlockType::oreLootProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    NETHERRACK_ORE("netherrack_ore", MaterialEnumHolder.both("netherrack", "ore"), MaterialEnumHolder.both("Netherrack", "Ore"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)),
            Set.of(0),
            (holder, provider) -> oreBlockStateProvider(holder, provider, provider.mcLoc("block/netherrack")),
            MaterialBlockType::oreLootProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DEEPSLATE_ORE("deepslate_ore", MaterialEnumHolder.both("deepslate", "ore"), MaterialEnumHolder.both("Deepslate", "Ore"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)),
            Set.of(0),
            (holder, provider) -> oreBlockStateProvider(holder, provider, provider.mcLoc("block/deepslate")),
            MaterialBlockType::oreLootProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    STORAGE_BLOCK("storage_block", MaterialEnumHolder.suffix("block"), MaterialEnumHolder.prefix("Block of"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
            Set.of(0),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::dropsSelfProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    RAW_STORAGE_BLOCK("raw_storage_block", MaterialEnumHolder.both("raw", "block"), MaterialEnumHolder.both("Raw", "Block"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)),
            Set.of(0),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::dropsSelfProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder keyHolder;
    @NotNull private final NameHolder nameHolder;
    @NotNull public final Supplier<Block> blockGetter;
    @NotNull private final Set<Integer> tintIndices;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialBlockType(@NotNull String id, @NotNull NameHolder keyHolder, @NotNull NameHolder nameHolder, @NotNull Supplier<Block> blockGetter,
                      @NotNull Set<Integer> tintIndices, @NotNull BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.keyHolder = keyHolder;
        this.nameHolder = nameHolder;
        this.blockGetter = blockGetter;
        this.tintIndices = tintIndices;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.materials = materials;
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

    public void registerModel(Holder.BlockHolder holder, BlockStateProvider provider) {
        modelGetter.accept(holder, provider);
    }

    public void registerLoot(Holder.BlockHolder holder, BlockLootSubProvider provider) {
        lootGetter.accept(holder, provider);
    }

    private static void basicBlockStateProvider(Holder.BlockHolder holder, BlockStateProvider provider) {
        BlockModelBuilder model = provider.models().cubeAll(holder.key, provider.modLoc(ItemModelProvider.BLOCK_FOLDER + "/" + holder.object.id));

        model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction).tintindex(0));
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    private static void oreBlockStateProvider(Holder.BlockHolder holder, BlockStateProvider provider, ResourceLocation baseStone) {
        BlockModelBuilder model = provider.models().cubeAll(holder.key, baseStone);

        model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction));
        model.renderType(provider.mcLoc("cutout"));
        model.texture("overlay", provider.modLoc(ItemModelProvider.BLOCK_FOLDER + "/ore_overlay"));
        model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#overlay").cullface(direction).tintindex(0));
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    private static void dropsSelfProvider(Holder.BlockHolder holder, BlockLootSubProvider provider) {
        provider.dropSelf(holder.block.get());
    }

    private static void oreLootProvider(Holder.BlockHolder holder, BlockLootSubProvider provider) {
        provider.add(holder.block.get(), (block -> provider.createOreDrop(block, GeneralUtils.getFromMap(HOLDER.items(),
                MaterialItemType.RAW_MATERIAL, holder.material).item.get())));
    }
}
