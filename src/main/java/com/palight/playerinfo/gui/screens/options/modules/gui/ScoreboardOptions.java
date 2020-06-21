package com.palight.playerinfo.gui.screens.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;

public class ScoreboardOptions extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox scoreboardEnabled;
    private GuiCheckBox scoreboardNumbersEnabled;

    public ScoreboardOptions() {
        super("Scoreboard");
    }

    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 32;
        buttonY = (this.height - ySize) / 2 + 32;

        scoreboardEnabled = new GuiCheckBox(0, buttonX, buttonY, "Enable scoreboard", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GUI, "scoreboardEnabled"));
        scoreboardNumbersEnabled = new GuiCheckBox(1, buttonX, buttonY + 32, "Enable scoreboard numbers", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GUI, "scoreboardNumbersEnabled"));

        this.guiElements.addAll(Arrays.asList(
                this.scoreboardEnabled,
                this.scoreboardNumbersEnabled
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
        }

        ModConfiguration.syncFromGUI();

        super.widgetClicked(widget);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
