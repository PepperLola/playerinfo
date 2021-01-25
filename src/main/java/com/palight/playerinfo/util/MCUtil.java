package com.palight.playerinfo.util;

import com.google.gson.reflect.TypeToken;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.commands.InfoCommand;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.modules.impl.misc.DiscordRichPresenceMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import java.util.Map;
import java.util.UUID;

public class MCUtil {
    public static void sendPlayerMessage(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    public static void sendPlayerMessage(EntityPlayer player, String message, ChatStyle formatting) {
        player.addChatMessage(new ChatComponentText(message).setChatStyle(formatting));
    }

    public static int getTotalItems(InventoryPlayer inv, Item itemStack) {
        int total = 0;
        for (ItemStack item : inv.mainInventory) {
            if (item == null) continue;
            if (item.getItem().equals(itemStack))
                total += item.stackSize;
        }

        return total;
    }

    public static String getPlayerStatus(UUID uuid) {
        String res = HttpUtil.httpGet("https://api.hypixel.net/status?uuid=" + uuid.toString() + "&key=" + ModConfiguration.hypixelApiKey);
        Map<String, Object> data = (Map<String, Object>) ((Map<String, Object>) PlayerInfo.gson.fromJson(res, new TypeToken<Map<String, Object>>(){}.getType())).get("session");
        return data.get("gameType") + " " + data.get("mode");
    }

    public static String getPlayerStatus() {
        if (DiscordRichPresenceMod.serverIp.contains("hypixel.net")) {
            ScoreboardWidget scoreboardWidget = ((ScoreboardWidget) PlayerInfo.getModules().get("scoreboard").getWidget());
            ScoreObjective scoreObjective = scoreboardWidget.scoreObjective;
            if (scoreObjective != null) {
                return ColorUtil.stripColor(scoreObjective.getDisplayName());
            }
        }

        return "Hypixel";
    }

    public static boolean isPlayerNicked(String playerName) {
        // TODO finish this
        System.out.println(Minecraft.getMinecraft().getCurrentServerData().playerList);
        String uuid = InfoCommand.getPlayerUUID(playerName);

        return false;
    }

    public static enum ParticleTypes {
        POOF("poof", 0, 7),
        SPLASH("splash", 1, 1),
        BUBBLE("bubble", 0, 2),
        BOBBER("bobber", 1, 2),
        FLAME("flame", 0, 3),
        FIRE("fire", 1, 3),
        NOTE("note", 0, 4),
        CRIT("crit", 1, 4),
        CRIT_MAGIC("crit_magic", 2, 4),
        HEART("heart", 0, 5),
        ANGRY_VILLAGER("angry_villager", 1, 5),
        HAPPY_VILLAGER("happy_villager", 2, 5),
        VILLAGER_FACE("villager_face", 3, 5),
        WATER_DROP("water_drop", 0, 6),
        WATER("water", 1, 6);

        public String name;
        public int x;
        public int y;

        ParticleTypes(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }
}
