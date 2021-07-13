package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PotionsWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.PotionsGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class PotionsMod extends Module {
    @ConfigOption
    public boolean renderBackground = true;
    @ConfigOption
    public boolean renderLevelAsNumber = false;

    public PotionsMod() {
        super("potions", ModuleType.GUI, new PotionsGui(), new PotionsWidget());
    }
}
