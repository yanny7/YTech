package com.yanny.ytech;

import com.yanny.ytech.configuration.YTechConfigSpec;
import com.yanny.ytech.generation.DataGeneration;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationNetwork;
import com.yanny.ytech.network.irrigation.IrrigationUtils;
import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.KineticNetwork;
import com.yanny.ytech.network.kinetic.KineticUtils;
import com.yanny.ytech.registration.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;

@Mod(YTechMod.MOD_ID)
public class YTechMod {
    public static final String MOD_ID = "ytech";
    public static final DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> KINETIC_PROPAGATOR;
    public static final DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> IRRIGATION_PROPAGATOR;
    public static final YTechConfigSpec CONFIGURATION;

    private static final String PROTOCOL_VERSION = "1";
    private static final ForgeConfigSpec CONFIGURATION_SPEC;

    static {
        Pair<YTechConfigSpec, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(YTechConfigSpec::new);
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(YTechMod.MOD_ID, "network"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        CONFIGURATION = pair.getKey();
        CONFIGURATION_SPEC = pair.getValue();
        KINETIC_PROPAGATOR = KineticUtils.registerKineticPropagator(channel);
        IRRIGATION_PROPAGATOR = IrrigationUtils.registerIrrigationPropagator(channel);
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
