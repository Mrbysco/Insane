package com.mrbysco.insane.registry;

import com.mrbysco.insane.config.InsaneConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;

public class SanityMapStorage {
	public static final HashMap<ResourceLocation, Double> entitySanityMap = new HashMap<>();
	public static final HashMap<ResourceLocation, Double> foodSanityMap = new HashMap<>();
	public static final HashMap<ResourceLocation, Double> craftingItemList = new HashMap<>();
	public static final HashMap<ResourceLocation, Double> pickupItemList = new HashMap<>();
	public static final HashMap<ResourceLocation, Double> blockBreakList = new HashMap<>();

	public static void updateMaps() {
		entitySanityMap.clear();
		List<? extends String> mobList = InsaneConfig.COMMON.mobDamageList.get();
		if (!mobList.isEmpty()) {
			for (String string : mobList) {
				String[] array = string.split(",");
				if (array.length == 2) {
					ResourceLocation location = new ResourceLocation(array[0]);
					double amount = new Double(array[1]);

					entitySanityMap.put(location, amount);
				}
			}
		}

		foodSanityMap.clear();
		List<? extends String> foodList = InsaneConfig.COMMON.rawFoodList.get();
		if (!foodList.isEmpty()) {
			for (String string : foodList) {
				String[] array = string.split(",");
				if (array.length == 2) {
					ResourceLocation location = new ResourceLocation(array[0]);
					double amount = new Double(array[1]);

					foodSanityMap.put(location, amount);
				}
			}
		}

		craftingItemList.clear();
		List<? extends String> craftingItems = InsaneConfig.COMMON.craftingItemList.get();
		if (!craftingItems.isEmpty()) {
			for (String string : craftingItems) {
				String[] array = string.split(",");
				if (array.length == 2) {
					ResourceLocation location = new ResourceLocation(array[0]);
					double amount = new Double(array[1]);

					craftingItemList.put(location, amount);
				}
			}
		}

		pickupItemList.clear();
		List<? extends String> pickupItems = InsaneConfig.COMMON.pickupItemList.get();
		if (!pickupItems.isEmpty()) {
			for (String string : pickupItems) {
				String[] array = string.split(",");
				if (array.length == 2) {
					ResourceLocation location = new ResourceLocation(array[0]);
					double amount = new Double(array[1]);

					pickupItemList.put(location, amount);
				}
			}
		}

		blockBreakList.clear();
		List<? extends String> blockBrokenList = InsaneConfig.COMMON.blockBrokenList.get();
		if (!blockBrokenList.isEmpty()) {
			for (String string : blockBrokenList) {
				String[] array = string.split(",");
				if (array.length == 2) {
					ResourceLocation location = new ResourceLocation(array[0]);
					double amount = new Double(array[1]);

					blockBreakList.put(location, amount);
				}
			}
		}
	}
}
