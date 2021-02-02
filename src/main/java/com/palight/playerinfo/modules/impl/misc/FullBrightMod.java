package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;


public class FullBrightMod extends Module {
    public FullBrightMod() {
        super("fullBright", "Full Bright", "Makes everything look bright.", ModuleType.MISC, null, null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "fullBrightModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.fullBrightModEnabled);
    }
}