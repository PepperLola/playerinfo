package com.palight.playerinfo.commands;

import com.palight.playerinfo.math.parsing.InvalidExpressionException;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;

public class CalcCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "calc";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/calc <equation>";
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
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
