package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.math.Vector2;

public class GuiLabel extends DynamicGuiComponent {
    private String text;
    private Baseline baseline = Baseline.TOP;
    private Alignment align = Alignment.LEFT;

    public GuiLabel(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);
    }

    public GuiLabel(DynamicGuiScreen screen, int x, int y, int width, int height) {
        super(screen, x, y, width, height);
    }

    public GuiLabel(DynamicGuiScreen screen, int x, int y) {
        super(screen, x, y);
    }

    public GuiLabel(DynamicGuiScreen screen) {
        super(screen);
    }

    public GuiLabel setText(String text) {
        this.text = text;

        UnicodeFontRenderer fr = PlayerInfo.instance.fontRendererObj;

        this.size.x = (int) fr.getWidth(text);
        this.size.y = fr.FONT_HEIGHT;

        return this;
    }

    public GuiLabel setBaseline(Baseline baseline) {
        this.baseline = baseline;
        return this;
    }

    public GuiLabel setAlignment(Alignment align) {
        this.align = align;
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        // TODO add scaling
        this.renderText(this.text, this.position.x, this.position.y, DefaultUIConfig.DEFAULT_LABEL_COLOR, baseline, align);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {

    }
}
