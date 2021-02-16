package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PingWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class PingMod extends Module {

    public PingMod() {
        super("ping", "Ping", "Displays your ping on screen.", ModuleType.GUI, null, new PingWidget());
    }
}
