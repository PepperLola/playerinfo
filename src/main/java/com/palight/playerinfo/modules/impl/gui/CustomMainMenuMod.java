package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.screens.impl.options.modules.gui.CustomMainMenuGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class CustomMainMenuMod extends Module {
    @ConfigOption
    public boolean mainMenuChroma = false;
    public CustomMainMenuMod() {
        super("mainMenu", ModuleType.GUI, new CustomMainMenuGui(), null);
    }
}
