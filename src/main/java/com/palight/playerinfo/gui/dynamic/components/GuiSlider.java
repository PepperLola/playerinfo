package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.math.Vector2;

public class GuiSlider extends DynamicGuiComponent {
    private GuiLabel label;
    private int min;
    private int max;
    private int step;
    private String prefix;
    private String suffix;
    private double value = 0;

    public GuiSlider(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size, int min, int max, int step, String prefix, String suffix) {
        super(screen, position, size);
        this.min = min;
        this.max = max;
        this.step = step;
        this.prefix = prefix;
        this.suffix = suffix;

        this.label = new GuiLabel(screen, position.x + size.x / 2, position.y + size.y / 2, 0, size.y)
                .setBaseline(UnicodeFontRenderer.Baseline.MIDDLE)
                .setAlignment(UnicodeFontRenderer.Alignment.CENTER);
    }

    public GuiSlider(DynamicGuiScreen screen, int x, int y, int width, int height, int min, int max, int step, String prefix, String suffix) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height), min, max, step, prefix, suffix);
    }

    public GuiSlider(DynamicGuiScreen screen, int x, int y, int min, int max, int step, String prefix, String suffix) {
        this(screen, new Vector2<>(x, y), new Vector2<>(0, 0), min, max, step, prefix, suffix);
    }

    public GuiSlider(DynamicGuiScreen screen, int min, int max, int step, String prefix, String suffix) {
        this(screen, new Vector2<>(0, 0), new Vector2<>(0, 0), min, max, step, prefix, suffix);
    }

    public GuiSlider(DynamicGuiScreen screen, int x, int y, int width, int height, int min, int max, int step) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height), min, max, step, "", "");
    }

    public GuiSlider(DynamicGuiScreen screen, int x, int y, int min, int max, int step) {
        this(screen, new Vector2<>(x, y), new Vector2<>(0, 0), min, max, step, "", "");
    }

    public GuiSlider(DynamicGuiScreen screen, int min, int max, int step) {
        this(screen, new Vector2<>(0, 0), new Vector2<>(0, 0), min, max, step, "", "");
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        double percent = (double) mouseX / this.size.x;
        this.value = min + (max - min) * percent;
        this.label.setText(prefix + Math.floor(value * 100) / 100 + suffix);
        super.onClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public <T extends DynamicGuiComponent> T setPosition(Vector2<Integer> position) {
        super.setPosition(position);
        this.label.setPosition(this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
        return (T) this;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.label.render(mouseX, mouseY, partialTicks);

        double percent = (value - min) / (max - min);
        int barOffset = (int) (percent * this.size.x);
        this.drawGradientRect(this.position.x + barOffset - 2, this.position.y, this.position.x + barOffset + 2, this.position.y + this.size.y, DefaultUIConfig.DEFAULT_LABEL_COLOR, DefaultUIConfig.DEFAULT_LABEL_COLOR);
    }
}
