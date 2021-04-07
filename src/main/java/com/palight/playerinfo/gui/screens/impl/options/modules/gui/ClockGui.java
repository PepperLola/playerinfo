package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.modules.impl.gui.ClockMod;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.Arrays;

public class ClockGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiTextField dateFormat;
    private GuiButton setDateFormat;

    private ClockMod module;

    public ClockGui() {
        super("screen.clock");
    }

    @Override
    public void initGui() {
        super.initGui();
        if (module == null) {
            module = ((ClockMod) PlayerInfo.getModules().get("clock"));
        }

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + headerHeight;

        dateFormat = new GuiTextField(0, this.fontRendererObj, buttonX + 4, buttonY, 128, 18);
        dateFormat.setText(module.dateFormat);
        setDateFormat = new GuiButton(0, buttonX + 4 + 128, buttonY, 48, 20, "Set Format");

        this.guiElements.addAll(Arrays.asList(
                this.setDateFormat
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == setDateFormat.id) {
            module.dateFormat = dateFormat.getText();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        dateFormat.drawTextBox();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        dateFormat.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
        dateFormat.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void keyTyped(char character, int p_keyTyped_2_) throws IOException {
        super.keyTyped(character, p_keyTyped_2_);
        dateFormat.textboxKeyTyped(character, p_keyTyped_2_);
    }
}
