package com.simplenames.simplenames;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public final class NameCommands {
    private NameCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("namecolour")
                .requires(source -> true)
                .then(Commands.argument("color", StringArgumentType.word())
                        .executes(ctx -> colorSelf(ctx.getSource(),
                                StringArgumentType.getString(ctx, "color"))))
                .then(Commands.literal("clear")
                        .executes(ctx -> clearSelf(ctx.getSource())))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.argument("color", StringArgumentType.word())
                                        .executes(ctx -> colorOther(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "player"),
                                                StringArgumentType.getString(ctx, "color")))))));

        dispatcher.register(Commands.literal("namecolor")
                .requires(source -> true)
                .redirect(dispatcher.getRoot().getChild("namecolour")));
    }

    private static ServerPlayer playerOf(CommandSourceStack source) {
        return source.getEntity() instanceof ServerPlayer player ? player : null;
    }

    static void apply(ServerPlayer player, ChatFormatting color) {
        Scoreboard board = player.getServer().getScoreboard();
        String teamName = NameColors.teamName(player.getUUID());
        PlayerTeam team = board.getPlayerTeam(teamName);
        if (team == null) {
            team = board.addPlayerTeam(teamName);
        }
        team.setColor(color);
        board.addPlayerToTeam(player.getScoreboardName(), team);
    }

    static void clear(ServerPlayer player) {
        Scoreboard board = player.getServer().getScoreboard();
        PlayerTeam team = board.getPlayerTeam(NameColors.teamName(player.getUUID()));
        if (team != null) {
            board.removePlayerFromTeam(player.getScoreboardName(), team);
            if (team.getPlayers().isEmpty()) {
                board.removePlayerTeam(team);
            }
        }
    }

    private static int colorSelf(CommandSourceStack source, String colorName) {
        ServerPlayer player = playerOf(source);
        if (player == null) {
            source.sendFailure(Component.literal("Only players can color names."));
            return 0;
        }
        ChatFormatting color = NameColors.parse(colorName);
        if (color == null) {
            source.sendFailure(Component.literal("Unknown color. Choices: " + NameColors.COLORS));
            return 0;
        }
        apply(player, color);
        NameEvents.remember(player.getUUID(), color.getName().toLowerCase());
        source.sendSuccess(() -> Component.literal("Name color set.").withStyle(color), false);
        return 1;
    }

    private static int clearSelf(CommandSourceStack source) {
        ServerPlayer player = playerOf(source);
        if (player == null) {
            source.sendFailure(Component.literal("Only players can clear names."));
            return 0;
        }
        clear(player);
        NameEvents.forget(player.getUUID());
        source.sendSuccess(() -> Component.literal("Name color cleared."), false);
        return 1;
    }

    private static int colorOther(CommandSourceStack source, String name, String colorName) {
        ServerPlayer target = source.getServer().getPlayerList().getPlayerByName(name);
        if (target == null) {
            source.sendFailure(Component.literal("Player '" + name + "' is not online."));
            return 0;
        }
        ChatFormatting color = NameColors.parse(colorName);
        if (color == null) {
            source.sendFailure(Component.literal("Unknown color. Choices: " + NameColors.COLORS));
            return 0;
        }
        apply(target, color);
        NameEvents.remember(target.getUUID(), color.getName().toLowerCase());
        source.sendSuccess(() -> Component.literal("Set color for " + name + ".").withStyle(color), true);
        return 1;
    }
}
