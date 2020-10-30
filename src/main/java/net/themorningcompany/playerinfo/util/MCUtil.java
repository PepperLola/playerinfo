package net.themorningcompany.playerinfo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class MCUtil {
    public static void sendPlayerMessage(ClientPlayerEntity player, String message) {
        player.sendMessage(new StringTextComponent(message), player.getUniqueID());
    }

    public static void sendPlayerMessage(ClientPlayerEntity player, String message, TextFormatting formatting) {
        player.sendMessage(new StringTextComponent(message).func_240701_a_(formatting), player.getUniqueID());
    }
}
