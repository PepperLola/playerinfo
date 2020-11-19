package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.NumberUtil;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class CustomGuiScreenScrollable extends CustomGuiScreen {

    private float amountScrolled = 0;
    private int slotHeight = 20;

    /**
     * Constructor for CustomGuiScreenScrollable class.
     * @param screenName Name to be displayed when in the GUI.
     */
    public CustomGuiScreenScrollable(String screenName) {
        super(screenName);
    }

    /**
     * Same as CustomGuiScreen drawScreen method except it takes into account the scroll offset.
     * @param mouseX X position of the mouse. Provided by Minecraft.
     * @param mouseY Y position of the mouse. Provided by Minecraft.
     * @param partialTicks Partial ticks. Provided by Minecraft.
     */
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

    /**
     * Offsets the elements on mouse scroll, creating a scrolling effect.
     * @throws IOException If there's an error getting mouse events.
     */
    @Override
    public void handleMouseInput() throws IOException {
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = Integer.signum(scrollAmount);
            amountScrolled = (float)(scrollAmount * slotHeight / 2);
        }

        super.handleMouseInput();
    }

    /**
     * Gets the amount the elements should be offset by.
     * @return Amount the user has scrolled.
     */
    public int getScrollAmount() {
        return (int) Math.floor(amountScrolled);
    }
}
