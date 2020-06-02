package com.mrbysco.insane.util;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import com.mrbysco.insane.capability.SanityCapability;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

public class SanityUtil {
    public static void syncSanity(ISanity sanity, PlayerEntity player) {
        if (sanity.getSanity() > sanity.getSanityMax()){  sanity.setSanity(sanity.getSanityMax()); }
        if (sanity.getSanity() < sanity.getSanityMin()){  sanity.setSanity(sanity.getSanityMin()); }
        Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SanitySyncMessage(sanity, player.getUniqueID()));
    }

    public static void addSanity(PlayerEntity player, double amount) {
        LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
        sanityCap.ifPresent(c -> {
            double currentSanity = c.getSanity();
            double newSanity = currentSanity + amount;
            if(newSanity > c.getSanityMax()) { newSanity = c.getSanityMax(); }
            if(newSanity < c.getSanityMin()) { newSanity = c.getSanityMin(); }

            c.setSanity(newSanity);
            syncSanity(c, player);
        });
    }

    public static double getSanity(PlayerEntity player) {
        LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
        if(sanityCap.isPresent()) {
            ISanity sanity = sanityCap.orElse(new SanityCapability());
            return sanity.getSanity();
        }
        return 0;
    }
}
