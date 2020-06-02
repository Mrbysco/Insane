package com.mrbysco.insane.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import org.apache.commons.lang3.tuple.Pair;

public class InsaneConfig {
    public static class Server {
        public final DoubleValue minSanity;
        public final DoubleValue maxSanity;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Client settings")
                    .push("client");

            minSanity = builder
                    .comment("The min Sanity value")
                    .defineInRange("minSanity", -10.0D, Double.MIN_VALUE, Double.MAX_VALUE);

            maxSanity = builder
                    .comment("The max Sanity value")
                    .defineInRange("maxSanity", 10.0D, Double.MIN_VALUE, Double.MAX_VALUE);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }
}
