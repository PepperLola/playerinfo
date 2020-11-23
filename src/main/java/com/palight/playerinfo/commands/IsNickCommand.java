package com.palight.playerinfo.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IsNickCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "isnick";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/isnick <player>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>(Arrays.asList("isnick", "in", "nicked", "isnicked"));
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return iCommandSender.equals(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
