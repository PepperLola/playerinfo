package com.palight.playerinfo.gui.screens.options.modules.movement;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;

public class ToggleSprintGui extends CustomGuiScreenScrollable {

    public ToggleSprintGui() {
        super("Toggle Sprint");
    }

    @Override
    public void initGui() {
        super.initGui();

        int buttonX = guiX + leftOffset + 2;
        int buttonY = guiY + headerHeight + 16;
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
    }
}
