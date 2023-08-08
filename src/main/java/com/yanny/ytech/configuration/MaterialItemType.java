package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.*;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public enum MaterialItemType implements INameable, IMaterialModel<Holder.ItemHolder, ItemModelProvider>, IRecipe<Holder.ItemHolder> {
    INGOT("ingot", INameable.suffix("ingot"), INameable.suffix("Ingot"),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(IModel.modItemLoc("ingot")),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DUST("dust", INameable.suffix("dust"), INameable.suffix("Dust"),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(IModel.modItemLoc("dust")),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.TIN)),
    RAW_MATERIAL("raw_material", INameable.prefix("raw"), INameable.prefix("Raw"),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(IModel.modItemLoc("raw_material")),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe, // handled in block
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    PLATE("plate", INameable.suffix("plate"), INameable.suffix("Plate"),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(IModel.modItemLoc("plate")),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            EnumSet.of(MaterialType.COPPER, MaterialType.IRON)),

    AXE("axe", INameable.suffix("axe"), INameable.suffix("Axe"),
            (holder) -> new MaterialAxeItem(holder.material.tier),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_axe"), IModel.modItemLoc("axe_overlay")),
            MaterialItemType::toolItemModelProvider,
            MaterialAxeItem::registerRecipe,
            EnumSet.of(MaterialType.FLINT)),
    PICKAXE("pickaxe", INameable.suffix("pickaxe"), INameable.suffix("Pickaxe"),
            (holder) -> new MaterialPickaxeItem(holder.material.tier),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_pickaxe"), IModel.modItemLoc("pickaxe_overlay")),
            MaterialItemType::toolItemModelProvider,
            MaterialPickaxeItem::registerRecipe,
            EnumSet.of(MaterialType.FLINT)),
    SHOVEL("shovel", INameable.suffix("shovel"), INameable.suffix("Shovel"),
            (holder) -> new MaterialShovelItem(holder.material.tier),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_shovel"), IModel.modItemLoc("shovel_overlay")),
            MaterialItemType::toolItemModelProvider,
            MaterialShovelItem::registerRecipe,
            EnumSet.of(MaterialType.FLINT)),
    HOE("hoe", INameable.suffix("hoe"), INameable.suffix("Hoe"),
            (holder) -> new MaterialHoeItem(holder.material.tier),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_hoe"), IModel.modItemLoc("hoe_overlay")),
            MaterialItemType::toolItemModelProvider,
            MaterialHoeItem::registerRecipe,
            EnumSet.of(MaterialType.FLINT)),
    SWORD("sword", INameable.suffix("sword"), INameable.suffix("Sword"),
            (holder) -> new MaterialSwordItem(holder.material.tier),
            (material) -> toolTexture(IModel.mcItemLoc("wooden_sword"), IModel.modItemLoc("sword_overlay")),
            MaterialItemType::toolItemModelProvider,
            MaterialSwordItem::registerRecipe,
            EnumSet.of(MaterialType.FLINT)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull private final Function<Holder.ItemHolder, Item> itemGetter;
    @NotNull private final Set<Integer> tintIndices;
    @NotNull private final HashMap<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.ItemHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialItemType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Function<Holder.ItemHolder, Item> itemGetter,
                     @NotNull Function<MaterialType, TextureHolder[]> textureGetter, @NotNull BiConsumer<Holder.ItemHolder, ItemModelProvider> modelGetter,
                     @NotNull BiConsumer<Holder.ItemHolder, Consumer<FinishedRecipe>> recipeGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.itemGetter = itemGetter;
        this.recipeGetter = recipeGetter;
        this.tintIndices = new HashSet<>();
        this.textures = new HashMap<>();
        this.modelGetter = modelGetter;
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
    public ResourceLocation[] getTextures(@NotNull MaterialType material) {
        return textures.get(material);
    }

    @Override
    public void registerModel(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    public Item getItem(@NotNull Holder.ItemHolder holder) {
        return itemGetter.apply(holder);
    }

    private static Item simpleItem(Holder.ItemHolder holder) {
        return new Item(new Item.Properties());
    }

    private static void basicItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
    }

    private static void toolItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
        builder.texture("layer1", textures[1]);
    }

    private static TextureHolder[] basicTexture(ResourceLocation overlay) {
        return List.of(new TextureHolder(0, overlay)).toArray(TextureHolder[]::new);
    }

    private static TextureHolder[] toolTexture(ResourceLocation base, ResourceLocation overlay) {
        return List.of(new TextureHolder(-1, base), new TextureHolder(1, overlay)).toArray(TextureHolder[]::new);
    }
}
