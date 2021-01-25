package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;

public class DisplayTweaksGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox disableWater;
    private GuiCheckBox lowerFire;
    private GuiCheckBox windowedFullscreen;

    public DisplayTweaksGui() {
        super(I18n.format("screen.displayTweaks"));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        disableWater = new GuiCheckBox(0, buttonX, buttonY, "Disable water overlay", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_DISPLAY, "disableWaterOverlay"));
        lowerFire = new GuiCheckBox(1, buttonX, buttonY + 32, "Enable lower fire", ModConfiguration.lowerFire);
        windowedFullscreen = new GuiCheckBox(2, buttonX, buttonY + 64, "Enable windowed fullscreen", ModConfiguration.windowedFullscreen);

        this.guiElements.addAll(Arrays.asList(
                this.disableWater,
                this.lowerFire,
                this.windowedFullscreen
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == disableWater.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_DISPLAY, "disableWaterOverlay", disableWater.checked);
        } else if (widget.id == lowerFire.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_DISPLAY, "lowerFire", lowerFire.checked);
        } else if (widget.id == windowedFullscreen.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_DISPLAY, "windowedFullscreen", windowedFullscreen.checked);
        }

        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }
}
