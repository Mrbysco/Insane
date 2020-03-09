package com.mrbysco.insane.packets;

import com.mrbysco.insane.capability.SanityCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class SanitySyncMessage {
    private CompoundNBT data;

    private SanitySyncMessage(PacketBuffer buf) {
        this.data = buf.readCompoundTag();
    }

    public SanitySyncMessage(CompoundNBT nbt) {
        this.data = nbt;
    }

    void encode(PacketBuffer buf) {
        buf.writeCompoundTag(data);
    }

    void handle(Supplier<Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient() && ctx.getDirection().getOriginationSide().isServer()) {
                PlayerEntity player = Minecraft.getInstance().player;
                player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null)
                        .ifPresent(sanityCap -> {
                            SanityCapProvider.SANITY_CAPABILITY.readNBT(sanityCap, null, data);
                        });
            }
        });
        ctx.setPacketHandled(true);
    }
}
