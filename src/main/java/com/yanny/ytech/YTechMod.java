package com.yanny.ytech;

import com.yanny.ytech.configuration.YTechConfigSpec;
import com.yanny.ytech.generation.DataGeneration;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationClientNetwork;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.registration.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@Mod(YTechMod.MOD_ID)
public class YTechMod {
    public static final String MOD_ID = "ytech";
    public static DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> IRRIGATION_PROPAGATOR;
    public static final YTechConfigSpec CONFIGURATION;
    private static final ModConfigSpec CONFIGURATION_SPEC;

    static {
        Pair<YTechConfigSpec, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(YTechConfigSpec::new);

        CONFIGURATION = pair.getKey();
        CONFIGURATION_SPEC = pair.getValue();
    }

    public YTechMod(IEventBus modEventBus, ModContainer container) {
        YTechItems.register(modEventBus);
        YTechBlocks.register(modEventBus);
        YTechBlockEntityTypes.register(modEventBus);
        YTechMenuTypes.register(modEventBus);
        YTechEntityTypes.register(modEventBus);
        YTechRecipeTypes.register(modEventBus);
        YTechRecipeSerializers.register(modEventBus);
        YTechGLMCodecs.register(modEventBus);
        YTechCreativeTabs.register(modEventBus);
        YTechArmorMaterials.register(modEventBus);
        YTechDataComponentTypes.register(modEventBus);
        YTechSoundEvents.register(modEventBus);
        YTechMobEffects.register(modEventBus);

        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(DataGeneration::generate);

        container.registerConfig(ModConfig.Type.COMMON, CONFIGURATION_SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            YTechMod.clientStuff(modEventBus);
        }
    }

    public static void clientStuff(final IEventBus modEventBus) {
        modEventBus.addListener(Registration::addItemColors);
    }

    public record DistHolder<Client, Server>(
            Client client,
            Server server
    ) {}
}
