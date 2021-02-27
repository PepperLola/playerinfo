package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.gui.ScoreboardGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;

public class ScoreboardMod extends Module {
    @ConfigOption
    public boolean scoreboardEnabled = false;

    @ConfigOption
    public boolean scoreboardNumbersEnabled = false;

    @ConfigOption
    public int scoreboardHeaderColor = 1610612736;

    @ConfigOption
    public int scoreboardBodyColor = 1342177280;

    public ScoreboardMod() {
        super("scoreboard", "Scoreboard", "Customize your scoreboard!", ModuleType.GUI, new ScoreboardGui(), new ScoreboardWidget());
    }
}
