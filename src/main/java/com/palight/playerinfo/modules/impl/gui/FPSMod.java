package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.FPSWidget;
import com.palight.playerinfo.modules.Module;

public class FPSMod extends Module {

    public FPSMod() {
        super("fps", ModuleType.GUI, null, new FPSWidget());
    }
}