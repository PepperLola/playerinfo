package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.ScoreboardGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;

public class ScoreboardMod extends Module {

    public ScoreboardMod() {
        super("scoreboard", "Scoreboard", "Customize your scoreboard!", ModuleType.GUI, new ScoreboardGui(), new ScoreboardWidget(Minecraft.getMinecraft()));
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.scoreboardModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "scoreboardModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }
}
