package to.itsme.forbiddenColors.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import to.itsme.forbiddenColors.enums.DefinedColor;
import to.itsme.forbiddenColors.ForbiddenColorsPlugin;
import to.itsme.forbiddenColors.enums.GameState;

public class RCCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("rc")
                .requires(sender -> sender.getSender().hasPermission("rc.manage"))
                .then(Commands.literal("set")
                        .then(Commands.literal("timer")
                            .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 1000))
                                    .executes(RCCommand::runSetTimerLogic)
                            ))
                        .then(Commands.literal("amount")
                                .then(Commands.argument("number", IntegerArgumentType.integer(1, 5))
                                        .executes(RCCommand::runSetAmountLogic)
                                ))
                        .then(Commands.literal("color")
                                .then(Commands.argument("color", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (DefinedColor color : DefinedColor.values()) {
                                                builder.suggest(color.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(RCCommand::runSetColorLogic)
                                )))
                .then(Commands.literal("remove")
                        .then(Commands.literal("color").executes(RCCommand::runRemoveColorLogic)))
                .then(Commands.literal("start").executes(RCCommand::runStartLogic))
                .then(Commands.literal("stop").executes(RCCommand::runStopLogic))
                .then(Commands.literal("pause").executes(RCCommand::runPauseLogic));
    }

    private static int runSetTimerLogic(CommandContext<CommandSourceStack> ctx) {
        int seconds = IntegerArgumentType.getInteger(ctx, "seconds");
        ForbiddenColorsPlugin.getGameController().setTimer(seconds);
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("Le timer a été défini sur " + seconds));
        return Command.SINGLE_SUCCESS;
    }

    private static int runSetAmountLogic(CommandContext<CommandSourceStack> ctx) {
        int number = IntegerArgumentType.getInteger(ctx, "number");
        ForbiddenColorsPlugin.getGameController().setAmount(number);
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("Le nombre de couleur mortelle a été changé à " + number));
        return Command.SINGLE_SUCCESS;
    }

    private static int runSetColorLogic(CommandContext<CommandSourceStack> ctx) {
        String color = StringArgumentType.getString(ctx, "color");
        CommandSender sender = ctx.getSource().getSender();
        try {
            DefinedColor definedColor = DefinedColor.valueOf(color);
            ForbiddenColorsPlugin.getGameController().setRequiredColor(definedColor);
            sender.sendMessage(Component.text("La couleur par défaut a été mise sur " + definedColor.getName()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("La couleur n'existe pas : " + color));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int runRemoveColorLogic(CommandContext<CommandSourceStack> ctx) {
        ForbiddenColorsPlugin.getGameController().removeRequiredColor();
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("La couleur par défaut a été supprimée"));
        return Command.SINGLE_SUCCESS;
    }

    private static int runStartLogic(CommandContext<CommandSourceStack> ctx) {
        ForbiddenColorsPlugin.getGameController().start();
        ForbiddenColorsPlugin.getInstance().startGameTask();
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("La partie a commencé"));
        return Command.SINGLE_SUCCESS;
    }

    private static int runStopLogic(CommandContext<CommandSourceStack> ctx) {
        ForbiddenColorsPlugin.getGameController().stop();
        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(Component.text("Fin de la partie"));
        return Command.SINGLE_SUCCESS;
    }

    private static int runPauseLogic(CommandContext<CommandSourceStack> ctx) {
        ForbiddenColorsPlugin.getGameController().pause();
        CommandSender sender = ctx.getSource().getSender();
        if (ForbiddenColorsPlugin.getGameController().gameState == GameState.PAUSED) {
            sender.sendMessage(Component.text("La partie a été mise sur pause"));
        } else {
            sender.sendMessage(Component.text("La partie a été reprise"));
        }

        return Command.SINGLE_SUCCESS;
    }
}