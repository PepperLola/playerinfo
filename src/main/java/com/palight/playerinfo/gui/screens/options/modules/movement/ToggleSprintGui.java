package com.palight.playerinfo.gui.screens.options.modules.movement;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;

public class ToggleSprintGui extends CustomGuiScreenScrollable {

    private GuiCheckBox widgetEnabled;

    public ToggleSprintGui() {
        super(I18n.format("screen.toggleSprint"));
    }

    @Override
    public void initGui() {
        super.initGui();

        int buttonX = guiX + leftOffset + 6;
        int buttonY = guiY + headerHeight + 16;

        widgetEnabled = new GuiCheckBox(0, buttonX, buttonY, "Widget enabled", ModConfiguration.toggleSprintWidgetEnabled);

        this.guiElements.addAll(Arrays.asList(
                widgetEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == widgetEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_DISPLAY, "toggleSprintWidgetEnabled", widget.enabled);
        }
        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }
}
