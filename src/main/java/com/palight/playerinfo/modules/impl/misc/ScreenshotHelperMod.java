package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.ScreenshotHelperGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class ScreenshotHelperMod extends Module {

    @ConfigOption
    public boolean asyncScreenshotSaving = false;
    @ConfigOption
    public boolean copyToClipboard = false;

    public ScreenshotHelperMod() {
        super("screenshotHelper", "Screenshot Helper", "Enhancements to the Minecraft screenshot helper", ModuleType.MISC, new ScreenshotHelperGui(), null);
    }
}
