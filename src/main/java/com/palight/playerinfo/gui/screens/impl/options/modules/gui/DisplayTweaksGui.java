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
    private GuiCheckBox renderUnicodeText;
    private GuiCheckBox stackChatMessages;
    private GuiCheckBox transparentChat;

    private DisplayTweaksMod module;

    public DisplayTweaksGui() {
        super("screen.displayTweaks");
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        if (module == null) {
            module = (DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks");
        }

        disableWater = new GuiCheckBox(0, buttonX, buttonY, "Disable water overlay", module.disableWaterOverlay);
        lowerFire = new GuiCheckBox(1, buttonX, buttonY + 32, "Enable lower fire", module.lowerFire);
        windowedFullscreen = new GuiCheckBox(2, buttonX, buttonY + 64, "Enable windowed fullscreen", module.windowedFullscreen);
        hardcoreHeartsEnabled = new GuiCheckBox(3, buttonX, buttonY + 96, "Enable hardcore hearts", module.hardcoreHeartsEnabled);
        renderPingAsText = new GuiCheckBox(4, buttonX, buttonY + 128, "Render ping as text", module.renderPingAsText);
        renderUnicodeText = new GuiCheckBox(5, buttonX, buttonY + 160, "Render custom unicode font", module.unicodeFontRendererEnabled);
        stackChatMessages = new GuiCheckBox(6, buttonX, buttonY + 192, "Stack chat messages", module.stackChatMessages);
        transparentChat = new GuiCheckBox(7, buttonX, buttonY + 224, "Transparent Chat", module.transparentChat);

        this.guiElements.addAll(Arrays.asList(
                this.disableWater,
                this.lowerFire,
                this.windowedFullscreen,
                this.hardcoreHeartsEnabled,
                this.renderPingAsText,
                this.renderUnicodeText,
                this.stackChatMessages,
                this.transparentChat
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == disableWater.id) {
            module.disableWaterOverlay = disableWater.checked;
        } else if (widget.id == lowerFire.id) {
            module.lowerFire = lowerFire.checked;
        } else if (widget.id == windowedFullscreen.id) {
            module.windowedFullscreen = windowedFullscreen.checked;
        } else if (widget.id == hardcoreHeartsEnabled.id) {
            module.hardcoreHeartsEnabled = hardcoreHeartsEnabled.checked;
        } else if (widget.id == renderPingAsText.id) {
            module.renderPingAsText = renderPingAsText.checked;
        } else if (widget.id == renderUnicodeText.id) {
            module.unicodeFontRendererEnabled = renderUnicodeText.checked;
        } else if (widget.id == stackChatMessages.id) {
            module.stackChatMessages = stackChatMessages.checked;
        } else if (widget.id == transparentChat.id) {
            module.transparentChat = transparentChat.checked;
        }

        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }
}
