package com.mrbysco.insane.packets;

import com.mrbysco.insane.api.capability.ISanity;
import com.mrbysco.insane.api.capability.SanityCapability;
import com.mrbysco.insane.handler.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class SanitySyncMessage {
	private final CompoundTag data;
	private final UUID playerUUID;

	private SanitySyncMessage(FriendlyByteBuf buf) {
		this.data = buf.readNbt();
		this.playerUUID = buf.readUUID();
	}

	public SanitySyncMessage(ISanity sanity, UUID playerUUID) {
		this.data = SanityCapability.writeNBT(sanity);
		this.playerUUID = playerUUID;
	}

	public SanitySyncMessage(CompoundTag nbt, UUID playerUUID) {
		this.data = nbt;
		this.playerUUID = playerUUID;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(data);
		buf.writeUUID(playerUUID);
	}

	public static SanitySyncMessage decode(final FriendlyByteBuf packetBuffer) {
		return new SanitySyncMessage(packetBuffer.readNbt(), packetBuffer.readUUID());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient() && ctx.getDirection().getOriginationSide().isServer()) {
				Player player = Minecraft.getInstance().level.getPlayerByUUID(this.playerUUID);
				if (player != null) {
					player.getCapability(CapabilityHandler.SANITY_CAPABILITY, null)
							.ifPresent(sanityCap -> {
								SanityCapability.readNBT(sanityCap, data);
							});
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
