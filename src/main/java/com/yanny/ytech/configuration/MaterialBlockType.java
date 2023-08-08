package com.yanny.ytech.configuration;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.block.DryingRack;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.yanny.ytech.registration.Registration.HOLDER;

public enum MaterialBlockType implements INameable, IMaterialModel<Holder.BlockHolder, BlockStateProvider>, ILootable<Holder.BlockHolder, BlockLootSubProvider> {
    STONE_ORE("stone_ore", INameable.both("stone", "ore"), INameable.suffix("Ore"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)),
            (material) -> oreTexture(IModel.mcBlockLoc("stone")),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    NETHERRACK_ORE("netherrack_ore", INameable.both("netherrack", "ore"), INameable.both("Netherrack", "Ore"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)),
            (material) -> oreTexture(IModel.mcBlockLoc("netherrack")),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DEEPSLATE_ORE("deepslate_ore", INameable.both("deepslate", "ore"), INameable.both("Deepslate", "Ore"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)),
            (material) -> oreTexture(IModel.mcBlockLoc("deepslate")),
            MaterialBlockType::oreBlockStateProvider,
            MaterialBlockType::oreLootProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    STORAGE_BLOCK("storage_block", INameable.suffix("block"), INameable.prefix("Block of"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
            (material) -> basicTexture(IModel.modBlockLoc("storage_block")),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::dropsSelfProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    RAW_STORAGE_BLOCK("raw_storage_block", INameable.both("raw", "block"), INameable.both("Raw", "Block"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)),
            (material) -> basicTexture(IModel.modBlockLoc("raw_storage_block")),
            MaterialBlockType::basicBlockStateProvider,
            MaterialBlockType::dropsSelfProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),

    DRYING_RACK("drying_rack", INameable.suffix("drying_rack"), INameable.suffix("Drying Rack"),
            DryingRack::new,
            DryingRack::getTexture,
            DryingRack::registerModel,
            MaterialBlockType::dropsSelfProvider,
            EnumSet.of(MaterialType.ACACIA_WOOD, MaterialType.OAK_WOOD)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder keyHolder;
    @NotNull private final NameHolder nameHolder;
    @NotNull public final Supplier<Block> blockGetter;
    @NotNull private final HashSet<Integer> tintIndices;
    @NotNull private final HashMap<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialBlockType(@NotNull String id, @NotNull NameHolder keyHolder, @NotNull NameHolder nameHolder, @NotNull Supplier<Block> blockGetter,
                      @NotNull Function<MaterialType, TextureHolder[]> textureGetter, @NotNull BiConsumer<Holder.BlockHolder, BlockStateProvider> modelGetter,
                      @NotNull BiConsumer<Holder.BlockHolder, BlockLootSubProvider> lootGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.keyHolder = keyHolder;
        this.nameHolder = nameHolder;
        this.blockGetter = blockGetter;
        this.tintIndices = new HashSet<>();
        this.textures = new HashMap<>();
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.materials = materials;

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
    public ResourceLocation[] getTextures(MaterialType material) {
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
}
