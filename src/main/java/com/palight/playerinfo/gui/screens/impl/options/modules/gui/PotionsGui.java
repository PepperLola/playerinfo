package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.gui.PotionsMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class PotionsGui extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiCheckBox renderLevelAsNumber;

    private PotionsMod module;

    public PotionsGui() {
        super("screen.potions");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (PotionsMod) PlayerInfo.getModules().get("potions");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        renderLevelAsNumber = new GuiCheckBox(1, buttonX, buttonY, "Show level as number", module.renderLevelAsNumber);

        this.guiElements.addAll(Arrays.asList(
                this.renderLevelAsNumber
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (module == null) {
            module = (PotionsMod) PlayerInfo.getModules().get("potions");
        }

        super.widgetClicked(widget);

        if (widget.id == renderLevelAsNumber.id) {
            module.renderLevelAsNumber = renderLevelAsNumber.checked;
        }

        ModConfiguration.syncFromGUI();
    }
}
