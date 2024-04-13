package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.loot_modifier.ReplaceItemModifier;
import com.yanny.ytech.registration.Registration;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.Objects;

public class YTechGlobalLootModifier extends GlobalLootModifierProvider {
    public YTechGlobalLootModifier(PackOutput output) {
        super(output, YTechMod.MOD_ID);
    }

    @Override
    protected void start() {
        add("grass_drops_fibers", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.1f).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(SimpleItemType.SHARP_FLINT.itemTag)).build(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SHORT_GRASS).build()
                },
                Registration.item(SimpleItemType.GRASS_FIBERS)
        ));
        add("gravel_drops_pebble", new AddItemModifier(
                new LootItemCondition[]{
                        LootItemRandomChanceCondition.randomChance(0.2f).build(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRAVEL).build()
                },
                Registration.item(SimpleItemType.PEBBLE)
        ));

        addReplaceLeatherByRawHide(EntityType.COW);
        addReplaceLeatherByRawHide(EntityType.HORSE);
        addReplaceLeatherByRawHide(EntityType.DONKEY);
        addReplaceLeatherByRawHide(EntityType.LLAMA);
        addReplaceLeatherByRawHide(EntityType.MULE);
        addReplaceLeatherByRawHide(EntityType.MOOSHROOM);
        addReplaceLeatherByRawHide(EntityType.TRADER_LLAMA);
        addReplaceLeatherByRawHide(EntityType.HOGLIN);
    }

    private void addReplaceLeatherByRawHide(EntityType<?> entityType) {
        add(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entityType)).getPath() + "_replace_leather_by_raw_hide", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityType)).build()).build()
                },
                Items.LEATHER,
                Registration.item(SimpleItemType.RAW_HIDE)
        ));
    }
}
