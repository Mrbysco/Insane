package com.mrbysco.insane.util;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.api.capability.ISanity;
import com.mrbysco.insane.api.capability.SanityCapability;
import com.mrbysco.insane.handler.CapabilityHandler;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class SanityUtil {
	public static void syncSanity(ISanity sanity, Player player) {
		if (sanity.getSanity() > sanity.getSanityMax()) {
			sanity.setSanity(sanity.getSanityMax());
		}
		if (sanity.getSanity() < sanity.getSanityMin()) {
			sanity.setSanity(sanity.getSanityMin());
		}
		Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SanitySyncMessage(sanity, player.getUUID()));
	}

	public static void addSanity(Player player, double amount) {
		LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY, null);
		sanityCap.ifPresent(c -> {
			double currentSanity = c.getSanity();
			double newSanity = currentSanity + amount;
			if (newSanity > c.getSanityMax()) {
				newSanity = c.getSanityMax();
			}
			if (newSanity < c.getSanityMin()) {
				newSanity = c.getSanityMin();
			}
			c.setSanity(newSanity);
			syncSanity(c, player);
		});
	}

	public static void setSanity(Player player, double newSanity) {
		LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY, null);
		sanityCap.ifPresent(c -> {
			c.setSanity(newSanity);
			syncSanity(c, player);
		});
	}

	public static double getSanity(Player player) {
		LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY, null);
		if (sanityCap.isPresent()) {
			ISanity sanity = sanityCap.orElse(new SanityCapability());
			return sanity.getSanity();
		}
		return 0;
	}
}
