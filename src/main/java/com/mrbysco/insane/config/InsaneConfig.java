package com.mrbysco.insane.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class InsaneConfig {
    public static class Common {
        public final ConfigValue<Float> minSanity;
        public final ConfigValue<Float> maxSanity;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Client settings")
                    .push("client");

            minSanity = builder
                    .comment("The min Sanity value")
                    .define("minSanity", -10.0F);

            maxSanity = builder
                    .comment("The max Sanity value")
                    .define("maxSanity", 10.0F);

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
}
