package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.IType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.PartType;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class YTechItemTagsProvider extends ItemTagsProvider {
    public YTechItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(YTechItemTags.ANTLERS).add(YTechItems.ANTLER.get());
        tag(YTechItemTags.BASKETS).add(YTechItems.BASKET.get());
        tag(YTechItemTags.BEESWAXES).add(YTechItems.BEESWAX.get());
        tag(YTechItemTags.BONE_NEEDLES).add(YTechItems.BONE_NEEDLE.get());
        tag(YTechItemTags.BREAD_DOUGHS).add(YTechItems.BREAD_DOUGH.get());
        tag(YTechItemTags.BRICK_MOLDS).add(YTechItems.BRICK_MOLD.get());
        tag(YTechItemTags.CLAY_BUCKETS).add(YTechItems.CLAY_BUCKET.get());
        tag(YTechItemTags.COOKED_VENISON).add(YTechItems.COOKED_VENISON.get());
        tag(YTechItemTags.DIVINING_RODS).add(YTechItems.DIVINING_ROD.get());
        tag(YTechItemTags.DRIED_BEEFS).add(YTechItems.DRIED_BEEF.get());
        tag(YTechItemTags.DRIED_CHICKENS).add(YTechItems.DRIED_CHICKEN.get());
        tag(YTechItemTags.DRIED_CODS).add(YTechItems.DRIED_COD.get());
        tag(YTechItemTags.DRIED_MUTTONS).add(YTechItems.DRIED_MUTTON.get());
        tag(YTechItemTags.DRIED_PORKCHOP).add(YTechItems.DRIED_PORKCHOP.get());
        tag(YTechItemTags.DRIED_RABBITS).add(YTechItems.DRIED_RABBIT.get());
        tag(YTechItemTags.DRIED_SALMONS).add(YTechItems.DRIED_SALMON.get());
        tag(YTechItemTags.DRIED_VENISON).add(YTechItems.DRIED_VENISON.get());
        tag(YTechItemTags.FLOURS).add(YTechItems.FLOUR.get());
        tag(YTechItemTags.GRASS_FIBERS).add(YTechItems.GRASS_FIBERS.get());
        tag(YTechItemTags.GRASS_TWINES).add(YTechItems.GRASS_TWINE.get());
        tag(YTechItemTags.IRON_BLOOMS).add(YTechItems.IRON_BLOOM.get());
        tag(YTechItemTags.LAVA_BUCKETS).add(Items.LAVA_BUCKET, YTechItems.LAVA_CLAY_BUCKET.get());
        tag(YTechItemTags.LEATHER_STRIPS).add(YTechItems.LEATHER_STRIPS.get());
        tag(YTechItemTags.MAMMOTH_TUSKS).add(YTechItems.MAMMOTH_TUSK.get());
        tag(YTechItemTags.PEBBLES).add(YTechItems.PEBBLE.get());
        tag(YTechItemTags.RAW_HIDES).add(YTechItems.RAW_HIDE.get());
        tag(YTechItemTags.RHINO_HORNS).add(YTechItems.RHINO_HORN.get());
        tag(YTechItemTags.SHARP_FLINTS).add(YTechItems.SHARP_FLINT.get());
        tag(YTechItemTags.UNFIRED_AMPHORAE).add(YTechItems.UNFIRED_AMPHORA.get());
        tag(YTechItemTags.UNFIRED_BRICKS).add(YTechItems.UNFIRED_BRICK.get());
        tag(YTechItemTags.UNFIRED_CLAY_BUCKETS).add(YTechItems.UNFIRED_CLAY_BUCKET.get());
        tag(YTechItemTags.UNFIRED_FLOWER_POTS).add(YTechItems.UNFIRED_FLOWER_POT.get());
        tag(YTechItemTags.UNFIRED_DECORATED_POTS).add(YTechItems.UNFIRED_DECORATED_POT.get());
        tag(YTechItemTags.UNLIT_TORCH).add(YTechItems.UNLIT_TORCH.get());
        tag(YTechItemTags.VENISON).add(YTechItems.VENISON.get());
        tag(YTechItemTags.WATER_BUCKETS).add(Items.WATER_BUCKET, YTechItems.WATER_CLAY_BUCKET.get());

        tag(YTechItemTags.AMPHORAE).add(YTechItems.AMPHORA.get());
        tag(YTechItemTags.AQUEDUCTS).add(YTechItems.AQUEDUCT.get());
        tag(YTechItemTags.AQUEDUCT_FERTILIZERS).add(YTechItems.AQUEDUCT_FERTILIZER.get());
        tag(YTechItemTags.AQUEDUCT_HYDRATORS).add(YTechItems.AQUEDUCT_HYDRATOR.get());
        tag(YTechItemTags.AQUEDUCT_VALVES).add(YTechItems.AQUEDUCT_VALVE.get());
        tag(YTechItemTags.BRICK_CHIMNEYS).add(YTechItems.BRICK_CHIMNEY.get());
        tag(YTechItemTags.BRONZE_ANVILS).add(YTechItems.BRONZE_ANVIL.get());
        tag(YTechItemTags.CRAFTING_WORKSPACES).add(YTechItems.CRAFTING_WORKSPACE.get());
        tag(YTechItemTags.FIRE_PITS).add(YTechItems.FIRE_PIT.get());
        tag(YTechItemTags.GRASS_BEDS).add(YTechItems.GRASS_BED.get());
        tag(YTechItemTags.MILLSTONES).add(YTechItems.MILLSTONE.get());
        tag(YTechItemTags.POTTERS_WHEELS).add(YTechItems.POTTERY_WHEEL.get());
        tag(YTechItemTags.PRIMITIVE_ALLOY_SMELTERS).add(YTechItems.PRIMITIVE_ALLOY_SMELTER.get());
        tag(YTechItemTags.PRIMITIVE_SMELTERS).add(YTechItems.PRIMITIVE_SMELTER.get());
        tag(YTechItemTags.REINFORCED_BRICKS).add(YTechItems.REINFORCED_BRICKS.get());
        tag(YTechItemTags.REINFORCED_BRICK_CHIMNEYS).add(YTechItems.REINFORCED_BRICK_CHIMNEY.get());
        tag(YTechItemTags.TERRACOTTA_BRICKS).add(YTechItems.TERRACOTTA_BRICKS.get());
        tag(YTechItemTags.TERRACOTTA_BRICK_SLABS).add(YTechItems.TERRACOTTA_BRICK_SLAB.get());
        tag(YTechItemTags.TERRACOTTA_BRICK_STAIRS).add(YTechItems.TERRACOTTA_BRICK_STAIRS.get());
        tag(YTechItemTags.THATCH).add(YTechItems.THATCH.get());
        tag(YTechItemTags.THATCH_SLABS).add(YTechItems.THATCH_SLAB.get());
        tag(YTechItemTags.THATCH_STAIRS).add(YTechItems.THATCH_STAIRS.get());
        tag(YTechItemTags.TOOL_RACKS).add(YTechItems.TOOL_RACK.get());
        tag(YTechItemTags.TREE_STUMPS).add(YTechItems.TREE_STUMP.get());
        tag(YTechItemTags.WELL_PULLEYS).add(YTechItems.WELL_PULLEY.get());
        tag(YTechItemTags.WOODEN_BOXES).add(YTechItems.WOODEN_BOX.get());

        tag(YTechItemTags.FERTILIZER).add(Items.BONE_MEAL);
        tag(YTechItemTags.FIRE_SOURCE)
                .add(Items.TORCH, Items.LANTERN, Items.CAMPFIRE, Items.FURNACE, Items.BLAST_FURNACE)
                .addTag(YTechItemTags.FIRE_PITS)
                .addTag(YTechItemTags.PRIMITIVE_ALLOY_SMELTERS)
                .addTag(YTechItemTags.PRIMITIVE_SMELTERS);
        tag(YTechItemTags.SOUL_FIRE_SOURCE).add(Items.SOUL_TORCH, Items.SOUL_LANTERN, Items.SOUL_CAMPFIRE);

        tag(YTechItemTags.AUROCHS_FOOD).add(Items.WHEAT);
        tag(YTechItemTags.AUROCHS_TEMP_ITEMS).add(Items.WHEAT);
        tag(YTechItemTags.DEER_FOOD).add(Items.APPLE, Items.CARROT, Items.WHEAT);
        tag(YTechItemTags.DEER_TEMP_ITEMS).add(Items.WHEAT);
        tag(YTechItemTags.FOWL_FOOD).addTag(Tags.Items.SEEDS);
        tag(YTechItemTags.FOWL_TEMP_ITEMS).addTag(Tags.Items.SEEDS);
        tag(YTechItemTags.MOUFLON_FOOD).add(Items.WHEAT);
        tag(YTechItemTags.MOUFLON_TEMP_ITEMS).add(Items.WHEAT);
        tag(YTechItemTags.SABER_TOOTH_TIGER_TEMP_ITEMS).add(Items.BEEF, Items.CHICKEN, Items.COD, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, Items.SALMON).addTag(YTechItemTags.VENISON);
        tag(YTechItemTags.TERROR_BIRD_TEMP_ITEMS).add(Items.BEEF, Items.CHICKEN, Items.COD, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, Items.SALMON).addTag(YTechItemTags.VENISON);
        tag(YTechItemTags.WILD_BOAR_FOOD).add(Items.CARROT, Items.POTATO, Items.APPLE);
        tag(YTechItemTags.WILD_BOAR_TEMP_ITEMS).add(Items.CARROT, Items.POTATO, Items.APPLE);
        tag(YTechItemTags.WOOLLY_MAMMOTH_TEMP_ITEMS).add(Items.CARROT, Items.APPLE);
        tag(YTechItemTags.WOOLLY_RHINO_TEMP_ITEMS).add(Items.WHEAT);

        tag(YTechItemTags.CURIOS_BRACELETS).add(YTechItems.CHLORITE_BRACELET.get());
        tag(YTechItemTags.CURIOS_CHARMS).add(YTechItems.LION_MAN.get(), YTechItems.WILD_HORSE.get());
        tag(YTechItemTags.CURIOS_NECKLACES).add(YTechItems.SHELL_BEADS.get(), YTechItems.VENUS_OF_HOHLE_FELS.get());

        tag(YTechItemTags.CHLORITE_BRACELETS).add(YTechItems.CHLORITE_BRACELET.get());
        tag(YTechItemTags.LION_MANS).add(YTechItems.LION_MAN.get());
        tag(YTechItemTags.SHELL_BEADS).add(YTechItems.SHELL_BEADS.get());
        tag(YTechItemTags.VENUS_OF_HOHLE_FELS).add(YTechItems.VENUS_OF_HOHLE_FELS.get());
        tag(YTechItemTags.WILD_HORSES).add(YTechItems.WILD_HORSE.get());

        typedTag(YTechItems.CLAY_MOLDS, YTechItemTags.CLAY_MOLDS);
        typedTag(YTechItems.PATTERNS, YTechItemTags.PATTERNS);
        typedTag(YTechItems.SAND_MOLDS, YTechItemTags.SAND_MOLDS);
        typedTag(YTechItems.UNFIRED_MOLDS, YTechItemTags.UNFIRED_MOLDS);

        PartType.ALL_PARTS.stream().sorted(Comparator.comparing(IType::key)).forEach((partType) -> {
            tag(YTechItemTags.MOLDS.get(partType))
                    .addTag(YTechItemTags.CLAY_MOLDS.get(partType))
                    .addTag(YTechItemTags.SAND_MOLDS.get(partType));
            tag(YTechItemTags.MOLDS.tag).addTag(YTechItemTags.MOLDS.get(partType));
        });

        typedTag(YTechItems.ARROWS, YTechItemTags.ARROWS);
        typedTag(YTechItems.AXES, YTechItemTags.AXES, EnumSet.of(MaterialType.GOLD, MaterialType.IRON));
        typedTag(YTechItems.BOLTS, YTechItemTags.BOLTS);
        typedTag(YTechItems.BOOTS, YTechItemTags.BOOTS, EnumSet.of(MaterialType.GOLD, MaterialType.IRON, MaterialType.LEATHER));
        typedTag(YTechItems.CHESTPLATES, YTechItemTags.CHESTPLATES, EnumSet.of(MaterialType.GOLD, MaterialType.IRON, MaterialType.LEATHER));
        typedTag(YTechItems.CRUSHED_MATERIALS, YTechItemTags.CRUSHED_MATERIALS);
        typedTag(YTechItems.FILES, YTechItemTags.FILES);
        typedTag(YTechItems.HAMMERS, YTechItemTags.HAMMERS);
        typedTag(YTechItems.HELMETS, YTechItemTags.HELMETS, EnumSet.of(MaterialType.GOLD, MaterialType.IRON, MaterialType.LEATHER));
        typedTag(YTechItems.HOES, YTechItemTags.HOES, EnumSet.of(MaterialType.GOLD, MaterialType.IRON));
        typedTag(YTechItems.INGOTS, YTechItemTags.INGOTS, MaterialType.VANILLA_METALS);
        typedTag(YTechItems.KNIVES, YTechItemTags.KNIVES);
        typedTag(YTechItems.LEGGINGS, YTechItemTags.LEGGINGS, EnumSet.of(MaterialType.GOLD, MaterialType.IRON, MaterialType.LEATHER));
        typedTag(YTechItems.MORTAR_AND_PESTLES, YTechItemTags.MORTAR_AND_PESTLES);
        typedTag(YTechItems.PICKAXES, YTechItemTags.PICKAXES, EnumSet.of(MaterialType.GOLD, MaterialType.IRON));
        typedTag(YTechItems.PLATES, YTechItemTags.PLATES);
        typedTag(YTechItems.RAW_MATERIALS, YTechItemTags.RAW_MATERIALS, MaterialType.VANILLA_METALS);
        typedTag(YTechItems.RODS, YTechItemTags.RODS);
        typedTag(YTechItems.SAWS, YTechItemTags.SAWS);
        typedTag(YTechItems.SAW_BLADES, YTechItemTags.SAW_BLADES);
        typedTag(YTechItems.SHEARS, YTechItemTags.SHEARS, EnumSet.of(MaterialType.IRON));
        typedTag(YTechItems.SHOVELS, YTechItemTags.SHOVELS, EnumSet.of(MaterialType.GOLD, MaterialType.IRON, MaterialType.WOODEN));
        typedTag(YTechItems.SPEARS, YTechItemTags.SPEARS);
        typedTag(YTechItems.SWORDS, YTechItemTags.SWORDS, EnumSet.of(MaterialType.GOLD, MaterialType.IRON));

        materialOreTag(YTechItems.DEEPSLATE_ORES, YTechItemTags.DEEPSLATE_ORES, MaterialType.VANILLA_METALS);
        typedTag(YTechItems.DRYING_RACKS, YTechItemTags.DRYING_RACKS);
        typedTag(YTechItems.GRAVEL_DEPOSITS, YTechItemTags.GRAVEL_DEPOSITS);
        materialOreTag(YTechItems.NETHER_ORES, YTechItemTags.NETHER_ORES, EnumSet.of(MaterialType.GOLD));
        typedTag(YTechItems.RAW_STORAGE_BLOCKS, YTechItemTags.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS);
        typedTag(YTechItems.SAND_DEPOSITS, YTechItemTags.SAND_DEPOSITS);
        materialOreTag(YTechItems.STONE_ORES, YTechItemTags.STONE_ORES, MaterialType.VANILLA_METALS);
        typedTag(YTechItems.STORAGE_BLOCKS, YTechItemTags.STORAGE_BLOCKS, MaterialType.VANILLA_METALS);
        typedTag(YTechItems.TANNING_RACKS, YTechItemTags.TANNING_RACKS);

        multiTypedTag(YTechItems.PARTS, YTechItemTags.PARTS);

        tag(ItemTags.ANVIL).add(YTechItems.BRONZE_ANVIL.get());
        tag(ItemTags.BEDS).add(YTechItems.GRASS_BED.get());
        tag(Tags.Items.BONES)
                .addTag(YTechItemTags.ANTLERS)
                .addTag(YTechItemTags.MAMMOTH_TUSKS)
                .addTag(YTechItemTags.RHINO_HORNS);
        tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(filteredMaterials(YTechItems.DEEPSLATE_ORES, MaterialType.VANILLA_METALS));
        tag(Tags.Items.ORES_IN_GROUND_NETHERRACK).add(filteredMaterials(YTechItems.NETHER_ORES, EnumSet.of(MaterialType.GOLD)));
        tag(Tags.Items.ORES_IN_GROUND_STONE).add(filteredMaterials(YTechItems.STONE_ORES, MaterialType.VANILLA_METALS));
        tag(ItemTags.SLABS).add(YTechItems.TERRACOTTA_BRICK_SLAB.get(), YTechItems.THATCH_SLAB.get());
        tag(ItemTags.STAIRS).add(YTechItems.TERRACOTTA_BRICK_STAIRS.get(), YTechItems.THATCH_STAIRS.get());
        tag(Tags.Items.TOOLS) //ItemTags.SWORDS, ItemTags.AXES, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES
                .addTag(YTechItemTags.FILES.tag)
                .addTag(YTechItemTags.HAMMERS.tag)
                .addTag(YTechItemTags.KNIVES.tag)
                .addTag(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .addTag(YTechItemTags.SAWS.tag);
//                .addTag(YTechItemTags.SHEARS.tag); ??? Why not part of TOOLS ??? (See ForgeItemTagsProvider)
        tag(Tags.Items.TOOLS_SHEAR)
                .addTag(YTechItemTags.SHEARS.tag);
        tag(Tags.Items.TOOLS_SPEAR)
                .addTag(YTechItemTags.SPEARS.tag);
    }

    private <E extends Enum<E> & IType> void typedTag(YTechItems.TypedItem<E> typedItem, YTechItemTags.TypedTag<E> typedTag) {
        typedItem.keySet().stream().sorted(Comparator.comparing(IType::key)).forEach((type) -> {
            tag(typedTag.get(type)).add(typedItem.get(type).get());
            tag(typedTag.tag).addTag(typedTag.get(type));
        });
    }

    private <E extends Enum<E> & IType> void typedTag(YTechItems.TypedItem<E> typedItem, YTechItemTags.TypedTag<E> typedTag, EnumSet<E> excludeTypes) {
        typedItem.keySet().stream().sorted(Comparator.comparing(IType::key)).forEach((type) -> {
            if (!excludeTypes.contains(type)) {
                tag(typedTag.get(type)).add(typedItem.get(type).get());
                tag(typedTag.tag).addTag(typedTag.get(type));
            }
        });
    }

    private <E extends Enum<E> &IType, F extends Enum<F> & IType> void multiTypedTag(YTechItems.MultiTypedItem<E, F> multiTypedItem, YTechItemTags.MultiTypedTag<E, F> multiTypedTag) {
        multiTypedItem.entrySet().stream().sorted(Comparator.comparing((e) -> e.getKey().key())).forEach((entry1) -> {
            entry1.getValue().entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().key())).forEach((entry2) -> {
                E type1 = entry1.getKey();
                F type2 = entry2.getKey();
                tag(multiTypedTag.get(type1, type2)).add(multiTypedItem.get(type1, type2).get());
                tag(multiTypedTag.getSubType(type2)).add(multiTypedItem.get(type1, type2).get());
                tag(multiTypedTag.tag).addTag(multiTypedTag.get(type1, type2));
            });
        });
    }

    private void materialOreTag(YTechItems.TypedItem<MaterialType> materialItem, YTechItemTags.TypedTag<MaterialType> materialTag, EnumSet<MaterialType> excludeMaterials) {
        materialItem.entrySet().stream().sorted(Comparator.comparing(t -> t.getKey().key)).forEach((entry) -> {
            MaterialType material = entry.getKey();

            if (!excludeMaterials.contains(material)) {
                DeferredItem<Item> item = entry.getValue();

                tag(materialTag.get(material)).add(item.get());
                tag(materialTag.tag).add(item.get());

                switch (material) {
                    case IRON -> tag(ItemTags.IRON_ORES).add(item.get().asItem());
                    case COPPER -> tag(ItemTags.COPPER_ORES).add(item.get().asItem());
                    case GOLD -> tag(ItemTags.GOLD_ORES).add(item.get().asItem());
                }
            }
        });
    }

    private static Item[] filteredMaterials(YTechItems.TypedItem<MaterialType> item, EnumSet<MaterialType> exclude) {
        return item.entrySet().stream().filter((entry) -> !exclude.contains(entry.getKey())).sorted(Comparator.comparing(t -> t.getKey().key)).map(Map.Entry::getValue).map(DeferredItem::get).toArray(Item[]::new);
    }
}
