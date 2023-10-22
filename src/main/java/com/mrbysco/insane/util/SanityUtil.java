package com.mrbysco.insane.util;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.api.capability.ISanity;
import com.mrbysco.insane.handler.CapabilityHandler;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class SanityUtil {
	public static void syncSanity(ISanity sanity, Player player) {
		Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SanitySyncMessage(sanity, player.getUUID()));
	}

	public static void addSanity(Player player, double amount) {
		ISanity cap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY).orElse(null);
		if (cap != null) {
			double currentSanity = cap.getSanity();
			cap.setSanity(currentSanity + amount);
			syncSanity(cap, player);
		}
	}

	public static void setSanity(Player player, double newSanity) {
		ISanity cap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY).orElse(null);
		if (cap != null) {
			cap.setSanity(newSanity);
			syncSanity(cap, player);
		}
	}

	public static double getSanity(Player player) {
		ISanity cap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY).orElse(null);
		if (cap != null) {
			return cap.getSanity();
		}
		return 0;
	}
}
