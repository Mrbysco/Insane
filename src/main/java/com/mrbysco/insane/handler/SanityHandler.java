package com.mrbysco.insane.handler;

import com.mrbysco.insane.config.InsaneConfig;
import com.mrbysco.insane.registry.SanityMapStorage;
import com.mrbysco.insane.util.SanityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
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
		if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
			Player player = event.player;
			BlockPos pos = player.blockPosition();
			Level world = player.level;
			if (world.getGameTime() % 200 == 0) { //Check for darkness every 10 seconds
				if (SanityUtil.getSanity(event.player) > 0 && !player.getAbilities().instabuild) {
					final double darknessPenalty = InsaneConfig.COMMON.darknessSanity.get();

					if (darknessPenalty < 0) {
						boolean flag = world.isNight() || !world.canSeeSky(pos);
						boolean flag2 = world.getBrightness(LightLayer.BLOCK, pos) == 0;
						if (flag && flag2) {
							SanityUtil.addSanity(player, darknessPenalty);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerDamageEvent(LivingDamageEvent event) {
		Entity attacker = event.getSource().getEntity();
		if (!SanityMapStorage.entitySanityMap.isEmpty() && event.getEntityLiving() instanceof Player && attacker != null && !event.getEntityLiving().level.isClientSide) {
			Player player = (Player) event.getEntityLiving();
			ResourceLocation registryName = attacker.getType().getRegistryName();
			if (!player.getAbilities().instabuild && SanityMapStorage.entitySanityMap.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.entitySanityMap.get(registryName));
			}
		}
	}

	@SubscribeEvent
	public void craftingEvent(ItemCraftedEvent event) {
		if (!SanityMapStorage.craftingItemList.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !event.getCrafting().isEmpty() && !event.getEntityLiving().level.isClientSide) {
			Player player = event.getPlayer();
			ResourceLocation registryName = event.getCrafting().getItem().getRegistryName();
			if (!player.getAbilities().instabuild && SanityMapStorage.craftingItemList.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.craftingItemList.get(registryName));
			}
		}
	}

	@SubscribeEvent
	public void itemEatenEvent(LivingEntityUseItemEvent.Finish event) {
		if (!SanityMapStorage.foodSanityMap.isEmpty() && event.getEntityLiving() instanceof Player && !(event.getEntityLiving() instanceof FakePlayer) && !event.getItem().isEmpty() && !event.getEntityLiving().level.isClientSide) {
			Player player = (Player) event.getEntityLiving();
			ResourceLocation registryName = event.getItem().getItem().getRegistryName();
			if (!player.getAbilities().instabuild && SanityMapStorage.foodSanityMap.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.foodSanityMap.get(registryName));
			}
		}
	}

	@SubscribeEvent
	public void itemEatenEvent(EntityItemPickupEvent event) {
		if (!SanityMapStorage.pickupItemList.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !isUUIDKnown(event.getEntityLiving().level, event.getItem().getThrower()) && !event.getEntityLiving().level.isClientSide) {
			Player player = event.getPlayer();
			ResourceLocation registryName = event.getItem().getItem().getItem().getRegistryName();
			if (!player.getAbilities().instabuild && SanityMapStorage.pickupItemList.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.pickupItemList.get(registryName));
			}
		}
	}

	public boolean isUUIDKnown(Level world, UUID uuid) {
		return world.getServer() != null && world.getServer().getProfileCache().get(uuid) != null;
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void breakEvent(BreakEvent event) {
		if (!SanityMapStorage.foodSanityMap.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !event.getWorld().isClientSide()) {
			Player player = event.getPlayer();
			ResourceLocation registryName = event.getState().getBlock().getRegistryName();
			if (!player.getAbilities().instabuild && SanityMapStorage.foodSanityMap.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.foodSanityMap.get(registryName));
			}
		}
	}
}
