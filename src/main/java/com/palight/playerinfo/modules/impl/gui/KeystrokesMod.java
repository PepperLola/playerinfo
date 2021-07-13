package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.KeystrokesWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class KeystrokesMod extends Module {
    @ConfigOption
    public String keystrokesMode = "WASD_SPRINT_MOUSE";

    public KeystrokesMod() {
        super("keystrokes", ModuleType.GUI, null, new KeystrokesWidget());
    }
}
