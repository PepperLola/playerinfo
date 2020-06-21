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
