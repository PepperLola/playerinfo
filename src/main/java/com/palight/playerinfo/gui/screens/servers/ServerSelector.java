package com.palight.playerinfo.gui.screens.servers;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiDropdown;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class ServerSelector extends CustomGuiScreen {

    private int buttonWidth = 64;
    private int buttonHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiDropdown serverDropdown;
    private GuiButton selectButton;

    public ServerSelector() {
        super(I18n.format("screen.serverSelector"));
    }


    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        this.serverDropdown = new GuiDropdown(0, buttonX, buttonY, 80, 16, new String[]{"Hypixel"});

        serverDropdown.setSelectedItem(ModConfiguration.getString(ModConfiguration.CATEGORY_SERVERS, "selectedServer"));

        selectButton = new GuiButton(1, buttonX + 84, buttonY - 2, 64, 20, "Select");

        this.buttonList.add(selectButton);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (b.id == selectButton.id) {
            if (serverDropdown.getSelectedItem().equals("Hypixel")) {
                player.openGui(PlayerInfo.instance, GuiHandler.HYPIXEL_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
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
        super.drawScreen(mouseX, mouseY, partialTicks);

        serverDropdown.drawWidget(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        serverDropdown.mousePressed(mc, x, y, btn);

        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_SERVERS, "selectedServer", serverDropdown.getSelectedItem());

        super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
