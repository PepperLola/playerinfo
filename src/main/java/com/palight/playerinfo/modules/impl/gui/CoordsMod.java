package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.CoordinatesWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class CoordsMod extends Module {

    public CoordsMod() {
        super("coords", "Coordinates", "Show your coordinates on screen", ModuleType.GUI, null, new CoordinatesWidget(2, 2));
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.coordsModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "coordsModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
