package com.palight.playerinfo.modules.gui;

import com.palight.playerinfo.gui.ingame.widgets.PumpkinWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class PumpkinMod extends Module {
    public PumpkinMod() {
        super("pumpkinOverlay", "Pumpkin Overlay", "Disable the pumpkin overlay.", ModuleType.GUI, null, new PumpkinWidget());
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.pumpkinModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "pumpkinModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
