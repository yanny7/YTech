package com.yanny.ytech.loot_modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechGLMCodecs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ReplaceItemModifier extends LootModifier {
    public static final Supplier<Codec<ReplaceItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst ->
            codecStart(inst)
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("oldItem").forGetter(m -> m.oldItem))
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("newItem").forGetter(m -> m.newItem))
                    .apply(inst, ReplaceItemModifier::new)));
    private final Item oldItem;
    private final Item newItem;

    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item oldItem, Item newItem) {
        super(conditionsIn);
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack item = generatedLoot.get(i);

            if (item.is(oldItem)) {
                generatedLoot.set(i, new ItemStack(newItem, item.getCount()));
            }
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return YTechGLMCodecs.REPLACE_ITEM.get();
    }
}
