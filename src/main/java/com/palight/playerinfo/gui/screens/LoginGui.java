package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.util.ApiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class LoginGui extends GuiScreen {
    private int xSize = 176;
    private int ySize = 166;

    private GuiTextField username;
    private GuiTextField password;
    private GuiButton login;

    private int userX;
    private int userY;

    private int passX;
    private int passY;

    public static final int SUBMIT_BUTTON_ID = 0;

    @Override
    public void initGui() {
        userX = width / 2 - fontRendererObj.getStringWidth("Username:") - 4;
        userY = (height - ySize) / 2 + 32;

        passX = width / 2 - fontRendererObj.getStringWidth("Password:") - 4;
        passY = userY + 32;

        this.username = new GuiTextField(0, this.fontRendererObj, width / 2 + 4, userY - 5, 64, 18);
        username.setMaxStringLength(24);
        username.setFocused(true);

        this.password = new GuiTextField(1, this.fontRendererObj, width / 2 + 4, passY - 5, 64, 18);
        password.setMaxStringLength(24);
        password.setFocused(false);

        login = new GuiButton(SUBMIT_BUTTON_ID, width / 2 - 32, (height + xSize) / 2 - 32, 64, 20, "Submit");
        this.buttonList.add(login);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        username.updateCursorCounter();
        password.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char p1, int p2) throws IOException {
        super.keyTyped(p1, p2);
        if (username.isFocused()) {
            username.textboxKeyTyped(p1, p2);
        } else if (password.isFocused()) {
            password.textboxKeyTyped(p1, p2);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        this.username.mouseClicked(x, y, btn);
        this.password.mouseClicked(x, y, btn);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == SUBMIT_BUTTON_ID) {
            String playerUsername = username.getText();
            String playerPassword = password.getText();
            String playerId = Minecraft.getSessionInfo().get("X-Minecraft-UUID");
            ApiUtil.authenticate(playerUsername, playerPassword, playerId);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("pi:textures/gui/infogui.png"));
        drawTexturedModalRect((this.width - xSize) / 2, (this.height - ySize) / 2, 0, 0, xSize, ySize);

        fontRendererObj.drawString("Login", (width - xSize) / 2 + 8, (height - ySize) / 2 + 8, 1);

        fontRendererObj.drawString("Username:", userX, userY, 1);
        fontRendererObj.drawString("Password:", passX, passY, 1);

        this.username.drawTextBox();
        this.password.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
