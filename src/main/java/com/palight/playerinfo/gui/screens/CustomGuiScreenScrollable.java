package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.NumberUtil;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class CustomGuiScreenScrollable extends CustomGuiScreen {

    private float targetY, animatedY, lastAnimatedY, calculatedY;
    private int slotHeight = 20;

    /**
     * Constructor for CustomGuiScreenScrollable class.
     *
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
        this.drawDefaultBackground();
        this.updateScrollAmount();

        calculatedY = lastAnimatedY + (animatedY - lastAnimatedY) * partialTicks;
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiCustomWidget guiElement : guiElements) {
            if (NumberUtil.isBetween(guiElement.yPosition, (height - ySize) / 2.0 + headerHeight, (height + ySize) / 2.0 - footerHeight - guiElement.height)) {
                guiElement.drawWidget(mc, mouseX, mouseY);
            }

            guiElement.yPosition = guiElement.originalY + (int) Math.floor(calculatedY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);
    }

    /**
     * Offsets the elements on mouse scroll, creating a scrolling effect.
     *
     * @throws IOException If there's an error getting mouse events.
     */
    @Override
    public void handleMouseInput() throws IOException {
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = Integer.signum(scrollAmount);
            targetY += (float) (scrollAmount * slotHeight / 6.0);
        }

        super.handleMouseInput();
    }

    /**
     * Updates the scroll amounts.
     */
    public void updateScrollAmount() {
        lastAnimatedY = animatedY;
        animatedY += (targetY - animatedY) * 0.6F;
    }
}
