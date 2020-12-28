package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiDropdown;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.MCUtil;
import com.sun.jna.StringArray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeChangerGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiDropdown timePicker;
    private GuiButton setTimeButton;

    public TimeChangerGui() {
        super(I18n.format("screen.particle"));
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        String[] times = new String[]{"Day", "Night", "Dawn", "Dusk"};



        timePicker = new GuiDropdown(0, buttonX, buttonY, times);
        setTimeButton = new GuiButton(1, buttonX + 64, buttonY, 64, 20, "Set Time");

        this.guiElements.addAll(Arrays.asList(
                this.timePicker,
                this.setTimeButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setTimeButton.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_PARTICLE, "selectedTime", timePicker.getSelectedItem().toLowerCase());
            ModConfiguration.syncFromGUI();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        timePicker.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY, btn);
    }
}
