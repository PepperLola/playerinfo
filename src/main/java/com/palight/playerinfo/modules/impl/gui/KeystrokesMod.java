package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.KeystrokesWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.KeystrokesGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class KeystrokesMod extends Module {
    @ConfigOption
    public String keystrokesMode = "WASD_SPRINT_MOUSE";

    @ConfigOption
    public boolean showCPS = false;

    public KeystrokesMod() {
        super("keystrokes", ModuleType.GUI, new KeystrokesGui(), new KeystrokesWidget());
    }
}
