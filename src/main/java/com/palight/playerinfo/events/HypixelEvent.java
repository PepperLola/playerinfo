package com.palight.playerinfo.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class HypixelEvent extends Event {
    public static class FriendEvent extends HypixelEvent {
        private String username;
        private FriendEventType type;

        public FriendEvent(String username, FriendEventType type) {
            this.username = username;
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public FriendEventType getType() {
            return type;
        }

        public void setType(FriendEventType type) {
            this.type = type;
        }

        public static enum FriendEventType {
            JOIN,
            LEAVE;
            public static FriendEventType getType(String type) {
                return type.equals("join") || type.equals("joined") ? JOIN : type.equals("leave") || type.equals("left") ? LEAVE : null;
            }
            public static String getStringToUse(FriendEventType type) {
                return type == JOIN ? "joined" : "left";
            }
        }
    }
}
