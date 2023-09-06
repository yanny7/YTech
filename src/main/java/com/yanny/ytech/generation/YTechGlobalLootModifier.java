package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.loot_modifier.ReplaceItemModifier;
import com.yanny.ytech.registration.Registration;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

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
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()
                },
                Registration.item(SimpleItemType.GRASS_FIBERS)
        ));
        add("replace_leather_by_raw_hide", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.COW)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.HORSE)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.DONKEY)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.LLAMA)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.MULE)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.MOOSHROOM)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.TRADER_LLAMA)).build()).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.HOGLIN)).build()).build()
                },
                Items.LEATHER,
                Registration.item(SimpleItemType.RAW_HIDE)
        ));
    }
}
