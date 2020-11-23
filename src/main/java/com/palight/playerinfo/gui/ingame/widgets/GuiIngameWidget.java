package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class GuiIngameWidget extends Gui {

    private WidgetPosition position;
    public int width;
    public int height;
    public boolean movable = true;

    private boolean chromaEnabled;

    private WidgetState state;

    public GuiIngameWidget(int xPosition, int yPosition, int width, int height) {
        this.position = new WidgetPosition(this, xPosition, yPosition);
        this.width = width;
        this.height = height;
        this.state = WidgetState.INGAME;
    }

    public void render(Minecraft mc) {
        this.drawGradientRect(position.getX(), position.getY(), position.getX() + width, position.getY() + height, 0x55000000, 0x55000000);
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

    protected int getChromaColor(long offset) {
        if (chromaEnabled) {
            return ColorUtil.getChromaColor(offset);
        }

        return ColorUtil.getColorInt(255, 255, 255);
    }

    protected int getChromaColor() {
        return getChromaColor(0);
    }

    protected WidgetState getState() {
        return state;
    }

    protected void drawText(String text, int x, int y) {
        int leftOffset = 0;
        int character = 0;
        if (text == null) return;
        for (String sub : text.split("")) {
            Minecraft.getMinecraft().fontRendererObj.drawString(sub, x + leftOffset, y, getChromaColor(character * -300));
            leftOffset += Minecraft.getMinecraft().fontRendererObj.getStringWidth(sub);
            character ++;
        }
    }

    public boolean shouldRender(Module module) {
        return module.isEnabled();
    }

    public WidgetPosition getPosition() {
        return position;
    }

    public void setPosition(WidgetPosition position) {
        this.position = position;
    }

    protected enum WidgetState {
        INGAME,
        EDITING
    }
}
