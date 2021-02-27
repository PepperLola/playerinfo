package com.palight.playerinfo.gui.screens.impl.options;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
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

    private GuiCheckBox blurEnabled;
    private GuiCheckBox pumpkinDisabled;
    private GuiCheckBox noteBlockHelper;
    private GuiCheckBox unicodeFontRendererEnabled;
    private GuiButton configButton;

    public GuiOptions() {
        super("screen.options");
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonX = (this.width - xSize) / 2 + 32;
        buttonY = (this.height - ySize) / 2 + 32;

        blurEnabled = new GuiCheckBox(0, buttonX, buttonY, "Enable background blur", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GENERAL, "enableBlur"));
        pumpkinDisabled = new GuiCheckBox(1, buttonX, buttonY + 32, "Disable pumpkin overlay", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GENERAL, "pumpkinOverlayDisabled"));
        noteBlockHelper = new GuiCheckBox(2, buttonX, buttonY + 64, "Show note block notes in chat", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GENERAL, "noteBlockHelper"));
        unicodeFontRendererEnabled = new GuiCheckBox(3, buttonX, buttonY + 96, "Enable unicode font rendering", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_GENERAL, "unicodeFontRendererEnabled"));
        configButton = new GuiButton(4, (width + xSize) / 2 - 64, (height + ySize) / 2 - 24, 32, 20, "Config");

        blurEnabled.checked = ModConfiguration.blurModEnabled;
        pumpkinDisabled.checked = ModConfiguration.pumpkinModEnabled;
        noteBlockHelper.checked = ModConfiguration.noteBlockModEnabled;

        guiElements.addAll(Arrays.asList(
                this.blurEnabled,
                this.pumpkinDisabled,
                this.noteBlockHelper,
                this.unicodeFontRendererEnabled,
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

        if (widget.id == blurEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GENERAL, "enableBlur", blurEnabled.checked);
        } else if (widget.id == pumpkinDisabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GENERAL, "pumpkinOverlayDisabled", pumpkinDisabled.checked);
        } else if (widget.id == noteBlockHelper.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GENERAL, "noteBlockHelper", noteBlockHelper.checked);
        } else if (widget.id == unicodeFontRendererEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GUI, "unicodeFontRendererEnabled", unicodeFontRendererEnabled.checked);
        } else if (widget.id == configButton.id) {
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
