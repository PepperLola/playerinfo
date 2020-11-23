package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.impl.GuiColorPicker;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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

    public ScoreboardGui() {
        super(I18n.format("screen.scoreboard"));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = guiX + 32;
        buttonY = guiY + 32;

        scoreboardEnabled = new GuiCheckBox(0, buttonX, buttonY, "Enable scoreboard", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GUI, "scoreboardEnabled"));
        scoreboardNumbersEnabled = new GuiCheckBox(1, buttonX, buttonY + 32, "Enable scoreboard numbers", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GUI, "scoreboardNumbersEnabled"));
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (widget.id == scoreboardEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "scoreboardEnabled", scoreboardEnabled.checked);
        } else if (widget.id == scoreboardNumbersEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "scoreboardNumbersEnabled", scoreboardNumbersEnabled.checked);
        } else if (widget.id == primaryColorButton.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "scoreboardHeaderColor", colorPicker.getColor());
        } else if (widget.id == secondaryColorButton.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "scoreboardBodyColor", colorPicker.getColor());
        } else if (widget.id == resetColorsButton.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "scoreboardHeaderColor", 1610612736);
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "scoreboardBodyColor", 1342177280);
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
