package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class PlayerHiderMod extends Module {

    @ConfigOption
    public String showName = "";

    public PlayerHiderMod() {
        super("playerHider", "Player Hider", "Hides/shows players based on specific criteria.", ModuleType.MISC, null, null);
    }
}
