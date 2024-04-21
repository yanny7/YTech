package com.yanny.ytech.registration;

import com.mojang.serialization.Codec;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.loot_modifier.AddItemModifier;
import com.yanny.ytech.loot_modifier.ReplaceItemModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechGLMCodecs {
    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM_CODECS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, YTechMod.MOD_ID);

    public static final RegistryObject<Codec<AddItemModifier>> ADD_ITEM = GLM_CODECS.register("add_item", AddItemModifier.CODEC);
    public static final RegistryObject<Codec<ReplaceItemModifier>> REPLACE_ITEM = GLM_CODECS.register("replace_item", ReplaceItemModifier.CODEC);

    public static void register(IEventBus eventBus) {
        GLM_CODECS.register(eventBus);
    }
}
