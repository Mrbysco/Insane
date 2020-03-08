package com.mrbysco.insane.handler;

import com.mrbysco.insane.Reference;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {
    @SubscribeEvent
    public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Reference.SANITY_CAP), new SanityCapProvider());
        }
    }

    public static LazyOptional<ISanity> getSanityCap(LivingEntity player)
    {
        return player.getCapability(SanityCapProvider.SANITY_CAPABILITY);
    }
}
