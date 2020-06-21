package com.palight.playerinfo.gui.ingame.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiIngameWidget extends Gui {
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;

    public GuiIngameWidget(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }

    public void render(Minecraft mc) {
        this.drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x55000000, 0x55000000);
    }
}
