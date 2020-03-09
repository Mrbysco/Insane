package com.mrbysco.insane.util;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraftforge.fml.network.PacketDistributor;

public class SanityUtil {
    public static void syncSanity(ISanity sanity) {
        if (sanity.getSanity() > sanity.getSanityMax()){
            sanity.setSanity(sanity.getSanityMax());
        }
        Insane.CHANNEL.send(PacketDistributor.ALL.noArg(), new SanitySyncMessage(sanity));
    }
}
