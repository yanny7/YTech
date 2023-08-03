package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.BlockObjectType;
import com.yanny.ytech.configuration.ItemObjectType;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
        HOLDER.blocks().get(BlockObjectType.RAW_STORAGE_BLOCK).forEach(((material, holder) -> {
            RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), ItemObjectType.RAW_MATERIAL, material).item;
            TagKey<Item> unpackedTag = Registration.FORGE_RAW_MATERIAL_TAGS.get(material);
            TagKey<Item> packedTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).item();
            nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
        }));
        HOLDER.blocks().get(BlockObjectType.STORAGE_BLOCK).forEach(((material, holder) -> {
            RegistryObject<Item> unpacked = GeneralUtils.getFromMap(HOLDER.items(), ItemObjectType.INGOT, material).item;
            TagKey<Item> unpackedTag = Registration.FORGE_INGOT_TAGS.get(material);
            TagKey<Item> packedTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).item();
            nineBlockStorageRecipes(recipeConsumer, RecipeCategory.MISC, unpacked, unpackedTag, RecipeCategory.BUILDING_BLOCKS, holder.block, packedTag);
        }));
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
