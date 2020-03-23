package com.mrbysco.insane.util;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
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

    public static void addSanity(PlayerEntity player, float amount) {
        LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
        if(sanityCap.isPresent()) {
            ISanity sanity = sanityCap.orElseThrow(NullPointerException::new);
            float currentSanity = sanity.getSanity();
            float newSanity = currentSanity + amount;
            if(newSanity > sanity.getSanityMax()) { newSanity = sanity.getSanityMax(); }
            if(newSanity < sanity.getSanityMin()) { newSanity = sanity.getSanityMin(); }

            sanity.setSanity(newSanity);
            syncSanity(sanity, player);
        }
    }
}
