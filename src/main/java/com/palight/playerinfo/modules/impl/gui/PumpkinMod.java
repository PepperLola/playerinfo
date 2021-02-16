package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PumpkinWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class PumpkinMod extends Module {
    public PumpkinMod() {
        super("pumpkinOverlay", "Pumpkin Overlay", "Disable the pumpkin overlay.", ModuleType.GUI, null, new PumpkinWidget());
    }
}
