package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Arrays;

public class MainGui extends GuiScreen {
    private int xSize = 176;
    private int ySize = 166;

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiButton infoGuiButton;
    private GuiButton serverGuiButton;
    private GuiButton integrationGuiButton;


    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        this.infoGuiButton = new GuiButton(0, buttonX, buttonY, 64, 20, "Player Info");
        this.serverGuiButton = new GuiButton(1, buttonX, buttonY + 24, 64, 20, "Server Util");
        this.integrationGuiButton = new GuiButton(2, buttonX, buttonY + 48, 64, 20, "Integrations");

        this.buttonList.addAll(Arrays.asList(this.infoGuiButton, this.serverGuiButton, this.integrationGuiButton));
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (b.id == 0) {
            player.openGui(PlayerInfo.instance, GuiHandler.INFO_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        } else if (b.id == 1) {
            player.openGui(PlayerInfo.instance, GuiHandler.SERVER_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        } else if (b.id == 2) {
            player.openGui(PlayerInfo.instance, GuiHandler.INTEGRATION_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("pi:textures/gui/infogui.png"));
        drawTexturedModalRect((this.width - xSize) / 2, (this.height - ySize) / 2, 0, 0, xSize, ySize);

        super.drawScreen(mouseX, mouseY, partialTicks);
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
