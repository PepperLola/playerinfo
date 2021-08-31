package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.modules.impl.gui.KeystrokesMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.Arrays;

public class KeystrokesGui extends CustomGuiScreenScrollable {

    private KeystrokesMod module;

    private int buttonX;
    private int buttonY;

    private GuiDropdown keystrokesMode;
    private GuiButton setKeystrokesModeButton;

    private GuiCheckBox showCPSButton;

    public KeystrokesGui() {
        super("screen.keystrokes");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (KeystrokesMod) PlayerInfo.getModules().get("keystrokes");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        keystrokesMode = new GuiDropdown(0, buttonX, buttonY, new String[]{"WASD", "WASD Mouse", "WASD Sprint Mouse"});
        setKeystrokesModeButton = new GuiButton(1, buttonX + 64, buttonY, 64, 20, "Set Mode");
        showCPSButton = new GuiCheckBox(2, buttonX , buttonY + 32, "Show CPS", module.showCPS);

        this.guiElements.addAll(Arrays.asList(
                this.keystrokesMode,
                this.setKeystrokesModeButton,
                this.showCPSButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setKeystrokesModeButton.id) {
            module.keystrokesMode = keystrokesMode.getSelectedItem().toLowerCase();
        } else if (widget.id == showCPSButton.id) {
            module.showCPS = showCPSButton.checked;
        }
        ModConfiguration.syncFromGUI();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        keystrokesMode.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
    }
}
