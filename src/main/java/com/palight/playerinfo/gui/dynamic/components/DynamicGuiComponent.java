package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.NumberUtil;
import com.palight.playerinfo.util.math.Vector2;
import net.minecraft.client.gui.Gui;

import java.util.function.BiConsumer;

public abstract class DynamicGuiComponent extends Gui {

    protected Vector2<Integer> position;
    protected Vector2<Integer> size;
    protected DynamicGuiScreen screen;
    private boolean isHovered = false;
    private BiConsumer<Vector2<Integer>, Integer> onClick = (pos, mouseButton) -> { }; // (position, mouseButton) -> {};

    public DynamicGuiComponent(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        this.screen = screen;
        this.position = position;
        this.size = size;
    }

    public DynamicGuiComponent(DynamicGuiScreen screen, int x, int y, int width, int height) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height));
    }

    public DynamicGuiComponent(DynamicGuiScreen screen, int x, int y) {
        this(screen, new Vector2<>(x, y), new Vector2<>(0, 0));
    }

    public DynamicGuiComponent(DynamicGuiScreen screen) {
        this(screen, new Vector2<>(0, 0), new Vector2<>(0, 0));
    }

    public <T extends DynamicGuiComponent> T setOnClick(BiConsumer<Vector2<Integer>, Integer> onClick) {
        this.onClick = onClick;
        return (T) this;
    }

    public <T extends DynamicGuiComponent> T setPosition(Vector2<Integer> position) {
        this.position = position;
        return (T) this;
    }

    public <T extends DynamicGuiComponent> T setPosition(int x, int y) {
        return this.setPosition(new Vector2<>(x, y));
    }

    public <T extends DynamicGuiComponent> T setSize(Vector2<Integer> size) {
        this.size = size;
        return (T) this;
    }

    public <T extends DynamicGuiComponent> T setSize(int width, int height) {
        this.size = new Vector2<>(width, height);
        return (T) this;
    }

    public <T extends DynamicGuiComponent> T setScreen(DynamicGuiScreen screen) {
        this.screen = screen;
        return (T) this;
    }

    public <T extends DynamicGuiComponent> T setHovered(boolean isHovered) {
        this.isHovered = isHovered;
        return (T) this;
    }

    protected void renderText(String text, int x, int y, int color, Baseline baseline, Alignment alignment) {
        UnicodeFontRenderer fr = PlayerInfo.instance.fontRendererObj;
        double textWidth = fr.getWidth(text);
        double textHeight = fr.FONT_HEIGHT;
        switch (baseline) {
            case TOP:
                break;
            case BOTTOM:
                y -= textHeight;
                break;
            case MIDDLE:
                y -= textHeight / 2;
                break;
        }
        switch (alignment) {
            case LEFT:
                break;
            case RIGHT:
                x -= textWidth;
                break;
            case CENTER:
                x -= textWidth / 2;
                break;
        }
        fr.drawString(text, x, y, color);
    }

    protected void renderCenteredString(String text, int x, int y, int color) {
        this.renderText(text, x + this.size.x / 2, y + this.size.y / 2, color, Baseline.MIDDLE, Alignment.CENTER);
    }

    protected void renderHorizontallyCenteredString(String text, int x, int y, int color) {
        this.renderText(text, x + this.size.x / 2, y, color, Baseline.TOP, Alignment.CENTER);
    }

    protected void renderVerticallyCenteredString(String text, int x, int y, int color) {
        this.renderText(text, x, y + this.size.y / 2, color, Baseline.MIDDLE, Alignment.LEFT);
    }

    protected void renderBackground() {
        // eventually should be textured rect
        int color = this.isHovered ? DefaultUIConfig.DEFAULT_COMPONENT_BACKGROUND_COLOR_HOVER : DefaultUIConfig.DEFAULT_COMPONENT_BACKGROUND_COLOR;
        this.drawGradientRect(this.position.x, this.position.y, this.position.x + this.size.x, this.position.y + this.size.y, color, color);
    }

    public boolean mouseIsOver(int mouseX, int mouseY) {
        return NumberUtil.pointIsBetween(mouseX, mouseY, this.position.x, this.position.y, this.position.x + this.size.x, this.position.y + this.size.y);
    }

    public abstract void render(int mouseX, int mouseY, float partialTicks);

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        this.onClick.accept(new Vector2<>(mouseX, mouseY), mouseButton);
    }

    public enum Baseline {
        TOP, MIDDLE, BOTTOM;
    }

    public enum Alignment {
        LEFT, CENTER, RIGHT;
    }
}
