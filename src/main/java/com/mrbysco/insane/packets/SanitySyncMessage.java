package com.mrbysco.insane.packets;

import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class SanitySyncMessage {
    private CompoundNBT data;
    private UUID playerUUID;

    private SanitySyncMessage(PacketBuffer buf) {
        this.data = buf.readCompoundTag();
        this.playerUUID = buf.readUniqueId();
    }

    public SanitySyncMessage(ISanity sanity, UUID playerUUID) {
        this.data = (CompoundNBT) SanityCapProvider.SANITY_CAPABILITY.writeNBT(sanity, null);
        this.playerUUID = playerUUID;
    }

    public SanitySyncMessage(CompoundNBT nbt, UUID playerUUID) {
        this.data = nbt;
        this.playerUUID = playerUUID;
    }

    public void encode(PacketBuffer buf) {
        buf.writeCompoundTag(data);
        buf.writeUniqueId(playerUUID);
    }

    public static SanitySyncMessage decode(final PacketBuffer packetBuffer) {
        return new SanitySyncMessage(packetBuffer.readCompoundTag(), packetBuffer.readUniqueId());
    }

    public void handle(Supplier<Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient() && ctx.getDirection().getOriginationSide().isServer()) {
                PlayerEntity player = Minecraft.getInstance().world.getPlayerByUuid(this.playerUUID);
                player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null)
                        .ifPresent(sanityCap -> {
                            SanityCapProvider.SANITY_CAPABILITY.readNBT(sanityCap, null, data);
                        });
            }
        });
        ctx.setPacketHandled(true);
    }
}
