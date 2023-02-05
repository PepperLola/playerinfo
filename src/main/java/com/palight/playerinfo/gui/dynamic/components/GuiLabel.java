package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.math.Vector2;

public class GuiLabel extends DynamicGuiComponent {
    private String text;
    private UnicodeFontRenderer.Baseline baseline = UnicodeFontRenderer.Baseline.TOP;
    private UnicodeFontRenderer.Alignment align = UnicodeFontRenderer.Alignment.LEFT;

    public GuiLabel(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);
    }

    public GuiLabel(DynamicGuiScreen screen, int x, int y, int width, int height) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height));
    }

    public GuiLabel(DynamicGuiScreen screen, int x, int y) {
        this(screen, x, y, 0, 0);
    }

    public GuiLabel(DynamicGuiScreen screen) {
        this(screen, 0, 0);
    }

    public GuiLabel setText(String text) {
        this.text = text;

        this.size.x = (int) this.fontRenderer.getWidth(text);
        this.size.y = (int) this.fontRenderer.getHeight(text);

        return this;
    }

    @Override
    public <T extends DynamicGuiComponent> T setFontRenderer(UnicodeFontRenderer fontRenderer) {
        super.setFontRenderer(fontRenderer);
        return (T) setText(this.text);
    }

    public GuiLabel setBaseline(UnicodeFontRenderer.Baseline baseline) {
        this.baseline = baseline;
        return this;
    }

    public GuiLabel setAlignment(UnicodeFontRenderer.Alignment align) {
        this.align = align;
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderText(this.text, this.position.x, this.position.y, DefaultUIConfig.DEFAULT_LABEL_COLOR, baseline, align);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {

    }
}
