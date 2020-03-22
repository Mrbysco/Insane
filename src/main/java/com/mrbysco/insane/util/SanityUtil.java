package com.mrbysco.insane.util;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class SanityUtil {
    public static void syncSanity(ISanity sanity, UUID uuid) {
        if (sanity.getSanity() > sanity.getSanityMax()){
            sanity.setSanity(sanity.getSanityMax());
        }
        Insane.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.noArg(), new SanitySyncMessage(sanity, uuid));
    }
}
