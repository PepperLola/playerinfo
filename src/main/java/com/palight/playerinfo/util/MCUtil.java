package com.palight.playerinfo.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

public class MCUtil {
    public static void sendPlayerMessage(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    public static void sendPlayerMessage(EntityPlayer player, String message, ChatStyle formatting) {
        player.addChatMessage(new ChatComponentText(message).setChatStyle(formatting));
    }
}
