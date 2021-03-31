package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.ingame.widgets.impl.MemoryWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.misc.MemoryGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class MemoryMod extends Module {
    @ConfigOption
    public String format = "PERCENT";

    public MemoryMod() {
        super("memory", "Memory", "Displays your current memory usage.", ModuleType.MISC, new MemoryGui(), new MemoryWidget());
    }
}
