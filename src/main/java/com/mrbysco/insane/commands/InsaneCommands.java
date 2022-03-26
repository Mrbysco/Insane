package com.mrbysco.insane.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.insane.config.InsaneConfig;
import com.mrbysco.insane.util.SanityUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class InsaneCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("insane");
		root.requires((p_198721_0_) -> p_198721_0_.hasPermission(2))
				.then(Commands.literal("get").then(Commands.argument("player", EntityArgument.players()).executes(InsaneCommands::getSanity)))
				.then(Commands.literal("set").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("sanity", DoubleArgumentType.doubleArg(0.0F)).executes((ctx) -> InsaneCommands.setSanity(ctx, false)).then(Commands.argument("hideMessage", BoolArgumentType.bool()).executes((ctx) -> InsaneCommands.setSanity(ctx, !BoolArgumentType.getBool(ctx, "hideMessage")))))))
				.then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("sanity", DoubleArgumentType.doubleArg()).executes((ctx) -> InsaneCommands.addSanity(ctx, false)).then(Commands.argument("hideMessage", BoolArgumentType.bool()).executes((ctx) -> InsaneCommands.addSanity(ctx, !BoolArgumentType.getBool(ctx, "hideMessage")))))));
		dispatcher.register(root);
	}

	private static int getSanity(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
			Double sanity = SanityUtil.getSanity(player);
			ctx.getSource().sendSuccess(new TranslatableComponent("commands.insane.get.success", player.getDisplayName(), sanity), false);
		}

		return 0;
	}

	private static int setSanity(CommandContext<CommandSourceStack> ctx, boolean silent) throws CommandSyntaxException {
		final double sanity = DoubleArgumentType.getDouble(ctx, "sanity");
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
			if (sanity >= 0 && sanity <= InsaneConfig.COMMON.maxSanity.get()) {
				SanityUtil.setSanity(player, sanity);
				if (!silent) {
					ctx.getSource().sendSuccess(new TranslatableComponent("commands.insane.set.success", player.getDisplayName(), sanity), false);
				}
			}
		}

		return 0;
	}

	private static int addSanity(CommandContext<CommandSourceStack> ctx, boolean silent) throws CommandSyntaxException {
		final double sanity = DoubleArgumentType.getDouble(ctx, "sanity");
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
			SanityUtil.addSanity(player, sanity);
			double currentSanity = SanityUtil.getSanity(player);
			if (!silent) {
				ctx.getSource().sendSuccess(new TranslatableComponent("commands.insane.add.success", sanity, player.getDisplayName(), currentSanity), false);
			}
		}

		return 0;
	}
}
