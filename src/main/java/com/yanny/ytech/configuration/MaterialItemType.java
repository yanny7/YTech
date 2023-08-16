package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.*;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MaterialItemType implements INameable, IMaterialModel<Holder.ItemHolder, ItemModelProvider>, IRecipe<Holder.ItemHolder>,
        IItemTag<Holder.ItemHolder> {
    INGOT("ingot", INameable.suffix("ingot"), INameable.suffix("Ingot"),
            (material) -> ItemTags.create(Utils.forgeLoc("ingots/" + material.key)),
            Tags.Items.INGOTS,
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("ingot"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    DUST("dust", INameable.suffix("dust"), INameable.suffix("Dust"),
            (material) -> ItemTags.create(Utils.forgeLoc("dusts/" + material.key)),
            Tags.Items.DUSTS,
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("dust"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.COPPER, MaterialType.TIN)),
    RAW_MATERIAL("raw_material", INameable.prefix("raw"), INameable.prefix("Raw"),
            (material) -> ItemTags.create(Utils.forgeLoc("raw_materials/" + material.key)),
            Tags.Items.RAW_MATERIALS,
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("raw_material"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe, // handled in block
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON, MaterialType.TIN)),
    PLATE("plate", INameable.suffix("plate"), INameable.suffix("Plate"),
            (material) -> ItemTags.create(Utils.modLoc("plates/" + material.key)),
            ItemTags.create(Utils.modLoc("plates")),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("plate"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.COPPER, MaterialType.IRON)),
    BOLT("bolt", INameable.suffix("bolt"), INameable.suffix("Bolt"),
            (material) -> ItemTags.create(Utils.modLoc("bolts/" + material.key)),
            ItemTags.create(Utils.modLoc("bolts")),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("bolt"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.COPPER)),

    AXE("axe", INameable.suffix("axe"), INameable.suffix("Axe"),
            (material) -> ItemTags.create(Utils.modLoc("axes/" + material.key)),
            ItemTags.AXES,
            (holder) -> new MaterialAxeItem(holder.material.tier),
            (material) -> toolTexture(Utils.mcItemLoc("wooden_axe"), Utils.modItemLoc("axe_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialAxeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.FLINT)),
    PICKAXE("pickaxe", INameable.suffix("pickaxe"), INameable.suffix("Pickaxe"),
            (material) -> ItemTags.create(Utils.modLoc("pickaxes/" + material.key)),
            ItemTags.PICKAXES,
            (holder) -> new MaterialPickaxeItem(holder.material.tier),
            (material) -> toolTexture(Utils.mcItemLoc("wooden_pickaxe"), Utils.modItemLoc("pickaxe_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialPickaxeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.FLINT)),
    SHOVEL("shovel", INameable.suffix("shovel"), INameable.suffix("Shovel"),
            (material) -> ItemTags.create(Utils.modLoc("shovels/" + material.key)),
            ItemTags.SHOVELS,
            (holder) -> new MaterialShovelItem(holder.material.tier),
            (material) -> toolTexture(Utils.mcItemLoc("wooden_shovel"), Utils.modItemLoc("shovel_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialShovelItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.FLINT)),
    HOE("hoe", INameable.suffix("hoe"), INameable.suffix("Hoe"),
            (material) -> ItemTags.create(Utils.modLoc("hoes/" + material.key)),
            ItemTags.HOES,
            (holder) -> new MaterialHoeItem(holder.material.tier),
            (material) -> toolTexture(Utils.mcItemLoc("wooden_hoe"), Utils.modItemLoc("hoe_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialHoeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.FLINT)),
    SWORD("sword", INameable.suffix("sword"), INameable.suffix("Sword"),
            (material) -> ItemTags.create(Utils.modLoc("swords/" + material.key)),
            ItemTags.SWORDS,
            (holder) -> new MaterialSwordItem(holder.material.tier),
            (material) -> toolTexture(Utils.mcItemLoc("wooden_sword"), Utils.modItemLoc("sword_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialSwordItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.of(MaterialType.FLINT)),
    SAW("saw", INameable.suffix("saw"), INameable.suffix("Saw"), //FIXME missing item texture
            (material) -> ItemTags.create(Utils.modLoc("saws/" + material.key)),
            ItemTags.create(Utils.modLoc("saws")),
            (holder) -> new MaterialSwordItem(holder.material.tier),
            (material) -> basicTexture(Utils.modItemLoc("saw"), material),
            MaterialItemType::basicItemModelProvider,
            MaterialSwordItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.noneOf(MaterialType.class)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull public final Map<MaterialType, TagKey<Item>> itemTag;
    @NotNull public final TagKey<Item> groupTag;
    @NotNull private final Function<Holder.ItemHolder, Item> itemGetter;
    @NotNull private final Map<Integer, Integer> tintColors;
    @NotNull private final HashMap<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.ItemHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemTagsProvider> itemTagsGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialItemType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Function<MaterialType, TagKey<Item>> itemTag, @NotNull TagKey<Item> groupTag,
                     @NotNull Function<Holder.ItemHolder, Item> itemGetter, @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                     @NotNull BiConsumer<Holder.ItemHolder, ItemModelProvider> modelGetter, @NotNull BiConsumer<Holder.ItemHolder, Consumer<FinishedRecipe>> recipeGetter,
                     @NotNull BiConsumer<Holder.ItemHolder, ItemTagsProvider> itemTagsGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.itemTag = materials.stream().map((material) -> Pair.of(material, itemTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.groupTag = groupTag;
        this.itemGetter = itemGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.tintColors = new HashMap<>();
        this.textures = new HashMap<>();
        this.modelGetter = modelGetter;
        this.materials = materials;

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    this.tintColors.put(holder.tintIndex(), holder.color());
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
    public Map<Integer, Integer> getTintColors() {
        return tintColors;
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

    @Override
    public void registerTag(@NotNull Holder.ItemHolder holder, @NotNull ItemTagsProvider provider) {
        itemTagsGetter.accept(holder, provider);
    }

    public Item getItem(@NotNull Holder.ItemHolder holder) {
        return itemGetter.apply(holder);
    }

    private static Item simpleItem(@NotNull Holder.ItemHolder holder) {
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

    @NotNull
    private static TextureHolder[] basicTexture(@NotNull ResourceLocation overlay, MaterialType material) {
        return List.of(new TextureHolder(0, material.color, overlay)).toArray(TextureHolder[]::new);
    }

    @NotNull
    private static TextureHolder[] toolTexture(@NotNull ResourceLocation base, @NotNull ResourceLocation overlay, MaterialType material) {
        return List.of(new TextureHolder(-1, -1, base), new TextureHolder(1, material.color, overlay)).toArray(TextureHolder[]::new);
    }

    private static void registerMaterialTag(@NotNull Holder.ItemHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag.get(holder.material)).add(holder.item.get());
        provider.tag(holder.object.groupTag).addTag(holder.object.itemTag.get(holder.material));
    }
}
