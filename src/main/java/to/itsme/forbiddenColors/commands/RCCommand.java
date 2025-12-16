package to.itsme.forbiddencolors.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import to.itsme.forbiddencolors.GameTick;
import to.itsme.forbiddencolors.enums.DefinedColor;
import to.itsme.forbiddencolors.ForbiddenColorsPlugin;
import to.itsme.forbiddencolors.enums.GameState;

public class RCCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand(ForbiddenColorsPlugin plugin) {
        return Commands.literal("rc")
                .requires(sender -> sender.getSender().hasPermission("rc.manage"))
                .then(Commands.literal("set")
                        .then(Commands.literal("timer")
                            .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 1000))
                                    .executes(ctx -> RCCommand.runSetTimerLogic(ctx, plugin))
                            ))
                        .then(Commands.literal("amount")
                                .then(Commands.argument("number", IntegerArgumentType.integer(1, 5))
                                        .executes(ctx -> RCCommand.runSetAmountLogic(ctx, plugin))
                                ))
                        .then(Commands.literal("color")
                                .then(Commands.argument("color", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (DefinedColor color : DefinedColor.values()) {
                                                builder.suggest(color.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> RCCommand.runSetColorLogic(ctx, plugin))
                                )))
                .then(Commands.literal("remove")
                        .then(Commands.literal("color").executes(ctx -> RCCommand.runRemoveColorLogic(ctx, plugin))))
                .then(Commands.literal("start").executes(ctx -> RCCommand.runStartLogic(ctx, plugin)))
                .then(Commands.literal("stop").executes(ctx -> RCCommand.runStopLogic(ctx, plugin)))
                .then(Commands.literal("pause").executes(ctx -> RCCommand.runPauseLogic(ctx, plugin)));
    }

    private static int runSetTimerLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        int seconds = IntegerArgumentType.getInteger(ctx, "seconds");
        plugin.getGameController().setTimer(seconds);
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("Le timer a été défini sur " + seconds));
        return Command.SINGLE_SUCCESS;
    }

    private static int runSetAmountLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        int number = IntegerArgumentType.getInteger(ctx, "number");
        plugin.getGameController().setColorAmount(number);
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("Le nombre de couleur mortelle a été changé à " + number));
        return Command.SINGLE_SUCCESS;
    }

    private static int runSetColorLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        String color = StringArgumentType.getString(ctx, "color");
        CommandSender sender = ctx.getSource().getSender();
        try {
            DefinedColor definedColor = DefinedColor.valueOf(color);
            plugin.getGameController().setRequiredColor(definedColor);
            sender.sendMessage(Component.text("La couleur par défaut a été mise sur " + definedColor.getName()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("La couleur n'existe pas : " + color));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int runRemoveColorLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        plugin.getGameController().removeRequiredColor();
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("La couleur par défaut a été supprimée"));
        return Command.SINGLE_SUCCESS;
    }

    private static int runStartLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        CommandSender sender = ctx.getSource().getSender();

        if (plugin.getGameController().gameState != GameState.STOPPED) {
            sender.sendMessage(Component.text("La partie doit être stoppée avant de pouvoir recommencer."));
            return Command.SINGLE_SUCCESS;
        }

        plugin.getGameController().start();
        plugin.getServer().getScheduler().runTaskTimer(plugin, new GameTick(plugin.getGameController()), 0, 1);
        sender.sendMessage(Component.text("La partie a commencé"));
        return Command.SINGLE_SUCCESS;
    }

    private static int runStopLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        plugin.getGameController().stop();
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("Fin de la partie"));
        return Command.SINGLE_SUCCESS;
    }

    private static int runPauseLogic(CommandContext<CommandSourceStack> ctx, ForbiddenColorsPlugin plugin) {
        CommandSender sender = ctx.getSource().getSender();
        if (plugin.getGameController().gameState == GameState.PAUSED) {
            plugin.getGameController().pause();
            sender.sendMessage(Component.text("La partie a été mise sur pause"));
        } else {
            plugin.getGameController().resume();
            sender.sendMessage(Component.text("La partie a été reprise"));
        }

        return Command.SINGLE_SUCCESS;
    }
}