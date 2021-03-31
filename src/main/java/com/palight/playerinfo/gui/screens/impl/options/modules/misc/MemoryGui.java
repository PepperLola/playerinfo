package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.modules.impl.misc.MemoryMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.Arrays;

public class MemoryGui extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiDropdown formatPicker;
    private GuiButton setFormatButton;

    private MemoryMod module;

    public MemoryGui() {
        super("screen.memory");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = ((MemoryMod) PlayerInfo.getModules().get("memory"));
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        formatPicker = new GuiDropdown(0, buttonX, buttonY, new String[]{"Raw", "Percent"});
        setFormatButton = new GuiButton(1, buttonX + 64, buttonY, 64, 20, "Set Format");

        this.guiElements.addAll(Arrays.asList(
                this.formatPicker,
                this.setFormatButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setFormatButton.id) {
            module.format = formatPicker.getSelectedItem().toLowerCase();
            ModConfiguration.syncFromGUI();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        formatPicker.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
    }
}
