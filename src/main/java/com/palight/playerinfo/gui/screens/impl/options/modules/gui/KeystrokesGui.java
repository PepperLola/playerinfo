package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.Arrays;

public class KeystrokesGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiDropdown keystrokesMode;
    private GuiButton setKeystrokesModeButton;

    public KeystrokesGui() {
        super(I18n.format("screen.keystrokes"));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        keystrokesMode = new GuiDropdown(0, buttonX, buttonY, new String[]{"WASD", "WASD Mouse", "WASD Sprint Mouse"});
        setKeystrokesModeButton = new GuiButton(1, buttonX + 64, buttonY, 64, 20, "Set Mode");

        this.guiElements.addAll(Arrays.asList(
                this.keystrokesMode,
                this.setKeystrokesModeButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setKeystrokesModeButton.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "keystrokesMode", keystrokesMode.getSelectedItem().toLowerCase());
            ModConfiguration.syncFromGUI();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        keystrokesMode.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
    }
}
