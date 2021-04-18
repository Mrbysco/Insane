package com.mrbysco.insane.handler;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.config.InsaneConfig;
import com.mrbysco.insane.util.SanityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class SanityHandler {
    @SubscribeEvent
    public void playerTickEvent(PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START && event.side.isServer()) {
            PlayerEntity player = event.player;
            BlockPos pos = player.getPosition();
            World world = player.world;
            if (world.getGameTime() % 200 == 0) { //Check for darkness every 10 seconds
                if (SanityUtil.getSanity(event.player) > 0 && !player.abilities.isCreativeMode) {
                    final double darknessPenalty = InsaneConfig.COMMON.darknessSanity.get();

                    if(darknessPenalty < 0) {
                        boolean flag = world.isNightTime() || !world.canSeeSky(pos);
                        boolean flag2 = world.getLightFor(LightType.BLOCK, pos) == 0;
                        if(flag && flag2) {
                            SanityUtil.addSanity(player, darknessPenalty);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void playerDamageEvent(LivingDamageEvent event) {
        Entity attacker = event.getSource().getTrueSource();
        if(!Insane.entitySanityMap.isEmpty() && event.getEntityLiving() instanceof PlayerEntity && attacker != null && !event.getEntityLiving().world.isRemote) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            ResourceLocation registryName = attacker.getType().getRegistryName();
            if(!player.abilities.isCreativeMode && Insane.entitySanityMap.containsKey(registryName)) {
                SanityUtil.addSanity(player, Insane.entitySanityMap.get(registryName));
            }
        }
    }

    @SubscribeEvent
    public void craftingEvent(ItemCraftedEvent event) {
        if(!Insane.craftingItemList.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !event.getCrafting().isEmpty() && !event.getEntityLiving().world.isRemote) {
            PlayerEntity player = event.getPlayer();
            ResourceLocation registryName = event.getCrafting().getItem().getRegistryName();
            if(!player.abilities.isCreativeMode && Insane.craftingItemList.containsKey(registryName)) {
                SanityUtil.addSanity(player, Insane.craftingItemList.get(registryName));
            }
        }
    }

    @SubscribeEvent
    public void itemEatenEvent(LivingEntityUseItemEvent.Finish event) {
        if(!Insane.foodSanityMap.isEmpty() && event.getEntityLiving() instanceof PlayerEntity && !(event.getEntityLiving() instanceof FakePlayer) && !event.getItem().isEmpty() && !event.getEntityLiving().world.isRemote) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            ResourceLocation registryName = event.getItem().getItem().getRegistryName();
            if(!player.abilities.isCreativeMode && Insane.foodSanityMap.containsKey(registryName)) {
                SanityUtil.addSanity(player, Insane.foodSanityMap.get(registryName));
            }
        }
    }

    @SubscribeEvent
    public void itemEatenEvent(EntityItemPickupEvent event) {
        if(!Insane.pickupItemList.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !isUUIDKnown(event.getEntityLiving().world, event.getItem().getThrowerId()) && !event.getEntityLiving().world.isRemote) {
            PlayerEntity player = event.getPlayer();
            ResourceLocation registryName = event.getItem().getItem().getItem().getRegistryName();
            if(!player.abilities.isCreativeMode && Insane.pickupItemList.containsKey(registryName)) {
                SanityUtil.addSanity(player, Insane.pickupItemList.get(registryName));
            }
        }
    }

    public boolean isUUIDKnown(World world, UUID uuid) {
        return world.getServer() != null && world.getServer().getPlayerProfileCache().getProfileByUUID(uuid) != null;
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void breakEvent(BreakEvent event) {
        if(!Insane.foodSanityMap.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !event.getWorld().isRemote()) {
            PlayerEntity player = event.getPlayer();
            ResourceLocation registryName = event.getState().getBlock().getRegistryName();
            if(!player.abilities.isCreativeMode && Insane.foodSanityMap.containsKey(registryName)) {
                SanityUtil.addSanity(player, Insane.foodSanityMap.get(registryName));
            }
        }
    }
}
