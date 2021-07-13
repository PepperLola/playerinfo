package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PumpkinWidget;
import com.palight.playerinfo.modules.Module;

public class PumpkinMod extends Module {
    public PumpkinMod() {
        super("pumpkinOverlay", ModuleType.GUI, null, new PumpkinWidget());
    }
}
