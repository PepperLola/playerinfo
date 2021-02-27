package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class DisplayTweaksGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox disableWater;
    private GuiCheckBox lowerFire;
    private GuiCheckBox windowedFullscreen;
    private GuiCheckBox hardcoreHeartsEnabled;
    private GuiCheckBox renderPingAsText;

    public DisplayTweaksGui() {
        super("screen.displayTweaks");
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        disableWater = new GuiCheckBox(0, buttonX, buttonY, "Disable water overlay", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_DISPLAY, "disableWaterOverlay"));
        lowerFire = new GuiCheckBox(1, buttonX, buttonY + 32, "Enable lower fire", ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).lowerFire);
        windowedFullscreen = new GuiCheckBox(2, buttonX, buttonY + 64, "Enable windowed fullscreen", ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).windowedFullscreen);
        hardcoreHeartsEnabled = new GuiCheckBox(3, buttonX, buttonY + 96, "Enable hardcore hearts", ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).hardcoreHeartsEnabled);
        renderPingAsText = new GuiCheckBox(4, buttonX, buttonY + 128, "Render ping as text", ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).renderPingAsText);

        this.guiElements.addAll(Arrays.asList(
                this.disableWater,
                this.lowerFire,
                this.windowedFullscreen,
                this.hardcoreHeartsEnabled,
                this.renderPingAsText
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == disableWater.id) {
            ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).disableWaterOverlay = disableWater.checked;
        } else if (widget.id == lowerFire.id) {
            ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).lowerFire = lowerFire.checked;
        } else if (widget.id == windowedFullscreen.id) {
            ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).windowedFullscreen = windowedFullscreen.checked;
        } else if (widget.id == hardcoreHeartsEnabled.id) {
            ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).hardcoreHeartsEnabled = hardcoreHeartsEnabled.checked;
        } else if (widget.id == renderPingAsText.id) {
            ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).renderPingAsText = renderPingAsText.checked;
        }

        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }
}
