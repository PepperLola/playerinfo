package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiModuleEntry extends GuiCustomWidget {

    private GuiButton toggleButton;
    private GuiTexturedButton optionsButton;
    private Module module;
    private CustomGuiScreen owningScreen;

    private String formattedName;
    private String formattedDescription;

    private int buttonX;
    private int buttonY;

    public GuiModuleEntry(CustomGuiScreen owningScreen, int id, Module module, int xPosition, int yPosition, int width, int height) {
        super(id, xPosition, yPosition, width, height);

        this.owningScreen = owningScreen;
        this.module = module;

        buttonX = xPosition;
        buttonY = yPosition + 32;

        toggleButton = new GuiButton(0, buttonX, buttonY, getButtonText());
        toggleButton.width = width - (module.getOptionsGui() == null ? 0 : 20);

        if (module.getOptionsGui() != null) {
            optionsButton = new GuiTexturedButton(1, buttonX + (width - 20), buttonY, 20, 20, 0, 0);
        }

        formattedName = I18n.format(String.format("module.%s.name", module.getId()));
        formattedDescription = I18n.format(String.format("module.%s.description", module.getId()));
    }

    public void init() {
        setButtonText();
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);
        this.drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x22ffffff, 0x22ffffff);
        this.drawString(mc.fontRendererObj, formattedName, xPosition + (width - mc.fontRendererObj.getStringWidth(formattedName)) / 2, yPosition, 0xffffffff);
        owningScreen.drawTextMultiLine(formattedDescription, xPosition, yPosition + mc.fontRendererObj.FONT_HEIGHT + 4, 0xffffffff, width, true);
        buttonX = xPosition;
        buttonY = yPosition + 32;
        this.toggleButton.yPosition = this.yPosition + 32;
        this.toggleButton.drawWidget(mc, mouseX, mouseY);

        if (module.getOptionsGui() != null) {
            this.optionsButton.yPosition = this.yPosition + 32;
            this.optionsButton.drawButton(mc, mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (NumberUtil.pointIsBetween(mouseX, mouseY, buttonX, buttonY, buttonX + width - 20, buttonY + height)) {
            module.setEnabled(!module.isEnabled());
            setButtonText();
        } else if (NumberUtil.pointIsBetween(mouseX, mouseY, buttonX + width - 20, buttonY, buttonX + width, buttonY + height)) {
            Minecraft.getMinecraft().displayGuiScreen(module.getOptionsGui());
        }
        super.mouseClicked(mouseX, mouseY);
    }

    private void setButtonText() {
        this.toggleButton.displayString = getButtonText();
    }

    private String getButtonText() {
        return module.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" + EnumChatFormatting.RESET : EnumChatFormatting.RED + "Disabled" + EnumChatFormatting.RESET;
    }
}
