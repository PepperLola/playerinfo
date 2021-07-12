package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.PlayerHiderGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class PlayerHiderMod extends Module {

    @ConfigOption
    public String showNamePattern = "";

    public PlayerHiderMod() {
        super("playerHider", ModuleType.MISC, new PlayerHiderGui(), null);
    }
}
