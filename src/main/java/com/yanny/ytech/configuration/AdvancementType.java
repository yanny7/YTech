package com.yanny.ytech.configuration;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
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

import static com.yanny.ytech.configuration.MaterialBlockType.DRYING_RACK;
import static com.yanny.ytech.configuration.MaterialItemType.AXE;
import static com.yanny.ytech.configuration.MaterialItemType.PICKAXE;
import static com.yanny.ytech.configuration.SimpleBlockType.PRIMITIVE_ALLOY_SMELTER;
import static com.yanny.ytech.configuration.SimpleBlockType.PRIMITIVE_SMELTER;
import static com.yanny.ytech.configuration.SimpleItemType.*;
import static com.yanny.ytech.configuration.Utils.getHasName;
import static com.yanny.ytech.configuration.Utils.modLoc;
import static com.yanny.ytech.registration.Registration.block;
import static com.yanny.ytech.registration.Registration.item;
import static net.minecraft.world.item.Items.FLINT;

public enum AdvancementType {
    STONE_AGE(new Builder("stone_age", "root")
            .root(FLINT, modLoc("textures/advancements/stone_age.png"), "Stone Age", "Lasted for roughly 3.4 million years and ended with the advent of metalworking.")
            .hasItems(FLINT)),
    FIRST_STEPS(new Builder("stone_age", "first_steps").parent(() -> STONE_AGE.advancement)
            .display(item(SHARP_FLINT), FrameType.TASK, "First Steps", "Hit the flint against the stone. Simple.")
            .hasTag(SHARP_FLINT.itemTag)),
    GRASS_HUNT(new Builder("stone_age", "grass_hunt").parent(() -> FIRST_STEPS.advancement)
            .display(item(GRASS_FIBERS), FrameType.TASK, "Grass Hunt", "Collect grass fibers by breaking grass with a sharp flint, it's used to make simple tools.")
            .hasTag(GRASS_FIBERS.itemTag)),
    RUN_FOREST_RUN(new Builder("stone_age", "run_forest_run").parent(() -> FIRST_STEPS.advancement)
            .display(item(ANTLER), FrameType.TASK, "Run Forest Run", "Go and hunt deer for antlers.")
            .hasTag(ANTLER.itemTag)),
    LEATHERCRAFT(new Builder("stone_age", "leathercraft").parent(() -> FIRST_STEPS.advancement)
            .display(Items.LEATHER, FrameType.TASK, "Leathercraft", "Process raw hide to create leather.")
            .hasItems(Items.LEATHER)),
    SUNNY_DAY(new Builder("stone_age", "sunny_day").parent(() -> FIRST_STEPS.advancement)
            .display(block(DRYING_RACK, MaterialType.OAK_WOOD), FrameType.CHALLENGE, "Sunny Day", "Create all dried foods.")
            .hasTag(DRYING_RACK.groupItemTag)),
    SMELTER_TIME(new Builder("stone_age", "smelter_time").parent(() -> FIRST_STEPS.advancement)
            .display(block(PRIMITIVE_SMELTER), FrameType.TASK, "Smelter Time", "Craft smelter and some chimneys to be able melt crushed ore.")
            .hasTag(PRIMITIVE_SMELTER.itemTag)),
    BETTER_THAN_NOTHING(new Builder("stone_age", "better_than_nothing").parent(() -> GRASS_HUNT.advancement)
            .display(item(FLINT_KNIFE), FrameType.TASK, "Better Than Nothing", "First real close range weapon.")
            .hasTag(FLINT_KNIFE.itemTag)),
    NOT_THAT_SIMPLE(new Builder("stone_age", "not_that_simple").parent(() -> GRASS_HUNT.advancement)
            .display(item(AXE, MaterialType.FLINT), FrameType.TASK, "Not That Simple", "Finally not breaking your fists.")
            .hasTag(AXE.itemTag.get(MaterialType.FLINT))),
    MINECRAFT(new Builder("stone_age", "minecraft").parent(() -> GRASS_HUNT.advancement)
            .display(item(PICKAXE, MaterialType.ANTLER), FrameType.TASK, "Minecraft", "Really, first pickaxe was made from antler.")
            .hasTag(PICKAXE.itemTag.get(MaterialType.ANTLER))),
    DIRTY_THINGS(new Builder("stone_age", "dirty_things").parent(() -> GRASS_HUNT.advancement)
            .display(Items.WOODEN_SHOVEL, FrameType.TASK, "Dirty Things", "Go and build shack, you earned it.")
            .hasItems(Items.WOODEN_SHOVEL)),
    STORAGE_MANAGEMENT(new Builder("stone_age", "storage_management").parent(() -> GRASS_HUNT.advancement)
            .display(item(BASKET), FrameType.TASK, "Storage Management", "Simple and useful.")
            .hasTag(BASKET.itemTag)),
    COVER_ME_IN_LEATHER(new Builder("stone_age", "cover_me_in_leather").parent(() -> LEATHERCRAFT.advancement)
            .display(Items.LEATHER_CHESTPLATE, FrameType.GOAL, "Cover Me in Leather", "Craft all parts of leather armor.")
            .hasItems(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS)),
    COPPER(new Builder("stone_age", "copper").parent(() -> SMELTER_TIME.advancement)
            .display(Items.COPPER_INGOT, FrameType.TASK, "Copper", "Smelt copper ingot.")
            .hasTag(MaterialItemType.INGOT.itemTag.get(MaterialType.COPPER))),
    TIN(new Builder("stone_age", "tin").parent(() -> SMELTER_TIME.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.TIN), FrameType.TASK, "Tin", "Smelt tin ingot.")
            .hasTag(MaterialItemType.INGOT.itemTag.get(MaterialType.TIN))),
    TAKE_THE_LEAD(new Builder("stone_age", "take_the_lead").parent(() -> SMELTER_TIME.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.LEAD), FrameType.TASK, "Take The Lead", "Smelt lead ingot.")
            .hasTag(MaterialItemType.INGOT.itemTag.get(MaterialType.LEAD))),
    IN_GOLD_WE_TRUST(new Builder("stone_age", "in_gold_we_trust").parent(() -> SMELTER_TIME.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.GOLD), FrameType.TASK, "In Gold We Trust", "Smelt gold ingot.")
            .hasTag(MaterialItemType.INGOT.itemTag.get(MaterialType.GOLD))),
    ALLOY(new Builder("stone_age", "alloy").parent(() -> COPPER.advancement)
            .display(block(PRIMITIVE_ALLOY_SMELTER), FrameType.TASK, "Alloy", "Craft the alloy smelter and raise the temperature.")
            .hasTag(PRIMITIVE_ALLOY_SMELTER.itemTag)),
    NEW_FRONTIER(new Builder("stone_age", "new_frontier").parent(() -> ALLOY.advancement)
            .display(item(MaterialItemType.INGOT, MaterialType.GOLD), FrameType.GOAL, "New Frontier", "Alloy smelting copper and tin creates new stronger material.")
            .hasTag(MaterialItemType.INGOT.itemTag.get(MaterialType.BRONZE))),
    ;

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

        Builder hasItems(@NotNull ItemLike ...items) {
            builder.addCriterion(getHasName(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
            return this;
        }

        Builder hasTag(@NotNull TagKey<Item> tag) {
            builder.addCriterion(getHasName(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()));
            return this;
        }
    }
}
