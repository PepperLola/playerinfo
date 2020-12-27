package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class GuiIngameWidget extends Gui {

    private Module module;
    private WidgetState position;
    public int width;
    public int height;
    public boolean movable = true;

    private WidgetEditingState state;

    public GuiIngameWidget(int xPosition, int yPosition, int width, int height) {
        this.position = new WidgetState(this, xPosition, yPosition, false);
        this.width = width;
        this.height = height;
        this.state = WidgetEditingState.INGAME;
    }

    public void render(Minecraft mc) {
        this.drawGradientRect(position.getX(), position.getY(), position.getX() + width, position.getY() + height, 0x55000000, 0x55000000);
        GlStateManager.color(1f, 1f, 1f);
    }

    public void startEditing() {
        this.state = WidgetEditingState.EDITING;
    }

    public void stopEditing() {
        this.state = WidgetEditingState.INGAME;
    }

    public void toggleChroma() {
        getPosition().setChroma(!getPosition().isChroma());
    }

    protected int getChromaColor(long offset) {
        if (getPosition().isChroma()) {
            return ColorUtil.getChromaColor(offset);
        }

        return ColorUtil.getColorInt(255, 255, 255);
    }

    protected int getChromaColor() {
        return getChromaColor(0);
    }

    protected WidgetEditingState getState() {
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

    public WidgetState getPosition() {
        return position;
    }

    public void setPosition(WidgetState position) {
        this.position = position;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    protected enum WidgetEditingState {
        INGAME,
        EDITING
    }
}
