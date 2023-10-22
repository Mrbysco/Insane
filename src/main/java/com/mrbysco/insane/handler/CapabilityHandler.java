package com.mrbysco.insane.handler;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.api.capability.ISanity;
import com.mrbysco.insane.api.capability.SanityCapability;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class CapabilityHandler {
	public static final Capability<ISanity> SANITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	@SubscribeEvent
	public void register(RegisterCapabilitiesEvent event) {
		event.register(ISanity.class);
	}

	@SubscribeEvent
	public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			event.addCapability(Reference.SANITY_CAP, new SanityCapability());
		}
	}

	@SubscribeEvent
	public void playerStartTracking(PlayerEvent.StartTracking event) {
		Player player = event.getEntity();
		if (!player.level().isClientSide) {
			LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY, null);
			sanityCap.ifPresent(c -> Insane.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new SanitySyncMessage(c, player.getUUID())));
		}
	}

	@SubscribeEvent
	public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		Player joiningPlayer = event.getEntity();
		if (!joiningPlayer.level().isClientSide) {
			BlockPos playerPos = joiningPlayer.blockPosition();

			AABB hitbox = new AABB(playerPos).inflate(5);

			//Send the joining player their own Sanity to their client
			LazyOptional<ISanity> playerCap = joiningPlayer.getCapability(CapabilityHandler.SANITY_CAPABILITY, null);
			playerCap.ifPresent(c -> Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) joiningPlayer), new SanitySyncMessage(c, joiningPlayer.getUUID())));
			//Send the joining player the sanity of nearby players
			List<Player> nearbyPlayers = joiningPlayer.level().getEntitiesOfClass(Player.class, hitbox);
			for (Player player : nearbyPlayers) {
				LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY, null);
				sanityCap.ifPresent(c -> Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) joiningPlayer), new SanitySyncMessage(c, player.getUUID())));
			}
		}
	}
}
