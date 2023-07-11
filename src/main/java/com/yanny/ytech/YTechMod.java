package com.yanny.ytech;

import com.yanny.ytech.generation.DataGeneration;
import com.yanny.ytech.network.KineticPropagator;
import com.yanny.ytech.registration.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(YTechMod.MOD_ID)
public class YTechMod {
    public static final String MOD_ID = "ytech";
    public static final KineticPropagator ROTARY_PROPAGATOR = new KineticPropagator();

    public YTechMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init(modEventBus);

        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(Registration::addBlockColors);
        modEventBus.addListener(Registration::addItemColors);
        modEventBus.addListener(DataGeneration::generate);
    }
}
