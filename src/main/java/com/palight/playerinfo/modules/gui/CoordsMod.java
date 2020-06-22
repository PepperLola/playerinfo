package com.palight.playerinfo.modules.gui;

import com.palight.playerinfo.gui.ingame.widgets.CoordinatesWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;

public class CoordsMod extends Module {

    private static CoordinatesWidget widget = new CoordinatesWidget(2, 2);

    public CoordsMod() {
        super("coords", "Coordinates", "Show your coordinates on screen", ModuleType.GUI, null);
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

    public static CoordinatesWidget getWidget() {
        return widget;
    }

    public static void setWidget(CoordinatesWidget widget) {
        CoordsMod.widget = widget;
    }
}
