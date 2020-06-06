package com.mrbysco.insane.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.insane.util.SanityUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class InsaneCommands {
    public static void initializeCommands (CommandDispatcher<CommandSource> dispatcher) {
        final LiteralArgumentBuilder<CommandSource> root = Commands.literal("insane");
        root.requires((p_198721_0_) -> p_198721_0_.hasPermissionLevel(2))
        .then(Commands.literal("get").then(Commands.argument("player", EntityArgument.players()).executes(InsaneCommands::getSanity)))
        .then(Commands.literal("set").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("sanity", DoubleArgumentType.doubleArg(0.0F)).executes((ctx) -> InsaneCommands.setSanity(ctx, false)).then(Commands.argument("hideMessage", BoolArgumentType.bool()).executes((ctx) -> InsaneCommands.setSanity(ctx, !BoolArgumentType.getBool(ctx, "hideMessage")))))))
        .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("sanity", DoubleArgumentType.doubleArg()).executes((ctx) -> InsaneCommands.addSanity(ctx, false)).then(Commands.argument("hideMessage", BoolArgumentType.bool()).executes((ctx) -> InsaneCommands.addSanity(ctx, !BoolArgumentType.getBool(ctx, "hideMessage")))))));
        dispatcher.register(root);
    }

    private static int getSanity(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        for(ServerPlayerEntity player : EntityArgument.getPlayers(ctx, "player")) {
            Double sanity = SanityUtil.getSanity(player);
            ctx.getSource().sendFeedback(new TranslationTextComponent("commands.insane.get.success", player.getDisplayName(), sanity), false);
        }

        return 0;
    }

    private static int setSanity(CommandContext<CommandSource> ctx, boolean silent) throws CommandSyntaxException {
        final double sanity = DoubleArgumentType.getDouble(ctx, "sanity");
        for(ServerPlayerEntity player : EntityArgument.getPlayers(ctx, "player")) {
            if(sanity > 0) {
                SanityUtil.setSanity(player, sanity);
                if(!silent) {
                    ctx.getSource().sendFeedback(new TranslationTextComponent("commands.insane.set.success", player.getDisplayName(), sanity), false);
                }
            }
        }

        return 0;
    }

    private static int addSanity(CommandContext<CommandSource> ctx, boolean silent) throws CommandSyntaxException {
        final double sanity = DoubleArgumentType.getDouble(ctx, "sanity");
        for(ServerPlayerEntity player : EntityArgument.getPlayers(ctx, "player")) {
            SanityUtil.addSanity(player, sanity);
            double currentSanity = SanityUtil.getSanity(player);
            if(!silent) {
                ctx.getSource().sendFeedback(new TranslationTextComponent("commands.insane.add.success", sanity, player.getDisplayName(), currentSanity), false);
            }
        }

        return 0;
    }
}
