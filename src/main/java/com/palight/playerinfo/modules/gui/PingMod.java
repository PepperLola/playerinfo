package com.palight.playerinfo.modules.gui;

import com.palight.playerinfo.gui.ingame.widgets.PingWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class PingMod extends Module {
    public PingMod() {
        super("ping", "Ping", "Show your ping on screen", ModuleType.GUI, null, new PingWidget(50, 2));
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.pingModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "pingModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
