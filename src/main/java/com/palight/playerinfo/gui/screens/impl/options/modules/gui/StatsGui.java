package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.gui.StatsMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class StatsGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox onlyShowOwnStats;

    private StatsMod module;

    public StatsGui() {
        super("screen.stats");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (StatsMod) PlayerInfo.getModules().get("stats");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        onlyShowOwnStats = new GuiCheckBox(1, buttonX, buttonY, "Only show your stats", module.onlyShowOwnStats);

        this.guiElements.addAll(Arrays.asList(
                this.onlyShowOwnStats
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (module == null) {
            module = (StatsMod) PlayerInfo.getModules().get("stats");
        }

        super.widgetClicked(widget);
        if (widget.id == onlyShowOwnStats.id) {
            module.onlyShowOwnStats = onlyShowOwnStats.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
