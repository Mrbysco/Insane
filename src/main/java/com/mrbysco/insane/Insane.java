package com.mrbysco.insane;

import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapability;
import com.mrbysco.insane.capability.SanityStorage;
import com.mrbysco.insane.handler.CapabilityHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class Insane
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Insane() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SpoiledConfig.serverSpec);
//        eventBus.register(SpoiledConfig.class);

        eventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        CapabilityManager.INSTANCE.register(ISanity.class, new SanityStorage(), SanityCapability::new);
    }
}
