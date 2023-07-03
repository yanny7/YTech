package com.yanny.ytech.generation;

import com.yanny.ytech.registration.Registration;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
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
            storageBlockRecipe(recipeConsumer, Registration.REGISTRATION_HOLDER.rawMaterial().get(material), registry);
        });
    }

    private void storageBlockRecipe(Consumer<FinishedRecipe> recipeConsumer, RegistryObject<Item> item, RegistryObject<Block> compressed) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, compressed.get())
                .define('#', item.get()).pattern("###").pattern("###").pattern("###")
                .unlockedBy("has_" + getItemName(item.get()), has(MinMaxBounds.Ints.atLeast(9), item.get()))
                .save(recipeConsumer, item.getId());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item.get(), 9)
                .requires(compressed.get())
                .unlockedBy("has_" + getItemName(compressed.get()), has(compressed.get()))
                .save(recipeConsumer, compressed.getId());
    }
}
