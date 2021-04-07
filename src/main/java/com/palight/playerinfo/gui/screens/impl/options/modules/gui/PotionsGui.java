package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.gui.CustomMainMenuMod;
import com.palight.playerinfo.modules.impl.gui.PotionsMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class PotionsGui extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiCheckBox renderBackground;
    private GuiCheckBox renderLevelAsNumber;
    private GuiCheckBox renderTransparentBackground;

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

        renderBackground = new GuiCheckBox(0, buttonX, buttonY, "Show background", module.renderBackground);
        renderLevelAsNumber = new GuiCheckBox(1, buttonX, buttonY + 32, "Show level as number", module.renderLevelAsNumber);
        renderTransparentBackground = new GuiCheckBox(2, buttonX, buttonY + 64, "Show transparent background", module.renderTransparentBackground);

        this.guiElements.addAll(Arrays.asList(
                this.renderBackground,
                this.renderLevelAsNumber,
                this.renderTransparentBackground
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (module == null) {
            module = (PotionsMod) PlayerInfo.getModules().get("potions");
        }

        super.widgetClicked(widget);

        if (widget.id == renderBackground.id) {
            module.renderBackground = renderBackground.checked;
        } else if (widget.id == renderLevelAsNumber.id) {
            module.renderLevelAsNumber = renderLevelAsNumber.checked;
        } else if (widget.id == renderTransparentBackground.id) {
            module.renderTransparentBackground = renderTransparentBackground.checked;
        }

        ModConfiguration.syncFromGUI();
    }
}
