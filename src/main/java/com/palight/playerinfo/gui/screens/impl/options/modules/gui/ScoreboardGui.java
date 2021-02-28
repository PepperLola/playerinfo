package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiColorPicker;
import com.palight.playerinfo.modules.impl.gui.ScoreboardMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.io.IOException;
import java.util.Arrays;

public class ScoreboardGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox scoreboardEnabled;
    private GuiCheckBox scoreboardNumbersEnabled;
    private GuiColorPicker colorPicker;
    private GuiButton primaryColorButton;
    private GuiButton secondaryColorButton;
    private GuiButton resetColorsButton;

    private ScoreboardMod module;

    public ScoreboardGui() {
        super("screen.scoreboard");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = ((ScoreboardMod) PlayerInfo.getModules().get("scoreboard"));
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        scoreboardEnabled = new GuiCheckBox(0, buttonX, buttonY, "Enable scoreboard", module.scoreboardEnabled);
        scoreboardNumbersEnabled = new GuiCheckBox(1, buttonX, buttonY + 32, "Enable scoreboard numbers", module.scoreboardNumbersEnabled);
        colorPicker = new GuiColorPicker(2, buttonX, buttonY + 64);
        int buttonWidth = (xSize - 3 * leftOffset - 2) / 3;
        int buttonLeft = guiX + leftOffset + 2;
        primaryColorButton = new GuiButton(3, buttonLeft, guiY + ySize - 22,  buttonWidth, 20, "Primary");
        secondaryColorButton = new GuiButton(4, buttonLeft + buttonWidth, guiY + ySize - 22, buttonWidth, 20, "Secondary");
        resetColorsButton = new GuiButton(5, buttonLeft + 2 * buttonWidth, guiY + ySize - 22, buttonWidth, 20, "Reset");

        this.guiElements.addAll(Arrays.asList(
                this.scoreboardEnabled,
                this.scoreboardNumbersEnabled,
                this.colorPicker,
                this.primaryColorButton,
                this.secondaryColorButton,
                this.resetColorsButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (widget.id == scoreboardEnabled.id) {
            module.scoreboardEnabled = scoreboardEnabled.checked;
        } else if (widget.id == scoreboardNumbersEnabled.id) {
            module.scoreboardNumbersEnabled = scoreboardNumbersEnabled.checked;
        } else if (widget.id == primaryColorButton.id) {
            module.scoreboardHeaderColor = colorPicker.getColor();
        } else if (widget.id == secondaryColorButton.id) {
            module.scoreboardBodyColor = colorPicker.getColor();
        } else if (widget.id == resetColorsButton.id) {
            module.scoreboardHeaderColor = 1610612736;
            module.scoreboardBodyColor = 1342177280;
        }

        ModConfiguration.syncFromGUI();

        super.widgetClicked(widget);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        colorPicker.mousePressed();
        super.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int btn) {
        colorPicker.mouseReleased();
        super.mouseReleased(mouseX, mouseY, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
