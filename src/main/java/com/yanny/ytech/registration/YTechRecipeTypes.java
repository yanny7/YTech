package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, YTechMod.MOD_ID);

    public static final RegistryObject<RecipeType<AlloyingRecipe>> ALLOYING = create("alloying");
    public static final RegistryObject<RecipeType<BlockHitRecipe>> BLOCK_HIT = create("block_hit");
    public static final RegistryObject<RecipeType<DryingRecipe>> DRYING = create("drying");
    public static final RegistryObject<RecipeType<HammeringRecipe>> HAMMERING = create("hammering");
    public static final RegistryObject<RecipeType<MillingRecipe>> MILLING = create("milling");
    public static final RegistryObject<RecipeType<SmeltingRecipe>> SMELTING = create("smelting");
    public static final RegistryObject<RecipeType<TanningRecipe>> TANNING = create("tanning");

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> create(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
                    @Override
                    public String toString() {
                        return Utils.modLoc("drying").toString();
                    }
                }
        );
    }
}
