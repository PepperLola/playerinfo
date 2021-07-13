package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PingWidget;
import com.palight.playerinfo.modules.Module;

public class PingMod extends Module {

    public PingMod() {
        super("ping", ModuleType.GUI, null, new PingWidget());
    }
}
