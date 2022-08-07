package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.util.math.Vector2;

public class GuiButton extends DynamicGuiComponent {
    private String text;

    public GuiButton(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);
    }

    public GuiButton(DynamicGuiScreen screen, int x, int y, int width, int height) {
        super(screen, x, y, width, height);
    }

    public GuiButton(DynamicGuiScreen screen, int width, int height) {
        super(screen, width, height);
    }

    public GuiButton(DynamicGuiScreen screen) {
        super(screen);
    }

    public GuiButton setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.renderCenteredString(this.text, this.position.x, this.position.y, DefaultUIConfig.DEFAULT_LABEL_COLOR);
    }
}
