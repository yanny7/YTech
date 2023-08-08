package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechRecipes extends RecipeProvider {
    public YTechRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        HOLDER.blocks().get(MaterialBlockType.RAW_STORAGE_BLOCK).forEach(((material, holder) -> {
            RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.RAW_MATERIAL, material).item;
            TagKey<Item> unpackedTag = Registration.FORGE_RAW_MATERIAL_TAGS.get(material);
            TagKey<Item> packedTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).item();
            nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
        }));
        HOLDER.blocks().get(MaterialBlockType.STORAGE_BLOCK).forEach(((material, holder) -> {
            RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), MaterialItemType.INGOT, material).item;
            TagKey<Item> unpackedTag = Registration.FORGE_INGOT_TAGS.get(material);
            TagKey<Item> packedTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).item();
            nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
        }));

        {// grass_twine
            Item input = HOLDER.simpleItems().get(SimpleItemType.GRASS_FIBERS).item.get();
            Holder.SimpleItemHolder resultHolder = HOLDER.simpleItems().get(SimpleItemType.GRASS_TWINE);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, resultHolder.item.get())
                    .define('#', input)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy(getHasName(input), has(input))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, resultHolder.key));
        }

        {// flint_pickaxe
            Holder.ItemHolder resultHolder = HOLDER.items().get(MaterialItemType.PICKAXE).get(MaterialType.FLINT);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, resultHolder.item.get())
                    .define('S', Items.STICK)
                    .define('F', Items.FLINT)
                    .define('T', HOLDER.simpleItems().get(SimpleItemType.GRASS_TWINE).item.get())
                    .define('#', HOLDER.simpleItems().get(SimpleItemType.SHARP_FLINT).item.get())
                    .pattern("FTF")
                    .pattern("#S ")
                    .pattern(" S ")
                    .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, resultHolder.key));
        }

        {// flint_axe
            Holder.ItemHolder resultHolder = HOLDER.items().get(MaterialItemType.AXE).get(MaterialType.FLINT);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, resultHolder.item.get())
                    .define('S', Items.STICK)
                    .define('F', Items.FLINT)
                    .define('T', HOLDER.simpleItems().get(SimpleItemType.GRASS_TWINE).item.get())
                    .define('#', HOLDER.simpleItems().get(SimpleItemType.SHARP_FLINT).item.get())
                    .pattern("FT#")
                    .pattern("FS ")
                    .pattern(" S ")
                    .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, resultHolder.key));
        }

        {// flint_shovel
            Holder.ItemHolder resultHolder = HOLDER.items().get(MaterialItemType.SHOVEL).get(MaterialType.FLINT);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, resultHolder.item.get())
                    .define('S', Items.STICK)
                    .define('F', Items.FLINT)
                    .define('T', HOLDER.simpleItems().get(SimpleItemType.GRASS_TWINE).item.get())
                    .define('#', HOLDER.simpleItems().get(SimpleItemType.SHARP_FLINT).item.get())
                    .pattern("FT#")
                    .pattern(" S ")
                    .pattern(" S ")
                    .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, resultHolder.key));
        }

        {// flint_hoe
            Holder.ItemHolder resultHolder = HOLDER.items().get(MaterialItemType.HOE).get(MaterialType.FLINT);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, resultHolder.item.get())
                    .define('S', Items.STICK)
                    .define('F', Items.FLINT)
                    .define('T', HOLDER.simpleItems().get(SimpleItemType.GRASS_TWINE).item.get())
                    .define('#', HOLDER.simpleItems().get(SimpleItemType.SHARP_FLINT).item.get())
                    .pattern("#TF")
                    .pattern(" S ")
                    .pattern(" S ")
                    .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, resultHolder.key));
        }

        {// flint_sword
            Holder.ItemHolder resultHolder = HOLDER.items().get(MaterialItemType.SWORD).get(MaterialType.FLINT);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, resultHolder.item.get())
                    .define('S', Items.STICK)
                    .define('F', Items.FLINT)
                    .define('T', HOLDER.simpleItems().get(SimpleItemType.GRASS_TWINE).item.get())
                    .define('#', HOLDER.simpleItems().get(SimpleItemType.SHARP_FLINT).item.get())
                    .pattern("#F")
                    .pattern(" T")
                    .pattern(" S")
                    .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, resultHolder.key));
        }
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> recipeConsumer, RecipeCategory unpackedCategory,
                                                RegistryObject<?> unpacked, TagKey<Item> unpackedTag, RecipeCategory packedCategory,
                                                RegistryObject<?> packed, TagKey<Item> packedTag) {
        String unpackedPath = unpacked.getId().getPath();
        String packedPath = packed.getId().getPath();
        String unpackedName = unpackedPath + "_to_" + packedPath;
        String packedName = packedPath + "_to_" + unpackedPath;
        nineBlockStorageRecipes(recipeConsumer, unpackedCategory, (ItemLike) unpacked.get(), unpackedTag, packedCategory,
                (ItemLike) packed.get(), packedTag, packedName, packedPath, unpackedName, unpackedPath);
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> recipeConsumer, RecipeCategory unpackedCategory,
                                                ItemLike unpacked, TagKey<Item> unpackedTag, RecipeCategory packedCategory,
                                                ItemLike packed, TagKey<Item> packedTag, String packedName, @Nullable String packedGroup,
                                                String unpackedName, @Nullable String unpackedGroup) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9)
                .requires(packedTag)
                .group(unpackedGroup)
                .unlockedBy(getHasName(packed), has(packedTag))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('#', unpackedTag)
                .pattern("###").pattern("###").pattern("###")
                .group(packedGroup).unlockedBy(getHasName(unpacked), has(unpackedTag))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, packedName));
    }
}
