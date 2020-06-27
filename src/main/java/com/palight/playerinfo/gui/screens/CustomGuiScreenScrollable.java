package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class CustomGuiScreenScrollable extends CustomGuiScreen {

    private float amountScrolled = 0;
    private int slotHeight = 20;

    public CustomGuiScreenScrollable(String screenName) {
        super(screenName);
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
}
