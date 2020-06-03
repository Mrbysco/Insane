package com.mrbysco.insane;

import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapability;
import com.mrbysco.insane.capability.SanityStorage;
import com.mrbysco.insane.client.ClientHandler;
import com.mrbysco.insane.config.InsaneConfig;
import com.mrbysco.insane.handler.CapabilityHandler;
import com.mrbysco.insane.handler.SanityHandler;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(Reference.MOD_ID)
public class Insane
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static HashMap<ResourceLocation, Double> entitySanityMap = new HashMap<>();
    public static HashMap<ResourceLocation, Double> foodSanityMap = new HashMap<>();
    public static HashMap<ResourceLocation, Double> craftingItemList = new HashMap<>();

    public Insane() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.COMMON, InsaneConfig.commonSpec);
        eventBus.register(InsaneConfig.class);

        eventBus.addListener(this::setup);

        if(ModList.get().isLoaded("neat")) {
            LOGGER.info("Time to share everyone's insanity");
            LogManager.getLogger("Neat").info("Why would you do that?");
            LOGGER.info("So I could put it under Neat");
        }

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(new SanityHandler());

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new ClientHandler());
        });
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        CHANNEL.registerMessage(0, SanitySyncMessage.class, SanitySyncMessage::encode, SanitySyncMessage::decode, SanitySyncMessage::handle);

        CapabilityManager.INSTANCE.register(ISanity.class, new SanityStorage(), SanityCapability::new);
    }

    public static void updateMaps() {
        HashMap<ResourceLocation, Double> entityMap = new HashMap<>();
        List<? extends String> mobList = InsaneConfig.COMMON.mobDamageList.get();
        if(!mobList.isEmpty()) {
            for(String string : mobList) {
                String[] array = string.split(",");
                if(array.length == 2) {
                    ResourceLocation location = new ResourceLocation(array[0]);
                    double amount = new Double(array[1]).doubleValue();

                    entityMap.put(location, amount);
                }
            }
        }
        entitySanityMap = entityMap;

        HashMap<ResourceLocation, Double> foodMap = new HashMap<>();
        List<? extends String> foodList = InsaneConfig.COMMON.rawFoodList.get();
        if(!foodList.isEmpty()) {
            for(String string : foodList) {
                String[] array = string.split(",");
                if(array.length == 2) {
                    ResourceLocation location = new ResourceLocation(array[0]);
                    double amount = new Double(array[1]).doubleValue();

                    entityMap.put(location, amount);
                }
            }
        }
        foodSanityMap = foodMap;

        HashMap<ResourceLocation, Double> craftingMap = new HashMap<>();
        List<? extends String> craftingItems = InsaneConfig.COMMON.craftingItemList.get();
        if(!craftingItems.isEmpty()) {
            for(String string : craftingItems) {
                String[] array = string.split(",");
                if(array.length == 2) {
                    ResourceLocation location = new ResourceLocation(array[0]);
                    double amount = new Double(array[1]).doubleValue();

                    entityMap.put(location, amount);
                }
            }
        }
        craftingItemList = craftingMap;
    }
}
