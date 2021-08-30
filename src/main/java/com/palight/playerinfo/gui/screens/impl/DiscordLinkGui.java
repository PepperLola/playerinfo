package com.palight.playerinfo.gui.screens.impl;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class DiscordLinkGui extends CustomGuiScreen {

    private String token;

    private GuiTextField tokenField;
    private GuiButton copyButton;

    private int fieldX;
    private int fieldY;

    public DiscordLinkGui(String token) {
        super("screen.discordLink");
        this.token = token;
    }

    @Override
    public void initGui() {
        int fieldW = 96;
        int fieldH = 18;

        fieldX = this.guiX + (this.width / 2) - fieldW;
        fieldY = this.guiY + (this.height - fieldH) / 2;

        this.tokenField = new GuiTextField(0, this.fontRendererObj, fieldX, fieldY, fieldW, fieldH);
        tokenField.setMaxStringLength(11);
        tokenField.setFocused(true);
        tokenField.setText(this.token);
        tokenField.setEnabled(false);

        this.copyButton = new GuiButton(1, fieldX + fieldW + 4, fieldY, 64, 20, "Copy");
        this.buttonList.add(copyButton);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tokenField.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        this.tokenField.mouseClicked(x, y, btn);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == this.copyButton.id) {
            String token = this.tokenField.getText();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(token), null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRendererObj.drawString("DM @0ldC0w#8370 on Discord with this code.", this.guiX + 2, this.guiY + 32, 0xFFFFFFFF);

        this.tokenField.drawTextBox();
    }
}
