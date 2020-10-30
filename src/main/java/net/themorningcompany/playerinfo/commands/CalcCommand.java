package net.themorningcompany.playerinfo.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.themorningcompany.playerinfo.math.parsing.InvalidExpressionException;
import net.themorningcompany.playerinfo.util.MCUtil;
import net.themorningcompany.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

public class CalcCommand {
    public CalcCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("calc").requires(source -> source.hasPermissionLevel(0))
                .then(Commands.argument("equation", MessageArgument.message())
                        .executes(context -> {
                            String equation = MessageArgument.getMessage(context, "equation").getUnformattedComponentText();

                            try {
                                String result = new DecimalFormat("0.######").format(NumberUtil.evaluateExpression(equation));
                                String response = String.format("%s%s=%s%s", TextFormatting.GREEN, equation, TextFormatting.AQUA, result);
                                assert Minecraft.getInstance().player != null;
                                MCUtil.sendPlayerMessage(Minecraft.getInstance().player, response);
                            } catch (InvalidExpressionException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        })
                ).executes(context -> {
                    System.out.println("Called calc with no arguments");
                    return 1;
                })
        );
    }
}
