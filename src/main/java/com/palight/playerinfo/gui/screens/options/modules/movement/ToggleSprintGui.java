package com.palight.playerinfo.gui.screens.options.modules.movement;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import net.minecraft.client.resources.I18n;

public class ToggleSprintGui extends CustomGuiScreenScrollable {

    public ToggleSprintGui() {
        super(I18n.format("screen.toggleSprint"));
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
