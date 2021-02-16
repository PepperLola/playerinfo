package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.screens.impl.options.modules.gui.CustomMainMenuGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class CustomMainMenuMod extends Module {
    public CustomMainMenuMod() {
        super("mainMenu", "Main Menu", "Enable the custom main menu.", ModuleType.GUI, new CustomMainMenuGui(), null);
    }
}
