package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.widgets.GuiMenuBar;
import com.palight.playerinfo.gui.widgets.GuiModuleEntry;
import com.palight.playerinfo.gui.widgets.GuiTexturedButton;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

public class MainGui extends CustomGuiScreenScrollable {

    private final int buttonWidth = 64;
    private final int buttonHeight = 20;

    private final int hPadding = 2;
    private final int columns = 2;
    private int columnWidth;
    private final int rowHeight = 32;

    private int buttonX;
    private int buttonY;

    private GuiMenuBar menuBar;

    private GuiTexturedButton settingsGuiButton;

    List<GuiModuleEntry> modules = new ArrayList<GuiModuleEntry>();

    public MainGui() {
        super("playerinfo");
    }


    @Override
    public void initGui() {
        super.initGui();

        buttonX = (this.width - xSize) / 2 + 16;
        buttonY = (this.height - ySize) / 2 + 32;

        columnWidth = ((xSize - (2 * leftOffset)) - (2 * hPadding)) / columns;

        for (int i = 0; i < PlayerInfo.getModules().values().size(); i++) {
            Module module = new ArrayList<Module>(PlayerInfo.getModules().values()).get(i);
            int xPosition = guiX + leftOffset + hPadding + (columnWidth * (i % columns));
            int yPosition = guiY + headerHeight + (i - (i % columns)) * rowHeight + 4;
            GuiModuleEntry entry = new GuiModuleEntry(this, i, module, xPosition, yPosition, columnWidth, rowHeight);
            entry.init();
            modules.add(entry);
        }


        this.menuBar = new GuiMenuBar(0, (width - xSize) / 2 + 67, (height - ySize) / 2 + 5, 180, 16, new String[]{"Player Info", "Server Util", "Integrations"});

        this.settingsGuiButton = new GuiTexturedButton(3, (width - xSize) / 2 + 226 - 24, (this.height + ySize) / 2 - 24, 20, 20, 0, 0);

        this.buttonList.addAll(Arrays.asList(this.settingsGuiButton));

        this.guiElements.addAll(modules);
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        System.out.println(b.id);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (b.id == settingsGuiButton.id) {
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
        menuBar.drawWidget(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        menuBar.mouseClicked(x, y);
        super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
