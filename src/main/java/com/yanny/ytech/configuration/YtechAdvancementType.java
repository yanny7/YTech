package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.yanny.ytech.configuration.Utils.getHasName;
import static com.yanny.ytech.configuration.Utils.modLoc;
import static net.minecraft.world.item.Items.FLINT;

public enum YtechAdvancementType {
    STONE_AGE(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "root")
            .root(FLINT, modLoc("textures/advancements/stone_age.png"), "Stone Age", "Lasted for roughly 3.4 million years and ended with the advent of metalworking.")
            .hasOneOfItems(FLINT)),
    FIRST_STEPS(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "first_steps").parent(() -> STONE_AGE.advancement)
            .display(YTechItems.SHARP_FLINT.get(), AdvancementType.TASK, "First Steps", "Hit the flint against the stone.")
            .hasOneOfTags(YTechItemTags.SHARP_FLINTS)),
    GRASS_HUNT(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "grass_hunt").parent(() -> FIRST_STEPS.advancement)
            .display(YTechItems.GRASS_FIBERS.get(), AdvancementType.TASK, "Grass Hunt", "Collect grass fibers by breaking grass with a sharp flint.")
            .hasOneOfTags(YTechItemTags.GRASS_FIBERS)),
    RUN_FOREST_RUN(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "run_forest_run").parent(() -> FIRST_STEPS.advancement)
            .display(YTechItems.ANTLER.get(), AdvancementType.TASK, "Run Forest Run", "Go and hunt deer for antlers.")
            .hasOneOfTags(YTechItemTags.ANTLERS)),
    LEATHERCRAFT(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "leathercraft").parent(() -> FIRST_STEPS.advancement)
            .display(Items.LEATHER, AdvancementType.TASK, "Leathercraft", "Process raw hide to create leather.")
            .hasOneOfItems(Items.LEATHER)),
    SUNNY_DAY(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "sunny_day").parent(() -> FIRST_STEPS.advancement)
            .display(YTechItems.DRYING_RACKS.get(MaterialType.OAK_WOOD).get(), AdvancementType.CHALLENGE, "Sunny Day", "Create all dried foods.")
            .hasAllTags(YTechItemTags.DRIED_BEEFS, YTechItemTags.DRIED_CHICKENS, YTechItemTags.DRIED_CODS, YTechItemTags.DRIED_MUTTONS, YTechItemTags.DRIED_PORKCHOP, YTechItemTags.DRIED_RABBITS, YTechItemTags.DRIED_SALMONS, YTechItemTags.DRIED_VENISON)),
    SMELTER_TIME(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "smelter_time").parent(() -> FIRST_STEPS.advancement)
            .display(YTechBlocks.PRIMITIVE_SMELTER.get(), AdvancementType.TASK, "Smelter Time", "Craft smelter and some chimneys to be able melt crushed ore.")
            .hasOneOfTags(YTechItemTags.PRIMITIVE_SMELTERS)),
    IRRIGATION_SYSTEM(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "irrigation_system").parent(() -> FIRST_STEPS.advancement)
            .display(YTechBlocks.AQUEDUCT.get(), AdvancementType.TASK, "Irrigation System", "Craft aqueduct for long distance water transportation.")
            .hasOneOfTags(YTechItemTags.AQUEDUCTS)),
    BETTER_THAN_NOTHING(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "better_than_nothing").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.KNIVES.get(MaterialType.FLINT).get(), AdvancementType.TASK, "Better Than Nothing", "Craft flint knife.")
            .hasOneOfTags(YTechItemTags.KNIVES.get(MaterialType.FLINT))),
    NOT_THAT_SIMPLE(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "not_that_simple").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.AXES.get(MaterialType.FLINT).get(), AdvancementType.TASK, "Not That Simple", "Craft flint axe to get wood.")
            .hasOneOfTags(YTechItemTags.AXES.get(MaterialType.FLINT))),
    MINECRAFT(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "minecraft").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.PICKAXES.get(MaterialType.ANTLER).get(), AdvancementType.TASK, "Minecraft", "First pickaxe was made from antler.")
            .hasOneOfTags(YTechItemTags.PICKAXES.get(MaterialType.ANTLER))),
    DIRTY_THINGS(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "dirty_things").parent(() -> GRASS_HUNT.advancement)
            .display(Items.WOODEN_SHOVEL, AdvancementType.TASK, "Dirty Things", "Craft wooden shovel, so you can finally build an shack.")
            .hasOneOfItems(Items.WOODEN_SHOVEL)),
    STORAGE_MANAGEMENT(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "storage_management").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.BASKET.get(), AdvancementType.TASK, "Storage Management", "Increase your inventory capacity by crafting basket.")
            .hasOneOfTags(YTechItemTags.BASKETS)),
    COVER_ME_IN_LEATHER(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "cover_me_in_leather").parent(() -> LEATHERCRAFT.advancement)
            .display(Items.LEATHER_CHESTPLATE, AdvancementType.GOAL, "Cover Me in Leather", "Craft all parts of leather armor.")
            .hasAllItems(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS)),
    COPPER(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "copper").parent(() -> SMELTER_TIME.advancement)
            .display(Items.COPPER_INGOT, AdvancementType.TASK, "Copper", "Smelt copper ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.COPPER))),
    TIN(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "tin").parent(() -> SMELTER_TIME.advancement)
            .display(YTechItems.INGOTS.get(MaterialType.TIN).get(), AdvancementType.TASK, "Tin", "Smelt tin ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.TIN))),
    TAKE_THE_LEAD(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "take_the_lead").parent(() -> SMELTER_TIME.advancement)
            .display(YTechItems.INGOTS.get(MaterialType.LEAD).get(), AdvancementType.TASK, "Take The Lead", "Smelt lead ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.LEAD))),
    IN_GOLD_WE_TRUST(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "in_gold_we_trust").parent(() -> SMELTER_TIME.advancement)
            .display(Items.GOLD_INGOT, AdvancementType.TASK, "In Gold We Trust", "Smelt gold ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.GOLD))),
    ALLOY_SMELTER(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "alloy_smelter").parent(() -> COPPER.advancement)
            .display(YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get(), AdvancementType.TASK, "Alloy Smelter", "Craft the alloy smelter and raise the temperature.")
            .hasOneOfTags(YTechItemTags.PRIMITIVE_ALLOY_SMELTERS)),
    COPPER_ARMOR(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "copper_armor").parent(() -> COPPER.advancement)
            .display(YTechItems.CHESTPLATES.get(MaterialType.COPPER).get(), AdvancementType.GOAL, "Copper Armor", "Craft all pieces of copper armor.")
            .hasAllTags(YTechItemTags.HELMETS.get(MaterialType.COPPER), YTechItemTags.CHESTPLATES.get(MaterialType.COPPER), YTechItemTags.LEGGINGS.get(MaterialType.COPPER), YTechItemTags.BOOTS.get(MaterialType.COPPER))),
    NEW_FRONTIER(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "new_frontier").parent(() -> ALLOY_SMELTER.advancement)
            .display(YTechItems.INGOTS.get(MaterialType.BRONZE).get(), AdvancementType.GOAL, "New Frontier", "Smelting copper and tin together creates new stronger material.")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.BRONZE))),

    BRONZE_AGE(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "root")
            .root(YTechItems.AXES.get(MaterialType.BRONZE).get(), modLoc("textures/advancements/bronze_age.png"), "Bronze Age", "Bronze Age is characterized by the use of bronze")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.BRONZE))),
    HIT_HARD(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "hit_hard").parent(() -> BRONZE_AGE.advancement)
            .display(YTechBlocks.BRONZE_ANVIL.get(), AdvancementType.TASK, "Hit Hard", "Craft bronze anvil.")
            .hasOneOfTags(YTechItemTags.BRONZE_ANVILS)),
    FERTILIZING_CROPS(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "fertilizing_crops").parent(() -> HIT_HARD.advancement)
            .display(YTechBlocks.AQUEDUCT_FERTILIZER.get(), AdvancementType.TASK, "Fertilizing Crops", "Craft aqueduct fertilizer.")
            .hasOneOfTags(YTechItemTags.AQUEDUCT_FERTILIZERS)),
    BRONZE_ARMOR(new Builder(YtechAdvancementType.STONE_AGE_GROUP, "bronze_armor").parent(() -> BRONZE_AGE.advancement)
            .display(YTechItems.CHESTPLATES.get(MaterialType.BRONZE).get(), AdvancementType.GOAL, "Bronze Armor", "Craft all pieces of bronze armor.")
            .hasAllTags(YTechItemTags.HELMETS.get(MaterialType.BRONZE), YTechItemTags.CHESTPLATES.get(MaterialType.BRONZE), YTechItemTags.LEGGINGS.get(MaterialType.BRONZE), YTechItemTags.BOOTS.get(MaterialType.BRONZE))),
    BRONZE_TOOLS(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "bronze_tools").parent(() -> BRONZE_AGE.advancement)
            .display(YTechItems.PICKAXES.get(MaterialType.BRONZE).get(), AdvancementType.GOAL, "Bronze Tools", "Craft all bronze tools.")
            .hasAllTags(YTechItemTags.PICKAXES.get(MaterialType.BRONZE), YTechItemTags.AXES.get(MaterialType.BRONZE), YTechItemTags.SHOVELS.get(MaterialType.BRONZE), YTechItemTags.SWORDS.get(MaterialType.BRONZE))),
    MORE_AND_MORE(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "more_and_more").parent(() -> BRONZE_AGE.advancement)
            .display(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get(), AdvancementType.TASK, "More and More", "Craft reinforced brick chimney.")
            .hasOneOfTags(YTechItemTags.REINFORCED_BRICK_CHIMNEYS)),
    BLOOMBERG(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "bloomberg").parent(() -> MORE_AND_MORE.advancement)
            .display(YTechItems.IRON_BLOOM.get(), AdvancementType.TASK, "Bloomberg", "Smelt iron with charcoal to create iron bloom.")
            .hasOneOfTags(YTechItemTags.IRON_BLOOMS)),
    IRON_MAN(new Builder(YtechAdvancementType.BRONZE_AGE_GROUP, "iron_man").parent(() -> BLOOMBERG.advancement)
            .display(Items.IRON_INGOT, AdvancementType.TASK, "Iron Man", "Using hammer on iron bloom creates iron ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.get(MaterialType.IRON))),
    ;

    private static final String STONE_AGE_GROUP = "stone_age";
    private static final String BRONZE_AGE_GROUP = "bronze_age";

    private final Builder builder;
    private AdvancementHolder advancement;

    YtechAdvancementType(@NotNull YtechAdvancementType.Builder builder) {
        this.builder = builder;
    }

    public String titleId() {
        return builder.titleId;
    }

    public String title() {
        return builder.title;
    }

    public String description() {
        return builder.description;
    }

    public String descriptionId() {
        return builder.descriptionId;
    }

    public void generate(@NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
        if (builder.advancement != null) {
            builder.builder.parent(builder.advancement.get());
        }

        advancement = builder.builder.save(saver, modLoc(builder.group + "/" + builder.id), existingFileHelper);
    }

    private static class Builder {
        private final Advancement.Builder builder;
        private final String group;
        private final String id;
        private Supplier<AdvancementHolder> advancement;
        private String titleId = "";
        private String title = "";
        private String descriptionId = "";
        private String description = "";

        Builder(@NotNull String group, @NotNull String id) {
            this.group = group;
            this.id  = id;
            builder = Advancement.Builder.advancement();
        }

        Builder parent(@NotNull Supplier<AdvancementHolder> advancement) {
            this.advancement = advancement;
            return this;
        }

        Builder root(@NotNull ItemLike icon, @NotNull ResourceLocation background, @NotNull String title, @NotNull String descr) {
            this.title = title;
            this.description = descr;
            titleId = "advancements.ytech." + group + ".title." + id;
            descriptionId = "advancements.ytech." + group + ".description." + id;

            builder.display(
                    icon,
                    Component.translatable(titleId),
                    Component.translatable(descriptionId),
                    background,
                    AdvancementType.TASK,
                    false,
                    false,
                    false
            );
            return this;
        }

        Builder display(@NotNull ItemLike icon, @NotNull AdvancementType type, @NotNull String title, @NotNull String descr) {
            this.title = title;
            this.description = descr;
            titleId = "advancements.ytech." + group + ".title." + id;
            descriptionId = "advancements.ytech." + group + ".description." + id;

            builder.display(
                    icon,
                    Component.translatable(titleId),
                    Component.translatable(descriptionId),
                    null,
                    type,
                    false,
                    false,
                    false
            );
            return this;
        }

        Builder hasOneOfItems(@NotNull ItemLike ...items) {
            builder.addCriterion(getHasName(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
            return this;
        }

        Builder hasAllItems(@NotNull ItemLike ...items) {
            builder.requirements(AdvancementRequirements.Strategy.AND);

            for (ItemLike item : items) {
                builder.addCriterion(RecipeProvider.getHasName(item), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build()));
            }

            return this;
        }

        @SafeVarargs
        final Builder hasOneOfTags(@NotNull TagKey<Item> ...tags) {
            builder.requirements(AdvancementRequirements.Strategy.OR);

            for (TagKey<Item> tag : tags) {
                builder.addCriterion(getHasName(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()));
            }

            return this;
        }

        @SafeVarargs
        final Builder hasAllTags(@NotNull TagKey<Item> ...tags) {
            builder.requirements(AdvancementRequirements.Strategy.AND);

            for (TagKey<Item> tag : tags) {
                builder.addCriterion(getHasName() + "_" + tag.location().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()));
            }

            return this;
        }
    }
}
