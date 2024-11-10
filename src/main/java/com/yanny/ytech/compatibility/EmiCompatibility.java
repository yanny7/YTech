package com.yanny.ytech.compatibility;

import com.yanny.ytech.compatibility.emi.*;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.BiFunction;

@EmiEntrypoint
public class EmiCompatibility implements EmiPlugin {
    public static final ResourceLocation TEXTURE = Utils.modLoc("textures/gui/emi.png");

    @Override
    public void register(EmiRegistry emiRegistry) {
        registerRecipe(emiRegistry, YTechRecipeTypes.ALLOYING, EmiAlloyingRecipe.CATEGORY, EmiAlloyingRecipe.WORKSTATION, EmiAlloyingRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.BLOCK_HIT, EmiBlockHitRecipe.CATEGORY, EmiBlockHitRecipe.WORKSTATION, EmiBlockHitRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.CHOPPING, EmiChoppingRecipe.CATEGORY, EmiChoppingRecipe.WORKSTATION, EmiChoppingRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.DRYING, EmiDryingRecipe.CATEGORY, EmiDryingRecipe.WORKSTATION, EmiDryingRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.HAMMERING, EmiHammeringRecipe.CATEGORY, EmiHammeringRecipe.WORKSTATION, EmiHammeringRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.MILLING, EmiMillingRecipe.CATEGORY, EmiMillingRecipe.WORKSTATION, EmiMillingRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.POTTERY, EmiPotteryRecipe.CATEGORY, EmiPotteryRecipe.WORKSTATION, EmiPotteryRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.SMELTING, EmiSmeltingRecipe.CATEGORY, EmiSmeltingRecipe.WORKSTATION, EmiSmeltingRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.TANNING, EmiTanningRecipe.CATEGORY, EmiTanningRecipe.WORKSTATION, EmiTanningRecipe::new);
        registerRecipe(emiRegistry, YTechRecipeTypes.WORKSPACE_CRAFTING, EmiWorkspaceCraftingRecipe.CATEGORY, EmiWorkspaceCraftingRecipe.WORKSTATION, EmiWorkspaceCraftingRecipe::new);
    }

    private <T extends Recipe<Container>> void registerRecipe(EmiRegistry registry, DeferredHolder<RecipeType<?>, RecipeType<T>> recipe, EmiRecipeCategory category, EmiIngredient icon, BiFunction<T, ResourceLocation, EmiRecipe> supplier) {
        RecipeManager manager = registry.getRecipeManager();

        registry.addCategory(category);
        registry.addWorkstation(category, icon);

        for (RecipeHolder<T> r : manager.getAllRecipesFor(recipe.value())) {
            registry.addRecipe(supplier.apply(r.value(), r.id()));
        }
    }

    public static <T extends Recipe<?>> ResourceLocation ref(DeferredHolder<RecipeType<?>, RecipeType<T>> recipeType) {
        return new ResourceLocation(recipeType.get().toString());
    }
}
