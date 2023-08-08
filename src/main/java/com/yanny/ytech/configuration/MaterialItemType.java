package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public enum MaterialItemType implements INameable, IMaterialModel<Holder.ItemHolder, ItemModelProvider> {
    INGOT("ingot", INameable.suffix("ingot"), INameable.suffix("Ingot"),
            () -> new Item(new Item.Properties()),
            (material) -> basicTexture(IModel.modItemLoc("ingot")),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DUST("dust", INameable.suffix("dust"), INameable.suffix("Dust"),
            () -> new Item(new Item.Properties()),
            (material) -> basicTexture(IModel.modItemLoc("dust")),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.TIN)),
    RAW_MATERIAL("raw_material", INameable.prefix("raw"), INameable.prefix("Raw"),
            () -> new Item(new Item.Properties()),
            (material) -> basicTexture(IModel.modItemLoc("raw_material")),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    PLATE("plate", INameable.suffix("plate"), INameable.suffix("Plate"),
            () -> new Item(new Item.Properties()),
            (material) -> basicTexture(IModel.modItemLoc("plate")),
            MaterialItemType::basicItemModelProvider,
            EnumSet.of(MaterialType.COPPER, MaterialType.IRON)),

    AXE("axe", INameable.suffix("axe"), INameable.suffix("Axe"),
            () -> new AxeItem(Tiers.WOOD, 6.0f, -3.2f, new Item.Properties()),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_axe"), IModel.modItemLoc("axe_overlay")),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    PICKAXE("pickaxe", INameable.suffix("pickaxe"), INameable.suffix("Pickaxe"),
            () -> new PickaxeItem(Tiers.WOOD, 1, -2.8f, new Item.Properties()),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_pickaxe"), IModel.modItemLoc("pickaxe_overlay")),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    SHOVEL("shovel", INameable.suffix("shovel"), INameable.suffix("Shovel"),
            () -> new ShovelItem(Tiers.WOOD, 1.5f, -3.0f, new Item.Properties()),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_shovel"), IModel.modItemLoc("shovel_overlay")),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    HOE("hoe", INameable.suffix("hoe"), INameable.suffix("Hoe"),
            () -> new HoeItem(Tiers.WOOD, 0, -3.0f, new Item.Properties()),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_hoe"), IModel.modItemLoc("hoe_overlay")),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    SWORD("sword", INameable.suffix("sword"), INameable.suffix("Sword"),
            () -> new ShovelItem(Tiers.WOOD, 3, -2.4f, new Item.Properties()),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_sword"), IModel.modItemLoc("sword_overlay")),
            MaterialItemType::toolItemModelProvider,
            EnumSet.of(MaterialType.FLINT)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull public final Set<Integer> tintIndices;
    @NotNull private final HashMap<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemModelProvider> model;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialItemType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Supplier<Item> itemGetter,
                     @NotNull Function<MaterialType, TextureHolder[]> textureGetter, @NotNull BiConsumer<Holder.ItemHolder, ItemModelProvider> model,
                     @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.itemGetter = itemGetter;
        this.tintIndices = new HashSet<>();
        this.textures = new HashMap<>();
        this.model = model;
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
        return key;
    }

    @NotNull
    @Override
    public NameHolder getNameHolder() {
        return name;
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
    public void registerModel(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        model.accept(holder, provider);
    }

    private static void basicItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.modLoc(ModelProvider.ITEM_FOLDER + "/" + holder.object.id));
    }

    private static void toolItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.mcLoc(ModelProvider.ITEM_FOLDER + "/wooden_" + holder.object.id));
        builder.texture("layer1", provider.modLoc(ModelProvider.ITEM_FOLDER + "/" + holder.object.id + "_overlay"));
    }

    private static TextureHolder[] basicTexture(ResourceLocation overlay) {
        return List.of(new TextureHolder(0, overlay)).toArray(TextureHolder[]::new);
    }

    private static TextureHolder[] toolTexture(ResourceLocation base, ResourceLocation overlay) {
        return List.of(new TextureHolder(-1, base), new TextureHolder(1, overlay)).toArray(TextureHolder[]::new);
    }
}
