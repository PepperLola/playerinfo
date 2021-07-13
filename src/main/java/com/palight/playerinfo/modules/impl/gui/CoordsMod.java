package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.CoordinatesWidget;
import com.palight.playerinfo.modules.Module;

public class CoordsMod extends Module {
    public CoordsMod() {
        super("coords", ModuleType.GUI, null, new CoordinatesWidget(2, 2));
    }
}
