package com.yanny.ytech;

import com.yanny.ytech.configuration.YTechConfigSpec;
import com.yanny.ytech.generation.DataGeneration;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationClientNetwork;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.KineticClientNetwork;
import com.yanny.ytech.network.kinetic.KineticServerNetwork;
import com.yanny.ytech.registration.Registration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@Mod(YTechMod.MOD_ID)
public class YTechMod {
    public static final String MOD_ID = "ytech";
    public static DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> KINETIC_PROPAGATOR;
    public static DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> IRRIGATION_PROPAGATOR;
    public static final YTechConfigSpec CONFIGURATION;
    private static final ModConfigSpec CONFIGURATION_SPEC;

    static {
        Pair<YTechConfigSpec, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(YTechConfigSpec::new);

        CONFIGURATION = pair.getKey();
        CONFIGURATION_SPEC = pair.getValue();
    }

    public YTechMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init(modEventBus);

        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(DataGeneration::generate);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIGURATION_SPEC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> YTechMod.clientStuff(modEventBus));
    }

    public static void clientStuff(final IEventBus modEventBus) {
        modEventBus.addListener(Registration::addBlockColors);
        modEventBus.addListener(Registration::addItemColors);
    }

    public record DistHolder<Client, Server>(
            Client client,
            Server server
    ) {}
}
