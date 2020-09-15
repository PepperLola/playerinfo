package com.palight.playerinfo.commands;

import com.palight.playerinfo.math.parsing.ImaginaryNumberException;
import com.palight.playerinfo.math.parsing.InvalidExpressionException;
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

public class CalcCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "calc";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/calc <equation>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>(Arrays.asList("calc"));
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) throws CommandException {
        if (!(iCommandSender instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) iCommandSender;

        String equation = args[0];
        try {
            String result = new DecimalFormat("0.######").format(NumberUtil.evaluateExpression(equation));
            String response = String.format("%s%s=%s%s", EnumChatFormatting.GREEN, equation, EnumChatFormatting.AQUA, result);
            MCUtil.sendPlayerMessage(player, response);
        } catch (InvalidExpressionException | ImaginaryNumberException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return iCommandSender.equals(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return new ArrayList<String>(Arrays.asList("calc"));
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
