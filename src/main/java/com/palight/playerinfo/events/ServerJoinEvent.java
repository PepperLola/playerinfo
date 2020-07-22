package com.palight.playerinfo.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ServerJoinEvent extends Event {

    private final String server;
    private final int port;

    public ServerJoinEvent(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }
}