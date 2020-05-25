package com.palight.playerinfo.gui.screens.integrations;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiDropdown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;

public class IntegrationSelector extends CustomGuiScreen {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiDropdown integrationDropdown;
    private GuiButton selectButton;


    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        this.integrationDropdown = new GuiDropdown(0, buttonX, buttonY, 80, 16, new String[]{"Lifx"});

        selectButton = new GuiButton(0, buttonX + 84, buttonY - 2, 64, 20, "Select");

        this.buttonList.add(selectButton);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (b.id == 0) {
            if (integrationDropdown.getSelectedItem().equals("Lifx")) {
                player.openGui(PlayerInfo.instance, GuiHandler.LIFX_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        integrationDropdown.drawDropdown(mc, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        integrationDropdown.mousePressed(mc, x, y, btn);

        super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
