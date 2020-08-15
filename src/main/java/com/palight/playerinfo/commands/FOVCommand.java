package com.palight.playerinfo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class FOVCommand {
    private FOVCommand() {}
    public static double customFOV = -1;

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("fov").requires(source -> source.hasPermissionLevel(0))
                .then(Commands.argument("target", DoubleArgumentType.doubleArg())
                        .executes(context -> setFOV(
                                context.getSource(),
                                DoubleArgumentType.getDouble(context, "target")
                        ))
                ).executes(context -> {
                    System.out.println("Called fov with no arguments");
                    return 1;
                })
        );
    }

    private static int setFOV(CommandSource source, double fov) {
        ClientPlayerEntity playerEntity = Minecraft.getInstance().player;

        customFOV = fov;
        return 1;
    }
}
