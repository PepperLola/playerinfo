package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.FPSWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class FPSMod extends Module {

    public FPSMod() {
        super("fps", "FPS", "Displays your frames per second on screen.", ModuleType.GUI, null, new FPSWidget());
    }
}