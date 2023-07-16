package com.yanny.ytech;

import com.yanny.ytech.generation.DataGeneration;
import com.yanny.ytech.network.kinetic.KineticUtils;
import com.yanny.ytech.network.kinetic.client.ClientKineticPropagator;
import com.yanny.ytech.network.kinetic.server.ServerKineticPropagator;
import com.yanny.ytech.registration.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(YTechMod.MOD_ID)
public class YTechMod {
    public static final String MOD_ID = "ytech";
    private static final String PROTOCOL_VERSION = "1";
    public static final DistHolder<ClientKineticPropagator, ServerKineticPropagator> KINETIC_PROPAGATOR;

    static {
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(YTechMod.MOD_ID, YTechMod.MOD_ID),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        KINETIC_PROPAGATOR = KineticUtils.registerKineticPropagator(channel);
    }

    public YTechMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init(modEventBus);

        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(DataGeneration::generate);

        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> YTechMod.clientStuff(modEventBus));
    }

    public static DistExecutor.SafeRunnable clientStuff(final IEventBus modEventBus) {
        return () -> {
            modEventBus.addListener(Registration::addBlockColors);
            modEventBus.addListener(Registration::addItemColors);
        };
    }

    public record DistHolder<Client, Server>(
            Client client,
            Server server
    ) {}
}
