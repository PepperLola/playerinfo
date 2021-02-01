package com.palight.playerinfo.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderTitleEvent extends Event {
    private final String title;
    private final String subtitle;

    public RenderTitleEvent(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}