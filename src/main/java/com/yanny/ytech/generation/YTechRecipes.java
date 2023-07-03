package com.yanny.ytech.generation;

import com.yanny.ytech.registration.Registration;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class YTechRecipes extends RecipeProvider {
    public YTechRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> {
            RegistryObject<Item> unpacked = Registration.REGISTRATION_HOLDER.rawMaterial().get(material);
            TagKey<Item> unpackedTag = Registration.FORGE_RAW_MATERIAL_TAGS.get(material);
            TagKey<Item> packedTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).item();
            nineBlockStorageRecipes(recipeConsumer, unpacked, unpackedTag, registry, packedTag);
        });
    }

    private void nineBlockStorageRecipes(Consumer<FinishedRecipe> recipeConsumer, RegistryObject<Item> unpacked, TagKey<Item> unpackedTag, RegistryObject<Block> packed, TagKey<Item> packedTag) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, packed.get())
                .define('#', unpackedTag).pattern("###").pattern("###").pattern("###")
                .unlockedBy("has_" + getItemName(unpacked.get()), has(MinMaxBounds.Ints.atLeast(9), unpacked.get()))
                .save(recipeConsumer, unpacked.getId());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, unpacked.get(), 9)
                .requires(packedTag)
                .unlockedBy("has_" + getItemName(packed.get()), has(packed.get()))
                .save(recipeConsumer, packed.getId());
    }
}
