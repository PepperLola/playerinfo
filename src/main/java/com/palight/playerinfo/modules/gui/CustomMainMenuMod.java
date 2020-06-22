package com.palight.playerinfo.modules.gui;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class CustomMainMenuMod extends Module {
    public CustomMainMenuMod() {
        super("mainMenu", "Main Menu", "Enable the custom main menu.", ModuleType.GUI, null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.mainMenuModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "mainMenuModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
