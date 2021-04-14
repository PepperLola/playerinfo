package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiColorPicker;
import com.palight.playerinfo.modules.impl.misc.LifxMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.Arrays;

public class LifxGui extends CustomGuiScreenScrollable {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiColorPicker colorPicker;
    private GuiButton colorButton;
    private GuiButton selectorSubmit;
    private GuiButton tokenSubmit;

    private GuiTextField tokenField;
    private GuiTextField selectorField;

    private GuiCheckBox teamMode;

    private LifxMod module;

    public LifxGui() {
        super("screen.lifx");
    }

    @Override
    public void initGui() {
        if (module == null) {
            module = ((LifxMod) PlayerInfo.getModules().get("lifx"));
        }

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + headerHeight;

        colorPicker = new GuiColorPicker(0, buttonX + 8, buttonY + 8, 48, 64);

        colorButton = new GuiButton(1, buttonX + 8, buttonY + 84, 48, 20, "Submit");

        selectorField = new GuiTextField(2, this.fontRendererObj, buttonX + 4, (this.height + ySize) / 2 - 64, 128, 18);
        tokenField = new GuiTextField(3, this.fontRendererObj, buttonX + 4, (this.height + ySize) / 2 - 32, 128, 18);
        tokenField.setMaxStringLength(64);

        selectorSubmit = new GuiButton(4, buttonX + 134, (this.height + ySize) / 2 - 65, 64, 20, "Set Selector");
        tokenSubmit = new GuiButton(5, buttonX + 134, (this.height + ySize) / 2 - 33, 64, 20, "Set Token");

        teamMode = new GuiCheckBox(6, buttonX + 8, buttonY + 114, "Team Mode", module.lifxTeamMode);

        this.guiElements.addAll(Arrays.asList(
                this.colorPicker,
                this.teamMode,
                this.colorButton,
                this.selectorSubmit,
                this.tokenSubmit
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == colorButton.id) {
            module.setColor(colorPicker.getColor());
        } else if (widget.id == selectorSubmit.id) {
            if (!selectorField.getText().isEmpty())
                module.lifxSelector = selectorField.getText();
        } else if (widget.id == tokenSubmit.id) {
            if (!tokenField.getText().isEmpty())
                module.lifxToken = tokenField.getText();
        } else if (widget.id == teamMode.id) {
            module.lifxTeamMode = teamMode.checked;
        }

        ModConfiguration.syncFromGUI();
        super.widgetClicked(widget);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tokenField.updateCursorCounter();
        selectorField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(1, 1, 1);
        colorPicker.drawWidget(mc, mouseX, mouseY);

        selectorField.drawTextBox();
        tokenField.drawTextBox();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        if (module == null) {
            module = ((LifxMod) PlayerInfo.getModules().get("lifx"));
        }

        colorPicker.mousePressed();
        selectorField.mouseClicked(x, y, btn);
        tokenField.mouseClicked(x, y, btn);

        super.mouseClicked(x, y, btn);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        colorPicker.mouseReleased();
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char p1, int p2) throws IOException {
        super.keyTyped(p1, p2);
        selectorField.textboxKeyTyped(p1, p2);
        tokenField.textboxKeyTyped(p1, p2);
    }
}
