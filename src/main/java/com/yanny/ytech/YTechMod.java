package com.yanny.ytech;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.generation.DataGeneration;
import com.yanny.ytech.registration.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(YTechMod.MOD_ID)
public class YTechMod {
    public static final String MOD_ID = "ytech";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final YTechConfigLoader CONFIGURATION = new YTechConfigLoader();

    public YTechMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(Registration::addBlockColors);
        modEventBus.addListener(Registration::addItemColors);
        modEventBus.addListener(DataGeneration::generate);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
