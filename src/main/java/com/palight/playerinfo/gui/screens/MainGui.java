package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.widgets.GuiTexturedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Arrays;

public class MainGui extends CustomGuiScreen {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiButton infoGuiButton;
    private GuiButton serverGuiButton;
    private GuiButton integrationGuiButton;
    private GuiTexturedButton settingsGuiButton;


    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        this.infoGuiButton = new GuiButton(0, buttonX, buttonY, buttonWidth, buttonHeight, "Player Info");
        this.serverGuiButton = new GuiButton(1, buttonX, buttonY + 24, buttonWidth, buttonHeight, "Server Util");
        this.integrationGuiButton = new GuiButton(2, buttonX, buttonY + 48, buttonWidth, buttonHeight, "Integrations");

        this.settingsGuiButton = new GuiTexturedButton(3, (width - xSize) / 2 + 226 - 24, (this.height + ySize) / 2 - 24, 20, 20, 0, 0);

        this.buttonList.addAll(Arrays.asList(this.infoGuiButton, this.serverGuiButton, this.integrationGuiButton));
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (b.id == 0) {
            player.openGui(PlayerInfo.instance, GuiHandler.SETTINGS_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        } else if (b.id == 1) {
            player.openGui(PlayerInfo.instance, GuiHandler.SERVER_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        } else if (b.id == 2) {
            player.openGui(PlayerInfo.instance, GuiHandler.INTEGRATION_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        } else if (b.id == 3) {
            player.openGui(PlayerInfo.instance, GuiHandler.SETTINGS_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        settingsGuiButton.drawButton(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
