package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ClockWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.ClockGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class ClockMod extends Module {
    @ConfigOption
    public String dateFormat = "hh:mm:ss a";

    public ClockMod() {
        super("clock", "Clock", "Shows the current time.", ModuleType.GUI, new ClockGui(), new ClockWidget());
    }
}
