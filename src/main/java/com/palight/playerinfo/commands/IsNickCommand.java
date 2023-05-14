package com.palight.playerinfo.commands;

import com.palight.playerinfo.util.HttpUtil;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + strings[0];
        Multithreading.runAsync(() ->
            HttpUtil.httpGet(url, response -> {
    //            String entity = EntityUtils.toString(response.getEntity());

                int statusCode = response.getStatusLine().getStatusCode();

    //            JsonParser parser = new JsonParser();
    //            JsonElement element = parser.parse(entity);
    //            JsonObject obj = element.getAsJsonObject();
    //
    //            if (obj.isJsonNull()) return;

                // parse data if the request succeeds
                if (statusCode >= 200 && statusCode < 300) {
                    MCUtil.sendPlayerinfoMessage((EntityPlayer) iCommandSender, strings[0] + " is not nicked.", new ChatStyle().setColor(EnumChatFormatting.GREEN));
                } else {
                    // if the request fails and the player is nicked, they're a nick
                    // otherwise it's watchdog
                    if (statusCode == 404) {
                        MCUtil.sendPlayerinfoMessage((EntityPlayer) iCommandSender, strings[0] + " is nicked.", new ChatStyle().setColor(EnumChatFormatting.RED));
                    }
                }
            })
        );
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
