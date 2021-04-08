package com.palight.playerinfo.gui.screens.impl.options.modules.movement;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.movement.ToggleSprintMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class ToggleSprintGui extends CustomGuiScreenScrollable {

    private GuiCheckBox widgetEnabled;
    private GuiCheckBox hideWidgetWhenNotMoving;
    private GuiCheckBox toggleSneakEnabled;

    private ToggleSprintMod module;

    public ToggleSprintGui() {
        super("screen.toggleSprint");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = ((ToggleSprintMod) PlayerInfo.getModules().get("toggleSprint"));
        }

        int buttonX = guiX + leftOffset + 6;
        int buttonY = guiY + headerHeight + 16;

        widgetEnabled = new GuiCheckBox(0, buttonX, buttonY, "Widget enabled", module.toggleSprintWidgetEnabled);
        hideWidgetWhenNotMoving = new GuiCheckBox(1, buttonX, buttonY + 20, "Hide widget when not moving", module.hideWidgetWhenNotMoving);
        toggleSneakEnabled = new GuiCheckBox(2, buttonX, buttonY + 40, "Toggle Sneak enabled", module.toggleSneakEnabled);

        this.guiElements.addAll(Arrays.asList(
                this.widgetEnabled,
                this.hideWidgetWhenNotMoving,
                this.toggleSneakEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == widgetEnabled.id) {
            module.toggleSprintWidgetEnabled = widgetEnabled.checked;
        } else if (widget.id == hideWidgetWhenNotMoving.id) {
            module.hideWidgetWhenNotMoving = hideWidgetWhenNotMoving.checked;
        } else if (widget.id == toggleSneakEnabled.id) {
            module.toggleSneakEnabled = toggleSneakEnabled.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
