package com.mrbysco.insane.handler;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.util.SanityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SanityHandler {
    @SubscribeEvent
    public void playerTickEvent(PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START && event.side.isServer() && event.player.world.getGameTime() % 20 == 0) {
            World world = event.player.world;
            if (world.getGameTime() % 20 == 0) {
                if (SanityUtil.getSanity(event.player) >= 0) {
                    //TODO: Check for stuff and then apply sanity
                    Insane.LOGGER.info(Insane.entitySanityMap.toString());
                }
            }
        }
    }

    @SubscribeEvent
    public void playerDamageEvent(LivingDamageEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity && !(event.getSource().getTrueSource() != null) && !event.getEntityLiving().world.isRemote) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            Entity attacker = event.getSource().getImmediateSource();
            ResourceLocation registryName = attacker.getType().getRegistryName();
            if(Insane.entitySanityMap.containsKey(registryName)) {
                SanityUtil.addSanity(player, Insane.entitySanityMap.get(registryName).doubleValue());
            }
        }
    }
}
