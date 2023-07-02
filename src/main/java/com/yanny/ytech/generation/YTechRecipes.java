package com.yanny.ytech.generation;

import com.yanny.ytech.registration.Registration;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class YTechRecipes extends RecipeProvider {
    public YTechRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        Registration.REGISTERED_RAW_STORAGE_BLOCK_ITEMS.forEach((material, registry) -> {
            RegistryObject<Item> input = Registration.REGISTERED_RAW_METAL_ITEMS.get(material);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, registry.get())
                    .define('#', input.get()).pattern("###").pattern("###").pattern("###")
                    .unlockedBy("has_" + getItemName(input.get()), has(MinMaxBounds.Ints.atLeast(9), input.get()))
                    .save(recipeConsumer, input.getId());
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, input.get(), 9)
                    .requires(registry.get())
                    .unlockedBy("has_" + getItemName(registry.get()), has(registry.get()))
                    .save(recipeConsumer, registry.getId());
        });
    }
}
