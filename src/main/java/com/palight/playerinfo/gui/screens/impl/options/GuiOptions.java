package com.palight.playerinfo.gui.screens.impl.options;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Arrays;

public class GuiOptions extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiButton configButton;

    public GuiOptions() {
        super("screen.options");
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = (this.width - xSize) / 2 + 32;
        buttonY = (this.height - ySize) / 2 + 32;

        configButton = new GuiButton(4, (width + xSize) / 2 - 64, (height + ySize) / 2 - 24, 32, 20, "Config");

        guiElements.addAll(Arrays.asList(
                this.configButton
        ));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        if (widget.id == configButton.id) {
            Minecraft.getMinecraft().thePlayer.openGui(PlayerInfo.instance, GuiHandler.CONFIG_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        }

        ModConfiguration.syncFromGUI();

        super.widgetClicked(widget);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
