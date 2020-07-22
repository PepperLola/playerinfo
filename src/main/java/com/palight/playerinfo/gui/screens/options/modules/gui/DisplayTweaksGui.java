package com.palight.playerinfo.gui.screens.options.modules.gui;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;

public class DisplayTweaksGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox disableWater;

    public DisplayTweaksGui() {
        super(I18n.format("screen.displayTweaks"));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        disableWater = new GuiCheckBox(0, buttonX, buttonY, "Disable water overlay", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_DISPLAY, "disableWaterOverlay"));

        this.guiElements.addAll(Arrays.asList(
                this.disableWater
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == disableWater.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_DISPLAY, "disableWaterOverlay", disableWater.checked);
        }

        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }
}
