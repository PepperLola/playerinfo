package com.palight.playerinfo.gui.screens.options;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import java.util.List;

public class GuiOptions extends CustomGuiScreen {
    private float amountScrolled = 0;
    private int slotHeight = 20;

    private int buttonX;
    private int buttonY;

    private GuiCheckBox blurEnabled;
    private GuiCheckBox pumpkinDisabled;

    @Override
    public void initGui() {
        buttonX = (this.width - xSize) / 2 + 32;
        buttonY = (this.height - ySize) / 2 + 32;

        blurEnabled = new GuiCheckBox(0, buttonX, buttonY, "Enable background blur", ModConfiguration.getBoolean("enableBlur", ModConfiguration.CATEGORY_GENERAL));
        pumpkinDisabled = new GuiCheckBox(1, buttonX, buttonY + 32, "Disable pumpkin overlay", ModConfiguration.getBoolean("pumpkinOverlayDisabled", ModConfiguration.CATEGORY_GENERAL));

        guiElements.addAll(Arrays.asList(this.blurEnabled, this.pumpkinDisabled));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiCustomWidget guiElement : guiElements) {
            if (NumberUtil.isBetween(guiElement.yPosition, (height - ySize) / 2 + headerHeight, (height + ySize) / 2 - footerHeight)) {
                guiElement.drawWidget(mc, mouseX, mouseY);
            }
            guiElement.yPosition += getScrollAmount();
        }

        amountScrolled = 0;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {

        if (widget.id == blurEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GENERAL, "enableBlur", blurEnabled.checked);
        } else if (widget.id == pumpkinDisabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_GENERAL, "pumpkinOverlayDisabled", blurEnabled.checked);
        }

        super.widgetClicked(widget);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = Integer.signum(scrollAmount);
            amountScrolled = (float)(scrollAmount * slotHeight / 2);
        }

        super.handleMouseInput();
    }

    public int getScrollAmount() {
        return (int) Math.floor(amountScrolled);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
