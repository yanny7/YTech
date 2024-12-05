package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class RemainingPartShapelessRecipe implements CraftingRecipe {
    public final String group;
    public final CraftingBookCategory category;
    public final ItemStack result;
    public final List<Ingredient> ingredients;
    @Nullable
    private PlacementInfo placementInfo;
    private final boolean isSimple;

    public RemainingPartShapelessRecipe(String p_249640_, CraftingBookCategory p_249390_, ItemStack p_252071_, List<Ingredient> p_361103_) {
        this.group = p_249640_;
        this.category = p_249390_;
        this.result = p_252071_;
        this.ingredients = p_361103_;
        this.isSimple = p_361103_.stream().allMatch(Ingredient::isSimple);
    }

    @NotNull
    public RecipeSerializer<? extends RemainingPartShapelessRecipe> getSerializer() {
        return YTechRecipeSerializers.REMAINING_PART_SHAPELESS.get();
    }

    @NotNull
    public String group() {
        return this.group;
    }

    @NotNull
    public CraftingBookCategory category() {
        return this.category;
    }

    @NotNull
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }

        return this.placementInfo;
    }

    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.size(), ItemStack.EMPTY);

        for(int i = 0; i < list.size(); ++i) {
            ItemStack item = container.getItem(i);
            ItemStack craftingRemainder = item.getCraftingRemainder();

            if (!craftingRemainder.isEmpty()) {
                list.set(i, craftingRemainder);
            } else if (item.isDamageableItem()) {
                ItemStack result = item.copy();
                list.set(i, result);

                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

                if (server != null) {
                    result.hurtAndBreak(1, server.overworld(), null, (it) -> {});
                }
            } else if (item.is(YTechItemTags.PARTS.tag)) {
                list.set(i, item.copy());
            }
        }

        return list;
    }

    public boolean matches(CraftingInput p_346123_, @NotNull Level p_44263_) {
        if (p_346123_.ingredientCount() != this.ingredients.size()) {
            return false;
        } else if (!this.isSimple) {
            ArrayList<ItemStack> nonEmptyItems = new ArrayList<>(p_346123_.ingredientCount());

            for (ItemStack item : p_346123_.items()) {
                if (!item.isEmpty()) {
                    nonEmptyItems.add(item);
                }
            }

            return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
        } else {
            return p_346123_.size() == 1 && this.ingredients.size() == 1 ? this.ingredients.getFirst().test(p_346123_.getItem(0)) : p_346123_.stackedContents().canCraft(this, null);
        }
    }

    @NotNull
    public ItemStack assemble(@NotNull CraftingInput p_345555_, @NotNull HolderLookup.Provider p_335725_) {
        return this.result.copy();
    }

    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(this.ingredients.stream().map(Ingredient::display).toList(), new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }

    public static class Serializer implements RecipeSerializer<RemainingPartShapelessRecipe> {
        private static final MapCodec<RemainingPartShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec((p_360072_) -> {
            return p_360072_.group(Codec.STRING.optionalFieldOf("group", "").forGetter((p_301127_) -> {
                return p_301127_.group;
            }), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((p_301133_) -> {
                return p_301133_.category;
            }), ItemStack.STRICT_CODEC.fieldOf("result").forGetter((p_301142_) -> {
                return p_301142_.result;
            }), Codec.lazyInitialized(() -> {
                return Ingredient.CODEC.listOf(1, 3);
            }).fieldOf("ingredients").forGetter((p_360071_) -> {
                return p_360071_.ingredients;
            })).apply(p_360072_, RemainingPartShapelessRecipe::new);
        });
        public static final StreamCodec<RegistryFriendlyByteBuf, RemainingPartShapelessRecipe> STREAM_CODEC;

        public Serializer() {
        }

        @NotNull
        public MapCodec<RemainingPartShapelessRecipe> codec() {
            return CODEC;
        }

        @NotNull
        public StreamCodec<RegistryFriendlyByteBuf, RemainingPartShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        static {
            STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, (p_360074_) -> {
                return p_360074_.group;
            }, CraftingBookCategory.STREAM_CODEC, (p_360073_) -> {
                return p_360073_.category;
            }, ItemStack.STREAM_CODEC, (p_360070_) -> {
                return p_360070_.result;
            }, Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), (p_360069_) -> {
                return p_360069_.ingredients;
            }, RemainingPartShapelessRecipe::new);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final HolderGetter<Item> items;
        private final RecipeCategory category;
        private final ItemStack result;
        private final List<Ingredient> ingredients = new ArrayList<>();
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        @Nullable
        private String group;

        private Builder(HolderGetter<Item> p_363417_, RecipeCategory p_250837_, ItemStack p_363612_) {
            this.items = p_363417_;
            this.category = p_250837_;
            this.result = p_363612_;
        }

        public static Builder shapeless(HolderGetter<Item> p_364294_, RecipeCategory p_361887_, ItemStack p_364359_) {
            return new Builder(p_364294_, p_361887_, p_364359_);
        }

        public static Builder shapeless(HolderGetter<Item> p_362315_, RecipeCategory p_250714_, ItemLike p_249659_) {
            return shapeless(p_362315_, p_250714_, p_249659_, 1);
        }

        public static Builder shapeless(HolderGetter<Item> p_360448_, RecipeCategory p_252339_, ItemLike p_250836_, int p_249928_) {
            return new Builder(p_360448_, p_252339_, p_250836_.asItem().getDefaultInstance().copyWithCount(p_249928_));
        }

        public Builder requires(TagKey<Item> p_206420_) {
            return this.requires(Ingredient.of(this.items.getOrThrow(p_206420_)));
        }

        public Builder requires(ItemLike p_126210_) {
            return this.requires((ItemLike)p_126210_, 1);
        }

        public Builder requires(ItemLike p_126212_, int p_126213_) {
            for(int i = 0; i < p_126213_; ++i) {
                this.requires(Ingredient.of(p_126212_));
            }

            return this;
        }

        public Builder requires(Ingredient p_126185_) {
            return this.requires((Ingredient)p_126185_, 1);
        }

        public Builder requires(Ingredient p_126187_, int p_126188_) {
            for(int i = 0; i < p_126188_; ++i) {
                this.ingredients.add(p_126187_);
            }

            return this;
        }

        @NotNull
        public Builder unlockedBy(@NotNull String p_176781_, @NotNull Criterion<?> p_300897_) {
            this.criteria.put(p_176781_, p_300897_);
            return this;
        }

        @NotNull
        public Builder group(@Nullable String p_126195_) {
            this.group = p_126195_;
            return this;
        }

        @NotNull
        public Item getResult() {
            return this.result.getItem();
        }

        public void save(RecipeOutput p_301215_, @NotNull ResourceKey<Recipe<?>> p_379987_) {
            this.ensureValid(p_379987_);
            Advancement.Builder advancement$builder = p_301215_.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_379987_)).rewards(AdvancementRewards.Builder.recipe(p_379987_)).requirements(AdvancementRequirements.Strategy.OR);
            Objects.requireNonNull(advancement$builder);
            this.criteria.forEach(advancement$builder::addCriterion);
            RemainingPartShapelessRecipe shapelessrecipe = new RemainingPartShapelessRecipe(Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), this.result, this.ingredients);
            p_301215_.accept(p_379987_, shapelessrecipe, advancement$builder.build(p_379987_.location().withPrefix("recipes/" + this.category.getFolderName() + "/")));
        }

        private void ensureValid(ResourceKey<Recipe<?>> p_379745_) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(p_379745_.location()));
            }
        }
    }
}
