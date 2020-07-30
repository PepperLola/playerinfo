package com.palight.playerinfo.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class HypixelFriendJoinEvent extends Event {
    private String username;

    public HypixelFriendJoinEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
