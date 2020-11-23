package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PingWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class PingMod extends Module {

    public PingMod() {
        super("ping", "Ping", "Displays your ping on screen.", ModuleType.GUI, null, new PingWidget());
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.pingModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "pingModEnabled", enabled);
        ModConfiguration.syncFromGUI();
    }
}