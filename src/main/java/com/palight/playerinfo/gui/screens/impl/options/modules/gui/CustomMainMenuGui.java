package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.gui.CustomMainMenuMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class CustomMainMenuGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox chromaEnabled;

    public CustomMainMenuGui() {
        super("screen.customMainMenu");
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        chromaEnabled = new GuiCheckBox(0, buttonX, buttonY, "Chroma enabled", ((CustomMainMenuMod) PlayerInfo.getModules().get("mainMenu")).mainMenuChroma);

        this.guiElements.addAll(Arrays.asList(
                this.chromaEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == chromaEnabled.id) {
            ((CustomMainMenuMod) PlayerInfo.getModules().get("mainMenu")).mainMenuChroma = chromaEnabled.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
