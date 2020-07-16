package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class GuiIngameWidget extends Gui {
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    public boolean movable = true;

    private boolean chromaEnabled;

    private WidgetState state;

    public GuiIngameWidget(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.state = WidgetState.INGAME;
    }

    public void render(Minecraft mc) {
        this.drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x55000000, 0x55000000);
        GlStateManager.color(1f, 1f, 1f);
    }

    public void startEditing() {
        this.state = WidgetState.EDITING;
    }

    public void stopEditing() {
        this.state = WidgetState.INGAME;
    }

    public void toggleChroma() {
        this.chromaEnabled = !chromaEnabled;
    }

    protected int getChromaColor() {
        if (chromaEnabled) {
            return ColorUtil.getChromaColor();
        }

        return ColorUtil.getColorInt(255, 255, 255);
    }

    protected WidgetState getState() {
        return state;
    }

    protected void drawText(String text, int x, int y) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, getChromaColor());
    }

    protected enum WidgetState {
        INGAME,
        EDITING
    }
}
