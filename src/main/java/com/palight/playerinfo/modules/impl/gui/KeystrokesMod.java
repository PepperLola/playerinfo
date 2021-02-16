package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.KeystrokesWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class KeystrokesMod extends Module {
    public KeystrokesMod() {
        super("keystrokes", "Keystrokes", "Shows pressed keys on screen.", ModuleType.GUI, null, new KeystrokesWidget());
    }
}
