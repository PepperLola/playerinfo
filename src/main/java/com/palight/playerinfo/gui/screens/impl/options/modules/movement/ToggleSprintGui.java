package com.palight.playerinfo.gui.screens.impl.options.modules.movement;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;

public class ToggleSprintGui extends CustomGuiScreenScrollable {

    private GuiCheckBox widgetEnabled;
    private GuiCheckBox toggleSneakEnabled;

    public ToggleSprintGui() {
        super(I18n.format("screen.toggleSprint"));
    }

    @Override
    public void initGui() {
        super.initGui();

        int buttonX = guiX + leftOffset + 6;
        int buttonY = guiY + headerHeight + 16;

        widgetEnabled = new GuiCheckBox(0, buttonX, buttonY, "Widget enabled", ModConfiguration.toggleSprintWidgetEnabled);
        toggleSneakEnabled = new GuiCheckBox(1, buttonX, buttonY + 20, "Toggle Sneak enabled", ModConfiguration.toggleSneakModEnabled);

        this.guiElements.addAll(Arrays.asList(
                this.widgetEnabled,
                this.toggleSneakEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == widgetEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_WIDGETS, "toggleSprintWidgetEnabled", widgetEnabled.checked);
        } else if (widget.id == toggleSneakEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "toggleSneakModEnabled", toggleSneakEnabled.checked);
        }
        ModConfiguration.syncFromGUI();
    }
}
