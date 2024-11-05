package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, YTechMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<DryingRecipe>> DRYING = RECIPE_SERIALIZERS.register("drying", DryingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<TanningRecipe>> TANNING = RECIPE_SERIALIZERS.register("tanning", TanningRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<MillingRecipe>> MILLING = RECIPE_SERIALIZERS.register("milling", MillingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<SmeltingRecipe>> SMELTING = RECIPE_SERIALIZERS.register("smelting", SmeltingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<BlockHitRecipe>> BLOCK_HIT = RECIPE_SERIALIZERS.register("block_hit", BlockHitRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<AlloyingRecipe>> ALLOYING = RECIPE_SERIALIZERS.register("alloying", AlloyingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<HammeringRecipe>> HAMMERING = RECIPE_SERIALIZERS.register("hammering", HammeringRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<RemainingPartShapelessRecipe>> REMAINING_PART_SHAPELESS = RECIPE_SERIALIZERS.register("remaining_part_shapeless_crafting", RemainingPartShapelessRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<RemainingShapelessRecipe>> REMAINING_SHAPELESS = RECIPE_SERIALIZERS.register("remaining_shapeless_crafting", RemainingShapelessRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<RemainingShapedRecipe>> REMAINING_SHAPED = RECIPE_SERIALIZERS.register("remaining_shaped_crafting", RemainingShapedRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<PotteryRecipe>> POTTERY = RECIPE_SERIALIZERS.register("pottery", PotteryRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<WorkspaceCraftingRecipe>> WORKSPACE_CRAFTING = RECIPE_SERIALIZERS.register("workspace_crafting", WorkspaceCraftingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ChoppingRecipe>> CHOPPING = RECIPE_SERIALIZERS.register("chopping", ChoppingRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
