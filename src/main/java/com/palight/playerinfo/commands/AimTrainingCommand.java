package com.palight.playerinfo.commands;

import com.palight.playerinfo.math.parsing.ImaginaryNumberException;
import com.palight.playerinfo.math.parsing.InvalidExpressionException;
import com.palight.playerinfo.training.AimTrainingController;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AimTrainingCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "training";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/training <mode>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>(Arrays.asList("training"));
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) throws CommandException {
        if (!(iCommandSender instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) iCommandSender;

        String mode = args[0].toLowerCase();

        if (args.length < 2) {
            MCUtil.sendPlayerinfoMessage(player, EnumChatFormatting.RED + "No difficulty provided! Using easy...");
            AimTrainingController.DIFFICULTY = AimTrainingController.Difficulty.EASY;
        } else {
            String difficulty = args[1].toLowerCase();

            switch (difficulty) {
                case "hard":
                    AimTrainingController.DIFFICULTY = AimTrainingController.Difficulty.HARD;
                    break;
                case "medium":
                    AimTrainingController.DIFFICULTY = AimTrainingController.Difficulty.MEDIUM;
                    break;
                case "easy":
                    AimTrainingController.DIFFICULTY = AimTrainingController.Difficulty.EASY;
                    break;
                default:
                    MCUtil.sendPlayerinfoMessage(player, EnumChatFormatting.RED + "Invalid difficulty! Valid difficulties are: hard, medium, easy.");
                    return;
            }
        }

        if (mode.equals("tracking")) {
            AimTrainingController.MODE = AimTrainingController.Mode.TRACKING;
            AimTrainingController.start();
            MCUtil.sendPlayerinfoMessage(player, EnumChatFormatting.GREEN + "Started tracking mode!");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return iCommandSender.equals(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return new ArrayList<>(Arrays.asList("calc"));
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
