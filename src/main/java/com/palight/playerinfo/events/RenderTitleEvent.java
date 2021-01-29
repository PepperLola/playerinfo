package com.palight.playerinfo.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderTitleEvent extends Event {
    private String title;
    private String subtitle;

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
