package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.SimpleToolType;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import static com.yanny.ytech.registration.Registration.HOLDER;

public class YTechGlobalLootModifier extends GlobalLootModifierProvider {
    public YTechGlobalLootModifier(PackOutput output) {
        super(output, YTechMod.MOD_ID);
    }

    @Override
    protected void start() {
        add("grass_drops_fibers", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.1f).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(HOLDER.simpleTools().get(SimpleToolType.SHARP_FLINT).tool.get())).build(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()
                }, HOLDER.simpleItems().get(SimpleItemType.GRASS_FIBERS).item.get())
        );
    }
}
