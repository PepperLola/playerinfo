package com.palight.playerinfo.gui.screens.impl;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.impl.GuiPasswordField;
import com.palight.playerinfo.util.ApiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class LoginGui extends CustomGuiScreen {

    private GuiTextField username;
    private GuiPasswordField password;
    private GuiButton login;

    private int userX;
    private int userY;

    private int passX;
    private int passY;

    public LoginGui() {
        super("screen.login");
    }

    @Override
    public void initGui() {

        userX = (this.width - xSize) / 2 + 16;
        userY = (this.height - ySize) / 2 + headerHeight + 16;

        passX = userX;
        passY = userY + 32;

        this.username = new GuiTextField(0, this.fontRendererObj, userX + Minecraft.getMinecraft().fontRendererObj.getStringWidth("Username: ") + 4, userY - 5, 64, 18);
        username.setMaxStringLength(24);
        username.setFocused(true);

        this.password = new GuiPasswordField(1, this.fontRendererObj, passX + Minecraft.getMinecraft().fontRendererObj.getStringWidth("Password: ") + 4, passY - 5, 64, 18);
        password.setMaxStringLength(24);
        password.setFocused(false);

        this.login = new GuiButton(0, (this.width - xSize) / 2 - 32, (height + ySize) / 2 - 32, 64, 20, "Submit");
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
        if (button.id == this.login.id) {
            String playerUsername = username.getText();
            String playerPassword = password.getText();
            System.out.println("MAKING API REQUEST WITH USERNAME " + playerUsername);
            ApiUtil.authenticate(playerUsername, playerPassword);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        fontRendererObj.drawString("Username:", userX, userY, 1);
        fontRendererObj.drawString("Password:", passX, passY, 1);

        this.username.drawTextBox();
        this.password.drawTextBox();

    }
}
