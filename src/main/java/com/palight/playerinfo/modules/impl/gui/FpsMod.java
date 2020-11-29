package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.FpsWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class FpsMod extends Module {

    public FpsMod() {
        super("fps", "Fps", "Displays your frames per second on screen.", ModuleType.GUI, null, new FpsWidget());
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.fpsModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "fpsModEnabled", enabled);
        ModConfiguration.syncFromGUI();
    }
}