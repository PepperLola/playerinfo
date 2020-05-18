package com.palight.playerinfo.listeners;

import com.palight.playerinfo.commands.InfoCommand;
import com.palight.playerinfo.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class CommandListener {
    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        if (!event.player.equals(Minecraft.getMinecraft().thePlayer)) return;

        String unformattedText = event.getComponent().getUnformattedText();

        if (!unformattedText.startsWith(Constants.COMMAND_PREFIX)) return;

        event.setCanceled(true);
        String eventMessage = event.getComponent().getUnformattedText();
        System.out.println(eventMessage);
        String[] commandComponents = eventMessage.split(" ");
        String command = commandComponents[0].replace(Constants.COMMAND_PREFIX, "");
        String[] args = Arrays.copyOfRange(commandComponents, 1, commandComponents.length);

        if (command.equals("getinfo")) {
            if (!(event.player instanceof ICommandSender)) return;
            ICommandSender sender = (ICommandSender) event.player;
            InfoCommand.executeCommand(sender, args[0]);
        }

        System.out.println(event.player.getDisplayName() + " issued command " + command);
    }
}
