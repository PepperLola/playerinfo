package com.palight.playerinfo.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ScoreboardTitleChangeEvent extends Event {
    private final String oldValue;
    private final String newValue;

    public ScoreboardTitleChangeEvent(String oldValue, String newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
