package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.yanny.ytech.configuration.Utils.getHasName;
import static com.yanny.ytech.configuration.Utils.modLoc;
import static net.minecraft.world.item.Items.FLINT;

public enum AdvancementType {
    STONE_AGE(new Builder(AdvancementType.STONE_AGE_GROUP, "root")
            .root(FLINT, modLoc("textures/advancements/stone_age.png"), "Stone Age", "Lasted for roughly 3.4 million years and ended with the advent of metalworking.")
            .hasOneOfItems(FLINT)),
    FIRST_STEPS(new Builder(AdvancementType.STONE_AGE_GROUP, "first_steps").parent(() -> STONE_AGE.advancement)
            .display(YTechItems.SHARP_FLINT.get(), FrameType.TASK, "First Steps", "Hit the flint against the stone.")
            .hasOneOfTags(YTechItemTags.SHARP_FLINTS)),
    GRASS_HUNT(new Builder(AdvancementType.STONE_AGE_GROUP, "grass_hunt").parent(() -> FIRST_STEPS.advancement)
            .display(YTechItems.GRASS_FIBERS.get(), FrameType.TASK, "Grass Hunt", "Collect grass fibers by breaking grass with a sharp flint.")
            .hasOneOfTags(YTechItemTags.GRASS_FIBERS)),
    RUN_FOREST_RUN(new Builder(AdvancementType.STONE_AGE_GROUP, "run_forest_run").parent(() -> FIRST_STEPS.advancement)
            .display(YTechItems.ANTLER.get(), FrameType.TASK, "Run Forest Run", "Go and hunt deer for antlers.")
            .hasOneOfTags(YTechItemTags.ANTLERS)),
    LEATHERCRAFT(new Builder(AdvancementType.STONE_AGE_GROUP, "leathercraft").parent(() -> FIRST_STEPS.advancement)
            .display(Items.LEATHER, FrameType.TASK, "Leathercraft", "Process raw hide to create leather.")
            .hasOneOfItems(Items.LEATHER)),
    SUNNY_DAY(new Builder(AdvancementType.STONE_AGE_GROUP, "sunny_day").parent(() -> FIRST_STEPS.advancement)
            .display(YTechItems.DRYING_RACKS.of(MaterialType.OAK_WOOD).get(), FrameType.CHALLENGE, "Sunny Day", "Create all dried foods.")
            .hasAllTags(YTechItemTags.DRIED_BEEFS, YTechItemTags.DRIED_CHICKENS, YTechItemTags.DRIED_CODS, YTechItemTags.DRIED_MUTTONS, YTechItemTags.DRIED_PORKCHOP, YTechItemTags.DRIED_RABBITS, YTechItemTags.DRIED_SALMONS, YTechItemTags.DRIED_VENISON)),
    SMELTER_TIME(new Builder(AdvancementType.STONE_AGE_GROUP, "smelter_time").parent(() -> FIRST_STEPS.advancement)
            .display(YTechBlocks.PRIMITIVE_SMELTER.get(), FrameType.TASK, "Smelter Time", "Craft smelter and some chimneys to be able melt crushed ore.")
            .hasOneOfTags(YTechItemTags.PRIMITIVE_SMELTERS)),
    IRRIGATION_SYSTEM(new Builder(AdvancementType.STONE_AGE_GROUP, "irrigation_system").parent(() -> FIRST_STEPS.advancement)
            .display(YTechBlocks.AQUEDUCT.get(), FrameType.TASK, "Irrigation System", "Craft aqueduct for long distance water transportation.")
            .hasOneOfTags(YTechItemTags.AQUEDUCTS)),
    BETTER_THAN_NOTHING(new Builder(AdvancementType.STONE_AGE_GROUP, "better_than_nothing").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.KNIVES.of(MaterialType.FLINT).get(), FrameType.TASK, "Better Than Nothing", "Craft flint knife.")
            .hasOneOfTags(YTechItemTags.KNIVES.of(MaterialType.FLINT))),
    NOT_THAT_SIMPLE(new Builder(AdvancementType.STONE_AGE_GROUP, "not_that_simple").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.AXES.of(MaterialType.FLINT).get(), FrameType.TASK, "Not That Simple", "Craft flint axe to get wood.")
            .hasOneOfTags(YTechItemTags.AXES.of(MaterialType.FLINT))),
    MINECRAFT(new Builder(AdvancementType.STONE_AGE_GROUP, "minecraft").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.PICKAXES.of(MaterialType.ANTLER).get(), FrameType.TASK, "Minecraft", "First pickaxe was made from antler.")
            .hasOneOfTags(YTechItemTags.PICKAXES.of(MaterialType.ANTLER))),
    DIRTY_THINGS(new Builder(AdvancementType.STONE_AGE_GROUP, "dirty_things").parent(() -> GRASS_HUNT.advancement)
            .display(Items.WOODEN_SHOVEL, FrameType.TASK, "Dirty Things", "Craft wooden shovel, so you can finally build an shack.")
            .hasOneOfItems(Items.WOODEN_SHOVEL)),
    STORAGE_MANAGEMENT(new Builder(AdvancementType.STONE_AGE_GROUP, "storage_management").parent(() -> GRASS_HUNT.advancement)
            .display(YTechItems.BASKET.get(), FrameType.TASK, "Storage Management", "Increase your inventory capacity by crafting basket.")
            .hasOneOfTags(YTechItemTags.BASKETS)),
    COVER_ME_IN_LEATHER(new Builder(AdvancementType.STONE_AGE_GROUP, "cover_me_in_leather").parent(() -> LEATHERCRAFT.advancement)
            .display(Items.LEATHER_CHESTPLATE, FrameType.GOAL, "Cover Me in Leather", "Craft all parts of leather armor.")
            .hasAllItems(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS)),
    COPPER(new Builder(AdvancementType.STONE_AGE_GROUP, "copper").parent(() -> SMELTER_TIME.advancement)
            .display(Items.COPPER_INGOT, FrameType.TASK, "Copper", "Smelt copper ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.COPPER))),
    TIN(new Builder(AdvancementType.STONE_AGE_GROUP, "tin").parent(() -> SMELTER_TIME.advancement)
            .display(YTechItems.INGOTS.of(MaterialType.TIN).get(), FrameType.TASK, "Tin", "Smelt tin ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.TIN))),
    TAKE_THE_LEAD(new Builder(AdvancementType.STONE_AGE_GROUP, "take_the_lead").parent(() -> SMELTER_TIME.advancement)
            .display(YTechItems.INGOTS.of(MaterialType.LEAD).get(), FrameType.TASK, "Take The Lead", "Smelt lead ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.LEAD))),
    IN_GOLD_WE_TRUST(new Builder(AdvancementType.STONE_AGE_GROUP, "in_gold_we_trust").parent(() -> SMELTER_TIME.advancement)
            .display(Items.GOLD_INGOT, FrameType.TASK, "In Gold We Trust", "Smelt gold ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.GOLD))),
    ALLOY_SMELTER(new Builder(AdvancementType.STONE_AGE_GROUP, "alloy_smelter").parent(() -> COPPER.advancement)
            .display(YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get(), FrameType.TASK, "Alloy Smelter", "Craft the alloy smelter and raise the temperature.")
            .hasOneOfTags(YTechItemTags.PRIMITIVE_ALLOY_SMELTERS)),
    COPPER_ARMOR(new Builder(AdvancementType.STONE_AGE_GROUP, "copper_armor").parent(() -> COPPER.advancement)
            .display(YTechItems.CHESTPLATES.of(MaterialType.COPPER).get(), FrameType.GOAL, "Copper Armor", "Craft all pieces of copper armor.")
            .hasAllTags(YTechItemTags.HELMETS.of(MaterialType.COPPER), YTechItemTags.CHESTPLATES.of(MaterialType.COPPER), YTechItemTags.LEGGINGS.of(MaterialType.COPPER), YTechItemTags.BOOTS.of(MaterialType.COPPER))),
    NEW_FRONTIER(new Builder(AdvancementType.STONE_AGE_GROUP, "new_frontier").parent(() -> ALLOY_SMELTER.advancement)
            .display(YTechItems.INGOTS.of(MaterialType.BRONZE).get(), FrameType.GOAL, "New Frontier", "Smelting copper and tin together creates new stronger material.")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.BRONZE))),

    BRONZE_AGE(new Builder(AdvancementType.BRONZE_AGE_GROUP, "root")
            .root(YTechItems.AXES.of(MaterialType.BRONZE).get(), modLoc("textures/advancements/bronze_age.png"), "Bronze Age", "Bronze Age is characterized by the use of bronze")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.BRONZE))),
    HIT_HARD(new Builder(AdvancementType.BRONZE_AGE_GROUP, "hit_hard").parent(() -> BRONZE_AGE.advancement)
            .display(YTechBlocks.BRONZE_ANVIL.get(), FrameType.TASK, "Hit Hard", "Craft bronze anvil.")
            .hasOneOfTags(YTechItemTags.BRONZE_ANVILS)),
    FERTILIZING_CROPS(new Builder(AdvancementType.BRONZE_AGE_GROUP, "fertilizing_crops").parent(() -> HIT_HARD.advancement)
            .display(YTechBlocks.AQUEDUCT_FERTILIZER.get(), FrameType.TASK, "Fertilizing Crops", "Craft aqueduct fertilizer.")
            .hasOneOfTags(YTechItemTags.AQUEDUCT_FERTILIZERS)),
    BRONZE_ARMOR(new Builder(AdvancementType.STONE_AGE_GROUP, "bronze_armor").parent(() -> BRONZE_AGE.advancement)
            .display(YTechItems.CHESTPLATES.of(MaterialType.BRONZE).get(), FrameType.GOAL, "Bronze Armor", "Craft all pieces of bronze armor.")
            .hasAllTags(YTechItemTags.HELMETS.of(MaterialType.BRONZE), YTechItemTags.CHESTPLATES.of(MaterialType.BRONZE), YTechItemTags.LEGGINGS.of(MaterialType.BRONZE), YTechItemTags.BOOTS.of(MaterialType.BRONZE))),
    BRONZE_TOOLS(new Builder(AdvancementType.BRONZE_AGE_GROUP, "bronze_tools").parent(() -> BRONZE_AGE.advancement)
            .display(YTechItems.PICKAXES.of(MaterialType.BRONZE).get(), FrameType.GOAL, "Bronze Tools", "Craft all bronze tools.")
            .hasAllTags(YTechItemTags.PICKAXES.of(MaterialType.BRONZE), YTechItemTags.AXES.of(MaterialType.BRONZE), YTechItemTags.SHOVELS.of(MaterialType.BRONZE), YTechItemTags.SWORDS.of(MaterialType.BRONZE))),
    MORE_AND_MORE(new Builder(AdvancementType.BRONZE_AGE_GROUP, "more_and_more").parent(() -> BRONZE_AGE.advancement)
            .display(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get(), FrameType.TASK, "More and More", "Craft reinforced brick chimney.")
            .hasOneOfTags(YTechItemTags.REINFORCED_BRICK_CHIMNEYS)),
    BLOOMBERG(new Builder(AdvancementType.BRONZE_AGE_GROUP, "bloomberg").parent(() -> MORE_AND_MORE.advancement)
            .display(YTechItems.IRON_BLOOM.get(), FrameType.TASK, "Bloomberg", "Smelt iron with charcoal to create iron bloom.")
            .hasOneOfTags(YTechItemTags.IRON_BLOOMS)),
    IRON_MAN(new Builder(AdvancementType.BRONZE_AGE_GROUP, "iron_man").parent(() -> BLOOMBERG.advancement)
            .display(Items.IRON_INGOT, FrameType.TASK, "Iron Man", "Using hammer on iron bloom creates iron ingot.")
            .hasOneOfTags(YTechItemTags.INGOTS.of(MaterialType.IRON))),
    ;

    private static final String STONE_AGE_GROUP = "stone_age";
    private static final String BRONZE_AGE_GROUP = "bronze_age";

    private final Builder builder;
    private Advancement advancement;

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

    public void generate(@NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
        if (builder.advancement != null) {
            builder.builder.parent(builder.advancement.get());
        }

        advancement = builder.builder.save(saver, modLoc(builder.group + "/" + builder.id), existingFileHelper);
    }

    private static class Builder {
        private final Advancement.Builder builder;
        private final String group;
        private final String id;
        private Supplier<Advancement> advancement;
        private String titleId = "";
        private String title = "";
        private String descriptionId = "";
        private String description = "";

        Builder(@NotNull String group, @NotNull String id) {
            this.group = group;
            this.id  = id;
            builder = Advancement.Builder.advancement();
        }

        Builder parent(@NotNull Supplier<Advancement> advancement) {
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
            builder.requirements(RequirementsStrategy.AND);

            for (ItemLike item : items) {
                builder.addCriterion(RecipeProvider.getHasName(item), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build()));
            }

            return this;
        }

        @SafeVarargs
        final Builder hasOneOfTags(@NotNull TagKey<Item> ...tags) {
            builder.requirements(RequirementsStrategy.OR);

            for (TagKey<Item> tag : tags) {
                builder.addCriterion(getHasName(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()));
            }

            return this;
        }

        @SafeVarargs
        final Builder hasAllTags(@NotNull TagKey<Item> ...tags) {
            builder.requirements(RequirementsStrategy.AND);

            for (TagKey<Item> tag : tags) {
                builder.addCriterion(getHasName() + "_" + tag.location().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()));
            }

            return this;
        }
    }
}
