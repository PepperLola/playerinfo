package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.impl.options.modules.WidgetSnapLine;
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

    private boolean selected = false;

    private WidgetEditingState state;

    public GuiIngameWidget(int xPosition, int yPosition, int width, int height) {
        this.position = new WidgetState(this, xPosition, yPosition, false);
        this.width = width;
        this.height = height;
        this.state = WidgetEditingState.INGAME;
    }

    public void render(Minecraft mc) {
        if (this.isSelected()) {
            this.drawHorizontalLine(this.getPosition().getX() - 1, this.getPosition().getX() + this.width + 1, this.getPosition().getY(), ColorUtil.getColorInt(255, 0, 0));
            this.drawHorizontalLine(this.getPosition().getX() - 1, this.getPosition().getX() + this.width + 1, this.getPosition().getY() + this.height, ColorUtil.getColorInt(255, 0, 0));
            this.drawVerticalLine(this.getPosition().getX() - 1, this.getPosition().getY(), this.getPosition().getY() + this.height, ColorUtil.getColorInt(255, 0, 0));
            this.drawVerticalLine(this.getPosition().getX() + this.width + 1, this.getPosition().getY(), this.getPosition().getY() + this.height, ColorUtil.getColorInt(255, 0, 0));
        }
        if (getModule().shouldRenderBackground()) {
            this.drawGradientRect(position.getX(), position.getY(), position.getX() + width, position.getY() + height, 0x55000000, 0x55000000);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean shouldSnap(WidgetSnapLine.Axis axis, double x1, double y1, double x2, double y2, double threshold) {
        if (axis == WidgetSnapLine.Axis.VERTICAL) {
            // x axis because vertical line has horizontal position
            return Math.abs(x1 - x2) <= threshold;
        } else {
            // y axis because horizontal line has vertical position
            return Math.abs(y1 - y2) <= threshold;
        }
    }

    public boolean shouldSnapTop(WidgetSnapLine line, double x, double y) {
        return shouldSnap(line.getAxis(), line.getPosSupplier().getAsInt(), line.getPosSupplier().getAsInt(), x, y, WidgetSnapLine.SnapStrength.WEAK.getThreshold());
    }

    public boolean shouldSnapTop(GuiIngameWidget other, WidgetSnapLine.Axis axis, double x, double y) {
        return shouldSnap(axis, other.getPosition().getX(), other.getPosition().getY(), x, y, WidgetSnapLine.SnapStrength.WEAK.getThreshold());
    }

    public boolean shouldSnapBottom(WidgetSnapLine line, double x, double y) {
        return shouldSnap(line.getAxis(), line.getPosSupplier().getAsInt(), line.getPosSupplier().getAsInt(), x + this.width, y + this.height, WidgetSnapLine.SnapStrength.WEAK.getThreshold());
    }

    public boolean shouldSnapBottom(GuiIngameWidget other, WidgetSnapLine.Axis axis, double x, double y) {
        return shouldSnap(axis, other.getPosition().getX() + other.width, other.getPosition().getY() + other.height, x, y, WidgetSnapLine.SnapStrength.WEAK.getThreshold());
    }

    // no method for middle snap with another widget because it doesn't seem like a necessary feature
    public boolean shouldSnapMiddle(WidgetSnapLine line, double x, double y) {
        return shouldSnap(line.getAxis(), line.getPosSupplier().getAsInt(), line.getPosSupplier().getAsInt(), x + this.width / 2.0, y + this.height / 2.0, line.getThresholdSupplier().getAsInt());
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

    protected int getChromaColor(long offset, boolean invertColors) {
        if (getPosition().isChroma()) {
            return ColorUtil.getChromaColor(offset);
        }

        return invertColors ? ColorUtil.getColorInt(0, 0, 0) : ColorUtil.getColorInt(255, 255, 255);
    }

    protected int getChromaColor(long offset) { return getChromaColor(offset, false); }

    protected int getChromaColor() {
        return getChromaColor(0);
    }

    protected WidgetEditingState getState() {
        return state;
    }

    protected void drawTextVerticallyCentered(String text, int x, int y) {
        drawTextVerticallyCentered(text, x, y, false);
    }

    protected void drawTextVerticallyCentered(String text, int x, int y, boolean invertColors) {
        float height = PlayerInfo.instance.fontRendererObj.getHeight(text);
        drawText(text, x, (int) Math.ceil(y - (3 * height) / 4), invertColors);
    }

    protected void drawText(String text, int x, int y) {
        drawText(text, x, y, false, -1, 1f);
    }

    protected void drawText(String text, int x, int y, boolean invertColors) {
        drawText(text, x, y, invertColors, -1, 1f);
    }

    protected void drawText(String text, int x, int y, int color) {
        drawText(text, x, y, false, color, 1f);
    }

    protected void drawText(String text, int x, int y, float scale) {
        drawText(text, x, y, false, -1, scale);
    }

    protected void drawText(String text, int x, int y, int color, float scale) {
        drawText(text, x, y, false, color, scale);
    }

    protected void drawText(String text, int x, int y, boolean invertColors, int color, float scale) {
        if (Minecraft.getMinecraft().gameSettings.hideGUI) return;
        int leftOffset = 0;
        int character = 0;
        if (text == null) return;
        for (String sub : text.split("")) {
            PlayerInfo.instance.fontRendererObj.drawStringScaled(sub, x + leftOffset, y, color == -1 ? getChromaColor(character * -300L, invertColors) : color, scale);
            leftOffset += (int) PlayerInfo.instance.fontRendererObj.getWidth(sub);
            character ++;
            GlStateManager.resetColor();
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

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    protected enum WidgetEditingState {
        INGAME,
        EDITING
    }
}
