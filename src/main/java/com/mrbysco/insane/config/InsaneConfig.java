package com.mrbysco.insane.config;

import com.mrbysco.insane.Insane;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class InsaneConfig {
    public static class Common {
        public final DoubleValue maxSanity;
        public final DoubleValue darknessSanity;
        public final ConfigValue<List<? extends String>> mobDamageList;
        public final ConfigValue<List<? extends String>> rawFoodList;
        public final ConfigValue<List<? extends String>> craftingItemList;
        public final ConfigValue<List<? extends String>> pickupItemList;
        public final ConfigValue<List<? extends String>> blockBrokenList;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Sanity settings")
                    .push("Sanity");

            maxSanity = builder
                    .comment("The max Sanity value [Default: 100.0]")
                    .defineInRange("maxSanity", 100.0f, 0.01D, Double.MAX_VALUE);

            darknessSanity = builder
                    .comment("The amount of sanity lost per second when in total darkness [Default: -50.0]")
                    .defineInRange("darknessSanity", -50.0f, -1000.0f, 0);

            builder.pop();
            builder.comment("Sanity lists")
                    .push("Sanity_Lists");

            String[] mobList = new String[] {
                "minecraft:cave_spider,-0.5",
                "minecraft:creeper,-1.0",
                "minecraft:vex,-1.0",
                "minecraft:zombie,-0.5",
                "minecraft:zombie_villager,-0.5",
                "minecraft:drowned,-0.5",
                "minecraft:husk,-0.5",
                "minecraft:stray,-0.5",
                "minecraft:skeleton,-0.5",
                "minecraft:wither_skeleton,-0.75",
                "minecraft:enderman,-1.5",
                "minecraft:silverfish,-0.25",
                "minecraft:endermite,-0.25",
                "minecraft:evoker,-0.5",
                "minecraft:illusioner,-0.5",
                "minecraft:vindicator,-0.5",
                "minecraft:pillager,-0.5",
                "minecraft:witch,-1.0",
                "minecraft:phantom,-1.0",
                "minecraft:spider,-0.5",
                "minecraft:ravager,-2.0",
                "minecraft:giant,-3.0",
                "minecraft:wither,-4.0"
            };

            mobDamageList = builder
                    .comment("Defines the insanity gained/lost from getting hurt by mobs "+
                            "[Syntax: 'modid:mob_name,sanity']" +
                            "[Example: 'minecraft:zombie,-0.5']")
                    .defineList("mobDamageList", Arrays.asList(mobList), o -> (o instanceof String));

            String[] rawFood = new String[] {
                    "minecraft:porkchop,-2.5",
                    "minecraft:cod,-2.5",
                    "minecraft:salmon,-2.5",
                    "minecraft:tropical_fish,-2.5",
                    "minecraft:pufferfish,-3.5",
                    "minecraft:beef,-2.5",
                    "minecraft:chicken,-2.5",
                    "minecraft:rabbit,-2.5",
                    "minecraft:mutton,-2.5",
                    "minecraft:rotten_flesh,-5.0",
                    "minecraft:spider_eye,-4.0",
                    "minecraft:poisonous_potato,-3.5"
            };
            rawFoodList = builder
                    .comment("Defines the insanity gained/lost from eating certain food"+
                            "[Syntax: 'modid:itemName,sanity']" +
                            "[Example: 'minecraft:beef,-2.5']")
                    .defineList("rawFoodList", Arrays.asList(rawFood), o -> (o instanceof String));

            String[] itemArray = new String[] {
                    ""
            };
            craftingItemList = builder
                    .comment("Defines the insanity gained/lost from crafting certain items"+
                            "[Syntax: 'modid:itemName,sanity']" +
                            "[Example: 'minecraft:wooden_sword,-2.0']")
                    .defineList("craftingItemList", Arrays.asList(itemArray), o -> (o instanceof String));

            String[] pickupArray = new String[] {
                    ""
            };
            pickupItemList = builder
                    .comment("Defines the insanity gained/lost from picking up certain items for the first time"+
                            "[Syntax: 'modid:itemName,sanity']" +
                            "[Example: 'minecraft:cornflower,2.0']")
                    .defineList("pickupItemList", Arrays.asList(pickupArray), o -> (o instanceof String));

            String[] breakBlockArray = new String[] {
                    ""
            };
            blockBrokenList = builder
                    .comment("Defines the insanity gained/lost from breaking certain blocks"+
                            "[Syntax: 'modid:itemName,sanity']" +
                            "[Example: 'minecraft:soul_sand,-2.0']")
                    .defineList("blockBrokenList", Arrays.asList(breakBlockArray), o -> (o instanceof String));

            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
       Insane.LOGGER.debug("Loaded Insane's config file {}", configEvent.getConfig().getFileName());
       Insane.updateMaps();
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        Insane.LOGGER.debug("Insane's config just got changed on the file system!");
        Insane.updateMaps();
    }
}
