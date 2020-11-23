package com.palight.playerinfo.util;

import com.google.gson.reflect.TypeToken;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.commands.InfoCommand;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

    /*public static Direction getDirectionFromLook(Vec3 look) {
        for (Direction direction : Direction.values()) {
            if ((NumberUtil.isBetween(look.xCoord, direction.getVector().xCoord - 0.5, direction.getVector().xCoord + 0.5)) && (direction.getVector().yCoord == newLook.yCoord) && (direction.getVector().zCoord == newLook.zCoord)) {
                return direction;
            }
        }

        Minecraft.getMinecraft().thePlayer.getHorizontalFacing()

        return null;
    }

    public static enum Direction {
        N(new Vec3(0, 0, -1)),
        NE(new Vec3(1, 0, -1)),
        E(new Vec3(1, 0, 0)),
        SE(new Vec3(1, 0, 1)),
        S(new Vec3(0, 0, 1)),
        SW(new Vec3(-1, 0, 1)),
        W(new Vec3(-1, 0, 0)),
        NW(new Vec3(-1, 0, -1));

        private Vec3 vector;

        Direction(Vec3 direction) {
            this.vector = direction;
        }

        public Vec3 getVector() {
            return this.vector;
        }

        public boolean equals(Direction direction) {
            Vec3 vector = direction.getVector();
            return (vector.xCoord == getVector().xCoord) && (vector.yCoord == getVector().yCoord) && (vector.zCoord == getVector().zCoord);
        }

        public String toString(boolean longName) {
            if (this.equals(Direction.N)) {
                return longName ? "North" : "N";
            } else if (this.equals(Direction.NE)) {
                return longName ? "Northeast" : "NE";
            } else if (this.equals(Direction.E)) {
                return longName ? "East" : "E";
            } else if (this.equals(Direction.SE)) {
                return longName ? "Southeast" : "SE";
            } else if (this.equals(Direction.S)) {
                return longName ? "South" : "S";
            } else if (this.equals(Direction.SW)) {
                return longName ? "Southwest" : "SW";
            } else if (this.equals(Direction.W)) {
                return longName ? "West" : "W";
            } else if (this.equals(Direction.NW)) {
                return longName ? "Northwest" : "NW";
            }

            return "";
        }

        public String toString() {
            return toString(false);
        }
    }*/
}
