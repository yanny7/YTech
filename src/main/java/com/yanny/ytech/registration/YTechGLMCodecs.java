package com.yanny.ytech.registration;

import com.mojang.serialization.MapCodec;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.loot_modifier.ReplaceItemModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class YTechGLMCodecs {
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM_CODECS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, YTechMod.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddItemModifier>> ADD_ITEM = GLM_CODECS.register("add_item", AddItemModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceItemModifier>> REPLACE_ITEM = GLM_CODECS.register("replace_item", ReplaceItemModifier.CODEC);

    public static void register(IEventBus eventBus) {
        GLM_CODECS.register(eventBus);
    }
}
