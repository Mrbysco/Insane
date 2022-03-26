package com.mrbysco.insane;

import com.mrbysco.insane.client.ClientHandler;
import com.mrbysco.insane.client.OverlayHandler;
import com.mrbysco.insane.commands.InsaneCommands;
import com.mrbysco.insane.config.InsaneConfig;
import com.mrbysco.insane.handler.CapabilityHandler;
import com.mrbysco.insane.handler.SanityHandler;
import com.mrbysco.insane.packets.SanitySyncMessage;
import com.mrbysco.insane.registry.InsaneRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class Insane {
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Reference.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	public Insane() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, InsaneConfig.commonSpec);
		eventBus.register(InsaneConfig.class);

		InsaneRegistry.SOUND_EVENTS.register(eventBus);

		eventBus.addListener(this::setup);

		if (ModList.get().isLoaded("neat")) {
			LOGGER.info("Time to share everyone's insanity");
			LogManager.getLogger("Neat").info("Why would you do that?");
			LOGGER.info("So I could put it under Neat");
		}

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new SanityHandler());
		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::onClientSetup);
			MinecraftForge.EVENT_BUS.register(new OverlayHandler());
		});
	}

	private void setup(final FMLCommonSetupEvent event) {
		CHANNEL.registerMessage(0, SanitySyncMessage.class, SanitySyncMessage::encode, SanitySyncMessage::decode, SanitySyncMessage::handle);
	}

	@SubscribeEvent
	public void onCommandRegister(RegisterCommandsEvent event) {
		InsaneCommands.initializeCommands(event.getDispatcher());
	}
}
