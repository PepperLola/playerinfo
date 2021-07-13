package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ResourceWidget;
import com.palight.playerinfo.modules.Module;

public class BedwarsResourcesMod extends Module {
    public BedwarsResourcesMod() {
        super("resources", ModuleType.GUI, null, new ResourceWidget());
    }
}
