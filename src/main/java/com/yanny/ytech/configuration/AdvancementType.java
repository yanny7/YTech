package com.yanny.ytech.configuration;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.FrameType;
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

import static com.yanny.ytech.configuration.MaterialBlockType.DRYING_RACK;
import static com.yanny.ytech.configuration.MaterialItemType.*;
import static com.yanny.ytech.configuration.SimpleBlockType.*;
import static com.yanny.ytech.configuration.SimpleItemType.*;
import static com.yanny.ytech.configuration.Utils.getHasName;
import static com.yanny.ytech.configuration.Utils.modLoc;
import static com.yanny.ytech.registration.Registration.block;
import static com.yanny.ytech.registration.Registration.item;
import static net.minecraft.world.item.Items.FLINT;

public enum AdvancementType {
    STONE_AGE(new Builder(AdvancementType.STONE_AGE_GROUP, "root")
            .root(FLINT, modLoc("textures/advancements/stone_age.png"), "Stone Age", "Lasted for roughly 3.4 million years and ended with the advent of metalworking.")
            .hasOneOfItems(FLINT)),
    FIRST_STEPS(new Builder(AdvancementType.STONE_AGE_GROUP, "first_steps").parent(() -> STONE_AGE.advancement)
            .display(item(SHARP_FLINT), FrameType.TASK, "First Steps", "Hit the flint against the stone.")
            .hasOneOfTags(SHARP_FLINT.itemTag)),
    GRASS_HUNT(new Builder(AdvancementType.STONE_AGE_GROUP, "grass_hunt").parent(() -> FIRST_STEPS.advancement)
            .display(item(GRASS_FIBERS), FrameType.TASK, "Grass Hunt", "Collect grass fibers by breaking grass with a sharp flint.")
            .hasOneOfTags(GRASS_FIBERS.itemTag)),
    RUN_FOREST_RUN(new Builder(AdvancementType.STONE_AGE_GROUP, "run_forest_run").parent(() -> FIRST_STEPS.advancement)
            .display(item(ANTLER), FrameType.TASK, "Run Forest Run", "Go and hunt deer for antlers.")
            .hasOneOfTags(ANTLER.itemTag)),
    LEATHERCRAFT(new Builder(AdvancementType.STONE_AGE_GROUP, "leathercraft").parent(() -> FIRST_STEPS.advancement)
            .display(Items.LEATHER, FrameType.TASK, "Leathercraft", "Process raw hide to create leather.")
            .hasOneOfItems(Items.LEATHER)),
    SUNNY_DAY(new Builder(AdvancementType.STONE_AGE_GROUP, "sunny_day").parent(() -> FIRST_STEPS.advancement)
            .display(block(DRYING_RACK, MaterialType.OAK_WOOD), FrameType.CHALLENGE, "Sunny Day", "Create all dried foods.")
            .hasAllTags(DRIED_BEEF.itemTag, DRIED_CHICKEN.itemTag, DRIED_COD.itemTag, DRIED_MUTTON.itemTag, DRIED_PORKCHOP.itemTag, DRIED_RABBIT.itemTag, DRIED_SALMON.itemTag, DRIED_VENISON.itemTag)),
    SMELTER_TIME(new Builder(AdvancementType.STONE_AGE_GROUP, "smelter_time").parent(() -> FIRST_STEPS.advancement)
            .display(block(PRIMITIVE_SMELTER), FrameType.TASK, "Smelter Time", "Craft smelter and some chimneys to be able melt crushed ore.")
            .hasOneOfTags(PRIMITIVE_SMELTER.itemTag)),
    IRRIGATION_SYSTEM(new Builder(AdvancementType.STONE_AGE_GROUP, "irrigation_system").parent(() -> FIRST_STEPS.advancement)
            .display(block(AQUEDUCT), FrameType.TASK, "Irrigation System", "Craft aqueduct for long distance water transportation.")
            .hasOneOfTags(AQUEDUCT.itemTag)),
    BETTER_THAN_NOTHING(new Builder(AdvancementType.STONE_AGE_GROUP, "better_than_nothing").parent(() -> GRASS_HUNT.advancement)
            .display(item(FLINT_KNIFE), FrameType.TASK, "Better Than Nothing", "Craft flint knife.")
            .hasOneOfTags(FLINT_KNIFE.itemTag)),
    NOT_THAT_SIMPLE(new Builder(AdvancementType.STONE_AGE_GROUP, "not_that_simple").parent(() -> GRASS_HUNT.advancement)
            .display(item(AXE, MaterialType.FLINT), FrameType.TASK, "Not That Simple", "Craft flint axe to get wood.")
            .hasOneOfTags(AXE.itemTag.get(MaterialType.FLINT))),
    MINECRAFT(new Builder(AdvancementType.STONE_AGE_GROUP, "minecraft").parent(() -> GRASS_HUNT.advancement)
            .display(item(PICKAXE, MaterialType.ANTLER), FrameType.TASK, "Minecraft", "First pickaxe was made from antler.")
            .hasOneOfTags(PICKAXE.itemTag.get(MaterialType.ANTLER))),
    DIRTY_THINGS(new Builder(AdvancementType.STONE_AGE_GROUP, "dirty_things").parent(() -> GRASS_HUNT.advancement)
            .display(Items.WOODEN_SHOVEL, FrameType.TASK, "Dirty Things", "Craft wooden shovel, so you can finally build an shack.")
            .hasOneOfItems(Items.WOODEN_SHOVEL)),
    STORAGE_MANAGEMENT(new Builder(AdvancementType.STONE_AGE_GROUP, "storage_management").parent(() -> GRASS_HUNT.advancement)
            .display(item(BASKET), FrameType.TASK, "Storage Management", "Increase your inventory capacity by crafting basket.")
            .hasOneOfTags(BASKET.itemTag)),
    COVER_ME_IN_LEATHER(new Builder(AdvancementType.STONE_AGE_GROUP, "cover_me_in_leather").parent(() -> LEATHERCRAFT.advancement)
            .display(Items.LEATHER_CHESTPLATE, FrameType.GOAL, "Cover Me in Leather", "Craft all parts of leather armor.")
            .hasAllItems(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS)),
    COPPER(new Builder(AdvancementType.STONE_AGE_GROUP, "copper").parent(() -> SMELTER_TIME.advancement)
            .display(Items.COPPER_INGOT, FrameType.TASK, "Copper", "Smelt copper ingot.")
            .hasOneOfTags(MaterialItemType.INGOT.itemTag.get(MaterialType.COPPER))),
    TIN(new Builder(AdvancementType.STONE_AGE_GROUP, "tin").parent(() -> SMELTER_TIME.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.TIN), FrameType.TASK, "Tin", "Smelt tin ingot.")
            .hasOneOfTags(MaterialItemType.INGOT.itemTag.get(MaterialType.TIN))),
    TAKE_THE_LEAD(new Builder(AdvancementType.STONE_AGE_GROUP, "take_the_lead").parent(() -> SMELTER_TIME.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.LEAD), FrameType.TASK, "Take The Lead", "Smelt lead ingot.")
            .hasOneOfTags(MaterialItemType.INGOT.itemTag.get(MaterialType.LEAD))),
    IN_GOLD_WE_TRUST(new Builder(AdvancementType.STONE_AGE_GROUP, "in_gold_we_trust").parent(() -> SMELTER_TIME.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.GOLD), FrameType.TASK, "In Gold We Trust", "Smelt gold ingot.")
            .hasOneOfTags(MaterialItemType.INGOT.itemTag.get(MaterialType.GOLD))),
    ALLOY_SMELTER(new Builder(AdvancementType.STONE_AGE_GROUP, "alloy_smelter").parent(() -> COPPER.advancement)
            .display(block(PRIMITIVE_ALLOY_SMELTER), FrameType.TASK, "Alloy Smelter", "Craft the alloy smelter and raise the temperature.")
            .hasOneOfTags(PRIMITIVE_ALLOY_SMELTER.itemTag)),
    COPPER_ARMOR(new Builder(AdvancementType.STONE_AGE_GROUP, "copper_armor").parent(() -> COPPER.advancement)
            .display(item(CHESTPLATE, MaterialType.COPPER), FrameType.GOAL, "Copper Armor", "Craft all pieces of copper armor.")
            .hasAllTags(HELMET.itemTag.get(MaterialType.COPPER), CHESTPLATE.itemTag.get(MaterialType.COPPER), LEGGINGS.itemTag.get(MaterialType.COPPER), BOOTS.itemTag.get(MaterialType.COPPER))),
    NEW_FRONTIER(new Builder(AdvancementType.STONE_AGE_GROUP, "new_frontier").parent(() -> ALLOY_SMELTER.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.BRONZE), FrameType.GOAL, "New Frontier", "Smelting copper and tin together creates new stronger material.")
            .hasOneOfTags(MaterialItemType.INGOT.itemTag.get(MaterialType.BRONZE))),

    BRONZE_AGE(new Builder(AdvancementType.BRONZE_AGE_GROUP, "root")
            .root(item(AXE, MaterialType.BRONZE), modLoc("textures/advancements/bronze_age.png"), "Bronze Age", "Bronze Age is characterized by the use of bronze")
            .hasOneOfTags(MaterialItemType.INGOT.itemTag.get(MaterialType.BRONZE))),
    HIT_HARD(new Builder(AdvancementType.BRONZE_AGE_GROUP, "hit_hard").parent(() -> BRONZE_AGE.advancement)
            .display(block(BRONZE_ANVIL), FrameType.TASK, "Hit Hard", "Craft bronze anvil.")
            .hasOneOfTags(BRONZE_ANVIL.itemTag)),
    FERTILIZING_CROPS(new Builder(AdvancementType.BRONZE_AGE_GROUP, "fertilizing_crops").parent(() -> HIT_HARD.advancement)
            .display(block(AQUEDUCT_FERTILIZER), FrameType.TASK, "Fertilizing Crops", "Craft aqueduct fertilizer.")
            .hasOneOfTags(AQUEDUCT_FERTILIZER.itemTag)),
    BRONZE_ARMOR(new Builder(AdvancementType.STONE_AGE_GROUP, "bronze_armor").parent(() -> BRONZE_AGE.advancement)
            .display(item(CHESTPLATE, MaterialType.BRONZE), FrameType.GOAL, "Bronze Armor", "Craft all pieces of bronze armor.")
            .hasAllTags(HELMET.itemTag.get(MaterialType.BRONZE), CHESTPLATE.itemTag.get(MaterialType.BRONZE), LEGGINGS.itemTag.get(MaterialType.BRONZE), BOOTS.itemTag.get(MaterialType.BRONZE))),
    BRONZE_TOOLS(new Builder(AdvancementType.BRONZE_AGE_GROUP, "bronze_tools").parent(() -> BRONZE_AGE.advancement)
            .display(item(PICKAXE, MaterialType.BRONZE), FrameType.GOAL, "Bronze Tools", "Craft all bronze tools.")
            .hasAllTags(PICKAXE.itemTag.get(MaterialType.BRONZE), AXE.itemTag.get(MaterialType.BRONZE), SHOVEL.itemTag.get(MaterialType.BRONZE), SWORD.itemTag.get(MaterialType.BRONZE))),
    MORE_AND_MORE(new Builder(AdvancementType.BRONZE_AGE_GROUP, "more_and_more").parent(() -> BRONZE_AGE.advancement)
            .display(block(REINFORCED_BRICK_CHIMNEY), FrameType.TASK, "More and More", "Craft reinforced brick chimney.")
            .hasOneOfTags(REINFORCED_BRICK_CHIMNEY.itemTag)),
    BLOOMBERG(new Builder(AdvancementType.BRONZE_AGE_GROUP, "bloomberg").parent(() -> MORE_AND_MORE.advancement)
            .display(item(IRON_BLOOM), FrameType.TASK, "Bloomberg", "Smelt iron with charcoal to create iron bloom.")
            .hasOneOfTags(IRON_BLOOM.itemTag)),
    IRON_MAN(new Builder(AdvancementType.BRONZE_AGE_GROUP, "iron_man").parent(() -> BLOOMBERG.advancement)
            .display(Items.IRON_INGOT, FrameType.TASK, "Iron Man", "Using hammer on iron bloom creates iron ingot.")
            .hasOneOfTags(INGOT.itemTag.get(MaterialType.IRON))),
    ;

    private static final String STONE_AGE_GROUP = "stone_age";
    private static final String BRONZE_AGE_GROUP = "bronze_age";

    private final Builder builder;
    private AdvancementHolder advancement;

    AdvancementType(@NotNull AdvancementType.Builder builder) {
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
                    FrameType.TASK,
                    false,
                    false,
                    false
            );
            return this;
        }

        Builder display(@NotNull ItemLike icon, @NotNull FrameType type, @NotNull String title, @NotNull String descr) {
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
