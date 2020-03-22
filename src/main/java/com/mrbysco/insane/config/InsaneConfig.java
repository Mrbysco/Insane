package com.mrbysco.insane.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class InsaneConfig {
    public static class Server {
        public final ConfigValue<Float> minSanity;
        public final ConfigValue<Float> maxSanity;

        Server(ForgeConfigSpec.Builder builder) {
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

    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }
}
