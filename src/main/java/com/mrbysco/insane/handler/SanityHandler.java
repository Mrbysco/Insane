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
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class SanityHandler {
	@SubscribeEvent
	public void playerTickEvent(PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
			Player player = event.player;
			BlockPos pos = player.blockPosition();
			Level level = player.level();
			//Check for darkness every 200 ticks (10 seconds)
			if (level.getGameTime() % 200 == 0 && SanityUtil.getSanity(event.player) > 0 && !player.getAbilities().instabuild) {
				final double darknessPenalty = InsaneConfig.COMMON.darknessSanity.get();

				if (darknessPenalty < 0) {
					boolean flag = level.isNight() || !level.canSeeSky(pos);
					boolean flag2 = level.getBrightness(LightLayer.BLOCK, pos) == 0;
					if (flag && flag2) {
						SanityUtil.addSanity(player, darknessPenalty);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerDamageEvent(LivingDamageEvent event) {
		Entity attacker = event.getSource().getEntity();
		if (!SanityMapStorage.entitySanityMap.isEmpty() && event.getEntity() instanceof Player player && attacker != null && !event.getEntity().level().isClientSide) {
			ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType());
			if (registryName != null && !player.getAbilities().instabuild && SanityMapStorage.entitySanityMap.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.entitySanityMap.get(registryName));
			}
		}
	}

	@SubscribeEvent
	public void craftingEvent(ItemCraftedEvent event) {
		if (!SanityMapStorage.craftingItemList.isEmpty() && !(event.getEntity() instanceof FakePlayer) &&
				!event.getCrafting().isEmpty() && !event.getEntity().level().isClientSide) {
			Player player = event.getEntity();
			ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(event.getCrafting().getItem());
			if (registryName != null && !player.getAbilities().instabuild && SanityMapStorage.craftingItemList.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.craftingItemList.get(registryName));
			}
		}
	}

	@SubscribeEvent
	public void itemEatenEvent(LivingEntityUseItemEvent.Finish event) {
		if (!SanityMapStorage.foodSanityMap.isEmpty() && event.getEntity() instanceof Player player &&
				!(event.getEntity() instanceof FakePlayer) && !event.getItem().isEmpty() && !event.getEntity().level().isClientSide) {
			ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());
			if (registryName != null && !player.getAbilities().instabuild && SanityMapStorage.foodSanityMap.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.foodSanityMap.get(registryName));
			}
		}
	}

	@SubscribeEvent
	public void itemEatenEvent(EntityItemPickupEvent event) {
		if (!SanityMapStorage.pickupItemList.isEmpty() && !(event.getEntity() instanceof FakePlayer) &&
				!isUUIDKnown(event.getEntity().level(), event.getItem().thrower) && !event.getEntity().level().isClientSide) {
			Player player = event.getEntity();
			ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(event.getItem().getItem().getItem());
			if (registryName != null && !player.getAbilities().instabuild && SanityMapStorage.pickupItemList.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.pickupItemList.get(registryName));
			}
		}
	}

	public boolean isUUIDKnown(Level level, UUID uuid) {
		if (!level.isClientSide && level.getServer() != null) {
			return level.getServer().getProfileCache().get(uuid).isPresent();
		}
		return false;
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void breakEvent(BlockEvent.BreakEvent event) {
		if (!SanityMapStorage.foodSanityMap.isEmpty() && !(event.getPlayer() instanceof FakePlayer) && !event.getLevel().isClientSide()) {
			Player player = event.getPlayer();
			ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(event.getState().getBlock());
			if (registryName != null && !player.getAbilities().instabuild && SanityMapStorage.foodSanityMap.containsKey(registryName)) {
				SanityUtil.addSanity(player, SanityMapStorage.foodSanityMap.get(registryName));
			}
		}
	}
}
